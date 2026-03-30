import UserValidation from './unauthorized.validate.js';
import Response from '../../response/response.js';
import uuidv1 from 'uuidv1';
import MailResponse from '../../mailService/userMailTemplate.js';
import jwt from 'jsonwebtoken';
import config from 'config';
import moment from 'moment';
import Logger from '../../resources/logs/logger.log.js';
import userSchema from '../profile/profile.model.js';
import userTracking from '../models/userTracking.model.js';
import otpGenerator from 'otp-generator';
import twilio from 'twilio';
import bcrypt from 'bcrypt';
import clientSchema from '../client/client.model.js';
import trackSchema from '../tracking/track.model.js';
import taskSchema from '../task/task.model.js';
import adminSchema from '../admin/admin.model.js';
import roleSchema from '../role/role.model.js';
import employeeLocationModel from '../hrmsAdmin/employeeLocation.model.js';


import transportModel from '../transport/transport.model.js';
import tempTrackingModel from '../../core/tracking/temp_tracking.model.js'
import orgLocationSchema from '../../core/hrmsAdmin/orgLocation.model.js'

class unauthorizedService {
    async verifyUser(req, res) {
        try {
            let userMail = req?.body?.userMail?.toLowerCase();
            const isDataExist = await userSchema.findOne({ email: userMail });
            if (!isDataExist){
                return res.send(Response.FailResp('Email Not Present'));
            } 
            else{
                return res.send(Response.SuccessResp('Email Verified'));
            }
        } 
        catch (err) {
            Logger.error(`Error in catch ${err}`);
            res.send(Response.FailResp('Something Went Wrong', err));
        }
    }
    
    async verifyPhone(req, res) {
        try {
            const { userNumber } = req?.body;
            const { error } = UserValidation.verifyPhone({userNumber});
            if (error) return res.send(Response.FailResp('Validation Failed', error.message));
            let validPhoneNumber = (phoneNumber => {
                const pn = phoneNumber.replace(/[^0-9]/g, '');
                return pn.length >= 10 ? pn : null;
            })(userNumber);
            const isDataExist = await userSchema.findOne({ phoneNumber: validPhoneNumber  });
            if (!isDataExist) return res.send(Response.FailResp(`Phone not yet registered.!!`));
            else {
                  let accessToken;
                  let filteredData = isDataExist;
                  accessToken = jwt.sign({ userData: filteredData }, config.get('token_secret'), { expiresIn: '24h' });
                  const data = await userSchema.findOneAndUpdate({ phoneNumber: validPhoneNumber });
                  return res.send(Response.SuccessResp(`Phone verified successfully`, { userData: filteredData, accessToken }));
            }
        } catch (err) {
            res.send(Response.FailResp(`UnExpected Error while verifying phone.`, err));
        }
    }
    async UserLogin(req, res) {
        try {
            const { password } = req?.body;
            const userMail = req?.body?.userMail?.toLowerCase();
            const { error } = UserValidation.verifyUserCreds({ userMail, password });
            if (error) return res.send(Response.validationFailResp('Validation Failed', error.details[0].message));

            let userData = await userSchema.findOne({ email: userMail });
            if (!userData) return res.send(Response.FailResp('Email Does not Exist'));

            const updatedEmployeeLocation = await employeeLocationModel.findOneAndUpdate(
                { employeeId: userData?._id },
                {
                  $set: {
                    orgId:userData?.orgId
                  }
                },
                {
                  new: true,      // return the updated document
                  upsert: true    // create the document if it doesn't exist
                }
              );

            const checkPassword = await bcrypt.compare(password, userData.password);
            if (!checkPassword) return res.send(Response.FailResp('Incorrect User password. Please try again!'));
                
            if (userData?.emailVerified == false) return res.send(Response.FailResp('Email is Not Verified'));

           // if (userData?.phoneVerified == false) return res.send(Response.FailResp('Phone NOT VERIFIED'));
            if (userData?.isSuspended) return res.send(Response.FailResp('User Suspended'));
            if (userData?.status===4) return res.send(Response.FailResp(`User ${userData.fullName} has been deleted!. Please contact Admin`));
            
            let accessToken;
            let { forgotPasswordToken, forgotTokenExpire, emailValidateToken, emailTokenExpire, passwordEmailSentCount, verificationEmailSentCount, ...filteredData}=userData.toJSON();
            delete filteredData.password;
            accessToken = jwt.sign({ userData: filteredData }, config.get('token_secret'), { expiresIn: '24h' });
            res.send(Response.SuccessResp(`Logged in successfully.`, { userData: filteredData, accessToken }));
        } catch (error) {
            Logger.error(`Error in the catch ${error}`);
            Response.FailResp('Something Went Wrong', error);
        }
    }

    async forgotPassword(req, res) {
        try {
            const email  = req?.body?.email?.toLowerCase();
            const { error } = UserValidation.forgotPasswordValidation({ email });
            if (error) return res.send(Response.FailResp('Validation Failed', error));
           
            let userData = await userSchema.findOne({ email: email });
            if (!userData) return res.send(Response.FailResp('Email Not Found'));
            let current_day = new Date();
            const verifyToken = otpGenerator.generate(4, { digits:true,lowerCaseAlphabets: false, specialChars : false,upperCaseAlphabets : false });
            const emailTokenExpire = moment().add(1, 'day')?._d;
            let data;
            //refresh the email sent count for a fresh day
            if (new Date(moment(current_day).format('YYYY-MM-DD')) >=new Date(moment(userData.forgotTokenExpire).format('YYYY-MM-DD'))) {           
                data = await userSchema
                    .findOneAndUpdate({ email: email }, { $set: { forgotPasswordToken: verifyToken, forgotTokenExpire: emailTokenExpire, passwordEmailSentCount: 1} },
                        { returnDocument: 'after' });
                
            } else if (userData.passwordEmailSentCount >= config.get('passwordEmailSentCount') && userData.forgotTokenExpire > current_day) {
                return res.send(Response.FailResp('Password Updation mail sent limit reached,Please try again next day.'));
             }
            else if (current_day <= userData.forgotTokenExpire) {
                data = await userSchema
                    .findOneAndUpdate(
                        { email: email },
                        { $set: { forgotPasswordToken: verifyToken, forgotTokenExpire: emailTokenExpire, passwordEmailSentCount: userData.passwordEmailSentCount + 1 } },
                        { returnDocument: 'after' }
                    );
            }
           // if (process.env.node_env !== 'localDev') {
                let mailResponse = await MailResponse.sendUserForgotPasswordVerificationMail(data);
                Logger.info(`Mail invitation response ${mailResponse}`);
             //}
            res.send(Response.SuccessResp('Mail Sent Successfully'));
        } catch (err) {   
            Logger.error(`Error in catch ${err}`);
            res.send(Response.FailResp('Something Went Wrong', err.message));
        }
    }

    async verifyOTP(req, res) {
        try {
            let otp = req.query.verifyOtp;
            let email = req.query.email;
            let userData = await userSchema.findOne({email});

            if (userData.forgotPasswordToken === otp) {
                res.send(Response.SuccessResp('Token Received','Successfully verified Token.'));
            } else {
                res.send(Response.FailResp('Invalid Token!.Please provide valid Token'));
            }
        } catch (err) {   
            Logger.error(`Error in catch ${err}`);
            res.send(Response.FailResp('Something Went Wrong', err.message));
        }
    }    

    async resetPassword(req, res, next) {
        try {
            const   email  = req?.body?.email?.toLowerCase();
            const { newPassword, verifyToken } = req?.body;
            const { error } = UserValidation.resetPassword({ email, newPassword, verifyToken });
            if (error) return res.send(Response.FailResp('Validation Failed', error));
           
            //finding email is exist or not
            const userData = await userSchema.findOne({ email: email });
            if (!userData) return res.send(Response.FailResp('Email is Not Found'));
            if (userData.forgotTokenExpire < moment()) return res.send(Response.FailResp('Token Expired'));
            const checkPassword = await bcrypt.compare(newPassword,userData.password);
            if (checkPassword) return res.send(Response.FailResp('New_Password is same as oldPassword, Please provide newPassword!'));
            const saltRounds = 10;
            const hashedPassword = await bcrypt.hash(newPassword, saltRounds);
            //verifying token, if it is true it will reset password otherwise it will give error
            if (userData.forgotPasswordToken == verifyToken) {
                const resultData = await userSchema.findOneAndUpdate({ email: email }, { $set: { password: hashedPassword,forgotPasswordToken: otpGenerator.generate(4, { digits:true,lowerCaseAlphabets: false, specialChars : false,upperCaseAlphabets : false }) } }, { returnDocument: 'after' });

                if (resultData) res.send(Response.SuccessResp('Password Reset Successfully', resultData.value));
            } else {
                res.send(Response.FailResp('Invalid Token'));
            }
        } catch (err) {          
            Logger.error(`Error in catch ${err}`);
            res.send(Response.FailResp('Error in Reset Password', err.message));
        }
    }

    async setPassword(req, res) {
        try {
            const mail = req?.body?.userMail?.toLowerCase();
            const { oldPassword,newPassword } = req?.body;
            const { error } = UserValidation.setPassword({ mail, password });
            if (error) return res.send(Response.validationFailResp('VALIDATION_FAILED', error));


            //finding user is exist or not
            const userData = await userSchema.findOne({ email: mail });
            if (!userData) return res.send(Response.FailResp('EMAIL_NOT_FOUND'));
            

            
            const checkPassword = await bcrypt.compare(oldPassword, userData.password);
            if (!checkPassword) return res.send(Response.FailResp('Invalid Old Password!'));

            const saltRounds = 10;
            const hashedPassword = await bcrypt.hash(newPassword, saltRounds);
            const resultData = await userSchema.findOneAndUpdate({ email: mail }, { $set: { password: hashedPassword,emailVerified: true, invitation: 1, emailValidateToken: uuidv1() } }, { returnDocument: 'after' });
           
            //if(process.env.node_env !== 'localDev'){
            await MailResponse.sendWelcomeMailBySendgridAPI(resultData);//}
            
            res.send(Response.SuccessResp('PASSWORD_SET'));
        } catch (err) {
            Logger.error(`Error in catch ${err}`);
            res.send(Response.FailResp('ERROR_IN_PWD_RESET', err.message));
        }
    }

    async generateToken(req, res) {
        try {
            const   email  = req?.body?.email?.toLowerCase();
            const { error } = UserValidation.forgotPasswordValidation({ email});
            if (error) return res.send(Response.validationFailResp('VALIDATION_FAILED', error));
            
            const ValidEmail = await userSchema.findOne({ email: email });
            if (!ValidEmail) return res.send(Response.FailResp('EMAIL_NOT_FOUND'));
            const verified = await userSchema.findOne({ email: email, emailVerified: true });
            const current_day = new Date();
            const token_expire = ValidEmail.emailTokenExpire;
            if (verified) return res.send(Response.FailResp('EMAIL already ACTIVATED'));
            let data;
            //refresh the email sent count for a fresh day
            if (new Date(moment(current_day).format('YYYY-MM-DD')) >= new Date(moment(token_expire).format('YYYY-MM-DD'))) {
                data = await userSchema
                    .findOneAndUpdate({ email: email }, { $set: { emailValidateToken: uuidv1(), emailTokenExpire: new Date(moment().add(1, 'day')), verificationEmailSentCount: 1 } });
            }
            //checking email sending restriction for a day
            else if (ValidEmail.verificationEmailSentCount >= config.get('verificationEmailSentCount') && new Date(token_expire) > new Date(current_day)) {
                return res.send(Response.FailResp('MAIL_GENERATE_LIMIT_REACHED'));
            }
            //increasing the count every time after mail sent
            else if (current_day <= token_expire) {
                data = await userSchema.findOneAndUpdate(
                    { email: email },
                    {
                        $set: {
                            emailValidateToken: uuidv1(),
                            emailTokenExpire: new Date(moment(current_day).add(1, 'day')),
                            verificationEmailSentCount: ValidEmail.verificationEmailSentCount + 1,
                        },
                    },
                    { returnDocument: 'after' }
                );
            }

           //if (process.env.node_env !== 'localDev') {
                let mailResponse = await MailResponse.sendUserVerificationMail(data);
                Logger.info(`Mail invitation response ${mailResponse}`);
          //  }
            res.send(Response.SuccessResp('VERIFICATION_MAIL_SUCCESS'));
        } catch (err) {
            Logger.error(`Error in catch ${err}`);
            res.send(Response.FailResp('GENERATE_TOKEN_ERROR', err.message));
        }
    }
    async generateOtp(req, res) {
        try {
            const userNumber = req?.body.number;
            const ValidNumber = await userSchema.findOne({ phoneNumber: userNumber });
            if (!ValidNumber) return res.send(Response.FailResp(` number does not exist `));

            //if (ValidNumber?.phoneVerified) return res.send(Response.FailResp(`phone already verified `));
        

            let resultData = await userSchema.findOneAndUpdate(
                    { phoneNumber: userNumber },
                    { $set: { numberValidateOtp: otpGenerator.generate(6, { digits:true,lowerCaseAlphabets: false, specialChars : false,upperCaseAlphabets : false }), numberValidateOtpExpire: moment().add(15, 'minutes')?._d } }
                );
                await client.messages
                .create({
                    body: `Your OTP code is: ${ resultData?.numberValidateOtp}`,
                    from: '+16824782084',
                    to: `+91${resultData?.phoneNumber}`
                })
                .then(message => {
                    console.log(message.sid)
                    return res.send(Response.SuccessResp(`User phone verification otp sent successfully`));
                })
                .catch(error => {
                    console.error(error)
                    return res.send(Response.FailResp('Something went wrong,Plz try again'))
                });
            
        } catch (err) {
            Logger.error(`Error in catch ${err}`);
            res.send(Response.FailResp('Something went wrong', err.message));
        }
    }

    async updateProfile(req, res) {
        try {
            const result = req.verified;
            const {_id:userId,orgId} = result?.userData?.userData;
                const data = req?.body;
                const { value, error } = UserValidation.updateUser(req?.body);
                if (error) return res.send(Response.FailResp('Validation Failed', error.details[0]?.message));
              
                let resultData = await userSchema.findOneAndUpdate({ _id: (userId) }, { $set: data }, { returnDocument: 'after' });

                let { forgotPasswordToken, forgotTokenExpire,numberValidateOtp,numberValidateOtpExpire,emailValidateToken,emailTokenExpire,passwordEmailSentCount,verificationEmailSentCount,password,isTwoFactorEnabled, ...filteredData } = resultData.toJSON();
                res.send(Response.SuccessResp(`User profile updated successfully.`, { resultData: filteredData }));
        } catch (err) {
            Logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp('Something went wrong', err.message));
        }
             
        
    }   
    
    async attendance(req, res) {
        try {
            const result = req.verified;
            const {_id:userId,orgId} = result?.userData?.userData;
                let data = req?.body;
                data.userId = userId;
                data.orgId = orgId;
                const { value, error } = UserValidation.updateTracking(data);
                if (error) return res.send(Response.FailResp('Validation Failed', error.details[0]?.message));
                let resultData;
                let userData = await userTracking.findOne({userId:userId,date:value?.date})
                if(userData){
                    let isCheckedOut = await userTracking.findOne({userId:userId,date:value?.date,trackingType : 2})     
                    if (isCheckedOut) { 
                        resultData = await userTracking.findOneAndUpdate({_id:isCheckedOut?._id},{$set:{value}})
                        return res.send(Response.SuccessResp(`User CheckOut successfully.`, { resultData })); }            
                    value.trackingType = 2;
                    resultData = await userTracking.create(value);  
                    return res.send(Response.SuccessResp(`User CheckOut successfully.`, { resultData })); 
                }
                resultData = await userTracking.create(value);

                res.send(Response.SuccessResp(`User CheckdIn successfully.`, { resultData }));
        } catch (err) {
            Logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp('Something went wrong', err.message));
        }
              
    }    
    async getAttendance(req, res) {
        try {
            const result = req.verified;
            const {_id,orgId,userType} = result?.userData?.userData;

            let userId = req?.query?.userId;
            const startDate = req?.query?.startDate;
            const endDate = req?.query?.endDate;
            if(userType) userId = _id;
            const { value, error } = UserValidation.getAttendance({startDate,endDate,userId});
            if (error) return res.send(Response.FailResp('Validation Failed', error.details[0]?.message));
            let query = {};
            if(userId) query = { userId:userId }
            if(req?.query?.startDate && req?.query?.endDate) {
                query.$and = [
                    { date: { $gte: new Date(startDate), $lt: new Date(new Date(endDate).setHours(23, 59, 59, 999)) } },       
                ];
            }
            
            let attendanceData = await userTracking.aggregate([{ $match: { $and: [query, { orgId: orgId } ] }}])
            if(!attendanceData.length)  return res.send(Response.FailResp(`No attendance history found.`, { attendanceData })); 

            res.send(Response.SuccessResp(`User attendance fetched successfully.`, { attendanceData }));
        } catch (err) {   
            Logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp('Something went wrong', err.message));
        }
              
    }   


    async importUsers(req, res){
        try{

            let ddData = req?.body;
            let userData = ddData.usersData;
            const promises = await Promise.all(userData?.map(async user => {
                let {full_name: fullName,  phone: phoneNumber, email,address: address1, department, location, timezone, role, password} = user;
                let orgId = user.organization_id.toString();
                let emp_id = user.id.toString();
                let pass = password
                const saltRounds = 10;
                const hashedPassword = await bcrypt.hash(password, saltRounds);
                password=hashedPassword
                phoneNumber = phoneNumber.replace('-', '');
                const emp = {
                    fullName,
                    email,
                    role,
                    location,
                    department,
                    phoneNumber,
                    address1,
                    orgId,
                    emp_id,
                    timezone,
                    password
                };
                const { value, error } = UserValidation.createUser(emp);
                if (error) { 
                    return res.send(Response.FailResp('Something went wrong', error));
                } else {
                    const locData = await orgLocationSchema.findOne({orgId:orgId,locationName:location})
                    const isDataExist = await userSchema.findOne({ email: email });
                    let orgValue
                    let newOrgValue;
                    if (!isDataExist) {
                        if(!locData){
                             orgValue={
                                orgId:orgId, 
                                locationName: location,  
                                address:address1,
                                global:[
                                    {
                                      mode:"bike",
                                      frequency:60,
                                      radius:10
                                    },
                                    {
                                      mode:"car",  
                                      frequency:120,
                                      radius:20
                                    }
                                ]
                            }

                            newOrgValue =  await orgLocationSchema.create(orgValue);
                        }
                        else{
                            newOrgValue = locData;
                        }
                        value.orgId = orgId;
                        value.createdAt = new Date();
                        value.email = value.email.toLowerCase();
                        let userCreated =await userSchema.create(value);
                        let individualLocation = await employeeLocationModel.create({
                            orgId,
                            employeeId:userCreated?._id
                        });
                        userCreated.password=pass
                        let transportValues = {
                            orgId:userCreated.orgId,
                            emp_id:userCreated.emp_id,
                            currentRange:10,
                            currentFrequency:25,
                            currentMode:'bike',
                            defaultConfig:newOrgValue.global
                        }
                        //crating transport Record
                        let transportDetails = await transportModel.create(transportValues)
                        let tempTrackingValues ={
                            orgId:userCreated.orgId,
                            emp_id:userCreated.emp_id,
                            geologs:[],
                        }
                        //creating tempTrack Record
                        let tempTrackedValue = await tempTrackingModel.create(tempTrackingValues)
                        //Checking if Role Exist
                        let roleExist = await roleSchema.findOne({ orgId: orgId, role: role });
                        if (!roleExist) {
                            let roleObject = {
                                role: role,
                                orgId: orgId,
                                permission: []
                            };
                            
                            // Use findOneAndUpdate with upsert: true to create the role if it doesn't exist
                           let roleUpdateOrCreation = await roleSchema.findOneAndUpdate(
                                { orgId: orgId, role: role }, // Match condition
                                { $setOnInsert: roleObject },  // Insert only if no match
                                { new: true, upsert: true }    // Options: return the new doc if created, upsert: true ensures insert
                            );
                        }
                        let mailResponse = await MailResponse.sendWelcomeMailBySendgridAPI(userCreated);

                        

                        

                        return userCreated;
                    } 
                    else {
                        return null; 
                    }
                }
            }));
            
            // Filter out the null values for skipped records
            const results = promises.filter(promise => promise !== null);
            
            // Wait for all promises to resolve
            await Promise.all(results);
            return res.send(Response.SuccessResp(`User added sucessfully.`,results));
        }
        catch(err){
            console.log(err);
            res.send(Response.FailResp('Something went wrong', err.message));
        }
    }

    async adminImportUsers(req, res){
        try{

            let data = req?.body;
            let userData = data.usersData;
            const promises = await Promise.all(userData?.map(async user => {
                let {full_name: fullName,  phone: phoneNumber, email,address: address1, department, location, timezone, role, password} = user;
                let orgId = user.organization_id.toString();
                let emp_id = user.id.toString();
                const saltRounds = 10;
                let pass = password
                const hashedPassword = await bcrypt.hash(password, saltRounds);
                password=hashedPassword
                phoneNumber = phoneNumber.replace('-', '');
                const emp = {
                    fullName,
                    email,
                    role,
                    location,
                    department,
                    phoneNumber,
                    address1,
                    orgId,
                    emp_id,
                    timezone,
                    password
                };
                const { value, error } = UserValidation.createUser(emp);
                if (error) { 
                    return res.send(Response.FailResp('Something went wrong', error.message));
                } else {
                    const isDataExist = await adminSchema.findOne({ email: email });
                    if (!isDataExist) {
                        value.orgId = orgId;
                        value.createdAt = new Date();
                        value.email = value.email.toLowerCase();
                        let adminCreated =await  adminSchema.create(value);
                        adminCreated.password=pass
                        let mailResponse = await MailResponse.sendWelcomeMailBySendgridAPI(adminCreated);
                        return adminCreated
                    } else {
                        return null; 
                    }
                }
            }));
            
            // Filter out the null values for skipped records
            const results = promises.filter(promise => promise !== null);
            
            // Wait for all promises to resolve
            await Promise.all(results);
            return res.send(Response.SuccessResp(`Admin added sucessfully.`,results));
        }
        catch(err){
            res.send(Response.FailResp('Something went wrong', err.message));
        }
    }

    async deleteUserPerm(req, res){
        try{

            let data = req?.body;
            if(data.secret=="ahfs76sf303ff3f"){
                let userDel = await userSchema.deleteOne({ emp_id: data.empId });
                let trackDel = await trackSchema.deleteMany({ emp_id: data.empId });
                let taskDel = await taskSchema.deleteMany({ emp_id: data.empId });
                let clientDel = await clientSchema.deleteMany({ emp_id: data.empId });
                return res.send(Response.SuccessResp(`User Deleted Permanently.`,data));
            }
            else{
                res.send(Response.FailResp('Invalid Code Provided', null));
            }
            
        }
        catch(err){
            res.send(Response.FailResp('Something went wrong', err.message));
        }
    }

    async getLocationList(req, res){
        try{
            let data = req?.body;
            let orgLocationList = await orgLocationSchema.find({orgId: data.orgId}).select('locationName');
            if(orgLocationList.length > 0){
                return res.send(Response.SuccessResp('List Fetched Successfully',orgLocationList));
            }
            else{
                res.send(Response.FailResp('No Location Found'));
            }
        }
        catch(err){
            res.send(Response.FailResp('Something went Wrong'));
        }
    }

    async getGeoLocationDetails(req, res){
        try{
            let data = req?.body;
            let locationDetails = await orgLocationSchema.find({orgId: data.orgId,locationName:data.locationName});
            if(locationDetails.length > 0){
                return res.send(Response.SuccessResp('List Fetched Successfully',locationDetails));
            }
            else{
                res.send(Response.FailResp('No Location Found'));
            }
        }
        catch(err){
            res.send(Response.FailResp('Something went Wrong'));
        }
    }

    async updateGeoLocationDetails(req, res){
        try{
            let data = req?.body;
            let updateLocationDetails = await orgLocationSchema.findOneAndUpdate({orgId:data.orgId,locationName:data.locationName},{
                $set:{
                    isMobEnabled: data.isMobEnabled,
                    geo_fencing: data.geo_fencing,
                    latitude: data.latitude,
                    longitude: data.longitude
                }
            },{ returnDocument: 'after' });
            if(updateLocationDetails){
                return res.send(Response.SuccessResp('List Fetched Successfully',updateLocationDetails));
            }
            else{
                res.send(Response.FailResp('No Location Found'));
            }
        }
        catch(err){
            res.send(Response.FailResp('Something went Wrong'));
        }
    }

    async empEmployeeImport(req, res){
        try{
            let ddData = req?.body;
            let userData = ddData.usersData;
            const promises = await Promise.all(userData?.map(async user => {
                let {full_name: fullName,  phone: phoneNumber, email,address: address1, department, location, timezone, role, password} = user;
                let orgId = user.organization_id.toString();
                let emp_id = user.id.toString();
                let pass = password
                const saltRounds = 10;
                const hashedPassword = await bcrypt.hash(password, saltRounds);
                password=hashedPassword;
                if(user.phoneNumber == null){
                    phoneNumber = user.phoneNumber;
                }
                else{
                    phoneNumber = user.phoneNumber.replace('-', '');
                }
                fullName = user.fullName;
                const emp = {
                    fullName,
                    email,
                    role,
                    location,
                    department,
                    phoneNumber,
                    address1,
                    orgId,
                    emp_id,
                    timezone,
                    password 
                };
                const { value, error } = UserValidation.createUser(emp);
                if (error) { 
                    return res.send(Response.FailResp('Something went wrong', error));
                } else {
                    const locData = await orgLocationSchema.findOne({orgId:orgId,locationName:location})
                    const isDataExist = await userSchema.findOne({ email: email });
                    let newOrgValue;
                    if (!isDataExist) {
                        if(!locData){
                            let orgValue={
                                orgId:orgId, 
                                locationName: location,  
                                address:address1,
                                global:[
                                    {
                                      mode:"bike",
                                      frequency:60,
                                      radius:10
                                    },
                                    {
                                      mode:"car",  
                                      frequency:120,
                                      radius:20
                                    }
                                ]
                            }
                            newOrgValue =  await orgLocationSchema.create(orgValue);
                        }
                        else{
                            newOrgValue = locData;
                        }
                        value.orgId = orgId;
                        value.createdAt = new Date();
                        value.email = value.email.toLowerCase();
                        let userCreated =await userSchema.create(value);
                        userCreated.password=pass
                        let transportValues = {
                            orgId:userCreated.orgId,
                            emp_id:userCreated.emp_id,
                            currentRange:10,
                            currentFrequency:25,
                            currentMode:'bike',
                            defaultConfig:newOrgValue.global
                        }
                        //crating transport Record
                        let transportDetails = await transportModel.create(transportValues)
                        let tempTrackingValues ={
                            orgId:userCreated.orgId,
                            emp_id:userCreated.emp_id,
                            geologs:[],
                        }
                        //creating tempTrack Record
                        let tempTrackedValue = await tempTrackingModel.create(tempTrackingValues)
                        //Checking if Role Exist
                        let roleExist = await roleSchema.findOne({ orgId: orgId, role: role });
                        if (!roleExist) {
                            let roleObject = {
                                role: role,
                                orgId: orgId,
                                permission: []
                            };
                            
                            // Use findOneAndUpdate with upsert: true to create the role if it doesn't exist
                           let roleUpdateOrCreation = await roleSchema.findOneAndUpdate(
                                { orgId: orgId, role: role }, // Match condition
                                { $setOnInsert: roleObject },  // Insert only if no match
                                { new: true, upsert: true }    // Options: return the new doc if created, upsert: true ensures insert
                            );
                        }
                        // let mailResponse = await MailResponse.sendWelcomeMailBySendgridAPI(userCreated);
                        return userCreated;
                    } 
                    else {
                        return null; 
                    }
                }
            }));
            
            // Filter out the null values for skipped records
            const results = promises.filter(promise => promise !== null);
            
            // Wait for all promises to resolve
            await Promise.all(results);
            return res.send(Response.SuccessResp(`User added sucessfully.`,results));
        }
        catch(err){
            res.send(Response.FailResp('Something Went Wrong'));
        }
    }
}

export default new unauthorizedService();
