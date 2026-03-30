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
            const attendanceDate = moment().format('YYYY-MM-DD');
            const result = req.verified;
            let data = req?.body;
            let { orgId, emp_id, timezone } = result?.userData?.userData;
            const body = { organization_id: orgId, employee_id: emp_id, timezone: timezone, secretKey: config.get('emp_secret_key') };
            let attendance = await axios.post(config.get('empDomain') + 'hrms/markAttendanceField', body);
            let toDaysAttendance =await trackSchema.findOne({emp_id:emp_id,orgId:orgId,date:attendanceDate})
            let transportDetails = await transportSchema.findOne({orgId:orgId, emp_id: emp_id});
            let pendingTask = await taskModel.find({
                orgId:orgId,
                emp_id:emp_id,
                taskApproveStatus: { $in: [1,3] }
            });
            if((toDaysAttendance===null&&attendance)||(toDaysAttendance.checkIn===null&&toDaysAttendance.checkOut===null&&attendance)){
                //function to store previous tracking incase present
                const checkInTrackData = await checkInTrack(orgId,emp_id);
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
                            let tempCordinateData='path=';
                            tempCordinateData = tempCordinateData+data.latitude+','+data.longitude;
                            //passing the snaproad api 
                            const url = config.get('googleSnapRoadApi') + '?interpolate=true&'+tempCordinateData+'&key='+config.get('googleSnapRoadSecretKey');
                            let snapLocData = await axios.get(url);
                            snapLocData = snapLocData.data.snappedPoints;
                            let count = 0;
                            for (let chitem of snapLocData) {
                                if(count == 0){
                                    locData.geologs.push({
                                        'time': moment().tz(timezone).format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
                                        'latitude': chitem.location.latitude,
                                        'longitude': chitem.location.longitude,
                                        'status': 5,
                                        'taskId': null
                                    });
                                }
                                else{
                                    locData.geologs.push({
                                        'time': moment().tz(timezone).format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
                                        'latitude': chitem.location.latitude,
                                        'longitude': chitem.location.longitude,
                                        'status': 0,
                                        'taskId': null
                                    });
                                }  
                            }
                            locData.checkIn = moment().tz(timezone).format('YYYY-MM-DD HH:mm:ss');
                            attendance.data.data.time = moment().tz(timezone).format('YYYY-MM-DD HH:mm:ss');
                            const addData = await trackSchema.create(locData);               
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
            }else if(toDaysAttendance&&toDaysAttendance.checkIn!==null&&toDaysAttendance.checkOut===null&&attendance?.data){

               
                if(pendingTask?.length) return res.send(Response.FailResp("Please Pause or complete OnGoing Tasks to Proceed!", pendingTask));
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
                                'time':data.time,
                                'latitude':data.latitude,
                                'longitude':data.longitude,
                                'status':6,
                                'taskId':null
                            };
                            //update checkin checkout in trackSchema and rest log details in tempTrack schema
                            const tempCheckOut = moment().tz(timezone).format('YYYY-MM-DD HH:mm:ss');
                            attendance.data.data.time = moment().tz(timezone).format('YYYY-MM-DD HH:mm:ss');
                            const ddjhf = await checkoutTrack(tempds,orgId,emp_id,timezone,attendanceDate,tempCheckOut);
                            return res.send(Response.SuccessResp('Attendance Recorded Sucessfully', attendance?.data))
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
            }
        }
        catch (err) {
            return res.send(Response.FailResp('Something went wrong', err))
        }
    }
    async getAttendance(req, res) {
        try {
            // const attendanceDate = moment().subtract(1, 'days').format('YYYY-MM-DD');
            const result = req.verified;
            const data = req?.body;
            let { orgId, emp_id, timezone, location } = result?.userData?.userData;
            let transportDetails = await transportSchema.findOne({orgId:orgId, emp_id: emp_id});
            const attendanceDate = moment().tz(timezone).subtract(1, 'days').format('YYYY-MM-DD');
            const todaysDate = moment().tz(timezone).format('YYYY-MM-DD');
            let resultData;
            const isExist = await adminSchema.find({ orgId: orgId });

            const orgLocData = await orgLocationSchema.find({orgId:orgId,locationName:location});

            //Fetching oranisation data
            if(orgLocData.length > 0){
                resultData = {
                    orglatitude : orgLocData[0].latitude,
                    orglongitude: orgLocData[0].longitude,
                    orgRadius : orgLocData[0].range
                }
            }
            else{
                return res.send(Response.FailResp('Invalid Employee Location "'+location+'"', null))
            }
            
            //Fetching Employee Data
            const empExist = await UserSchema.find({ emp_id: emp_id});
            if(empExist.length > 0){
                resultData.isGeoFencingOn = empExist[0].isGeoFencingOn;
                resultData.isMobileDeviceEnabled = empExist[0].isMobileDeviceEnabled;
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
async function checkoutTrack(tempds,orgId,emp_id,timezone,attendanceDate,tempCheckOut){
    let freData={};
    try {
        // Pushing all data from temp
        let checkTempData = await tempTrack.findOne({ orgId: orgId, emp_id: emp_id });
        let transportDetails = await transportSchema.findOne({orgId:orgId, emp_id: emp_id});
        let pathsData = checkTempData.geologs;
        let tempCordinateData='path=';
        let count = 1;
        for (let it of pathsData) {
            tempCordinateData = tempCordinateData+it.latitude+','+it.longitude;
            if(count != pathsData.length){
                tempCordinateData = tempCordinateData+'|';
            }
            count++;
        }  
        let attachedData=[];
        if(checkTempData.geologs.length > 0){
            const url = config.get('googleSnapRoadApi') + '?interpolate=true&'+tempCordinateData+'&key='+config.get('googleSnapRoadSecretKey');
            let snapLocData = await axios.get(url);
            snapLocData = snapLocData.data.snappedPoints;
            attachedData = trackArrange(pathsData,snapLocData);
        }
        
        const isExist = await trackSchema.findOne({ orgId: orgId, emp_id: emp_id, date:attendanceDate });
        var status,taskId;
        let locData = {
            orgId: orgId,
            emp_id: emp_id,
            date: isExist.date,
            distTravelled: 0,
            currentFrequency:transportDetails.currentFrequency,
            currentMode:transportDetails.currentMode,
        };

        // Calculating Distance
        for (let item of attachedData) {
            const oldLogs = isExist.geologs[isExist.geologs.length - 1];
            const lastLogs = await distanceTrack(item.latitude, item.longitude, oldLogs.latitude, oldLogs.longitude);
            locData.distTravelled = isExist.distTravelled + lastLogs;
            status = item.status || 0;
            taskId = item.taskId || null;
            isExist.geologs.push({
                'time': item.time,
                'latitude': item.latitude,
                'longitude': item.longitude,
                'status': status,
                'taskId': taskId
            });
            locData.geologs = isExist.geologs;
        }
        const updateTrackData = await trackSchema.findOneAndUpdate({ _id: isExist._id }, { $set: locData }, { returnDocument: 'after' });
        
        //insert checkout data to trackschema
        updateTrackData.checkOut = tempCheckOut;
        updateTrackData.geologs.push(tempds);
        const updatecheckoutData = await trackSchema.findOneAndUpdate({ _id: isExist._id }, { $set: updateTrackData }, { returnDocument: 'after' });
        
        //delete the temp data
        const temClsData = {
            orgId:orgId,
            emp_id:emp_id,
            geologs:[]
        }
        const deleteTrackData = await tempTrack.findOneAndUpdate({ orgId: orgId, emp_id:emp_id}, { $set: temClsData }, { returnDocument: 'after' });
        return true;
    } 
    catch (err) {
        return "Failed to Track User";
    }
}

async function checkInTrack(orgId,emp_id){
    try{
        //get the tempTrack data
        let checkTempData = await tempTrack.findOne({ orgId: orgId, emp_id: emp_id });
        let transportDetails = await transportSchema.findOne({orgId:orgId, emp_id: emp_id});
        //check if any geoLogs are present or not 
        if(checkTempData.geologs.length == 0){
            return true;
        }
        else{
            //Arranging the datisExist.geologsa for snap api
            let pathsData = checkTempData.geologs;
            let tempCordinateData='path=';
            let count = 1;
            for (let it of pathsData) {
                tempCordinateData = tempCordinateData+it.latitude+','+it.longitude;
                if(count != pathsData.length){
                    tempCordinateData = tempCordinateData+'|';
                }
                count++;
            }
            const url = config.get('googleSnapRoadApi') + '?interpolate=true&'+tempCordinateData+'&key='+config.get('googleSnapRoadSecretKey');
            let snapLocData = await axios.get(url);
            snapLocData = snapLocData.data.snappedPoints;
            const attachedData = trackArrange(pathsData,snapLocData);
            //storing the coordinates in taskSchema
            const isExist = await trackSchema.findOne({ orgId: orgId, emp_id: emp_id, date: checkTempData.geologs[0].time.split("T")[0] });
            let locData = {
                orgId: orgId,
                emp_id: emp_id,
                date: checkTempData.geologs[0].time.split("T")[0],
                distTravelled: 0,
                currentFrequency:transportDetails.currentFrequency,
                currentMode:transportDetails.currentMode,
            };
            var status,taskId;
            // Calculating Distance
            for (let item of attachedData) {
                const oldLogs = isExist.geologs[isExist.geologs.length - 1];
                const lastLogs = await distanceTrack(item.latitude, item.longitude, oldLogs.latitude, oldLogs.longitude);
                locData.distTravelled = isExist.distTravelled + lastLogs;
                status = item.status || 0;
                taskId = item.taskId || null;
                isExist.geologs.push({
                    'time': item.time,
                    'latitude': item.latitude,
                    'longitude': item.longitude,
                    'status': status,
                    'taskId': taskId
                });
                locData.geologs = isExist.geologs;
            }
            const updateTrackData = await trackSchema.findOneAndUpdate({ _id: isExist._id }, { $set: locData }, { returnDocument: 'after' });
            //Clearing the tempTracking data
            const temClsData = {
                orgId:orgId,
                emp_id:emp_id,
                geologs:[]
            }
            const updateTempTrackData = await tempTrack.findOneAndUpdate({ orgId: orgId, emp_id:emp_id}, { $set: temClsData }, { returnDocument: 'after' });   
            return true;
        }
    }
    catch(err){
        console.log(err)
        return res.send(Response.FailResp('Something Went Wrong', err))
    }
}

function trackArrange(tempData,snappedData){
    let finalSnappedArray = [];
    for (let item of tempData) {
        if(item.status != 0){
            let closestLocation=999999999;
            let snappedId = 0;
            let count = 0;
            for(let it of snappedData){
                let currentdist = distanceTrack(item.latitude,item.longitude,it.location.latitude,it.location.longitude)
                if(currentdist < closestLocation){
                    closestLocation = currentdist;
                    snappedId = count;
                }
                count++;
            }
            snappedData[snappedId].status = item.status;
            snappedData[snappedId].taskId = item.taskId;
        }
    }
    //Arranging the final Structure
    for (let item of snappedData){
        let tempArray = {};
        tempArray.latitude = item.location.latitude;
        tempArray.longitude = item.location.longitude;
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

export default new AttendanceService();
