import trackSchema from './track.model.js';
import Response from '../../response/response.js';
import logger from '../../resources/logs/logger.log.js';
import trackValidation from './track.validate.js';
import userService from '../profile/profile.model.js'
import tempTrack from './temp_tracking.model.js'
import adminSchema from '../admin/admin.model.js';
import transportSchema from '../transport/transport.model.js';
import tempTestTrackSchema from '../tracking/temp_test_tracking.model.js';
import moment from 'moment';
import axios from 'axios';
import config from 'config';

let NON_SNAP_TO_ROAD_CLIENTS = config.get('NON_SNAP_TO_ROAD_CLIENTS');

// import UserSchema from '../admin/admin.model.js';
class trackService {
async trackUser(req, res) {
  try {
    const arrayOfNonSnapToRoadClients =
      NON_SNAP_TO_ROAD_CLIENTS.split(",").map(Number);

    const result = req.verified;
    let { orgId, emp_id, timezone } = result?.userData?.userData;

    const numericOrgId = Number(orgId);

    const ds = req.body;
    if (!ds || !ds.length) {
      return res.send(Response.FailResp("Invalid Data", null));
    }

    let freData = {};

    // -------------------------
    // VALIDATIONS
    // -------------------------

    const geoLocStatus = await userService.findOne({ orgId, emp_id });
    if (!geoLocStatus || geoLocStatus.geoLogsStatus === false) {
      return res.send(
        Response.FailResp("Validation Failed", "GeoLocation is Not Enabled!")
      );
    }

    const transportDetails = await transportSchema.findOne({
      orgId,
      emp_id,
    });

    freData.currentMode = transportDetails?.currentMode;
    freData.currentFrequency = transportDetails?.currentFrequency;
    freData.currentRadius = transportDetails?.currentRadius;

    const snap_points_limit = geoLocStatus.snap_points_limit;
    const snap_duration_limit = geoLocStatus.snap_duration_limit;

    // -------------------------
    // CHUNK INPUT
    // -------------------------

    const collectiveData = chunkArray(ds, snap_points_limit - 2);

    for (let chunk of collectiveData) {
      const checkTempData = await tempTrack.findOne({ orgId, emp_id });

      if (!checkTempData) continue;

      let hourStatus = false;

      if (
        checkTempData.geologs?.length > 0 &&
        checkTempData.geologs.length < snap_points_limit
      ) {
        const lastFetchedTime = checkTempData.geologs[0].time;
        const currentTime = moment()
          .tz(timezone)
          .format("YYYY-MM-DDTHH:mm:ss.SSS[Z]");

        const minutesDiff = moment(currentTime).diff(
          moment(lastFetchedTime),
          "minutes"
        );

        if (minutesDiff > snap_duration_limit) {
          hourStatus = true;
        }
      }

      // ===================================================
      // TEMP STORAGE MODE
      // ===================================================

      if (!hourStatus && checkTempData.geologs.length < snap_points_limit) {
        const newTempLogs = chunk.map((item) => ({
          time: `${item.date}T${item.time}.000Z`,
          latitude: item.latitude,
          longitude: item.longitude,
          status: item.status || 0,
          taskId: item.taskId || null,
        }));

        await tempTrack.findOneAndUpdate(
          { _id: checkTempData._id },
          { $push: { geologs: { $each: newTempLogs } } },
          { new: true }
        );

        continue; // skip permanent save
      }

      // ===================================================
      // PERMANENT SAVE MODE
      // ===================================================

      let processedData = checkTempData.geologs || [];

      // -------- SNAP OR NOT --------
      if (!arrayOfNonSnapToRoadClients.includes(numericOrgId)) {
        // console.log("Snap To Road Enabled");
        
        let path = "path=";

        processedData.forEach((p, i) => {
          path += `${p.latitude},${p.longitude}`;
          if (i !== processedData.length - 1) path += "|";
        });

        const url =
          config.get("googleSnapRoadApi") +
          `?interpolate=true&${path}&key=${config.get(
            "googleSnapRoadSecretKey"
          )}`;

        const snapRes = await axios.get(url);
        const snappedPoints = snapRes?.data?.snappedPoints || [];

        processedData = trackArrange(processedData, snappedPoints);
      }
      // console.log("Snap To Road Disabled");
      

      const formattedLogs = processedData.map((item) => ({
        time: item.time,
        latitude: item.latitude,
        longitude: item.longitude,
        status: item.status || 0,
        taskId: item.taskId || null,
      }));

      const existingTrack = await trackSchema.findOne({
        orgId,
        emp_id,
        date: ds[0].date,
      });

      if (existingTrack) {
        let totalDistance = existingTrack.distTravelled || 0;

        let lastLog =
          existingTrack.geologs?.length > 0
            ? existingTrack.geologs[existingTrack.geologs.length - 1]
            : null;

        for (let log of formattedLogs) {
          if (lastLog) {
            totalDistance += distanceTrack(
              lastLog.latitude,
              lastLog.longitude,
              log.latitude,
              log.longitude
            );
          }
          lastLog = log;
        }

        await trackSchema.findOneAndUpdate(
          { _id: existingTrack._id },
          {
            $push: { geologs: { $each: formattedLogs } },
            $set: { distTravelled: totalDistance },
          },
          { new: true }
        );
      } else {
        await trackSchema.create({
          orgId,
          emp_id,
          date: ds[0].date,
          geologs: formattedLogs,
          distTravelled: 0,
        });
      }

      // -------- CLEAR TEMP --------
      await tempTrack.findOneAndUpdate(
        { orgId, emp_id },
        { $set: { geologs: [] } }
      );
    }

    return res.send(
      Response.SuccessResp("Location Recorded Successfully.", freData)
    );
  } catch (err) {
    console.log(err);
    return res.send(Response.FailResp("Failed to Track User", err));
  }
}   
}

function trackArrange(tempData,snappedData){
  let distance = 1; //2 meters diagonally from current coordinate
  let angleOffset = 0;
    let finalSnappedArray = [];
    for (let item of tempData) {
      if(item.status != 0){
          let closestLocation = 999999999;
          let snappedId = 0;
          let count = 0;
          for (let it of snappedData) {
              let currentdist = distanceTrack(item.latitude, item.longitude, it.location.latitude, it.location.longitude);
              if (currentdist < closestLocation) {
                  closestLocation = currentdist;
                  snappedId = count;
              }
              count++;
          }
          
          if (snappedData[snappedId].status === undefined && snappedData[snappedId].taskId === undefined ) {
              snappedData[snappedId].time = item.time;
              snappedData[snappedId].status = item.status;
              snappedData[snappedId].taskId = item.taskId;
          } else {

            const newCoordinates = getNextLatLon(snappedData[snappedId].location.latitude, snappedData[snappedId].location.longitude, distance, angleOffset);
            angleOffset += 10;
            // console.log('newCoordinates',newCoordinates,'  at index  ',snappedId + 1);
            snappedData.splice(snappedId + 1, 0, {
              location: {
                latitude: newCoordinates.newLat,
                longitude: newCoordinates.newLon
              },
              time: item.time, 
              status: item.status,
              taskId: item.taskId
            });
          }
  
      }
  }
  
    //Arranging the final Structure
    for (let item of snappedData){
        let tempArray = {};
        tempArray.time = item.time||null;
        tempArray.latitude = item.location.latitude;
        tempArray.longitude = item.location.longitude;
        tempArray.status = item.status || 0;
        tempArray.taskId = item.taskId || null;
        finalSnappedArray.push(tempArray);
    }
    // console.log(finalSnappedArray,'finalSnappedArray');
    return finalSnappedArray;
    //to find the closest route for the task and status
}
function distanceTrack(lat1,lon1,lat2,lon2) {
    var R = 6371; // Radius of the earth in km
    var dLat = deg2rad(lat2-lat1);  // deg2rad below
    var dLon = deg2rad(lon2-lon1); 
    var a = 
      Math.sin(dLat/2) * Math.sin(dLat/2) +
      Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
      Math.sin(dLon/2) * Math.sin(dLon/2)
      ; 
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
    var d = R * c; // Distance in km
    return d;
  }
  
  function deg2rad(deg) {
    return deg * (Math.PI/180)
  }

  function chunkArray(array, chunkSize) {
    const chunks = [];
    for (let i = 0; i < array.length; i += chunkSize) {
      chunks.push(array.slice(i, i + chunkSize));
    }
    return chunks;
  }


  function getNextLatLon(lat, lon, distance, angle = 0) {
    const R = 6371000; // Radius of the Earth in meters

    // Convert distance to degrees and angle to radians
    const distanceInDegrees = distance / R * (180 / Math.PI);
    const angleInRadians = angle * (Math.PI / 180);

    // Calculate the change in latitude and longitude based on the angle
    const newLat = lat + (distanceInDegrees * Math.cos(angleInRadians));
    const newLon = lon + (distanceInDegrees * Math.sin(angleInRadians) / Math.cos(lat * Math.PI / 180));

    return { newLat, newLon };
}
export default new trackService();