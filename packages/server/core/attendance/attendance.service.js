import Response from '../../response/response.js';
import Logger from '../../resources/logs/logger.log.js';
import config from 'config';
import axios from 'axios';
import adminSchema from '../admin/admin.model.js';
import UserSchema from '../profile/profile.model.js';
import trackSchema from '../tracking/track.model.js';
import logger from '../../resources/logs/logger.log.js';
import moment from 'moment-timezone';
import taskModel from '../task/task.model.js';
import tempTrack from '../tracking/temp_tracking.model.js';
import transportSchema from '../transport/transport.model.js';
import orgLocationSchema from '../../core/hrmsAdmin/orgLocation.model.js'
import userService from '../profile/profile.model.js'
import { ObjectId } from 'mongodb';
import employeeLocationModel from '../hrmsAdmin/employeeLocation.model.js';


let NON_SNAP_TO_ROAD_CLIENTS = config.get('NON_SNAP_TO_ROAD_CLIENTS');


class AttendanceService {

    async fetchAttendance(req, res) {
        try {
            const result = req.verified;
            const data = req?.body;

            let { orgId, emp_id, timezone } = result?.userData?.userData;
            const body = { organization_id: orgId, employee_id: emp_id, start_date: data.startDate, end_date: data.endDate, secretKey: config.get('emp_secret_key') };
            let attendance = await axios.post(config.get('empDomain') + 'hrms/getAttendanceField', body);
            if (attendance?.data?.data?.length > 0){
                const promises = await Promise.all(attendance.data.data[0].attendance?.map(async user => {
                    if(user.start_time){
                        user.start_time = moment(user.start_time).tz(timezone).format('YYYY-MM-DD HH:mm:ss');
                    }
                    if(user.end_time){
                        user.end_time = moment(user.end_time).tz(timezone).format('YYYY-MM-DD HH:mm:ss');
                    }
                    if(user.open_request == 0){
                        user.open_request = null;
                    }
                }));
                return res.send(Response.SuccessResp('Attendance fetched successfully', attendance?.data?.data))
            }
            else{
                return res.send(Response.FailResp('No Attendance found'))
            }

        }
        catch (err) {
            return res.send(Response.FailResp('Something went wrong', err))
        }
    }
    async markAttendance(req, res) {
        try {
            // convert into array and numbers
            let arrayOfNonSnapToRoadClients = NON_SNAP_TO_ROAD_CLIENTS.split(',').map(Number);

            const attendanceDate = moment().format('YYYY-MM-DD');
            const result = req.verified;
            let data = req?.body;
            let { orgId, emp_id, timezone } = result?.userData?.userData;
            let pendingTask = await taskModel.find({
                orgId:orgId,
                emp_id:emp_id,
                taskApproveStatus: { $in: [1,3] }
            });
            
            const body = { organization_id: orgId, employee_id: emp_id, timezone: timezone, secretKey: config.get('emp_secret_key') };
            let attendance = await axios.post(config.get('empDomain') + 'hrms/markAttendanceField', body);
            if (pendingTask?.length&&attendance.data.code != 403) {
                let taskIds = pendingTask.map(task => task._id);
                
               await taskModel.updateMany(
                    { _id: { $in: taskIds }, emp_id: emp_id },
                    { $set: { taskApproveStatus: 2 } }
                );
            }
            let toDaysAttendance =await trackSchema.findOne({emp_id:emp_id,orgId:orgId,date:attendanceDate})
            let transportDetails = await transportSchema.findOne({orgId:orgId, emp_id: emp_id});
            if((toDaysAttendance===null&&attendance&&attendance.data.message!=='Successfully Checked Out')||(toDaysAttendance&&toDaysAttendance.checkIn===null&&toDaysAttendance.checkOut===null&&attendance)){
                //function to store previous tracking incase present
                //If orgId is present in arrayOfNonSnapToRoadClients then don't use checkInTrack function to avoid snapToRoadApi concersion
                    const checkInTrackData = await checkInTrack(orgId,emp_id);
                let updatedTrack =await trackSchema.findOne({emp_id:emp_id,orgId:orgId,date:attendanceDate})

                if(pendingTask?.length){
                    const updated = await taskModel.findOneAndUpdate(
                        {
                            orgId: orgId,
                            emp_id: emp_id,
                            taskApproveStatus: { $in: [1, 3] }
                        },
                        { $set: { taskApproveStatus: 2 } },
                        { returnDocument: 'after' }  
                    )  
                }

                if (attendance?.data){
                    if(attendance.data.code == 200){
                        try {
                            let locData = {
                                orgId : orgId,
                                emp_id :emp_id,
                                date : attendanceDate,
                                distTravelled:0,
                                currentFrequency:transportDetails.currentFrequency,
                                currentMode:transportDetails.currentMode,
                                geologs:[]
                            };
                            // let tempCordinateData='path=';
                            // tempCordinateData = tempCordinateData+data.latitude+','+data.longitude;
                            // //passing the snaproad api 
                            // const url = config.get('googleSnapRoadApi') + '?interpolate=true&'+tempCordinateData+'&key='+config.get('googleSnapRoadSecretKey');
                            // let snapLocData = await axios.get(url);
                            // snapLocData = snapLocData.data.snappedPoints;
                            let count = 0;
                            // for (let chitem of snapLocData) {
                                if(count == 0){
                                    locData.geologs.push({
                                        'time': moment().tz(timezone).format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
                                        'latitude': data.latitude,
                                        'longitude': data.longitude,
                                        'status': 5,
                                        'taskId': null
                                    });
                                }
                            //     else{
                            //         locData.geologs.push({
                            //             'time': moment().tz(timezone).format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
                            //             'latitude': chitem.location.latitude,
                            //             'longitude': chitem.location.longitude,
                            //             'status': 0,
                            //             'taskId': null
                            //         });
                            //     }  
                            // }
                            locData.checkIn = moment().tz(timezone).format('YYYY-MM-DD HH:mm:ss');
                            attendance.data.data.time = moment().tz(timezone).format('YYYY-MM-DD HH:mm:ss');
                            if (updatedTrack !== null) {
                                await trackSchema.findOneAndUpdate(
                                    { _id: updatedTrack._id }, 
                                    { 
                                        $push: {
                                            geologs: {
                                                $each: [{
                                                    'time': moment().tz(timezone).format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
                                                    'latitude': data.latitude,
                                                    'longitude': data.longitude,
                                                    'status': 5,
                                                    'taskId': null
                                                }], 
                                                $position: 0 
                                            }
                                        },
                                        $set: {
                                            checkIn: moment().tz(timezone).format('YYYY-MM-DD HH:mm:ss')
                                        }
                                    },
                                    { new: true } 
                                );
                            } else {
                                const addData = await trackSchema.create(locData);               
                            }
                            
                            
                            return res.send(Response.SuccessResp('Attendance Recorded Sucessfully', attendance?.data))
                        } 
                        catch (err) {
                            return res.send(Response.FailResp("Something went wrong", err));
                        }
                    }
                    else if(attendance.data.code == 403){
                        return res.send(Response.SuccessResp('Checkout Restricted', attendance?.data))
                    }
                    else{
                        return res.send(Response.SuccessResp('Failed To Mark Attendance', attendance?.data))
                    }   
                } 
                return res.send(Response.FailResp('Something went wrong'))
            }else if((toDaysAttendance&&toDaysAttendance.checkIn!==null&&toDaysAttendance.checkOut===null&&attendance&&attendance.data.message==='Successfully Checked Out')||(toDaysAttendance&&toDaysAttendance.checkIn!==null&&toDaysAttendance.checkOut!==null&&attendance&&attendance.data.message==='Successfully Checked Out')){
                if (attendance?.data){
                    if(attendance.data.code == 200){
                        try {
                            let locData = {
                                orgId : orgId,
                                emp_id :emp_id,
                                date : attendanceDate,
                                currentFrequency:transportDetails.currentFrequency,
                                currentMode:transportDetails.currentMode,
                            };
                            // const isExist = await trackSchema.find({ orgId: orgId, emp_id: emp_id, date: attendanceDate});
                            let tempds = {
                                'time':moment().tz(timezone).format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
                                'latitude':data.latitude,
                                'longitude':data.longitude,
                                'status':6,
                                'taskId':null
                            };
                            //update checkin checkout in trackSchema and rest log details in tempTrack schema
                            const tempCheckOut = moment().tz(timezone).format('YYYY-MM-DD HH:mm:ss');
                            attendance.data.data.time = moment().tz(timezone).format('YYYY-MM-DD HH:mm:ss');
                            const hasStatusSix = toDaysAttendance.geologs.some(log => log.status === 6);
                            if(hasStatusSix){
                              //Update geologs time with updated time
                                toDaysAttendance.geologs.forEach(log => {
                                    if(log.status === 6){
                                        log.time = tempCheckOut;
                                    }
                                });
                                toDaysAttendance.checkOut = tempCheckOut;
                                await toDaysAttendance.save();
                                return res.send(Response.SuccessResp('Attendance Recorded Sucessfully', attendance?.data))
                            }else{
                                //If orgId is present in arrayOfNonSnapToRoadClients then don't use checkoutTrack function to avoid snapToRoadApi concersion
                                    
                                    const ddjhf = await checkoutTrack(tempds,orgId,emp_id,timezone,attendanceDate,tempCheckOut);
                                return res.send(Response.SuccessResp('Attendance Recorded Sucessfully', attendance?.data))
                            }
                        } 
                        catch (err) {
                            return res.send(Response.FailResp("Failed to add task", err));
                        }
                    }
                    else if(attendance.data.code == 403){
                        return res.send(Response.SuccessResp('Checkout Restricted', attendance?.data))
                    }
                    else{
                        return res.send(Response.SuccessResp('Failed To Mark Attendance', attendance?.data))
                    }   
                } 
                return res.send(Response.FailResp('Something went wrong'))
            } else if(attendance.data.code == 403){
                return res.send(Response.SuccessResp('Checkout Restricted', attendance?.data))
            }
            else{
                return res.send(Response.SuccessResp('Failed To Mark Attendance', attendance?.data))
            }
        }
        catch (err) {
            logger.error(`${err}`);
            // console.log(err);
            return res.send(Response.FailResp('Something went wrong', err))
        }
    }
    async getAttendance(req, res) {
        try {
            // const attendanceDate = moment().subtract(1, 'days').format('YYYY-MM-DD');
            const result = req.verified;
            const data = req?.body;
            let { orgId, emp_id, timezone, location ,_id} = result?.userData?.userData;
            let transportDetails = await transportSchema.findOne({orgId:orgId, emp_id: emp_id});
            const attendanceDate = moment().tz(timezone).subtract(1, 'days').format('YYYY-MM-DD');
            const todaysDate = moment().tz(timezone).format('YYYY-MM-DD');
            let resultData;
            const isExist = await adminSchema.find({ orgId: orgId });
            let employeeDetails = await userService.findOne({_id});
            const orgLocData = await orgLocationSchema.find({orgId:orgId,locationName:location});
            let employeeLocation = await employeeLocationModel.findOne({employeeId:_id});
            let latitude = null;
            let longitude = null;
            let geo_fencing = false;
            let range = 10;
            let isMobEnabled = true;
            // console.log(employeeDetails?.isGeoFencingOn,'isGeoFencingOn');
            // console.log(employeeLocation,'employeeLocation');
            if(employeeLocation&&(orgLocData.length>0)){
                if(employeeLocation&&employeeDetails?.isGeoFencingOn===1){
                    // console.log(employeeLocation,'1');
                    latitude = employeeLocation?.latitude != null ? employeeLocation.latitude.toString() : null;
                    longitude = employeeLocation?.longitude != null ? employeeLocation.longitude.toString() : null;
                    geo_fencing = employeeLocation?.geo_fencing ?? null;
                    range = employeeLocation?.range ?? null;
                    isMobEnabled = employeeLocation?.isMobEnabled ?? false;
                }else{
                    // console.log(orgLocData,'2');
                    latitude = orgLocData[0]?.latitude != null ? orgLocData[0].latitude.toString() : null;
                    longitude = orgLocData[0]?.longitude != null ? orgLocData[0].longitude.toString() : null;
                    geo_fencing = orgLocData[0]?.geo_fencing ?? null;
                    range = orgLocData[0]?.range ?? null;
                    isMobEnabled = orgLocData[0]?.isMobEnabled ?? false;
                }
                
            }else if(!employeeLocation&&(orgLocData.length>0)){
                // console.log(orgLocData,'3');
                latitude = orgLocData[0]?.latitude != null ? orgLocData[0].latitude.toString() : null;
                longitude = orgLocData[0]?.longitude != null ? orgLocData[0].longitude.toString() : null;
                geo_fencing = orgLocData[0]?.geo_fencing ?? null;
                range = orgLocData[0]?.range ?? null;
                isMobEnabled = orgLocData[0]?.isMobEnabled ?? false;
            }
            //Fetching oranisation data
            if(orgLocData.length > 0){
                resultData = {
                    orglatitude : latitude,
                    orglongitude: longitude,
                    orgRadius : range
                }
            }
            else{
                return res.send(Response.FailResp('Invalid Employee Location "'+location+'"', null))
            }
            //Fetching Employee Data
            const empExist = await UserSchema.find({ emp_id: emp_id});
            if(empExist.length > 0){
                resultData.isGeoFencingOn = geo_fencing===true?1:0;
                resultData.isMobileDeviceEnabled = isMobEnabled===true?1:0;
                resultData.isBioMetricEnabled = empExist[0].isBioMetricEnabled;
                resultData.isWebEnabled = empExist[0].isWebEnabled,
                resultData.geoLogStatus = empExist[0].geoLogsStatus;
                resultData.currentFrequency = transportDetails.currentFrequency;
                resultData.currentRadius = transportDetails.currentRadius;//tracking Radius
                resultData.currentMode = transportDetails.currentMode;

            }
            else{
                return res.send(Response.FailResp('Invalid Employee Id', null))
            }
            const startOfYesterday = moment.utc().subtract(1, 'days').startOf('day').toDate();
            const endOfYesterday = moment.utc().subtract(1, 'days').endOf('day').toDate();

            let startDate = attendanceDate + ' ' + '00:00:00';
            let endDate = attendanceDate + ' ' + '23:59:59';
            
            const yesterDaysTaskWithStatus = await taskModel.countDocuments({
                emp_id:emp_id,
                // createdAt: {
                //     $gte: startOfYesterday,
                //     $lte: endOfYesterday
                // },
                start_time: { $lte: endDate },
                end_time: { $gte: startDate },
                taskApproveStatus: { $in: [4] }
            });

            const yesterDaysTaskWithoutStatus = await taskModel.countDocuments({
                emp_id:emp_id,
                // createdAt: {
                //     $gte: startOfYesterday,
                //     $lte: endOfYesterday
                // },
                start_time: { $lte: endDate },
                end_time: { $gte: startDate },
                taskApproveStatus: { $nin: [5] }
            });
            var today = new Date();

            // Get yesterday's date
            var yesterday = new Date();
            yesterday.setDate(today.getDate());
            var yesterdayFormatted = yesterday.toISOString().split('T')[0];
            const body1 = { organization_id: orgId, employee_id: emp_id, start_date: attendanceDate, end_date: todaysDate, secretKey: config.get('emp_secret_key') };
            let attendance1 = await axios.post(config.get('empDomain') + 'hrms/getAttendanceField', body1);
            let checkInTime
            let checkoutTime
            let totalSeconds
            let yesterdayInTime;
            let yesterdayOutTime;

            if(!attendance1?.data?.data?.length){
                 checkInTime = null;
                 checkoutTime = null;
                 totalSeconds = 0;
                 yesterdayOutTime=null;
                 yesterdayInTime=null;
            }else{
                 checkInTime = attendance1?.data?.data[0]?.attendance[1]?.start_time
                 checkoutTime = attendance1?.data?.data[0]?.attendance[1]?.end_time
                 totalSeconds = attendance1?.data?.data[0]?.attendance[0]?.logged_duration;
                 yesterdayInTime=attendance1?.data?.data[0]?.attendance[0]?.start_time;
                 yesterdayOutTime=attendance1?.data?.data[0]?.attendance[0]?.end_time;
            }
            //updating the timezone
            if(checkInTime != undefined && checkInTime != null){
                checkInTime = moment(checkInTime).tz(timezone).format('YYYY-MM-DD HH:mm:ss');
            }
            if(checkoutTime != undefined && checkoutTime != null){
                checkoutTime = moment(checkoutTime).tz(timezone).format('YYYY-MM-DD HH:mm:ss');
            }

            function convertSeconds(seconds) {
                const hours = Math?.floor(seconds / 3600);
                const minutes = Math?.floor((seconds % 3600) / 60);
                const remainingSeconds = seconds % 3600 % 60;
                
                // Format the result as HH:MM:SS
                const formattedHours = String(hours)?.padStart(2, '0');
                const formattedMinutes = String(minutes)?.padStart(2, '0');
                const formattedSeconds = String(remainingSeconds)?.padStart(2, '0');
                
                return `${formattedHours}:${formattedMinutes}:${formattedSeconds}`;
            }
         
            let time
            if(yesterdayInTime&&yesterdayOutTime){
            time = convertSeconds(totalSeconds);
            }else if(yesterdayInTime&&!yesterdayOutTime){
            //If Checkout not Present 
            const checkInDate = new Date(yesterdayInTime);
            const checkOutDate = new Date(new Date(yesterdayInTime)?.setUTCHours(23, 59, 59, 999));
            const differenceInMilliseconds = checkOutDate - checkInDate;
            const differenceInSeconds =  Math?.round(differenceInMilliseconds / 1000);

            time =   convertSeconds(differenceInSeconds);  
            } else{
            time='--:--:--'
            }
            
            let yesterdayDist="--",yesterdaytask=`${(yesterDaysTaskWithStatus||'--')+'/'+(yesterDaysTaskWithoutStatus||'--')}`,yesterdayHrs=time||'--:--:--';
            const trackLogs = await trackSchema.find({ orgId: orgId, emp_id: emp_id, date: attendanceDate});           
            if(trackLogs.length>0 && trackLogs[0].distTravelled >= 0){
                yesterdayDist = trackLogs[0].distTravelled;
            }
            resultData.yesterdayDist = yesterdayDist;
            resultData.yesterdaytask = yesterdaytask;
            resultData.yesterdayHrs = yesterdayHrs;   
            resultData.data = {
                data:{
                    check_in: checkInTime,
                    check_out: checkoutTime
                }   
            };

            
            return res.send(Response.SuccessResp('Successfully fetched attendance details', resultData))

        }
        catch (err) {
            console.log(err);
            return res.send(Response.FailResp('Something went wrong', err))
        }
    }

    async attendanceRequest(req, res) {
        try {
            const result = req.verified;
            const data = req?.body;
            let { orgId, emp_id, timezone } = result?.userData?.userData;
            const language = 'en';
            const {date, check_in, check_out, reason} = req.body;
            const body = {  organization_id: orgId, 
                            employee_id: emp_id, 
                            timezone: timezone, 
                            date:date,
                            check_in:check_in,
                            check_out:check_out,
                            reason:reason,
                            secretKey: config.get('emp_secret_key') };
            let attendance = await axios.post(config.get('empDomain') + 'hrms/attendance-field-request', body);
            if (attendance?.data) return res.send(Response.SuccessResp('Request Created Successfully', attendance?.data))
            return res.send(Response.FailResp('Something went wrong')) 

        }
        catch (err) {
            return res.send(Response.FailResp('Something went wrong', err))
        }
    }

}

//the final tracking of the day with checkout
async function checkoutTrack(
  tempds,
  orgId,
  emp_id,
  timezone,
  attendanceDate,
  tempCheckOut
) {
  try {
    const arrayOfNonSnapToRoadClients =
      NON_SNAP_TO_ROAD_CLIENTS.split(",").map(Number);

    const numericOrgId = Number(orgId);

    const checkTempData = await tempTrack.findOne({ orgId, emp_id });
    if (!checkTempData) return false;

    const transportDetails = await transportSchema.findOne({
      orgId,
      emp_id,
    });

    let pathsData = checkTempData.geologs || [];

    let processedData = pathsData;

    // ============================================
    // SNAP OR NON-SNAP CONDITION
    // ============================================

    if (
      pathsData.length > 0 &&
      !arrayOfNonSnapToRoadClients.includes(numericOrgId)
    ) {
      let path = "path=";

      pathsData.forEach((p, i) => {
        path += `${p.latitude},${p.longitude}`;
        if (i !== pathsData.length - 1) path += "|";
      });

      const url =
        config.get("googleSnapRoadApi") +
        `?interpolate=true&${path}&key=${config.get(
          "googleSnapRoadSecretKey"
        )}`;

      const snapRes = await axios.get(url);
      const snappedPoints = snapRes?.data?.snappedPoints || [];

      processedData = trackArrange(pathsData, snappedPoints);
    }

    // ============================================
    // FORMAT LOGS
    // ============================================

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
      date: attendanceDate,
    });

    if (!existingTrack) return false;

    // ============================================
    // INCREMENTAL DISTANCE CALCULATION
    // ============================================

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

    // Add checkout location as final log
    if (tempds) {
      if (lastLog) {
        totalDistance += distanceTrack(
          lastLog.latitude,
          lastLog.longitude,
          tempds.latitude,
          tempds.longitude
        );
      }

      formattedLogs.push(tempds);
    }

    // ============================================
    // UPDATE TRACK DOCUMENT
    // ============================================

    await trackSchema.findOneAndUpdate(
      { _id: existingTrack._id },
      {
        $push: { geologs: { $each: formattedLogs } },
        $set: {
          distTravelled: totalDistance,
          checkOut: tempCheckOut,
          currentFrequency: transportDetails?.currentFrequency,
          currentMode: transportDetails?.currentMode,
        },
      },
      { new: true }
    );

    // ============================================
    // CLEAR TEMP DATA
    // ============================================

    await tempTrack.findOneAndUpdate(
      { orgId, emp_id },
      { $set: { geologs: [] } }
    );

    return true;
  } catch (err) {
    console.log(err);
    return false;
  }
}

async function checkInTrack(orgId, emp_id) {
  try {
    const arrayOfNonSnapToRoadClients =
      NON_SNAP_TO_ROAD_CLIENTS.split(",").map(Number);

    const numericOrgId = Number(orgId);

    const checkTempData = await tempTrack.findOne({ orgId, emp_id });
    if (!checkTempData || !checkTempData.geologs?.length) {
      return true;
    }

    const transportDetails = await transportSchema.findOne({
      orgId,
      emp_id,
    });

    const geoLocStatus = await userService.findOne({ orgId, emp_id });
    const snap_points_limit = geoLocStatus?.snap_points_limit || 100;

    const attendanceDate =
      checkTempData.geologs[0].time.split("T")[0];

    const collectiveData = chunkArray(
      checkTempData.geologs,
      snap_points_limit - 2
    );

    for (let pathsData of collectiveData) {
      let processedData = pathsData;

      // ============================================
      // SNAP OR NON-SNAP CONDITION
      // ============================================

      if (!arrayOfNonSnapToRoadClients.includes(numericOrgId)) {
        let path = "path=";

        pathsData.forEach((p, i) => {
          path += `${p.latitude},${p.longitude}`;
          if (i !== pathsData.length - 1) path += "|";
        });

        const url =
          config.get("googleSnapRoadApi") +
          `?interpolate=true&${path}&key=${config.get(
            "googleSnapRoadSecretKey"
          )}`;

        const snapRes = await axios.get(url);
        const snappedPoints = snapRes?.data?.snappedPoints || [];

        processedData = trackArrange(pathsData, snappedPoints);
      }

      // ============================================
      // FORMAT LOGS
      // ============================================

      const formattedLogs = processedData.map((item) => ({
        time: item.time,
        latitude: item.latitude,
        longitude: item.longitude,
        status: item.status || 0,
        taskId: item.taskId || null,
      }));

      let existingTrack = await trackSchema.findOne({
        orgId,
        emp_id,
        date: attendanceDate,
      });

      if (!existingTrack) {
        existingTrack = await trackSchema.create({
          orgId,
          emp_id,
          date: attendanceDate,
          geologs: [],
          distTravelled: 0,
          currentFrequency: transportDetails?.currentFrequency,
          currentMode: transportDetails?.currentMode,
        });
      }

      // ============================================
      // INCREMENTAL DISTANCE
      // ============================================

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
    }

    // ============================================
    // CLEAR TEMP DATA
    // ============================================

    await tempTrack.findOneAndUpdate(
      { orgId, emp_id },
      { $set: { geologs: [] } }
    );

    return true;
  } catch (err) {
    console.log(err);
    throw err;
  }
}

function trackArrange(tempData,snappedData){
    let finalSnappedArray = [];
    let distance = 1;
    let angleOffset = 0;
    if (tempData&&snappedData&&tempData.length > 0 && snappedData.length > 0) {
        for (let item of tempData) {
            if (item.status != 0) {
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
    
                if (snappedData[snappedId].status === undefined && snappedData[snappedId].taskId === undefined) {
                    snappedData[snappedId].time = item.time;
                    snappedData[snappedId].status = item.status;
                    snappedData[snappedId].taskId = item.taskId;
                } else {
                    const newCoordinates = getNextLatLon(snappedData[snappedId].location.latitude, snappedData[snappedId].location.longitude, distance, angleOffset);
                    angleOffset += 10;
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
    }
    if(tempData.length&&snappedData===undefined){
        snappedData=tempData;
    }
    //Arranging the final Structure
    for (let item of snappedData){
        let tempArray = {};
        tempArray.time = item.time||null;
        tempArray.latitude = item?.location?.latitude||item.latitude;
        tempArray.longitude = item?.location?.longitude||item.longitude;
        tempArray.status = item.status || 0;
        tempArray.taskId = item.taskId || null;
        finalSnappedArray.push(tempArray);
    }
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

export default new AttendanceService();
