import UserValidation from './user.validate.js';
import Response from '../../response/response.js';
import UserSchema from '../profile/profile.model.js';
import roleSchema from '../role/role.model.js';
import logger from '../../resources/logs/logger.log.js';
import config from 'config';
import axios from 'axios';
import PasswordGenerator from 'generate-password';
import bcrypt from 'bcrypt';
import MailResponse from '../../mailService/userMailTemplate.js';
import adminModel from './../admin/admin.model.js'


class UserService {
    async createUser(req, res) {
            try {
                const result = req.verified;
                const {orgId,isEmpMonitorAdmin} = result?.userData?.userData;

                let userData = req?.body;
                userData.orgId = orgId;
                const { email: userEmail,phoneNumber: userPhoneNum,role} = req.body;
                const { value,error } = UserValidation.createUser(userData);
               if (error) return res.send(Response.FailResp('Validation Failed', error?.details));
    
        
                //checking for duplication
                const dataDuplicate = await UserSchema.findOne({
                    $or: [ { phoneNumber: userPhoneNum }, { email: userEmail }],
                });   
               
                if (dataDuplicate) {
                    if (dataDuplicate?.email === userEmail) return res.send(Response.FailResp(`User email email already exist.`, `User email already exist.`));
                    if (dataDuplicate?.phoneNumber === userPhoneNum)
                        return res.send(Response.FailResp(`Phone number already exist, please check phone number.`, `Phone number already exist, please check phone number.`));
                } else {
                   
                    // if(req?.body?.role) {
                    // let roleExists = await roleSchema.findOne({orgId:orgId,_id:role})
                    // if(!roleExists) return  res.send(Response.FailResp('Invalid role Id,Please select valid role'));}
                    if(isEmpMonitorAdmin) value.empMonitor = true 
                    //registering the user
                    const saltRounds = 10;
                    const hashedPassword = await bcrypt.hash(userData.password, saltRounds);
                    value.password=hashedPassword;
                    let resultData = await UserSchema.create(value);
                   
                    let mailResponse = await MailResponse.sendUserVerificationMail(resultData);
                   
                    let { forgotPasswordToken, forgotTokenExpire, password, emailValidateToken, emailTokenExpire,numberValidateOtp,numberValidateOtpExpire,verificationEmailSentCount,passwordEmailSentCount, ...filteredData } = resultData.toJSON();
                    res.send(Response.SuccessResp(`User created successfully.`, { resultData: filteredData })); 
                }
        } catch (error) {
            return res.send(Response.FailResp('Something went wrong'));
        }
    }
    async fetchUsers(req, res) {
        const result = req.verified;
        try {
            const {orgId} = result?.userData?.userData;
            const skip = req?.query?.skip || '0';
            const limit = req?.query?.limit|| '10';
            const fullName = req?.query?.fullName;
            let query = {};
            let resultData = [];
            query.orgId=orgId;
            if (fullName && fullName.trim() !== "") {
                resultData = await UserSchema.find({orgId: orgId, fullName: { $regex: new RegExp(fullName, 'i') } ,status: { $nin: [2, 3,4] }}).select({
                    _id: 1,
                    fullName: 1,
                    email: 1,
                    role: 1,
                    emp_id:1,
                    profilePic: 1,
                    emailVerified: 1,
                    phoneVerified: 1,
                    orgId: 1,
                    frequency:1,
                    geoLogsStatus:1,
                    createdAt: 1
                }).skip(skip).limit(limit);
            }else{
            resultData = await UserSchema.find({orgId: orgId,status: { $nin: [2, 3,4] }})
            .select({
                _id: 1,
                fullName: 1,
                email: 1,
                role: 1,
                emp_id:1,
                profilePic: 1,
                emailVerified: 1,
                phoneVerified: 1,
                orgId: 1,
                frequency:1,
                geoLogsStatus:1,
                createdAt: 1
            }).skip(skip).limit(limit);
        }
            let usersCount = await UserSchema.countDocuments({ orgId: orgId,status: { $nin: [2, 3,4] } })
            res.send(Response.SuccessResp(`User Fetched  successfully.`, { count: usersCount,resultData}));
        } catch (err) {
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp('Error updating .', err.message));
        }
    }
    async updateUser(req, res) {
        try {
            const result = req.verified;
            const {orgId,emp_id} = result?.userData?.userData;
                const data = req?.body;
                const userId = req?.query?.userId;
                const { value, error } = UserValidation.updateUser(req?.body);
                if (error) return res.send(Response.FailResp('Validation Failed', error.details[0]?.message));
                let Exists = await UserSchema.find({emp_id:emp_id,orgId:orgId})
                if (!Exists) return res.send(Response.SuccessResp("User does not Exists!."))

                if(req?.body?.role){
                let roleExists = await roleSchema.findOne({orgId:orgId,_id:req?.body?.role})
                if(!roleExists) return  res.send(Response.FailResp('Invalid role Id,Please select valid role')); }
                let resultData = await UserSchema.findOneAndUpdate({ _id: (userId) }, { $set: data }, { returnDocument: 'after' });

                let { forgotPasswordToken, forgotTokenExpire,numberValidateOtp,numberValidateOtpExpire,emailValidateToken,emailTokenExpire,passwordEmailSentCount,verificationEmailSentCount, ...filteredData } = resultData.toJSON();
                res.send(Response.SuccessResp(`User details updated successfully.`, { resultData: filteredData }));
        } catch (err) {
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp('Error updating .', err.message));
        }
    }
    async deleteUser(req, res) {
        try {
            const result = req.verified;
            const {orgId } = result?.userData?.userData;
                const userId = req?.query?.userId;   
                
                let isExist = await UserSchema.findOne({ _id: (userId),orgId:orgId },{returnDocument:'after'});
                if(!isExist)return res.send(Response.SuccessResp("User does not Exists!."));

                let data ={
                    status : 4
                }
                let resultData
                if(resultData?.status!==4){
                    resultData =  await UserSchema.findOneAndUpdate({ _id: (userId) ,orgId:orgId}, { $set: data }, { returnDocument: 'after' });
                }
                if (resultData) return res.send(Response.SuccessResp("User deleted successfully."))
                res.send(Response.FailResp(`Invalid userId`));
        } catch (err) {
            logger.log(`Error in deleting user ${err}`);
            res.send(Response.FailResp('Error in deleting user', err.message));
        }
    }

    async fetchEmpUsers(req, res) {
       
            try {
                const result = req.verified;
                const {orgId,isEmpMonitorAdmin} = result?.userData?.userData;

                    const fieldTrackingId = result?.userData?.userData._id;
                    const data = {
                        fieldTrackingId: fieldTrackingId,
                        org_id: orgId,
                        secretKey: config.get('emp_secret_key'),
                        skip: req?.query?.skip || '0',
                        limit: req?.query?.limit || '10',
                        orderBy: req?.query?.orderby || 'firstName',
                        sort: req?.query?.sort || 'asc',
                        search: req?.query?.search,
                    };
                    let response = await axios.post(config.get('fetch_emp_users_link'), data);
                    
                    if (response) {
                        if (!response.data.data) return res.send(Response.SuccessResp('USER NOT FOUND', { user_data: [],count: 0}));
                        
                        const UserCount = await UserSchema.countDocuments({ orgId: orgId });
                        return res.send(
                            Response.SuccessResp('USER FETCH SUCCESS', {
                                total_remaining_empUser_count: response?.data?.total_count,
                                activated_field_users: UserCount,
                                user_data: response?.data?.data,
                                count: response?.data?.count,
                            })
                        );
                    }
                
            } catch (err) {
                logger.log(`Error in fetching emp user ${err}`);
                res.send(Response.FailResp('Error in fetching user', err.message));
            }
      
    }
   
    async addEmpUsers(req, res) {
        try {
            const result = req.verified;
            const {orgId} = result?.userData?.userData;
            
            let users = req?.body?.users;
            let emp_ids = [];
            let newUser = [];
            let existUser = [];
            let empUsers =  users.map(e => {
                e.fullName = e.firstName + ' '+ e.lastName;
                delete e.role;
                delete e.department;
                delete e.emp_code;
                return e;
            });
            const promises = await empUsers?.map(async user => {
               
            const { value, error } = UserValidation.createEmpUser(user);
            
               
            if (error) return res.status(400).send(Response.FailResp('VALIDATION_FAIL', error.message));
    
                const isDataExist = await UserSchema.findOne({ email: new RegExp('^' + value?.email, 'i') });
                if (!isDataExist) {
                    value.orgId = orgId;
                    value.password = PasswordGenerator.generate({ length: 10, numbers: true });
                    value.userType = 1
                    
                    let insertedResult = await UserSchema.create(value);
                    newUser.push(value);
                    emp_ids.push(value.emp_id); 
                } else {
                    existUser.push(value);
                }
            
        });
        await Promise.all(promises);
        if (emp_ids?.length ) {
            const data = { emp_id: emp_ids, register: true, secretKey: config.get('emp_secret_key') };
            await axios.post(config.get('update_emp_users_status'), data);
        }
        res.send(Response.SuccessResp(`User created successfully.`, { newUser })); 

    } catch (error) {
        return res.send(Response.FailResp('Something went wrong'));
    }
}

async setFrequencyAndGeoLoc(req,res){

    try{
        const result = req.verified;
        const {orgId} = result?.userData?.userData;

        let emp_id = req?.query?.emp_id
        let frequency = req?.query?.frequency
        let geoLocation= req?.query?.geoLocation
        let Exists = await UserSchema.find({emp_id:emp_id,orgId:orgId})

        if (!Exists) return res.send(Response.SuccessResp("User does not Exists!."))
        let data={}
        if (frequency) {
            data = {
                frequency: frequency,
            };
        } else if (geoLocation) {
            data = {
                geoLogsStatus: geoLocation,
            };
        } else {
            return res.send(Response.SuccessResp("Provide frequency or geoLocation!"));
        }
        
        let updatedUserDetails = await UserSchema.findOneAndUpdate({emp_id:emp_id,orgId:orgId }, { $set: data }, { returnDocument: 'after' })
        let { forgotPasswordToken, forgotTokenExpire,numberValidateOtp,numberValidateOtpExpire,emailValidateToken,emailTokenExpire,passwordEmailSentCount,verificationEmailSentCount, ...filteredData } = updatedUserDetails.toJSON();
        res.send(Response.SuccessResp(`User profile updated successfully.`, { resultData: filteredData }));

    }catch (error) {
        return res.send(Response.FailResp('Something went wrong'));
    }

        
} 

async updateBiometricConfig(req,res,next){
    try{

        const result = req.verified;
        const {_id,orgId} = result?.userData?.userData;


        let isAdminExists = await adminModel.findOne({_id,orgId:orgId})

        if (!isAdminExists) return res.send(Response.SuccessResp("Admin does not Exists!."))


        let { email } = req.body;
        let Exists = await UserSchema.findOne({email})

        if (!Exists) return res.send(Response.FailResp("User does not Exists!.",'Validation Failed!'));

        const { value, error } = UserValidation.updateUserBiometricConfig(req?.body);

        if (error) {
            return res.status(400).send(Response.FailResp(error.details[0].message,'Validation Failed!'));
        }
        // Update the user's biometric config
        let updateBiometricStatus = await UserSchema.findOneAndUpdate(
            { email }, // filter
            { $set: value },                  // update
            { new: true }                     // options: return updated document
        );

        return res.send(Response.SuccessResp("User biometric config updated successfully.", updateBiometricStatus));
    }catch (error) {
        console.log(error);
        return res.send(Response.FailResp(error?.message,'Something went wrong'));
    }
}
}
export default new UserService();
