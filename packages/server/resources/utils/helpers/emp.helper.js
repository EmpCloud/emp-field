// import adminSchema from '../../../core/admin/admin.model.js'                                     
import axios from 'axios';
import logger from  '../../../resources/logs/logger.log.js'                                                     
import config from 'config';
import Response from '../../../response/response.js'
import currencyModel from '../../../core/reports/report.currency.js';                                                                                   

async function isEmpAdmin({ resultData, empAdmin, res }) {
    try {
        let response = await axios.post(config.get('add_emp_admin_link'), { email: resultData?.email, FieldTrackingId: resultData?._id, secretKey: config.get('emp_secret_key') });
        if (response?.data?.data) {
            let orgId = response?.data?.data;
            await adminSchema.findOneAndUpdate({ email: resultData.email }, { isEmpMonitorAdmin: true, verified: true, orgId: orgId }, { returnDocument: 'after' });
            empAdmin = true;
            (resultData.isEmpMonitorAdmin = true), (resultData.verified = true), (resultData.orgId = orgId);
        }
    } catch (error) {
        logger.log(`Error in catch ${error}`);
        res.send(Response.projectFailResp('Error creating admin.', error.message));
    }
    return { resultData, empAdmin };
}

async function currencyValue( baseCode , amount) {
    let amt = amount;
    try {
        let currencySchema = await currencyModel.findOne({ base_code: "INR" });
        
        let conversionRates = currencySchema.conversion_rates.get(baseCode);
        let actualAmount = amt*(conversionRates??1);

        let rate = actualAmount;
        
        
        return rate;
    } catch (error) {
        logger.log(`Error in currencyValue: ${error}`);
        res.send(Response.projectFailResp('Error fetching conversion rate.', error.message));
    }
}


export { isEmpAdmin,currencyValue };
