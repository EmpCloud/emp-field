import Response from "../../response/response.js";
import holidaySchema from './holiday.model.js';
import holidayValidation from './holiday.validate.js'
import axios from 'axios'
import config from 'config'

class leaveService {

    async createHoliday(req, res) {

        try {
            const result = req.verified;
            let { orgId } = result?.userData?.userData;
            let data = req?.body;
            data.orgId = orgId;
            let { value, error } = holidayValidation.addNewHolidays(data);
            if (error) return res.send(Response.FailResp('Validation Failed', error));

            const { name, date } = value;
            let holiday_exist;
            holiday_exist = await holidaySchema.find({ name: name, orgId: orgId });
            if (holiday_exist.length > 0) return res.send(Response.FailResp('Holiday name already exists'))
            holiday_exist = await holidaySchema.find({ name: name, orgId: orgId, date: date });

            if (holiday_exist.length > 0) res.send(Response.FailResp('Holiday date already Exist.'))

            const add_holidays = await holidaySchema.create(value);
            if (add_holidays) return res.send(Response.SuccessResp('Holiday created successfully', value))
            return res.send(Response.FailResp('Error while creating holiday'))

        } catch (err) {
            console.log(err)
            return res.send(Response.FailResp('Something went wrong', err))
        }
    }


    async getHoliday(req, res) {

        try {
            const result = req.verified;
            let {orgId} = result?.userData?.userData;
           
            if(orgId) {
                const data = { organization_id: orgId,  secretKey: config.get('emp_secret_key') };
                let holiday = await axios.post(config.get('empDomain')+'hrms/fetch-holidays', data);
                console.log(holiday)
                if (holiday?.data?.data?.length > 0) return res.send(Response.SuccessResp('Holiday Fetched Successfully', holiday?.data?.data))
                return res.send(Response.FailResp('No Holiday Found'))
            }
            holiday = await holidaySchema.find({ orgId: orgId });
            if (holiday.length > 0) return res.send(Response.SuccessResp('Holiday Fetched Successfully', holiday))
            return res.send(Response.FailResp('No Holiday Found'))
        } catch (err) {
            console.log(err);
            return res.send(Response.FailResp('Something Went Wrong', err))
        }
    }


    async updateHoliday(req, res) {
        try {
            const result = req.verified;
            let { orgId } = result?.userData?.userData;

            let { value, error } = holidayValidation.updateHoliday(req.body);
            if (error) return res.send(Response.FailResp('Validation Failed', error.details[0].message));
            const holidayId = req.query?.holidayId;
            const { holiday_name, holiday_date } = value;

            let holidayDate = await holidaySchema.find({ _id: holidayId, date: holiday_date, orgId: orgId })
            if (holidayDate.length > 0) return res.send(Response.FailResp('Holiday already Exist on the data given!'))
            let holiday = await holidaySchema.findOneAndUpdate({ _id: holidayId }, { $set: value }, { returnDocument: 'after' });
            if (holiday) return res.send(Response.SuccessResp('Holiday updated successfully', holiday));

            return res.send(Response.FailResp('Error while updating holiday'))

        } catch (err) {
            return res.send(Response.FailResp('Something went wrong', err))
        }
    }

    async deleteHoliday(req, res) {
        try {
            const result = req.verified;
            let { orgId } = result?.userData?.userData;
            let holidayId = req.query?.holidayId;
            let holiday = await holidaySchema.findOneAndDelete({ _id: holidayId, orgId: orgId });
            if (holiday) return res.send(Response.SuccessResp('holiday deleted successfully', leaves));
            return res.send(Response.FailResp('Error while deleting holiday'))
        } catch (err) {
            return res.send(Response.FailResp('Something went wrong', err))
        }
    }
}

export default new leaveService()