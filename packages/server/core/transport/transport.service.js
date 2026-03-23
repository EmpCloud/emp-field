import jwt from 'jsonwebtoken';
import config from 'config';
import moment from 'moment';
import Logger from '../../resources/logs/logger.log.js';
import userModel from '../../core/profile/profile.model.js'
import transportModel from './transport.model.js';
import Response from '../../response/response.js';
import transportValidate from './transport.validate.js';
import adminModel from '../admin/admin.model.js';


class transportServices {
    async empModeOfTransport(req, res) {
        const result = req.verified;
        let { orgId, emp_id } = result?.userData?.userData;
        let {currentMode} = req.body
        let data = req.body

        let Exists = await userModel.findOne({orgId:orgId,emp_id:emp_id})
        if(!Exists) return res.send(Response.FailResp(`User does not Exist!`));
        let transportData = await transportModel.findOne({orgId:orgId,emp_id:emp_id})
        const { value, error } = transportValidate.transportModeValidate(data);
        if (error) {
            return res.send(Response.FailResp("Validation Failed", error))
        }

        let newValue ={}
        if(currentMode){
            const result = transportData?.defaultConfig?.find(item => item.mode === currentMode)
            newValue.currentMode = result.mode
            newValue.currentFrequency = result.frequency
            newValue.currentRadius = result.radius
        }

        let transportUpdated = await transportModel.findOneAndUpdate({emp_id:emp_id,orgId:orgId}, { $set: newValue }, { returnDocument: 'after' })
        return res.send(Response.SuccessResp("Transportation Mode saved Successfully.", transportUpdated));
    }


    async empUpdFreqRad(req, res) {
        const result = req.verified;
    let { orgId, emp_id } = result?.userData?.userData;
    let { Employee_orgId, Employee_id } = req.query;
    let { configFrequency, configRadius, configMode } = req.body;
    let data = req.body;

    let Exists = await adminModel.findOne({ orgId: orgId, emp_id: emp_id });
    if (!Exists) return res.send(Response.FailResp(`Admin does not Exist!`));
    
    let transportData = await transportModel.findOne({ emp_id: Employee_id, orgId: Employee_orgId }).select('-_id -createdAt -updatedAt -__v');
    if (!transportData) return res.send(Response.FailResp("Mode Of Transport Data not Present", transportData));
    
    const { value, error } = transportValidate.transportFreqRadValidate(data);
    if (error) {
        return res.send(Response.FailResp("Validation Failed", error));
    }

    if (transportData && configMode) {
        const index = configMode === 'bike' ? 0 : configMode === 'car' ? 1 : -1;

        if (index >= 0) {
            if (transportData.currentMode === configMode) {
                if (configFrequency !== undefined) {
                    transportData.currentFrequency = configFrequency;
                }
                if (configRadius !== undefined) {
                    transportData.currentRadius = configRadius;
                }
            }
            if (configFrequency !== undefined) {
                transportData.defaultConfig[index].frequency = configFrequency;
            }
            if (configRadius !== undefined) {
                transportData.defaultConfig[index].radius = configRadius;
            }
            transportData.defaultConfig[index].mode = configMode;
        }
    }

    
    let transportUpdated = await transportModel.findOneAndUpdate(
        { emp_id: Employee_id, orgId: Employee_orgId }, 
        { $set: transportData }, 
        { returnDocument: 'after' }
    );
    
    return res.send(Response.SuccessResp("User Mode Of Transport data Saved Successfully.", transportUpdated));
}
}


export default new transportServices();