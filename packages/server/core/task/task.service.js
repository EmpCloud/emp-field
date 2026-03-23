import taskSchema from './task.model.js';
import Response from '../../response/response.js';
import logger from '../../resources/logs/logger.log.js';
import taskValidation from './task.validate.js';
import clientSchema from '../client/client.model.js';
import trackSchema from '../tracking/track.model.js';
import axios from 'axios';
import tempTrack from '../tracking/temp_tracking.model.js';
import moment from 'moment-timezone';
import uuidv1 from 'uuidv1';
import config from 'config';
import { Storage } from '@google-cloud/storage';
import { UploadMessage } from '../../language/language.translator.js'
import { currencyValue } from '../../resources/utils/helpers/emp.helper.js';

const storage = new Storage({ keyFilename: 'storageconfig.json' });
const bucketName = config.get('BUCKET_NAME_BIOMETRICS');
const bucket = storage.bucket(bucketName);

const PNG_MIME_TYPE = 'image/png';
const JPEG_MIME_TYPE = 'image/jpeg';
const JPG_MIME_TYPE = 'image/jpg';
const SVG_MIME_TYPE = 'image/svg';
const SVG_XML_MIME_TYPE = 'image/svg+xml';
const TEXT_FILE = 'text.txt';
const DOC_FILE = 'text/doc';
const HTML_FILE = 'text/html';
const HTM_FILE = 'text/htm';
const PDF_FILE = 'text/pdf';
const CSV_FILE = 'text/csv';
const MP4_FILE = 'video/mp4';

const FILE_MAX_SIZE = 10 * 1024 * 1024;

const VIDEO_MAX_SIZE = 50 * 1024 * 1024;

const MIME_TYPES = [PNG_MIME_TYPE, SVG_MIME_TYPE, SVG_XML_MIME_TYPE, JPEG_MIME_TYPE, JPG_MIME_TYPE, TEXT_FILE, DOC_FILE, HTML_FILE, HTM_FILE, PDF_FILE, CSV_FILE];
const VIDEO_MIME_TYPES = [MP4_FILE];

import UserSchema from '../admin/admin.model.js';
import userModel from '../profile/profile.model.js'
import { ObjectId } from 'mongodb';
import tagModel from '../tags/tag.model.js';
import temp_test_trackingModel from '../tracking/temp_test_tracking.model.js';
class taskService {
    async createTask(req, res) {
        const result = req.verified;
        let { orgId, emp_id } = result?.userData?.userData;
    
        try {
            let data = req?.body;
            const { value, error } = taskValidation.createTask(data);
            if (error) {
                return res.send(Response.FailResp("Validation Failed", error));
            }
    
            if (typeof(data.value.amount) === "number") {
                data.value.convertedAmountInUSD = await currencyValue('USD', data.value.amount);
            }
    
            const clientExist = await clientSchema.find({ _id: data.clientId });
            if (clientExist.length === 0) {
                return res.send(Response.FailResp("Invalid Client Data"));
            }
    
            // Get excluded days from the user or allow all days by default
            const excludedDays = data?.recurrence?.excludedDays ?? []; // Defaults to an empty array if not provided
    
            // Handle recurrence logic
            const { recurrence } = data;
            let recurrenceId = uuidv1();

            if (recurrence?.startDate && recurrence?.endDate) {

                const { startDate, endDate } = recurrence;
                if (!moment(startDate).isValid() || !moment(endDate).isValid()) {
                    return res.send(Response.FailResp("Invalid recurrence date range."));
                }
    
                if (moment(startDate).isAfter(endDate)) {
                    return res.send(Response.FailResp("Recurrence startDate cannot be after endDate."));
                }
    
                let currentDate = moment(startDate);
                const tasksCreated = [];
    
                while (currentDate.isSameOrBefore(endDate)) {
                    const dayOfWeek = currentDate.day();
                    if (excludedDays.length > 0 && excludedDays.includes(dayOfWeek)) {
                        currentDate.add(1, 'days');
                        continue;
                    }
    
                    const taskDate = currentDate.format('YYYY-MM-DD');
                    
                    const isExist = await taskSchema.find({ 
                        orgId, 
                        taskName: data.taskName, 
                        clientId: data.clientId, 
                        date: taskDate 
                    });
    
                    if (isExist.length > 0) {
                        currentDate.add(1, 'days');
                        continue;
                    }
    
                    const taskData = {
                        ...data,
                        orgId,
                        emp_id,
                        date: taskDate,
                        start_time: `${taskDate} ${data.start_time.split(" ")[1]}`,
                        end_time: `${taskDate} ${data.end_time.split(" ")[1]}`,
                        recurrenceId: recurrenceId
                    };
    
                    const addData = await taskSchema.create(taskData);
                    if (addData) {
                        tasksCreated.push(addData);
                    }
    
                    currentDate.add(1, 'days');
                }
    
                if (tasksCreated.length > 0) {
                    return res.send(Response.SuccessResp("Tasks Created Successfully for the Recurrence Range.", tasksCreated));
                } else {
                    return res.send(Response.FailResp("No tasks were created. All tasks might already exist."));
                }
            } else {
                // Handle single date logic
                if (!data.date || !moment(data.date).isValid()) {
                    return res.send(Response.FailResp("Invalid or missing task date."));
                }
    
                const dayOfWeek = moment(data.date).day();
                if (excludedDays.length > 0 && excludedDays.includes(dayOfWeek)) {
                    return res.send(Response.FailResp("Tasks cannot be created on the specified excluded days."));
                }
    
                const isExist = await taskSchema.find({ 
                    orgId, 
                    taskName: data.taskName, 
                    clientId: data.clientId, 
                    date: data.date,
                });
    
                // if (isExist.length > 0) {
                //     return res.send(Response.FailResp("Task Name Already Present for the provided date."));
                // }
    
                data.orgId = orgId;
                data.emp_id = emp_id;
                data.recurrenceId = recurrenceId;
    
                const addData = await taskSchema.create(data);
                if (addData) {
                    return res.send(Response.SuccessResp("Task Created Successfully for the provided date.", addData));
                }
            }
        } catch (err) {
            logger.error(`${err}`);
            return res.send(Response.FailResp("Failed to Add Task", err));
        }
    }
    
    

    async fetchTask(req, res) {
        const result = req.verified;
        let { orgId, emp_id } = result?.userData?.userData;
    
        try {
            let condition = { orgId, emp_id, taskApproveStatus: { $ne: 5 } };
            let fetchData = await taskSchema.find(condition);
    
            fetchData = await Promise.all(fetchData.map(async task => {
                let clientData = await clientSchema.find({ _id: task.clientId });
    
                if (clientData.length !== 0) {
                    task = task.toObject();
                    task.clientName = clientData[0].clientName;
                    task.address1 = clientData[0].address1;
                    task.address2 = clientData[0].address2;
                    task.city = clientData[0].city;
                    task.longitude = clientData[0].longitude;
                    task.latitude = clientData[0].latitude;
                }
                return task;
            }));
    
            if (fetchData.length > 0) {
                return res.send(Response.SuccessResp("Task Fetched Successfully", fetchData));
            }
            return res.send(Response.FailResp("No Data Found"));
        } catch (err) {
            return res.send(Response.FailResp("Something Went Wrong", err));
        }
    }
    

    async updateTask(req, res) {
        const result = req.verified;
        let { orgId, emp_id } = result?.userData?.userData;
        try {
            let data = req?.body;
            const { value, error } = taskValidation.updateTask(data);
            if (error) {
                return res.send(Response.FailResp("Validation Failed", error))
            }
            if(data?.start_time && data?.end_time){
                data.taskApproveStatus=0
            }
            const clientExist = await clientSchema.find({_id: (data.clientId)});
            if(clientExist.length == 0){
                return res.send(Response.FailResp("Invalid Client Data"));
            }
            data.orgId = orgId;
            data.emp_id = emp_id;
            if (data.tagLogs && data.tagLogs.length > 0) {
                const tagLog = data.tagLogs[0];
                const tagNameToCheck = tagLog.tagName;
            
                if (!data.taskId) return res.send(Response.FailResp("Task ID is required"));
                if (!data.orgId) return res.send(Response.FailResp("Organization ID is required"));
            
                let existingTask = await taskSchema.findOne({ _id: new ObjectId(data.taskId) });
                if (!existingTask) return res.send(Response.FailResp("Task not found"));
            
                let tagExist = await tagModel.findOne({ orgId, tagName: tagNameToCheck });
                if (!tagExist) {
                    return res.send(Response.FailResp(`Tag ${tagNameToCheck} does not exist for the Organization`));
                }
            
                const tagExists = existingTask.tagLogs.some(tag => tag.tagName === tagNameToCheck);
            
                if (!tagExists) {
                    data.tagLogs = [...existingTask.tagLogs,tagLog];
                }else{
                    data.tagLogs = [...existingTask.tagLogs];
                }
            }
            if (typeof(data.value.amount) === "number") {
                data.value.convertedAmountInUSD = await currencyValue('USD', data.value.amount);
            }
            
            const existingTask = await taskSchema.findOne({ _id: data.taskId });
            const existingFiles = existingTask.files.map(file => file.url);
            const existingImages = existingTask.images.map(image => image.url);
            let updateObj = data;
            if (Array.isArray(data.files) && data.files.length > 0) {
                const newFiles = data.files.filter(file => !existingFiles.includes(file.url));
                if(!(existingTask.files.length<=5 && newFiles?.length >= 0 && (newFiles?.length <= (5-existingTask.files.length)))){
                    return res.send(Response.FailResp(`${existingTask.taskName} Task reached maximum limit to upload Documents!`,"Validation Failed"))
                }
                if (newFiles.length > 0) {
                    updateObj.$push = {
                        files: {
                            $each: newFiles
                        }
                    };
                }
            }
            
            if (Array.isArray(data.images) && data.images.length > 0) {
                const newImages = data.images.filter(image => !existingImages.includes(image.url));
                if(!(existingTask.images.length<=5 && newImages?.length >= 0 && (newImages?.length <= (5-existingTask?.images.length)))){
                    return res.send(Response.FailResp(`${existingTask.taskName} Task reached maximum limit to upload Images!`,"Validation Failed"))
                }
                if (newImages.length > 0) {
                    updateObj.$push = updateObj.$push || {};
                    updateObj.$push.images = {
                        $each: newImages
                    };
                }
            }
            delete updateObj.files
            delete updateObj.images
            const updateTaskData = await taskSchema.findOneAndUpdate(
                { _id: data.taskId },
                updateObj,
                { returnDocument: 'after' }
            );
            
            
            if (updateTaskData) {
                return res.send(Response.SuccessResp("Task Updated Successfully",updateTaskData));
            }
            
        } catch (err) {
            logger.error(`${err}`);
            return res.send(Response.FailResp("Something Went Wrong", err));
        }
    }

    async updateStatus(req, res){
        const statusArr = [1,2,3,4,5];
        const result = req.verified;
        let { orgId, emp_id, timezone } = result?.userData?.userData;
        try {
            let data = req?.body;
            const today = moment().format('YYYY-MM-DD');

            //Check if user is checked In or not
            const checkInCheck = await trackSchema.find({orgId:orgId,emp_id:emp_id,date:data.currentDateTime.split(" ")[0]});
            if(checkInCheck.length == 0){
                return res.send(Response.FailResp("Task Status Update is Restricted", null))
            }
            else if(checkInCheck[0].checkIn == null || checkInCheck[0].checkOut != null){
                return res.send(Response.FailResp("Task Status Update is Restricted", null))
            }else if(checkInCheck[0].checkIn !== null && checkInCheck[0].checkOut !== null){
                return res.send(Response.FailResp("You have been logged out. Unable to perform the update.", "Update Restricted"))

            }
            
            if (!statusArr.includes(data.status)) {
                return res.send(Response.FailResp("Invalid Status", null))
            }
            const clientExist = await taskSchema.find({_id:new ObjectId(data.taskId), orgId: orgId,date:today});
            if(clientExist.length === 0){
                return res.send(Response.FailResp("Task updates are restricted for future tasks!"));
            }

            const incompleteTasks = await taskSchema.find({
                orgId: orgId,
                emp_id:emp_id,
                _id: { $ne:new ObjectId(data?.taskId) },
                date: data.currentDateTime.split(" ")[0],
                taskApproveStatus: { $in: [1, 3] }
                });
                if(incompleteTasks?.length){
                    return res.send(Response.FailResp("Please Complete Previously Running Task"));
                }

            if(data.status == 1){
                data.empStartTime = data.currentDateTime;
            }
            else if(data.status == 4){
                data.empEndTime = data.currentDateTime;
            }
            if (data.tagLogs && data.tagLogs.length > 0) {
                const tagLog = data.tagLogs[0];
                const tagNameToCheck = tagLog.tagName;
            
                if (!data.taskId) return res.send(Response.FailResp("Stage ID is required"));
            
                let existingTask = await taskSchema.findOne({ _id: new ObjectId(data.taskId) });
                if (!existingTask) return res.send(Response.FailResp("Stage not found"));
            
                let tagExist = await tagModel.findOne({ orgId, tagName: tagNameToCheck });
                if (!tagExist) {
                    return res.send(Response.FailResp(`Stage ${tagNameToCheck} does not exist for the Organization`));
                }
            
                const tagExists = existingTask.tagLogs.some(tag => tag.tagName === tagNameToCheck);
            
                if (!tagExists) {
                    data.tagLogs = [...existingTask.tagLogs,tagLog];
                }else{
                    data.tagLogs = [...existingTask.tagLogs];
                }
            }
            data.taskApproveStatus = data.status;
            const udateTaskData = await taskSchema.findOneAndUpdate({ _id: (data.taskId) }, { $set: data }, { returnDocument: 'after' });
            if (udateTaskData) {
                if(data.status !== 5){
                    let locData = {
                        orgId : orgId,
                        emp_id :emp_id,
                        date : data.currentDateTime.split(' ')[0]
                    };
                    const isExist = await tempTrack.find({orgId: orgId, emp_id: emp_id});
                    if (isExist.length > 0) {
                        isExist[0].geologs.push({
                            'time':moment().tz(timezone).format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
                            'latitude':data.latitude,
                            'longitude':data.longitude,
                            'status':data.status,
                            'taskId':data.taskId
                        })

                            const geolog = {
                              time: moment().tz(timezone).format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
                              latitude: data.latitude,
                              longitude:data.longitude,
                              status: data.status,
                              taskId: data.taskId
                            };
                        locData.geologs = isExist[0].geologs;
                        const result = await temp_test_trackingModel.findOneAndUpdate(
                            { orgId, emp_id },
                            { $push: { geologs: geolog } },
                            { new: true, upsert: true } 
                          );
                        const udateTrackData = await tempTrack.findOneAndUpdate({ _id: (isExist[0]._id) }, { $set: locData }, { returnDocument: 'after' });
                        if(udateTrackData){
                            return res.send(Response.SuccessResp("Status Updated Successfully.", udateTrackData));
                        }
                        else{
                            return res.send(Response.FailResp("Something Went Wrong", null));
                        }
                    }else{
                        locData.geologs = [{
                            'time': moment().tz(timezone).format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
                            'latitude': data.latitude,
                            'longitude': data.longitude,
                            'date': data.currentDateTime.split(' ')[0],
                            'status': data.status,
                            'taskId': data.taskId 
                        }];
                        
                        const addData = await tempTrack.create(locData);
                        const geolog = {
                            time: moment().tz(timezone).format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
                            latitude: data.latitude,
                            longitude:data.longitude,
                            status: data.status||0,
                            taskId: data.taskId||null
                          }
                          const result = await temp_test_trackingModel.findOneAndUpdate(
                            { orgId, emp_id },
                            { $push: { geologs: geolog } },
                            { new: true, upsert: true } 
                          );
                        if(addData){
                            return res.send(Response.SuccessResp("Status Updated Successfully.", addData));
                        }
                    }
                    
                }
                else{
                    return res.send(Response.SuccessResp('Task Deleted SuccessFully'))
                } 
                return res.send(Response.FailResp('Something went wrong'))
            }
            
        } catch (err) {
            logger.error(`${err}`);
            console.log(err);
            return res.send(Response.FailResp("Something Went Wrong", err));
        }
    }

    async deleteTask(req, res) {
        const result = req.verified;
        let { orgId } = result?.userData?.userData;
        try {
            let data = req?.body;
            const clientExist = await taskSchema.find({_id: (data.taskId), orgId: orgId});
            if(clientExist.length == 0){
                return res.send(Response.FailResp("Invalid Task Id"));
            }
            data.taskApproveStatus = 5;
            const udateTaskData = await taskSchema.findOneAndUpdate({ _id: (data.taskId) }, { $set: data }, { returnDocument: 'after' });
            if (udateTaskData) {
                return res.send(Response.SuccessResp("Task Deleted Successfully"));
            }
        } catch (err) {
            logger.error(`${err}`);
            return res.send(Response.FailResp("Failed to Delete Task", err));
        }

    }
    async approveTask(req, res) {
        const result = req?.verified;
        let { userType } = result.userData?.userData;
        try {
            if (userType == 1) {
                return res.send(Response.FailResp("You can't able to able to approve the task", err));
            }
            let taskIds = req?.body?.taskIds;
            let status = req.query.taskapproveStatus;
            let allTaskIds = [];
            taskIds.map(async (ele) => {
                let findMatchedDocument = await taskSchema.findOne({ _id: ele.Id });
                if (!findMatchedDocument) { return res.send(Response.FailResp(`${ele.Id} Task Id is invalid, Please check`)); }
                if (findMatchedDocument) { allTaskIds.push(ele.Id) };
            })
            const updateOperation = { $set: { taskApproveStatus: status } };
            const condition = { _id: allTaskIds };
            const result = await taskSchema.updateMany(condition, updateOperation);
            if (result.modifiedCount > 0) {
                return res.send(Response.SuccessResp("Tasks approved successfully.", result));
            } else {
                return res.send(Response.SuccessResp("Failed to update task.", result));
            }

        } catch (err) {
            logger.error(`${err}`);
            return res.send(Response.FailResp("Failed to approve task", err));
        }
    }

    async filterTask(req, res) {
        const result = req.verified;
        let { orgId, emp_id } = result?.userData?.userData;
    
        try {
            //0:all,1:current,4:finished
            let data = req?.body;
            let condition;
            let startDate = data?.date + ' ' + '00:00:00';
            let endDate = data?.date + ' ' + '23:59:59';
            if(data.status === 1 || data.status === 4){
                condition = {
                    orgId,
                    emp_id,
                    $and: [
                      { taskApproveStatus: { $ne: 5 } },
                      { taskApproveStatus: data.status===1?{$in:[1,2,3]}:data.status }
                    ],
                    start_time: { $gte: startDate },
                    end_time: { $lte: endDate },
                  };
                  
            }
            else{
                condition = {
                    orgId,
                    emp_id,
                    start_time: { $gte: startDate },
                    end_time: { $lte: endDate },
                    taskApproveStatus: { $ne: 5 }
                  };
            }
            let fetchData = await taskSchema.find(condition);
    
            fetchData = await Promise.all(fetchData.map(async task => {
                let clientData = await clientSchema.find({ _id:new ObjectId(task.clientId)});
    
                if (clientData.length !== 0) {
                    task = task.toObject();
                    task.clientName = clientData[0].clientName;
                    task.address1 = clientData[0].address1;
                    task.address2 = clientData[0].address2;
                    task.city = clientData[0].city;
                    task.longitude = clientData[0].longitude;
                    task.latitude = clientData[0].latitude;
                }
                console.log(task?.tagLogs[task?.tagLogs.length-1]);
                console.log(task?.tagLogs.length);
                let tagData = await tagModel.findOne({tagName:task?.tagLogs[task?.tagLogs.length-1]?.tagName,orgId});
                task.currentTagDetails = tagData?tagData:{};
                return task;
            }));
    
            if (fetchData.length > 0) {
                return res.send(Response.SuccessResp("Task Fetched Successfully", fetchData));
            }
            return res.send(Response.FailResp("No Data Found"));
        } catch (err) {
            console.log(err);
            return res.send(Response.FailResp("Something Went Wrong", err));
        }
    }

    async uploadTaskData(req,res){
        const result = req.verified.userData.userData;
        try {
            let folderName;

            folderName = 'fieldTracking/task';
            if (!req.files) return res.send(Response.taskFailResp(UploadMessage['FILE_REQUIRED'][language ?? 'en']));
            if (req.files.length > 10) return res.send(Response.taskFailResp(UploadMessage['FILE_LIMIT_REACHED'][language ?? 'en']));

            let filesUrls = [];
            let negative = 0;
            let positive = 0;
            let result_status;

            req.files.map(async item => {
                if (item.size > FILE_MAX_SIZE && MIME_TYPES.find(type => type == item.mimetype)) {
                    negative++;
                    filesUrls.push({ message: 'Image size too large', fileName: item.originalname });
                } else if (item.size > VIDEO_MAX_SIZE && VIDEO_MIME_TYPES.find(type => type == item.mimetype)) {
                    negative++;
                    filesUrls.push({ message: 'video size too large', fileName: item.originalname });
                } else {
                    let url = await uploadImage(item, folderName);
                    positive++;
                    filesUrls.push({ url, message: 'Upload Successfully' });
                }
                if (positive > 0 && negative == 0) {
                    result_status = 200;
                } else if (negative > 0 && positive == 0) {
                    result_status = 400;
                } else if (positive > 0 || negative > 0) {
                    result_status = 201;
                }
                if (req?.files.length == filesUrls.length)
                    res.json({
                        code: result_status,
                        data: { filesUrls: filesUrls.filter(item => item.message === 'Upload Successfully') },
                        error: filesUrls.filter(item => item.message !== 'Upload Successfully'),
                    });
            });
            return true;
        } catch (err) {
            return res.send(Response.taskFailResp(UploadMessage['FAILED_TO_UPLOAD_FILES'][language ?? 'en'], err.message));
        }
    }

    async deleteDocs(req,res){
        try{
            const result = req.verified;
            let { orgId, emp_id,email: folderName } = result?.userData?.userData;
            let update;
            let data = req.body;
            const { value, error } = taskValidation.deleteDocument(data);
            if (error) {
                return res.send(Response.FailResp("Validation Failed", error))
            }
            let isExist = await userModel.findOne({orgId, emp_id});
            if(!isExist)return res.send(Response.FailResp("User does not exist!"));
            let {taskId,docIds} = req.body;
            const objectIds = docIds.map(id => new ObjectId(id));
            const filter = { _id: new ObjectId(taskId) };
            let isExistTaskId = taskSchema.findOne({_id:new ObjectId(taskId)});
            if(!isExistTaskId)res.send(Response.FailResp(`Task with Id ${taskId} not available, please provide valid taskId`, error));
            update = {
                $pull: {
                    images: { _id: { $in: objectIds } },
                    files: { _id: { $in: objectIds } }
                }
            };
            const results = await taskSchema.updateOne(filter, update);
            if (results.modifiedCount === 0) {
                return res.status(404).json({ message: 'Task or media not found' });
            }
            return res.send(Response.SuccessResp("Documents deleted Successfully", results));

        }catch(error){
            return res.send(Response.FailResp("Something Went Wrong", error));
        }
    }




 //Notification Services
 async getNotification(req, res) {
     try {
        const result = req.verified;
        let { orgId, emp_id } = result?.userData?.userData;
        let isExist = await userModel.findOne({orgId, emp_id});
        if(!isExist)return res.send(Response.FailResp("User does not exist!"));

        const todayStart = moment().startOf('day').toDate();
        const date = moment().format('YYYY-MM-DD');

        let previousTasks = await taskSchema.find({
            emp_id,
            orgId,
            taskApproveStatus: { $in: [0, 2] },
            date: { $lt: date } 
        });
        
        const startOfDay = moment().startOf('day').format('YYYY-MM-DD HH:mm');
        const endOfDay = moment().endOf('day').format('YYYY-MM-DD HH:mm');
        
        const startOfDayISO = moment(startOfDay, 'YYYY-MM-DD HH:mm').toISOString();
        const endOfDayISO = moment(endOfDay, 'YYYY-MM-DD HH:mm').toISOString();

        
        let rescheduledTasks = await taskSchema.find({
            emp_id,
            orgId,
            taskApproveStatus: { $in: [0, 2] },
            createdAt: { $lt: startOfDayISO },
            $and: [
                { start_time: { $gte: startOfDayISO, $lte: endOfDayISO } },
                { end_time: { $gte: startOfDayISO, $lte: endOfDayISO } }
            ]
        });
        
        const rescheduledTaskIds = new Set(rescheduledTasks.map(task => task._id.toString()));
        
        const filteredPreviousTasks = previousTasks.filter(task => !rescheduledTaskIds.has(task._id.toString()));
        
        let finalData = {
            previousTasks: filteredPreviousTasks,
            rescheduledTasks
        };
        
        if (finalData) {
            return res.send(Response.SuccessResp("Notifications Fetched Successfully", finalData));
        }
        return res.send(Response.FailResp("No Data Found"));
    } catch (err) {
        console.log(err);
        return res.send(Response.FailResp("Something Went Wrong", err));
    }
}
    
}


const uploadImage = (file, folderName) =>
    new Promise(async (resolve, reject) => {
        let innerFolder;
        if (file.mimetype == PNG_MIME_TYPE || file.mimetype == JPEG_MIME_TYPE || file.mimetype == JPG_MIME_TYPE || file.mimetype == SVG_MIME_TYPE || file.mimetype == SVG_XML_MIME_TYPE) {
            innerFolder = `${folderName}/Image`
        }
        else if (file.mimetype == MP4_FILE) {
            innerFolder = `${folderName}/Video`
        }
        else if (file.mimetype == TEXT_FILE || DOC_FILE || HTML_FILE || HTM_FILE || PDF_FILE || CSV_FILE) {
            innerFolder = `${folderName}/Document`
        }
        const { originalname, buffer } = file;
        try {
            let folderExist = checkFolderExists(bucketName, innerFolder)
            if (!folderExist) {
                await storage.bucket(bucketName).file(innerFolder).save('');
            }
            const blob = storage.bucket(bucketName).file(`${innerFolder}/${originalname.replace(/ /g, '_')}`);
            const blobStream = blob.createWriteStream({
                resumable: false,
            });

            blobStream
                .on('finish', async () => {
                    const options = {
                        entity: 'allUsers',
                        role: storage.acl.READER_ROLE,
                      };
            
                    try {

                        const corsConfiguration = [
                            {
                                origin: ['*'], 
                                responseHeader: ['Content-Type'],
                                method: ['GET', 'HEAD', 'PUT', 'POST', 'DELETE'],
                                maxAgeSeconds: 3600,
                            },
                        ];
                    
                            await storage.bucket(bucketName).setCorsConfiguration(corsConfiguration);

                        await blob.acl.add(options);
                        const publicUrl = `https://storage.googleapis.com/${bucketName}/${blob.name}`;

                        resolve(publicUrl);
                    } catch (aclError) {
                        reject(`Error applying ACL: ${aclError}`);
                    }
                })
                .on('error', (err) => {
                    reject(`Unable to upload file, something went wrong: ${err}`);
                })
                .end(buffer);
        } catch (err) {
            reject(`Error uploading image: ${err}`);
        }
    });

    async function checkFolderExists(bucketName, folderPath) {
        try {
            const [files] = await storage.bucket(bucketName).getFiles({
                prefix: folderPath,
                maxResults: 1,
            });
    
            return files.length > 0;
        } catch (err) {
            logger.error(err);
            throw err;
        }
    }
export default new taskService();