import Response from '../../response/response.js';
import jwt from 'jsonwebtoken';
import Logger from '../../resources/logs/logger.log.js';
import UserSchema from '../profile/profile.model.js';
import ProfileValidation from '../profile/profile.validate.js';
import config from 'config';
import axios from 'axios';
import adminSchema from '../../core/admin/admin.model.js'
import taskModel from '../task/task.model.js';
import tagModel from '../tags/tag.model.js';
import clientSchema from '../client/client.model.js'
import trackModel from '../tracking/track.model.js';
import userModel from '../profile/profile.model.js'
import leaveModel from '../../core/leave/leave.model.js'
import moment from 'moment';
import { validateRequest } from 'twilio/lib/webhooks/webhooks.js';
import transportModel from '../transport/transport.model.js';
import { currencyValue } from '../../resources/utils/helpers/emp.helper.js';



class ReportsService {
    async fetchConsolidatedReport(req, res) {
        try {
            const result = req.verified;
            let { orgId } = result?.userData?.userData;
            let skip = parseInt(req.query.skip) || 0;
            let limit = parseInt(req.query.limit) || 10;
            let searchQuery = req.query.searchQuery;
            let { department, role, CountFilters ,empIds,location} = req.body;
            let exportReport = req.body.exportReport||false;
            const sortBy = {};
            sortBy[req?.query?.orderBy || 'createdAt'] = req?.query?.sort?.toString() === 'asc' ? 1 : -1;
            
            const isUserExist = await adminSchema.findOne({ email: result?.userData?.userData?.email });
            if (!isUserExist) return res.send(Response.FailResp(`Admin does not exist.`));
            let matchStage = { orgId: orgId ,status:1};
            
            if (empIds&&empIds?.length) {
                matchStage.emp_id = { $in: empIds };
            }
            const today = moment().format('YYYY-MM-DD');
            let startDate = req.body.startDate ? req.body.startDate : null;
            let endDate = req.body.endDate ? req.body.endDate : null;
            if(!startDate&&endDate || startDate&&!endDate){
                res.send(Response.FailResp(`Provide startDate and endDate both to Filter by Date.`));
            }

    
            if (department) {
                matchStage.department = department;
            }
            if (location) {
                matchStage.location = location;
            }
            if (role) {
                matchStage.role = role;
            }
            if (searchQuery) {
                matchStage.$or = [
                    { fullName: { $regex: searchQuery, $options: "i" } },
                    { gender: { $regex: searchQuery, $options: "i" } },
                    { email: { $regex: searchQuery, $options: "i" } },
                    { location: { $regex: searchQuery, $options: "i" } },
                    { department: { $regex: searchQuery, $options: "i" } },
                    { role: { $regex: searchQuery, $options: "i" } },
                    { emp_id: { $regex: searchQuery, $options: "i" } },
                    { orgId: { $regex: searchQuery, $options: "i" } },
                    { address1: { $regex: searchQuery, $options: "i" } },
                    { address2: { $regex: searchQuery, $options: "i" } },
                    { city: { $regex: searchQuery, $options: "i" } },
                    { state: { $regex: searchQuery, $options: "i" } },
                    { country: { $regex: searchQuery, $options: "i" } },
                    { zipCode: { $regex: searchQuery, $options: "i" } },
                    { phoneNumber: { $regex: searchQuery, $options: "i" } }
                    
                ];
            }
            const taskCountFilters = {};
            if (CountFilters&&CountFilters?.minValue<=CountFilters?.maxValue) {
                taskCountFilters[CountFilters.fieldName] = {
                    $gte: parseInt(CountFilters.minValue, 10),
                    $lte: parseInt(CountFilters.maxValue, 10),
                };
            }else if(CountFilters&&CountFilters.minValue && CountFilters.maxValue&&CountFilters.minValue>CountFilters.maxValue){
                res.send(Response.FailResp( 'minValue should be less than maxValue','Validation error'));
            }
            const matchConditions =(CountFilters&&CountFilters.fieldName)? Object.keys(taskCountFilters).reduce((conditions, field) => {
                conditions[`${field}`] = taskCountFilters[field];
                return conditions;
            }, {}):{};
            const pipeline = [
                {
                    $match: matchStage
                },
                {
                    $lookup: {
                        from: "taskschemas",
                        let: { emp_id: "$emp_id", orgId: "$orgId" },
                        pipeline: [
                            {
                                $match: {
                                    $expr: {
                                        $and: [
                                            { $eq: ["$emp_id", "$$emp_id"] },
                                            { $eq: ["$orgId", "$$orgId"] }
                                        ]
                                    },
                                    ...(startDate && {
                                        date: {
                                            $gte: startDate,
                                            $lte: endDate
                                        }
                                    })
                                }
                            },
                            {
                                $group: {
                                    _id: null,
                                    uniqueClientIds: { $addToSet: "$clientId" },
                                    taskCounts: {
                                        $push: {
                                            taskApproveStatus: "$taskApproveStatus",
                                            count: 1
                                        }
                                    }
                                }
                            },
                            {
                                $unwind: "$taskCounts"
                            },
                            {
                                $group: {
                                    _id: "$taskCounts.taskApproveStatus",
                                    count: { $sum: "$taskCounts.count" }
                                }
                            }
                        ],
                        as: "taskCounts"
                    }
                },
                {
                    $addFields: {
                        overallTaskCount: {
                            $sum: "$taskCounts.count"
                        }
                    }
                },
                {
                    $lookup: {
                        from: "taskschemas",
                        let: { emp_id: "$emp_id", orgId: "$orgId" },
                        pipeline: [
                            {
                                $match: {
                                    $expr: {
                                        $and: [
                                            { $eq: ["$emp_id", "$$emp_id"] },
                                            { $eq: ["$orgId", "$$orgId"] }
                                        ]
                                    },
                                    ...(startDate && {
                                        date: {
                                            $gte: startDate,
                                            $lte: endDate
                                        }
                                    })
                                }
                            },
                            {
                                $group: {
                                    _id: null,
                                    uniqueClientIds: { $addToSet: "$clientId" }
                                }
                            },
                            {
                                $project: {
                                    _id: 0,
                                    uniqueClientCount: { $size: "$uniqueClientIds" }
                                }
                            }
                        ],
                        as: "uniqueClientCountData"
                    }
                },
                {
                    $addFields: {
                        Clients: {
                            $arrayElemAt: ["$uniqueClientCountData.uniqueClientCount", 0]
                        }
                    }
                },
                {
                    $addFields: {
                        TasksPending: {
                            $ifNull: [
                                {
                                    $let: {
                                        vars: {
                                            task: {
                                                $arrayElemAt: [
                                                    {
                                                        $filter: {
                                                            input: "$taskCounts",
                                                            as: "task",
                                                            cond: { $eq: ["$$task._id", 0] }
                                                        }
                                                    },
                                                    0
                                                ]
                                            }
                                        },
                                        in: { $ifNull: ["$$task.count", 0] }
                                    }
                                },
                                0
                            ]
                        },
                        startTaskCount: {
                            $ifNull: [
                                {
                                    $let: {
                                        vars: {
                                            task: {
                                                $arrayElemAt: [
                                                    {
                                                        $filter: {
                                                            input: "$taskCounts",
                                                            as: "task",
                                                            cond: { $eq: ["$$task._id", 1] }
                                                        }
                                                    },
                                                    0
                                                ]
                                            }
                                        },
                                        in: { $ifNull: ["$$task.count", 0] }
                                    }
                                },
                                0
                            ]
                        },
                        TasksPaused: {
                            $ifNull: [
                                {
                                    $let: {
                                        vars: {
                                            task: {
                                                $arrayElemAt: [
                                                    {
                                                        $filter: {
                                                            input: "$taskCounts",
                                                            as: "task",
                                                            cond: { $eq: ["$$task._id", 2] }
                                                        }
                                                    },
                                                    0
                                                ]
                                            }
                                        },
                                        in: { $ifNull: ["$$task.count", 0] }
                                    }
                                },
                                0
                            ]
                        },
                        TasksResumed: {
                            $ifNull: [
                                {
                                    $let: {
                                        vars: {
                                            task: {
                                                $arrayElemAt: [
                                                    {
                                                        $filter: {
                                                            input: "$taskCounts",
                                                            as: "task",
                                                            cond: { $eq: ["$$task._id", 3] }
                                                        }
                                                    },
                                                    0
                                                ]
                                            }
                                        },
                                        in: { $ifNull: ["$$task.count", 0] }
                                    }
                                },
                                0
                            ]
                        },
                        TasksFinished: {
                            $ifNull: [
                                {
                                    $let: {
                                        vars: {
                                            task: {
                                                $arrayElemAt: [
                                                    {
                                                        $filter: {
                                                            input: "$taskCounts",
                                                            as: "task",
                                                            cond: { $eq: ["$$task._id", 4] }
                                                        }
                                                    },
                                                    0
                                                ]
                                            }
                                        },
                                        in: { $ifNull: ["$$task.count", 0] }
                                    }
                                },
                                0
                            ]
                        },
                        deleteTaskCount: {
                            $ifNull: [
                                {
                                    $let: {
                                        vars: {
                                            task: {
                                                $arrayElemAt: [
                                                    {
                                                        $filter: {
                                                            input: "$taskCounts",
                                                            as: "task",
                                                            cond: { $eq: ["$$task._id", 5] }
                                                        }
                                                    },
                                                    0
                                                ]
                                            }
                                        },
                                        in: { $ifNull: ["$$task.count", 0] }
                                    }
                                },
                                0
                            ]
                        }
                    }
                },
                {
                    $match: matchConditions
                },
                {
                    $project: {
                        password: 0,
                        forgotPasswordToken: 0,
                        forgotTokenExpire: 0,
                        passwordEmailSentCount: 0,
                        isMobileDeviceEnabled: 0,
                        isBioMetricEnabled: 0,
                        isWebEnabled: 0,
                        frequency: 0,
                        geoLogsStatus: 0,
                        snap_points_limit: 0,
                        snap_duration_limit: 0,
                        __v: 0,
                        taskCounts: 0,
                        uniqueClientCountData: 0
                    }
                },
                {
                    $sort: sortBy
                }

            ];
            
            const usersCount = (await UserSchema.aggregate(pipeline)).length;
            
            if (!exportReport) {
                pipeline.push(
                    { $skip: skip },
                    { $limit: limit }
                );
            }
            
            // console.log(JSON.stringify(pipeline, null, 2));
            const results = await UserSchema.aggregate(pipeline);
            
  
            let finalResults = {
                usersCount,
                allUsers: results,
            };
    
            res.send(Response.SuccessResp(`User's Fetched Successfully`, finalResults));
        } catch (err) {
            Logger.error(`Error in catch ${err}`);
            res.send(Response.FailResp('Unable to Fetch Details', err));
        }
    }
    async updateGeoFencing(req,res){

        try{
            const result = req.verified;
            const {orgId} = result?.userData?.userData;
    
            let emp_id = req?.query?.emp_id
            let geoFencing= req?.query?.geoFencing
            let Exists = await UserSchema.find({emp_id:emp_id,orgId:orgId})
            if (!Exists) return res.send(Response.SuccessResp("User does not Exists!."))
            let data={}
            const geoFencingBoolean = geoFencing.toLowerCase() === 'true';
            data.isGeoFencingOn = geoFencingBoolean ? 1 : 0;

            let updatedUserDetails = await UserSchema.findOneAndUpdate({emp_id:emp_id,orgId:orgId }, { $set: data }, { returnDocument: 'after' })
            let { forgotPasswordToken, forgotTokenExpire,numberValidateOtp,numberValidateOtpExpire,emailValidateToken,emailTokenExpire,passwordEmailSentCount,verificationEmailSentCount, ...filteredData } = updatedUserDetails.toJSON();
            res.send(Response.SuccessResp(`Geo-fencing has been successfully updated for user ${updatedUserDetails.fullName}`, { resultData: filteredData }));
    
        }catch (error) {
            return res.send(Response.FailResp('Something went wrong'));
        }
    
            
    }    

    async getUserDetails(req, res) {
        try {
            const result = req.verified;
            const { orgId } = result?.userData?.userData || {};
            const { employee_Id } = req.body;
    
            if (!orgId || !employee_Id) {
                return res.send(Response.FailResp("Missing required fields: orgId or employee_Id."));
            }
    
            const isUserExist = await adminSchema.findOne({ orgId });
            if (!isUserExist) {
                return res.send(Response.FailResp("Admin does not exist!"));
            }
    
            const userDetails = await UserSchema.findOne({ orgId, emp_id: employee_Id })
                .select("fullName age gender email profilePic location department role emp_id orgId address1 address2 city state country zipCode phoneNumber timezone")
                .lean(); // Converts to plain object for modification
    
            if (!userDetails) {
                return res.send(Response.FailResp(`User details with emp_id ${employee_Id} not available!`));
            }
    
            const highest = await taskModel.aggregate([
                {
                    $match: {
                        orgId: orgId, // Filter tasks by the same orgId
                        "value.convertedAmountInUSD": { $type: "number" },
                        taskVolume: { $type: "number" }
                    }
                },
                {
                    $group: {
                        _id: null,
                        highestConvertedAmountInUSD: { $max: "$value.convertedAmountInUSD" },
                        highestTaskVolume: { $max: "$taskVolume" }
                    }
                },
                {
                    $project: {
                        _id: 0,
                        highestConvertedAmountInUSD: 1,
                        highestTaskVolume: 1
                    }
                }
            ]);
    
            // Add the highest values to userDetails
            userDetails.highestConvertedAmountInUSD = highest?.[0]?.highestConvertedAmountInUSD || null;
            userDetails.highestTaskVolume = highest?.[0]?.highestTaskVolume || null;
    
            // Send the response
            return res.send(Response.SuccessResp("User details fetched successfully", userDetails));
        } catch (err) {
            Logger.error(`Error in getUserDetails: ${err.message}`);
            return res.send(Response.FailResp("Unable to fetch details", err));
        }
    }
    
    

    async taskListDetails(req, res) {
        try {
            const result = req.verified;
            let { orgId } = result?.userData?.userData;
            const isUserExist = await adminSchema.findOne({ orgId });
            let exportTaskDetails = req.query.exportTaskDetails||'false';
    
            if (!isUserExist) {
                return res.send(Response.FailResp(`Admin does not exist!`));
            }
    
            let employee_Id = req.body.employee_Id;
            let skip = parseInt(req.query.skip || 0);
            let limit = parseInt(req.query.limit || 10);
            const date = moment().format('YYYY-MM-DD');
            let taskStatus = req.body.taskStatus ?? null;
            let taskStage = req.body.taskStage ?? null;

            let minTaskVolume = typeof(req.body?.Volume?.minTaskVolume)==='number'?parseInt(req.body?.Volume?.minTaskVolume):null;
            let maxTaskVolume = typeof(req.body?.Volume?.maxTaskVolume)==='number'?parseInt(req.body?.Volume?.maxTaskVolume):null;

            let minAmount = typeof(req?.body?.Amount?.minAmount)==='number'?parseInt(req?.body?.Amount?.minAmount):null;
            let maxAmount = typeof(req?.body?.Amount?.maxAmount)==='number'?parseInt(req?.body?.Amount?.maxAmount):null;

            // Determine start and end dates using moment, falling back to today's date if not provided
            // let startDate = req.body.startDate ? moment(req.body.startDate).startOf('day').toDate() : moment(date).startOf('day').toDate();
            // let endDate = req.body.endDate ? moment(req.body.endDate).endOf('day').toDate() : moment(date).endOf('day').toDate();

            let startDate = req.body.startDate ? req.body.startDate : moment().format('YYYY-MM-DD');
            let endDate = req.body.endDate ? req.body.endDate : moment().format('YYYY-MM-DD');
    
            let condition = { orgId, emp_id: employee_Id };
    
            if (date) {
                condition.date = { $gte: startDate, $lte: endDate };
            }
            if(taskStatus>=0&&taskStatus<=5&&taskStatus!==null){
                condition.taskApproveStatus = {$in:[taskStatus],$nin:[5]}
            }else{
                condition = {
                    ...condition,
                    taskApproveStatus: { $nin: [5] },
                }
            }
                
            if (taskStage&&taskStage!=='') {
                condition.tagLogs = {
                    "$elemMatch": {
                        "tagName": taskStage 
                    }
                };
            }
            if (minTaskVolume !== null && maxTaskVolume !== null) {  
                condition.taskVolume = {};
                if (minTaskVolume >= 0) condition.taskVolume["$gte"] = minTaskVolume;
                if (maxTaskVolume >= 0) condition.taskVolume["$lte"] = maxTaskVolume;
            }
            
            
            if (minAmount != null && maxAmount != null&&minAmount!=='') {  
                condition["value.convertedAmountInUSD"] = {};
                if (minAmount >= 0) condition["value.convertedAmountInUSD"]["$gte"] = minAmount;
                if (maxAmount >= 0) condition["value.convertedAmountInUSD"]["$lte"] = maxAmount;
            }

            let totalCount = await taskModel.countDocuments(condition);
            let query = taskModel.find(condition);

            if (exportTaskDetails === 'false') {
                query = query.skip(skip);
            }
            
            if (exportTaskDetails === 'false') {
                query = query.limit(limit);
            }
            
            let modifiedArray = await query;
        const percentageMapping = {
            1: 100,
            2: 100,
            3: 100,
            4: 100,
            5: 100
        };
        const promises = modifiedArray?.map(async (obj) => {
            const taskPercentage=percentageMapping[obj?.taskApproveStatus] || 0
            const clientId = obj?.clientId;
            const clientData = await clientSchema.find({_id:clientId}).lean();
            const tagLogsWithColors = await Promise.all(
                (obj.tagLogs || []).map(async (log) => {
                    const tag = await tagModel.findOne({ tagName: log.tagName }).lean();
                    return {
                        ...log._doc,
                        color: tag ? tag.color : null 
                    };
                })
            );
            return { ...obj._doc,clientData ,taskPercentage,tagLogsWithColors};
        });
        
        let userTaskDetails = await Promise.all(promises);
        const userTaskDetailsWithoutTagLogs = userTaskDetails.map(({ tagLogs, ...rest }) => rest);

                                       
            let overAllTags = await tagModel.find({orgId:orgId,isActive:true}).select('tagName color tagDescription createdBy isActive')                                     
            let finalResult = {
                totalCount,
                userTaskDetails:userTaskDetailsWithoutTagLogs,
                overAllTags
            }                                     
    
            if (userTaskDetails.length > 0) {
                res.send(Response.SuccessResp(`Task Details Fetched Successfully`, finalResult));
            } else {
                res.send(Response.SuccessResp(`No task details found for ${employee_Id}.`));
            }
        } catch (err) {
            Logger.error(`Error in catch ${err}`);
            res.send(Response.FailResp('Unable to Fetch Details', err));
        }
    }


    async userStats(req,res){
        try{

            const result = req.verified;
            let { orgId } = result?.userData?.userData;
            const isAdminExist = await adminSchema.findOne({ orgId });
    
            if (!isAdminExist) {
                return res.send(Response.FailResp(`Admin does not exist!`));
            }
            const today = moment().format('YYYY-MM-DD');

            // Determine start and end dates using moment, falling back to today's date if not provided
            let start = req.body.startDate ? moment(req.body.startDate).startOf('day').toDate() : moment(today).startOf('day').toDate();
            let end = req.body.endDate ? moment(req.body.endDate).endOf('day').toDate() : moment(today).endOf('day').toDate();
            
            let condition = {};
            let taskStatus = req.body.taskStatus ?? null;
            let taskStage = req.body.taskStage ?? null;

            let minTaskVolume = typeof(req.body?.Volume?.minTaskVolume)==='number'?parseInt(req.body?.Volume?.minTaskVolume):null;
            let maxTaskVolume = typeof(req.body?.Volume?.maxTaskVolume)==='number'?parseInt(req.body?.Volume?.maxTaskVolume):null;

            let minAmount = typeof(req?.body?.Amount?.minAmount)==='number'?parseFloat(req?.body?.Amount?.minAmount):null;
            let maxAmount = typeof(req?.body?.Amount?.maxAmount)==='number'?parseFloat(req?.body?.Amount?.maxAmount):null;
            if(taskStatus>=0&&taskStatus<=5&&taskStatus!==null){
                condition.taskApproveStatus =  {$in:[taskStatus],$nin:[5]};
            }else{
                condition = {
                    ...condition,
                    taskApproveStatus: { $nin: [5] },
                }
            }
            if (taskStage&&taskStage!=='') {
                condition.tagLogs = {
                    "$elemMatch": {
                        "tagName": taskStage 
                    }
                };
            }
            if (minTaskVolume !== null && maxTaskVolume !== null) {  
                condition.taskVolume = {};
                if (minTaskVolume >= 0) condition.taskVolume["$gte"] = minTaskVolume;
                if (maxTaskVolume >= 0) condition.taskVolume["$lte"] = maxTaskVolume;
            }
            
            
            if (minAmount != null && maxAmount != null&&minAmount!=='') {  
                condition["value.convertedAmountInUSD"] = {};
                if (minAmount >= 0) condition["value.convertedAmountInUSD"]["$gte"] = minAmount;
                if (maxAmount >= 0) condition["value.convertedAmountInUSD"]["$lte"] = maxAmount;
            }

            let {employee_Id} = req.body
            let isUserExist = await UserSchema.find({orgId,emp_id:employee_Id});
            if(!isUserExist)return res.send(Response.FailResp(`User does not exist!`));
            let transportData = await transportModel.findOne({orgId,emp_id:employee_Id})
            let taskCounts = await taskModel.countDocuments({
                orgId,
                emp_id: employee_Id,
                ...condition,
                date: { $gte: req.body.startDate, $lte: req.body.endDate }
            });
            let tasks = await taskModel.find({
                orgId,
                emp_id: employee_Id,
                ...condition,
                date: { $gte: req.body.startDate, $lte: req.body.endDate }
            }).select('value taskVolume createdAt clientId');
            const uniqueClientIds = new Set();
            let totalAmount = 0;
            let netTaskVolume = 0;

            for (const doc of tasks) {
                if (doc.clientId) {
                    uniqueClientIds.add(doc.clientId);
                }
                if (typeof doc.value.amount === 'number') {
                    let newAmount = await currencyValue(doc.value.currency || 'INR', doc.value.amount);
                    totalAmount += newAmount;
                }
                if (typeof doc.taskVolume === 'number') {
                    netTaskVolume += doc.taskVolume;
                }
            }
         
            
            const uniqueClientIdCount = uniqueClientIds.size;
            let clientCounts = uniqueClientIdCount;
            const distanceTraveled = await trackModel.aggregate([
                {
                  $match: {
                    orgId,
                    emp_id: employee_Id,
                    date: { $gte: req.body.startDate, $lte: req.body.endDate }
                  }
                },
                {
                  $group: {
                    _id: null, 
                    totalDistance: { $sum: "$distTravelled" }
                  }
                }
              ]);
              
              const totalDistance = distanceTraveled.length > 0 ? distanceTraveled[0].totalDistance : 0;
              

            let allDetails = {
                taskCounts,
                clientCounts,
                distanceTraveled:totalDistance,
                taskValue:await currencyValue('USD',totalAmount),
                netTaskVolume,
                modeOfTransport:transportData?transportData?.currentMode:null

            }
            res.send(Response.SuccessResp(`User Stats Fetched Successfully`, allDetails));


        }catch(err){
            Logger.error(`Error in catch ${err}`);
            res.send(Response.FailResp('Unable to Fetch Details', err));
        }
    }


    async taskStatus(req,res){
        try{
            const result = req.verified;
            let { orgId, emp_id } = result?.userData?.userData;
            let empId = req.query.employee_Id;
            let isAdminDataExist =await adminSchema.findOne({orgId:orgId,emp_id:emp_id})
            let isUserExist = await UserSchema.find({orgId,emp_id:empId});
            if(!isUserExist)return res.send(Response.FailResp(`User does not exist!`));
            //checking if Admin exists or not
            if (!isAdminDataExist) return res.send(Response.FailResp(`Admin not exist.`));
            const today = moment().format('YYYY-MM-DD');

            // Determine start and end dates using moment, falling back to today's date if not provided
            let todayStart = req.body.startDate ? moment(req.body.startDate).startOf('day').toDate() : moment(today).startOf('day').toDate();
            let todayEnd = req.body.endDate ? moment(req.body.endDate).endOf('day').toDate() : moment(today).endOf('day').toDate();

            const pendingTask = await taskModel.countDocuments({
                orgId:orgId,
                emp_id:empId,
                createdAt: {
                    $gte: todayStart,
                    $lte: todayEnd
                },
                taskApproveStatus: { $in: [0] }
            });
            const startedTask = await taskModel.countDocuments({
                orgId:orgId,
                emp_id:empId,
                createdAt: {
                    $gte: todayStart,
                    $lte: todayEnd
                },
                taskApproveStatus: { $in: [1] }
            });
            const pausedTask = await taskModel.countDocuments({
                orgId:orgId,
                emp_id:empId,
                createdAt: {
                    $gte: todayStart,
                    $lte: todayEnd
                },
                taskApproveStatus: { $in: [2] }
            });
            const resumedTask = await taskModel.countDocuments({
                orgId:orgId,
                emp_id:empId,
                createdAt: {
                    $gte: todayStart,
                    $lte: todayEnd
                },
                taskApproveStatus: { $in: [3] }
            });
            const completedTask = await taskModel.countDocuments({
                orgId:orgId,
                emp_id:empId,
                createdAt: {
                    $gte: todayStart,
                    $lte: todayEnd
                },
                taskApproveStatus: { $in: [4] }
            });

            const deletedTask = await taskModel.countDocuments({
                orgId:orgId,
                emp_id:empId,
                createdAt: {
                    $gte: todayStart,
                    $lte: todayEnd
                },
                taskApproveStatus: { $in: [5] }
            });
            
            const overAllTasks = await taskModel.countDocuments({
                orgId:orgId,
                emp_id:empId,
                createdAt: {
                    $gte: todayStart,
                    $lte: todayEnd
                }
            });

            const taskStats = {
                pendingTask:pendingTask|| 0,
                startedTask:startedTask|| 0,
                pausedTask:pausedTask|| 0,
                resumedTask:resumedTask|| 0,
                completedTask:completedTask|| 0,
                deletedTask:deletedTask||0
            };
            const data = {
                taskStats,
                overAllTasks:overAllTasks|| 0
            }

            res.send(Response.SuccessResp(`User TaskStatus Fetched Successfully`, data));

        }catch(error){
            Logger.error(`Error in catch ${error}`);
            res.send(Response.FailResp('Unable to Fetch Details', error));
        }
    }
    async taskStages(req,res){
        try{
            const result = req.verified;
            let { orgId, emp_id } = result?.userData?.userData;
            let isAdminDataExist =await adminSchema.findOne({orgId:orgId,emp_id:emp_id})
            //checking if Admin exists or not
            if (!isAdminDataExist) return res.send(Response.FailResp(`Admin not exist.`));
            const today = moment().format('YYYY-MM-DD');

            // Determine start and end dates using moment, falling back to today's date if not provided
            let start = req.body.startDate ? moment(req.body.startDate).startOf('day').toDate() : moment(today).startOf('day').toDate();
            let end = req.body.endDate ? moment(req.body.endDate).endOf('day').toDate() : moment(today).endOf('day').toDate();
            let {employee_Id} = req.query

            const tagCount = await taskModel.aggregate([
                {
                  $match: {
                    orgId: orgId,
                    emp_id: employee_Id,
                    createdAt: {
                      $gte: start,
                      $lte: end
                    },
                    taskApproveStatus: { $nin: [5] },
                    tagLogs: { $ne: [] }
                  }
                },
                {
                  $project: {
                    lastTagLog: { $arrayElemAt: ["$tagLogs", -1] }
                  }
                },
                {
                  $match: {
                    "lastTagLog.tagName": { $ne: null }
                  }
                },
                {
                  $group: {
                    _id: "$lastTagLog.tagName",
                    count: { $sum: 1 }
                  }
                },
                {
                  $lookup: {
                    from: "tags",  
                    localField: "_id", 
                    foreignField: "tagName",  
                    as: "tagDetails"
                  }
                },
                {
                  $unwind: "$tagDetails"
                },
                {
                  $project: {
                    _id: 0,
                    tagName: "$_id",
                    count: 1,
                    tagDetails: 1
                  }
                }
              ]);
              let allTags = await tagModel.find({orgId,isActive:true});
              let updatedTags = [...tagCount]; // Start with a copy of the existing tags

                allTags.forEach((tag) => {
                const tagExists = tagCount.some(t => t.tagDetails._id.equals(tag._id));
                
                if (!tagExists) {
                    updatedTags.push({
                    count: 0,
                    tagDetails: tag,
                    tagName: tag.tagName
                    });
                }
                });

              // Calculate the total taskCount by summing the count for all tags
              const totalTaskCount = updatedTags.reduce((sum, item) => sum + item.count, 0);
              const transformedData = {
                taskCount: totalTaskCount, 
                tags: updatedTags.map(item => ({
                  tagName: item.tagName,
                  count: item.count,
                  tagDetails: item.tagDetails
                }))
              };
              
              res.send(Response.SuccessResp(`User TagStats Fetched Successfully`, transformedData));
              
        }catch(error){
            Logger.error(`Error in catch ${error}`);
            res.send(Response.FailResp('Unable to Fetch Details', error));
        }
    }

    async clientDetails(req,res){
        try{
            const results = req.verified;
            let { orgId, emp_id } = results?.userData?.userData;
            let isAdminDataExist =await adminSchema.findOne({orgId:orgId,emp_id:emp_id})
            let skip = parseInt(req.query.skip || 0);
            let limit = parseInt(req.query.limit || 10);
            let {employee_Id} = req.query;
            let exportClient = req.query.exportClient||'false';
            const today = moment().format('YYYY-MM-DD');

            let condition = {};
            let taskStatus = parseInt(req.body.taskStatus)||null;
            let taskStage = req.body.taskStage||null;

            let minTaskVolume = typeof(req.body?.Volume?.minTaskVolume)==='number'?parseInt(req.body?.Volume?.minTaskVolume):null;
            let maxTaskVolume = typeof(req.body?.Volume?.maxTaskVolume)==='number'?parseInt(req.body?.Volume?.maxTaskVolume):null;

            let minAmount = typeof(req?.body?.Amount?.minAmount)==='number'?parseFloat(req?.body?.Amount?.minAmount):null;
            let maxAmount = typeof(req?.body?.Amount?.maxAmount)==='number'?parseFloat(req?.body?.Amount?.maxAmount):null;

            if(taskStatus>=0&&taskStatus<=5&&taskStatus!==null){
                condition.taskApproveStatus = taskStatus;
            }
            if (taskStage&&taskStage!=='') {
                condition.tagLogs = {
                    "$elemMatch": {
                        "tagName": taskStage 
                    }
                };
            }
            if (minTaskVolume !== null && maxTaskVolume !== null) {  
                condition.taskVolume = {};
                if (minTaskVolume >= 0) condition.taskVolume["$gte"] = minTaskVolume;
                if (maxTaskVolume >= 0) condition.taskVolume["$lte"] = maxTaskVolume;
            }
            
            
            if (minAmount != null && maxAmount != null&&minAmount!=='') {  
                condition["value.convertedAmountInUSD"] = {};
                if (minAmount >= 0) condition["value.convertedAmountInUSD"]["$gte"] = minAmount;
                if (maxAmount >= 0) condition["value.convertedAmountInUSD"]["$lte"] = maxAmount;
            }

            if (!isAdminDataExist) return res.send(Response.FailResp(`Admin not exist.`));
            let start = req.body.startDate ? moment(req.body.startDate).startOf('day').toDate() : moment(today).startOf('day').toDate();
            let end = req.body.endDate ? moment(req.body.endDate).endOf('day').toDate() : moment(today).endOf('day').toDate();
                let contactedDetailsPipeline = [
                    {
                    $match: {
                        emp_id: employee_Id,
                        orgId,
                        createdAt: { $gte: start, $lte: end },
                        taskApproveStatus: { $ne: 0 },
                        ...condition
                    }
                    },
                    {
                    $group: {
                        _id: "$_id",
                        taskName:{ $first:"$taskName" },
                        clientDetails: { $first: "$clientDetails" }
                    }
                    },
                    {
                    $project: {
                        _id: 0,
                        taskName:1,
                        clientDetails: 1,
                        
                    }
                    }
                ];
                
                if (exportClient==='false') {
                    contactedDetailsPipeline.push({ $skip: skip });
                    contactedDetailsPipeline.push({ $limit: limit });
                }
                
                let pipeline = [
                    {
                    $match: {
                        emp_id: employee_Id,
                        orgId,
                        ...condition,
                        createdAt: { $gte: start, $lte: end }
                    }
                    },
                    {
                    $addFields: {
                        clientIdAsObjectId: { $toObjectId: "$clientId" }
                    }
                    },
                    {
                    $lookup: {
                        from: 'clientschemas',
                        localField: 'clientIdAsObjectId',
                        foreignField: '_id',
                        as: 'clientDetails'
                    }
                    },
                    {
                    $unwind: {
                        path: '$clientDetails',
                        preserveNullAndEmptyArrays: true
                    }
                    },
                    {
                    //facet runs multiple pipeline in single pipeline    
                    $facet: {
                        contactedDetails: contactedDetailsPipeline,
                        //For contacted and notContacted
                        counts: [
                        {
                            $group: {
                            _id: "$_id",
                            taskApproveStatus: { $first: "$taskApproveStatus" }
                            }
                        },
                        {
                            $group: {
                            _id: null,
                            approvedCount: {
                                $sum: { $cond: [{ $eq: ["$taskApproveStatus", 0] }, 1, 0] }
                            },
                            notApprovedCount: {
                                $sum: { $cond: [{ $ne: ["$taskApproveStatus", 0] }, 1, 0] }
                            }
                            }
                        }
                        ],
                        //For totalCount
                        totalCount: [
                        {
                            $group: {
                            _id: "$_id"
                            }
                        },
                        {
                            $count: "totalCount"
                        }
                        ]
                    }
                    }
                ];
  
              
            //   console.log(JSON.stringify(pipeline, null, 2));
              const result = await taskModel.aggregate(pipeline);
              const contactedDetails = result[0].contactedDetails;
              const counts = result[0].counts.length > 0 ? result[0].counts[0] : { approvedCount: 0, notApprovedCount: 0 };
              const totalCount = result[0].totalCount.length > 0 ? result[0].totalCount[0].totalCount : 0;
              
              let finalResult = {
                totalCount,
                clientCounts: {
                  contacted: counts.notApprovedCount,
                  notContacted: counts.approvedCount
                },
                clientDetails: contactedDetails
              };
              
              
              res.send(Response.SuccessResp('User Clients Fetched Successfully', finalResult));
              

        }catch(error){
            Logger.error(`Error in catch ${error}`);
            res.send(Response.FailResp('Unable to Fetch Details', error));
        }
    }

    async distanceTraveled(req,res){
        try{
            
            const result = req.verified;
            let { orgId, emp_id } = result?.userData?.userData;
            let isAdminDataExist =await adminSchema.findOne({orgId:orgId,emp_id:emp_id})
            let skip = parseInt(req.query.skip || 0);
            let limit = parseInt(req.query.limit || 10);
            let exportDistanceTraveled = req.query.exportDistanceTraveled||'false';
            //checking if Admin exists or not
            if (!isAdminDataExist) return res.send(Response.FailResp(`Admin not exist.`));
            const today = moment().format('YYYY-MM-DD');

            // Determine start and end dates using moment, falling back to today's date if not provided
            let start = req.body.startDate ? moment(req.body.startDate).startOf('day').toDate() : moment(today).startOf('day').toDate();
            let end = req.body.endDate ? moment(req.body.endDate).endOf('day').toDate() : moment(today).endOf('day').toDate();
            let {employee_Id} = req.query;
            let pipeline = [
                {
                  $match: {
                    orgId: orgId,
                    emp_id: employee_Id,
                    createdAt: {
                      $gte: start,
                      $lte: end
                    }
                  }
                },
                {
                  $project: {
                    _id: 1,
                    emp_id: 1,
                    orgId: 1,
                    distTravelled: 1,
                    currentFrequency: 1,
                    currentMode: 1,
                    date: 1,
                    checkIn: 1,
                    checkOut: 1,
                    createdBy: 1,
                    updatedBy: 1
                  }
                }
              ];
              
              let totalCountPipeline = [
                  ...pipeline,
                  {
                      $count: "totalCount"
                    }
                ];
            
            const totalCountResult = await trackModel.aggregate(totalCountPipeline);
            const totalCount = totalCountResult.length > 0 ? totalCountResult[0].totalCount : 0;
            
            
            if(exportDistanceTraveled==='false'){
              pipeline.push(
                { $skip: skip },
                { $limit: limit }
              );
            }
              
              const trackData = await trackModel.aggregate(pipeline);
              
              let finalResult = {
                totalCount,
                trackData
              };
              let newResult
              const dateDiff = (end - start) / (1000 * 60 * 60 * 24);
              if (dateDiff <= 7) {
                // Weekly Aggregation
                newResult = await trackModel.aggregate([
                  {
                    $match: {
                      orgId: orgId,
                      emp_id: employee_Id,
                      createdAt: {
                        $gte: start,
                        $lte: end
                      }
                    }
                  },
                  {
                    $addFields: {
                      dateISO: { $dateFromString: { dateString: "$date" } }
                    }
                  },
                  {
                    $group: {
                      _id: {
                        week: { $isoWeek: "$dateISO" },
                        dayOfWeek: { $isoDayOfWeek: "$dateISO" }
                      },
                      totalDistTravelled: { $sum: "$distTravelled" }
                    }
                  },
                  {
                    $project: {
                      _id: 0,
                      week: "$_id.week",
                      x: {
                        $switch: {
                          branches: [
                            { case: { $eq: ["$_id.dayOfWeek", 1] }, then: "Monday" },
                            { case: { $eq: ["$_id.dayOfWeek", 2] }, then: "Tuesday" },
                            { case: { $eq: ["$_id.dayOfWeek", 3] }, then: "Wednesday" },
                            { case: { $eq: ["$_id.dayOfWeek", 4] }, then: "Thursday" },
                            { case: { $eq: ["$_id.dayOfWeek", 5] }, then: "Friday" },
                            { case: { $eq: ["$_id.dayOfWeek", 6] }, then: "Saturday" },
                            { case: { $eq: ["$_id.dayOfWeek", 7] }, then: "Sunday" }
                          ],
                          default: "Unknown"
                        }
                      },
                      dayOfWeekNum: "$_id.dayOfWeek",
                      y: "$totalDistTravelled"
                    }
                  },
                  {
                    $sort: {
                      week: 1,
                      dayOfWeekNum: 1 // Sorting by the numeric value of the day of the week
                    }
                  },
                  {
                    $project: {
                      week: 0,
                      dayOfWeekNum: 0 
                    }
                  }
                ]);
              } else {
                // Yearly Aggregation
                newResult = await trackModel.aggregate([
                  {
                    $match: {
                      orgId: orgId,
                      emp_id: employee_Id,
                      createdAt: {
                        $gte: start,
                        $lte: end
                      }
                    }
                  },
                  {
                    $addFields: {
                      dateISO: { $dateFromString: { dateString: "$date" } }
                    }
                  },
                  {
                    $group: {
                      _id: { month: { $month: "$dateISO" } },
                      totalDistTravelled: { $sum: "$distTravelled" }
                    }
                  },
                  {
                    $project: {
                      _id: 0,
                      monthNum: "$_id.month",
                      x: {
                        $arrayElemAt: [
                          ["", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
                          "$_id.month"
                        ]
                      },
                      y: "$totalDistTravelled"
                    }
                  },
                  { $sort: { "_id.month": 1 } },
                  {
                    $sort: {
                      monthNum: 1 
                    }
                  },
                  {
                    $project: {
                      monthNum: 0 
                    }
                  }
                ]);
              }
              
              let newData ={
                finalResult,
                newResult
              }
              
              
              
            res.send(Response.SuccessResp(`User Distance Traveled Data Fetched Successfully`, newData));

        }catch(error){
            Logger.error(`Error in catch ${error}`);
            res.send(Response.FailResp('Unable to Fetch Details', error));
        }
    }
    


    async getIndividualAttendanceData(req,res){
        try {
            const result = req.verified;
            const data = req?.body;
            let { orgId, timezone } = result?.userData?.userData;
            let body = { organization_id: orgId, employee_id: data.empId, start_date: data.start_date, end_date: data.end_date, secretKey: config.get('emp_secret_key') };
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
                if(data.allData == false){
                    attendance.data.data[0].attendance = attendance.data.data[0].attendance.slice(data.skip*data.limit, data.skip*data.limit + data.limit);
                }
                //Counting Absent, Present and Leaves
                let leaveCount=0, absentCount=0, presentCount=0, dayoff=0;
                for(let it of attendance.data.data[0].attendance){
                    //present count
                    if (it.hasOwnProperty("start_time") && it.hasOwnProperty("end_time") && it.end_time != null) {
                        presentCount++;
                    }
                    //leave count
                    if(it.leave_name != "Unpaid"){
                        leaveCount++;
                    }
                    if(it.day_off == true && it.holiday_name == ""){
                        dayoff++;
                    }
                }
                attendance.data.data[0].leaveCount  = leaveCount;
                attendance.data.data[0].presentCount = presentCount;
                attendance.data.data[0].absentCount = attendance.data.data[0].attendance.length - (leaveCount + presentCount);
                return res.send(Response.SuccessResp('Attendance fetched successfully', attendance.data.data))
            }
            else{
                return res.send(Response.FailResp('No Attendance found'))
            }
        }
        catch (err) {
            return res.send(Response.FailResp('Something went wrong', err))
        }
    }

}

export default new ReportsService();

