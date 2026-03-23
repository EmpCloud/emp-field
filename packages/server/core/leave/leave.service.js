import Response from "../../response/response.js";
import leaveSchema from './leave.model.js';
import LeaveValidation from './leave.validate.js'
import config from 'config';
import axios from 'axios';

class leaveService {

    async createLeaveType(req, res) {

        try {
            const result = req.verified;
            let { orgId } = result?.userData?.userData;
            let { value, error } = LeaveValidation.createLeaveType(req.body);
            if (error) return res.send(Response.FailResp('Validation Failed', error));

            const { name, duration, number_of_days, carry_forward } = value;
            const leaveName = await leaveSchema.findOne({ name: name, orgId: orgId });
            if (leaveName) return res.send(Response.FailResp('leave name already exists'))
            value.orgId = orgId;
            let leaves = await leaveSchema.create(value);
            if (leaves) return res.send(Response.SuccessResp('Leave type created successfully', leaves))
            return res.send(Response.FailResp('Error while creating leave type'))
        } catch (err) {
            return res.send(Response.FailResp('Something went wrong', err))
        }
    }


    async getLeaveType(req, res) {

        try {
            const result = req.verified;
            let { orgId } = result?.userData?.userData;
            let { leaveId } = req.query;
            let leaves;
            if (leaveId) {
                leaves = await leaveSchema.findOne({ _id: leaveId, orgId: orgId });
                if (!leaves) return res.send(Response.FailResp('No Leave Found'))
                return res.send(Response.SuccessResp('Leave Type Fetched Successfully', leaves))
            }
            leaves = await leaveSchema.find({ orgId: orgId });
            if (leaves.length > 0) return res.send(Response.SuccessResp('Leave Type Fetched Fuccessfully', leaves))

            return res.send(Response.FailResp('No Leave Found'))
        } catch (err) {
            return res.send(Response.FailResp('Something went wrong', err))
        }
    }


    async updateLeaveType(req, res) {
        try {
            const result = req.verified;
            let { orgId } = result?.userData?.userData;

            let { value, error } = LeaveValidation.updateLeaveType(req.body);
            if (error) return res.send(Response.FailResp('Validation Failed', error));
            const leaveId = req.query?.leaveId;
            const { name, duration, number_of_days, carry_forward } = value;
            let leaves = await leaveSchema.findOneAndUpdate({ _id: leaveId, orgId: orgId }, { $set: value }, { returnDocument: 'after' });
            if (leaves) return res.send(Response.SuccessResp('Leave type updated successfully', leaves));

            return res.send(Response.FailResp('Error while updating leave type'))
        } catch (err) {
            return res.send(Response.FailResp('Something went wrong', err))
        }
    }

    async deleteLeaveType(req, res) {
        try {
            const result = req.verified;
            let { orgId } = result?.userData?.userData;
            let leaveId = req.query?.leaveId;
            let leaves = await leaveSchema.findOneAndDelete({ _id: leaveId, orgId: orgId });

            if (leaves) return res.send(Response.SuccessResp('Leave type deleted successfully', leaves));

            return res.send(Response.FailResp('Error while deleting leave type'))
        } catch (err) {
            return res.send(Response.FailResp('Something went wrong', err))
        }
    }

    async getLeaves(req, res){
        try{
            const result = req.verified;
            const body = req?.body;
            
            let {orgId,emp_id} = result?.userData?.userData;
            if(orgId) {
                const data = { organization_id: orgId, employee_id:emp_id, startDate:body.startDate, endDate:body.endDate, secretKey: config.get('emp_secret_key') };
                let leaves = await axios.post(config.get('empDomain')+'hrms/fetch-leaves', data);
                // console.log(leaves)
                 if (leaves?.data?.data?.length > 0) return res.send(Response.SuccessResp('Holiday Fetched Successfully', leaves?.data?.data))
                return res.send(Response.FailResp('No Leaves found'))
            }
            holiday = await holidaySchema.find({ orgId: orgId });
            if (holiday.length > 0) return res.send(Response.SuccessResp('Leaves Fetched Successfully', holiday))
            return res.send(Response.FailResp('No Leaves found'))
        }
        catch(err){
            console.log(err);
            return res.send(Response.FailResp('Something went wrong', err))
        }
    }

    async createLeave(req, res){
        try{
            const result = req.verified;
            let { orgId, emp_id, timezone} = result?.userData?.userData;
            const language = 'en';
            const {day_type, leave_type, start_date, end_date, reason} = req.body;
            const data = {  organization_id: orgId, 
                            employee_id:emp_id,
                            language:language, 
                            timezone:timezone, 
                            day_type:day_type,
                            leave_type:leave_type, 
                            start_date:start_date, 
                            end_date:end_date, 
                            reason:reason, 
                            secretKey: config.get('emp_secret_key') };
            let leaves = await axios.post(config.get('empDomain')+'hrms/create-field-leaves', data);
            if (leaves?.data?.code == 200) return res.send(Response.SuccessResp('Leave Created successfully', leaves?.data))
            else if (leaves?.data?.code == 400) return res.send(Response.FailResp('Leave Request Rejected', leaves?.data))
                return res.send(Response.FailResp('Something Went Wrong'))
        }
        catch(err){
            return res.send(Response.FailResp('Something went wrong', err))
        }
    }


    async leaveTypeOption(req, res){
        try{
            const result = req.verified;
            let { orgId } = result?.userData?.userData;
            const language = 'en';
            const data = {  organization_id: orgId,  
                            language: language,
                            secretKey: config.get('emp_secret_key') };
            let leaves = await axios.post(config.get('empDomain')+'hrms/field-leave-type', data);
            if (leaves?.data?.code == 200) return res.send(Response.SuccessResp('Leave Type Fetched successfully', leaves?.data))
                return res.send(Response.FailResp('Something Went Wrong'))
        }
        catch(err){
            return res.send(Response.FailResp('Something went wrong', err))
        }
    }

    async updateLeaves(req, res){
        try{
            const result = req.verified;
            let { orgId, emp_id} = result?.userData?.userData;
            const language = 'en';
            const {day_type, leave_type, leave_id, start_date, end_date, reason} = req.body;
            const data = {  organization_id: orgId, 
                            employee_id:emp_id,
                            leave_id:leave_id,
                            language:language, 
                            day_type:day_type,
                            leave_type:leave_type, 
                            start_date:start_date, 
                            end_date:end_date, 
                            reason:reason, 
                            secretKey: config.get('emp_secret_key') };
            let leaves = await axios.post(config.get('empDomain')+'hrms/update-field-leaves', data);
            if (leaves?.data?.code == 200) return res.send(Response.SuccessResp('Leave Request Updated Successfully', leaves?.data))
            else if (leaves?.data?.code == 400) return res.send(Response.FailResp('Leave Updation Rejected', leaves?.data))
                return res.send(Response.FailResp('Something Went Wrong'))
        }
        catch(err){
            return res.send(Response.FailResp('Something Went Worng', err));
        }
    }

    async deleteLeaves(req, res){
        try{
            const result = req.verified;
            let { orgId} = result?.userData?.userData;
            const language = 'en';
            const {leave_id} = req.body;
            const data = {  organization_id: orgId, 
                            leave_id:leave_id,
                            language:language, 
                            secretKey: config.get('emp_secret_key') };
            let leaves = await axios.post(config.get('empDomain')+'hrms/delete-field-leaves', data);
            if (leaves?.data?.code == 200) return res.send(Response.SuccessResp('Leave Request Deleted successfully', leaves?.data))
            else if (leaves?.data?.code == 400) return res.send(Response.FailResp('Leave Deletion Rejected', leaves?.data))
                return res.send(Response.FailResp('Something Went Wrong'))
        }
        catch(err){
            return res.send(Response.FailResp('Something Went Worng', err));
        }
    }



}

export default new leaveService()