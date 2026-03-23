import Response from "../../response/response.js";
import orgLocationSchema from './orgLocation.model.js';
import UserSchema from '../profile/profile.model.js';
import config from 'config';
import axios from 'axios';
import { ObjectId } from "mongodb";
import employeeLocationModel from "./employeeLocation.model.js";
import hrmsAdminValidate from "./hrmsAdmin.validate.js";
import adminModel from "../admin/admin.model.js";


class hrmsAdminService {

    async updateLocation(req, res) {
        try {
            const result = req.verified;
            let { orgId } = result?.userData?.userData;
            let data = req?.body;
            let locExist = await orgLocationSchema.find({ orgId: orgId, locationName: data.locationName });
            if (locExist.length > 0){
                let resultData = await orgLocationSchema.findOneAndUpdate({ orgId: orgId, locationName: data.locationName }, { $set: data }, { returnDocument: 'after' });
                if (resultData) {
                    return res.send(Response.SuccessResp("Location Details Updated Successfully", resultData))
                }
                else{
                    return res.send(Response.FailResp('Error while Updating Location Details'))
                }
            }
            else{
                const addData = await orgLocationSchema.create(data)
                if (addData) {
                    return res.send(Response.SuccessResp("Location Added Successfully", addData))
                }
                else{
                    return res.send(Response.FailResp('Error While Adding Location'))
                }
            }
        } catch (err) {
            return res.send(Response.FailResp('Something went wrong', err))
        }
    }

    async getLocationDetails(req, res){
        try{
            const result = req.verified;
            let { orgId } = result?.userData?.userData;
            let data = req?.body;
            let locExist = await orgLocationSchema.find({ orgId: orgId, locationName: data.locationName });
            if (locExist.length > 0){
                return res.send(Response.SuccessResp("Location Details Fetched Successfuly", locExist))
            }
            else{
                return res.send(Response.FailResp('No Details Found for'.data.locationName))
            }
        }
        catch(err){
            return res.send(Response.FailResp('Something Went Wrong', err))
        }
    }

    async getEmployeeConf(req, res){
        try{
            const result = req.verified;
            let { orgId} = result?.userData?.userData;
            let data = req?.body;
            let userData = await UserSchema.find({ orgId: orgId, emp_id: data.empId });
            //CHeck the organisation if the employee exist
            if(userData.length > 0){
                if(orgId != data.orgId){
                    return res.send(Response.FailResp('Invalid Employee Details',null))
                }
                else{
                    let empSettings = {      
                        fullName: userData[0].fullName,
                        email:userData[0].email,
                        emp_id:userData[0].emp_id,
                        geoLogsStatus:userData[0].geoLogsStatus,
                        frequency:userData[0].frequency,
                        isBioMetricEnabled:userData[0].isBioMetricEnabled,
                        isMobileDeviceEnabled:userData[0].isMobileDeviceEnabled,
                        isGeoFencingOn:userData[0].isGeoFencingOn,
                        isWebEnabled:userData[0].sisWebEnabled
                    }
                    return res.send(Response.SuccessResp("Employee Settings Fetched Successfully", empSettings))
                }
            }
            else{
                return res.send(Response.FailResp('Invalid Employee Details', null))
            }
            //else invalid employee id

            //

            // let data = req?.body;
            // let locExist = await orgLocationSchema.find({ orgId: data.orgId, locationName: data.locationName });
            // if (locExist.length > 0){
            //     return res.send(Response.SuccessResp("Location Details Fetched Successfuly", locExist))
            // }
            // else{
            //     return res.send(Response.FailResp('No Details Found for'.data.locationName))
            // }
        }
        catch(err){
            console.log(err);
            return res.send(Response.FailResp('Something Went Wrong', err))
        }
    }

    async updateEmployeeConf(req, res){
        try{
            let data = req?.body;
            let locExist = await orgLocationSchema.find({ orgId: data.orgId, locationName: data.locationName });
            if (locExist.length > 0){
                return res.send(Response.SuccessResp("Location Details Fetched Successfuly", locExist))
            }
            else{
                return res.send(Response.FailResp('No Details Found for'.data.locationName))
            }
        }
        catch(err){
            console.log(err);
            return res.send(Response.FailResp('Something Went Wrong', err))
        }
    }

    async orgLocationList(req, res){
        try{
            const result = req.verified;
            let { orgId } = result?.userData?.userData;
            let locExist = await orgLocationSchema.find({ orgId: orgId });
            if (locExist.length > 0){
                return res.send(Response.SuccessResp("Location Details Fetched Successfuly", locExist))
            }
            else{
                return res.send(Response.FailResp('No Details Found',null))
            }
        }
        catch(err){
            console.log(err);
            return res.send(Response.FailResp('Something Went Wrong', err))
        }
    }

    async updateEmployeeLocation(req,res){
        try{
            let {employeeId,address,latitude,longitude,range,geo_fencing,isMobEnabled} = req.body;
            const result = req.verified;
            let { orgId ,_id} = result?.userData?.userData;
            let isAdmin = await adminModel.findOne({_id:new ObjectId(_id)});
            if(!isAdmin) return res.send(Response.FailResp('Admin not found! Please provide valid Admin details.',isAdmin));
            let isEmployeeExist = await UserSchema.findOne({_id:new ObjectId(employeeId)});
            if(!isEmployeeExist) return res.send(Response.FailResp('Employee does not Exist!',isEmployeeExist));
            const { error ,value} = hrmsAdminValidate.validateEmployeeLocation(req?.body);
            if(error) return res.send(Response.FailResp(error?.message),400);
            let isEmployeeLocationExist = await employeeLocationModel.findOne({employeeId:new ObjectId(employeeId)});
            if(!isEmployeeLocationExist) return res.send(Response.FailResp('Employee Location does not exist!.',isEmployeeLocationExist));
            const updatedEmployeeLocation = await employeeLocationModel.findOneAndUpdate(
                { orgId, employeeId: new ObjectId(value?.employeeId) },
                {
                  $set: {
                    latitude: value?.latitude,
                    longitude: value?.longitude,
                    range: value?.range,
                    geo_fencing: value?.geo_fencing,
                    isMobEnabled: value?.isMobEnabled,
                    address: value?.address,
                  }
                },
                {
                  new: true // return the updated document
                  // upsert is not set, so it won't create a new one
                }
              );
              
            
            if(updatedEmployeeLocation){
                return res.send(Response.SuccessResp("Employee Location updated successfully", updatedEmployeeLocation))
            }
        }catch(error){
            console.log(error);
            return res.send(Response.FailResp('Something Went Wrong', error))
        }
    }

    async getEmployeeLocation(req,res){
        try{
            let {employeeId} = req.query;
            const result = req.verified;
            let { orgId ,_id} = result?.userData?.userData;
            let isAdmin = await adminModel.findOne({_id:new ObjectId(_id)});
            if(!isAdmin) return res.send(Response.FailResp('Admin not found! Please provide valid Admin details.',isAdmin));
            let isEmployeeExist = await UserSchema.findOne({_id:new ObjectId(employeeId)});
            if(!isEmployeeExist) return res.send(Response.FailResp('Employee does not Exist!',isEmployeeExist));

            const isEmployeeLocationExist = await employeeLocationModel.aggregate([
                {
                  $match: {
                    employeeId:new ObjectId(employeeId)
                  }
                },
                {
                  $lookup: {
                    from: 'userschemas', // collection name in MongoDB (always lowercase plural)
                    localField: 'employeeId',
                    foreignField: '_id',
                    as: 'employeeDetails'
                  }
                },
                {
                  $unwind: {
                    path: '$employeeDetails',
                    preserveNullAndEmptyArrays: true // optional, keeps records even if no match
                  }
                },
                {
                  $project: {
                    orgId: 1,
                    address: 1,
                    latitude: 1,
                    longitude: 1,
                    range: 1,
                    geo_fencing: 1,
                    isMobEnabled: 1,
                    createdBy: 1,
                    updatedBy: 1,
                    createdAt: 1,
                    updatedAt: 1,
                    employeeDetails: {
                      _id: 1,
                      name: 1,
                      email: 1,
                      fullName:1,
                      age:1,
                      gender:1,
                      profilePic:1
                    }
                  }
                }
              ]);
              
            if(isEmployeeLocationExist.length===0) return res.send(Response.FailResp('Employee Location not found!.',isEmployeeLocationExist));

            return res.send(Response.SuccessResp("Employee Location fetched successfully", isEmployeeLocationExist))

        }catch(error){
            console.log(error);
            return res.send(Response.FailResp('Something Went Wrong', error))
        }
    }
}

export default new hrmsAdminService()