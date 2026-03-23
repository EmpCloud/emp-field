import adminSchema from '../admin/admin.model.js';
import orgSchema from '../models/organization.model.js'
import Response from '../../response/response.js';
import jwt from 'jsonwebtoken';
import config from 'config';
import logger from '../../resources/logs/logger.log.js';
import uuidv1 from 'uuidv1';
import MailResponse from '../../mailService/mailTemplate.js';
import moment from 'moment';
import adminValidation from './admin.validate.js'
import twilio from 'twilio';
import otpGenerator from 'otp-generator';
import { isEmpAdmin } from '../../resources/utils/helpers/emp.helper.js';
import taskModel from '../task/task.model.js';
import clientSchema from '../client/client.model.js'
import userModel from '../profile/profile.model.js'
const client = twilio(config.get('TWILIO_ACCOUNT_SID'), config.get('TWILIO_AUTH_TOKEN'));
import { Storage } from '@google-cloud/storage';
import profileModel from '../profile/profile.model.js';
import userSchema from '../profile/profile.model.js';
import trackSchema from '../tracking/track.model.js'
import axios from 'axios';
import bcrypt from 'bcrypt';
import transportModel from '../transport/transport.model.js';
import taskService from '../task/task.service.js';
import testTempTrackingModel from '../tracking/temp_test_tracking.model.js'
import tempTrackingModel from '../tracking/temp_tracking.model.js'
import taskSchema from '../task/task.model.js'
import orgLocationModel from '../hrmsAdmin/orgLocation.model.js';
import { ObjectId } from 'mongodb';
import { currencyValue } from '../../resources/utils/helpers/emp.helper.js';


const storage = new Storage({ keyFilename: 'storageconfig.json' });
const bucketName = config.get('BUCKET_NAME_BIOMETRICS');
const bucket = storage.bucket(bucketName);

class AdminService {
    async addAdmin(req, res) {
        try {
            const data = req?.body;
            const { email: userEmail, orgName, phoneNumber: userPhoneNum} = req.body;
            //const { error } = adminValidation.createAdmin(req.body);
            //if (error) return res.send(Response.FailResp('validation failed', error.message));
            let empAdmin = false;
            //checking for duplication
            const dataDuplicate = await adminSchema.findOne({
                $or: [ { phoneNumber: userPhoneNum }, { email: userEmail }],
            });   
            if (dataDuplicate) {
                if (dataDuplicate?.email === userEmail) return res.send(Response.FailResp(`Admin email ${data?.email} already exist.`, `Admin email ${data?.email} already exist.`));
                if (dataDuplicate?.phoneNumber === Number(userPhoneNum)) {
                    return res.send(Response.FailResp(`Phone number already exist, please check phone number.`, `Validation Failed`));
                }
            } else {
                let empDomain = config.get('empDomainForOrgId');
                let orgId = null;
                const response = await axios.post(
                    `${empDomain}auth/info`,
                     { email: userEmail },
                     { headers: { 'Content-Type': 'application/json' } }
                );
                orgId = response?.data?.data[0]?.id;
                const orgExist = await orgSchema.find({orgName: new RegExp(`^${orgName}$`, 'i')})
                if(orgExist.length) return res.send(Response.FailResp(`Admin orgName ${data?.orgName} already exist.`, `Admin orgName ${data?.orgName} already exist.`));
                let orgData = await orgSchema.create({orgName: data.orgName})
                data.orgId = orgId;
                data.email = data.email.toLowerCase();
                const saltRounds = 10;
                const hashedPassword = await bcrypt.hash(data.password, saltRounds);
                data.password=hashedPassword;
                //registering the admin
                let resultData = await adminSchema.create(data);
                
               
                res.send(Response.SuccessResp(`Admin stored successfully.`, { resultData }));    
            }
        } catch (err) {
            console.log(err);
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp('Error creating Admin.', err.message));
        }
    }

    async fetchAdmin(req, res) {
        try {
            const userEmail = req?.body?.email?.toLowerCase();
            const { password: userPassword } = req?.body;
            const { error } = adminValidation.fetchAdmin(req?.body);
            if (error) return res.send(Response.validationFailResp('Validation Failed', error.message));
            const isUserExist = await adminSchema.findOne({ email: userEmail });
            //checking user exists or not
            if (!isUserExist) return res.send(Response.FailResp(`Admin not exist.`));
            const checkPassword = await bcrypt.compare(userPassword, isUserExist.password);
            if (!checkPassword) return res.send(Response.FailResp('Invalid Admin password!'));

            //checking user email verified or not
            //if (!isUserExist?.emailVerified) return res.send(Response.FailResp(`email not verified`));

            //checking admin account is suspended or not
            if (isUserExist?.isSuspended) return res.status(400).send(Response.FailResp(`Admin's account is suspended and not allowed to login.`));
            // if(isUserExist?.password !== userPassword) return res.send(Response.FailResp(`Invalid the Password.!!`));
            
            if(isUserExist?.isTwoFactorEnabled){
                let resultData = await adminSchema.findOneAndUpdate(
                    { email: userEmail },
                    { $set: { numberValidateOtp: otpGenerator.generate(6, { digits:true,lowerCaseAlphabets: false, specialChars : false,upperCaseAlphabets : false }), numberValidateOtpExpire: moment().add(15, 'minutes')?._d } }
                );
                await  client.messages
                    .create({
                        body: `Your OTP code is: ${resultData?.numberValidateOtp}`,
                        from: '+16824782084',
                        to: `+91${isUserExist?.phoneNumber}`
                    })
                    .then(message => {
                        console.log(message.sid)
                        return res.send(Response.SuccessResp("Password verified. Please enter the OTP sent to your phone to complete login."))})
                    .catch(error => {
                        console.log(error)
                        return res.send(Response.FailResp("Something went wrong, Please try again."))
                        });    
        }
    
            let accessToken, filteredData;
            let { forgotPasswordToken, forgotTokenExpire, password, emailValidateToken, emailTokenExpire, passwordEmailSentCount, verificationEmailSentCount, ...filtereData } =
                    isUserExist.toJSON();
                filteredData = isUserExist;
                accessToken = jwt.sign({ userData: filtereData }, config.get('token_secret'), { expiresIn: '24h' });
            
            res.send(Response.SuccessResp(`Logged in successfully.`, { userData: filtereData, accessToken }));
        } catch (err) {
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp('Error in fetch admin details.', err.message));
        }
    }

    async allOrgEmployee(req, res){
        try{
            const result = req.verified;
            let { orgId, emp_id } = result?.userData?.userData;
            const skip = req.body.skip;
            const limit  = req.body.limit;
            let currentEmpArray = '(';
            //getting all current users id's
            let userDetails = await userModel.find({orgId: orgId}).select('emp_id');
            let count = 0;
            for(let item of userDetails){
                currentEmpArray = currentEmpArray + item.emp_id;
                if(count < userDetails.length - 1){
                    currentEmpArray = currentEmpArray + ','
                } 
                count++;
            }
            currentEmpArray = currentEmpArray + ')';
            const body = {  organization_id: orgId, 
                status: 1, 
                orgId: orgId, 
                skip: skip,
                limit: limit,
                currentEmpArray:currentEmpArray,
                secretKey: config.get('emp_secret_key') };
            let attendance = await axios.post(config.get('empDomain') + 'user/fieldAllEmployeeList', body);
            if (attendance?.data) {
                let empOrgDataArray = attendance.data.data;
                //fetching current imported users
                const isUserExist = await userModel.find({ orgId: orgId })
                .select({
                    emp_id: 1
                });
                if(isUserExist.length > 0){
                    const userArray = isUserExist.map(obj => obj['emp_id']);
                    for(let i=0;i<empOrgDataArray.length; i++){
                        if (userArray.includes(empOrgDataArray[i].id.toString())) {
                            empOrgDataArray[i].importedStatus = true;
                        }
                        else{
                            empOrgDataArray[i].importedStatus = false;
                        }
                    }
                }
                return res.send(Response.SuccessResp('Employee List Fetched Successfully', empOrgDataArray))
            }
            else{
                return res.send(Response.FailResp('Something went wrong'))
            }
            
        }
        catch(err){
            res.send(Response.FailResp(`Something Went Wrong in function allOrgEmployee`, err));
        }
    }

    async verifyEmail(req, res) {
        try {
            const adminMail = req?.body?.adminMail?.toLowerCase();
            const { activationLink } = req?.body;
            const { error } = adminValidation.verifyEmail({ activationLink, adminMail});
            if (error) return res.send(Response.FailResp('Validation Failed', error.message));
            const isDataExist = await adminSchema.findOne({ email: adminMail });
            if (!isDataExist) return res.send(Response.FailResp(`Email not yet registered.!!`));
            if (isDataExist?.emailVerified == true) return res.send(Response.FailResp(`Email already activated!.!!`));
            if (isDataExist?.emailValidateToken != activationLink) return res.send(Response.FailResp(`Invalid Activation token!.!!`));
            if (isDataExist?.emailTokenExpire < moment()) return res.send(Response.FailResp('Your Token has expired, please re-generated the email verify token'));
            else {
                const data = await adminSchema.findOneAndUpdate({ email: adminMail }, { $set: { emailVerified: true, emailValidateToken: uuidv1() } });
                
                if (!data) return res.send(Response.FailResp(`Error in active admin.!!`));
                let numberProvided;
                (data?.phoneNumber) ? numberProvided = true : numberProvided = false;
                await MailResponse.sendWelcomeMailAdminBySendgridAPI(isDataExist);
               
                res.send(Response.SuccessResp(`Admin email verified successfully.!!`,{phoneNum : numberProvided, AdminData: isDataExist}));    
            }
        } catch (err) {
            res.send(Response.FailResp(`UnExpected Error while activating the Admin.`, err));
        }
    }

    async updatePhone(req, res) {
        try {
            const { adminEmail,newNumber } = req?.body;
            const { error } = adminValidation.updateAdminPhoneNumber({  adminEmail,newNumber});
            if (error) return res.send(Response.FailResp('Validation Failed', error.message));
            const isDataExist = await adminSchema.findOne({ email:adminEmail });
            if (!isDataExist) return res.send(Response.FailResp(`Email not Exist,Provide valid mail`));
            const usersPhoneNumbers = await adminSchema.find({ phoneNumber: newNumber });
            if (usersPhoneNumbers?.length) return res.send(Response.FailResp(`Phone Number already exist.`));
            
              let resultData = await adminSchema.findOneAndUpdate(
                    { email: adminEmail },
                    { $set: {  phoneNumber: newNumber } },
                    { returnDocument: 'after' }
                );
            res.send(Response.SuccessResp(`Admin phone number updated successfully.`,resultData));
        } catch (err) {
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp('Something went wrong.', err.message));
        }
    }

    async verifyPhone(req, res) {
        try {
            const { adminNumber,activationOtp } = req?.body;
            const { error } = adminValidation.verifyPhone({ activationOtp, adminNumber});
            if (error) return res.send(Response.FailResp('Validation Failed', error.message));
            const isDataExist = await adminSchema.findOne({ phoneNumber: adminNumber });
            if (!isDataExist) return res.send(Response.FailResp(`Phone not yet registered.!!`));
            if (isDataExist?.numberValidateOtp != activationOtp) return res.send(Response.FailResp(`Invalid Activation Otp!.!!`));
            if (isDataExist?.numberValidateOtpExpire < moment()) return res.send(Response.FailResp('Your Otp has expired, please re-generated the Phone verify otp'));
            
            if(isDataExist?.isTwoFactorEnabled || isDataExist?.phoneVerified == true){
                let accessToken, filteredData;
                let { forgotPasswordToken, forgotTokenExpire, password, emailValidateToken, emailTokenExpire, passwordEmailSentCount, verificationEmailSentCount, ...filtereData } =
                isDataExist.toJSON();
              filteredData = isDataExist;
              accessToken = jwt.sign({ userData: filtereData }, config.get('token_secret'), { expiresIn: '24h' });
              const data = await adminSchema.findOneAndUpdate({ phoneNumber: adminNumber }, { $set: {  numberValidateOtp:  otpGenerator.generate(6, { digits:true,lowerCaseAlphabets: false,specialChars:false,upperCaseAlphabets:false }) } });
             return res.send(Response.SuccessResp(`success`, { userData: filteredData, accessToken }));
            }
            
            if (isDataExist?.phoneVerified == true) return res.send(Response.FailResp(`Phone already activated!.!!`));

            else {
                const data = await adminSchema.findOneAndUpdate({ phoneNumber: adminNumber }, { $set: { phoneVerified: true, numberValidateOtp:  otpGenerator.generate(6, { digits:true,lowerCaseAlphabets: false,specialChars:false,upperCaseAlphabets:false }) } });
                if (!data) return res.send(Response.FailResp(`Error in verifying phone.!!`));
                res.send(Response.SuccessResp(`Phone verified successfully.!!`));    
            }
        } catch (err) {
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp(`UnExpected Error while verifying phone.`, err));
        }
    }

    async updateAdmin(req, res) {
       
            try {
                const result = req.verified;
                const data = req?.body;
                const adminId = result?.userData?.userData?._id;
                const { value, error } = adminValidation.updateAdmin(req?.body);
                if (error) return res.send(Response.FailResp(error.message,'Validation Failed'));
                let resultData = await adminSchema.findOneAndUpdate({ _id: (adminId) }, { $set: data }, { returnDocument: 'after' });

                let { forgotPasswordToken, forgotTokenExpire, password, dashboardConfig, ...filteredData } = resultData.toJSON();
                res.send(Response.SuccessResp(`Admin profile updated successfully.`, { resultData: filteredData }));
            } catch (err) {
                logger.log(`Error in catch ${err}`);
                res.send(Response.FailResp('Error updating Admin.', err.message));
            }
        
    }

    async forgotPassword(req, res) {
        try {
            const email  = req?.query?.email?.toLowerCase();
            const { error } = adminValidation.forgotPasswordValidation({ email });
            if (error) return res.send(Response.FailResp('VALIDATION_FAILED', error));
           
            let adminData = await adminSchema.findOne({ email: email });
            if (!adminData) return res.send(Response.FailResp('Email not registered. Please check the email address or create a new account.'));
            let current_day = new Date();
            const verifyToken = otpGenerator.generate(4, { digits:true,lowerCaseAlphabets: false, specialChars : false,upperCaseAlphabets : false });
            const emailTokenExpire = moment().add(1, 'day')?._d;
            let data;
            //refresh the email sent count for a fresh day
            if (new Date(moment(current_day).format('YYYY-MM-DD')) >=new Date(moment(adminData.forgotTokenExpire).format('YYYY-MM-DD'))) {           
                data = await adminSchema
                    .findOneAndUpdate({ email: email }, { $set: { forgotPasswordToken: verifyToken, forgotTokenExpire: emailTokenExpire, passwordEmailSentCount: 1} },
                        { returnDocument: 'after' });
            } else if (adminData.passwordEmailSentCount >= config.get('passwordEmailSentCount') && adminData.forgotTokenExpire > current_day) {
                return res.send(Response.FailResp('Password Updation mail sent limit reached,Please try next day.'));
             }
            else if (current_day <= adminData.forgotTokenExpire) {
                data = await adminSchema
                    .findOneAndUpdate(
                        { email: email },
                        { $set: { forgotPasswordToken: verifyToken, forgotTokenExpire: emailTokenExpire, passwordEmailSentCount: adminData.passwordEmailSentCount + 1 } },
                        { returnDocument: 'after' }
                    );
            }
           // if (process.env.node_env !== 'localDev') {
                let mailResponse = await MailResponse.sendAdminForgotPasswordVerificationMail(data);
                logger.info(`Mail invitation response ${mailResponse}`);
             //}
            res.send(Response.SuccessResp('Password mail sent successfully.'));
        } catch (err) {   
            logger.error(`Error in catch ${err}`);
            res.send(Response.FailResp('ERROR_Forget_PWD', err.message));
        }
    }

    async resetPassword(req, res) {
        try {
            const   email  = req?.body?.email?.toLowerCase();
            const { newPassword, token } = req?.body;
            const { error } = adminValidation.resetPassword({ email, newPassword, token });
            if (error) return res.send(Response.FailResp('VALIDATION_FAILED', error));
           
            //finding email is exist or not
            const userData = await adminSchema.findOne({ email: email });
            if (!userData) return res.send(Response.FailResp('EMAIL_NOT_FOUND'));
            if (userData.forgotTokenExpire < moment()) return res.send(Response.FailResp('TOKEN_EXPIRED'));
           
            //verifying token, if it is true it will reset password otherwise it will give error
            if (userData.forgotPasswordToken == token) {
                const saltRounds = 10;
                const hashedPassword = await bcrypt.hash(newPassword, saltRounds);
                const resultData = await adminSchema.findOneAndUpdate({ email: email }, { $set: { password: hashedPassword,forgotPasswordToken: otpGenerator.generate(4, { digits:true,lowerCaseAlphabets: false, specialChars : false,upperCaseAlphabets : false }) } }, { returnDocument: 'after' });
                if (resultData) res.send(Response.SuccessResp('Password reset successful. You can now log in.', resultData.value));
            } else {
                res.send(Response.FailResp('INVALID_TOKEN'));
            }
        } catch (err) {          
            logger.error(`Error in catch ${err}`);
            res.send(Response.FailResp('ERROR_IN_PWD_RESET', err.message));
        }
    }

    async generateToken(req, res) {
        try {
            const email = req?.body?.email?.toLowerCase();
            const ValidEmail = await adminSchema.findOne({ email: email });
            if (!ValidEmail) return res.send(Response.FailResp(` email doesnot exist `));

            if (ValidEmail?.emailVerified) return res.send(Response.FailResp(`email already activated `));
            let current_day = new Date();
            const token_expire = ValidEmail?.emailTokenExpire;
        

            let resultData;
            //refresh the email sent count for a fresh day
            if (new Date(moment(current_day).format('YYYY-MM-DD')) >= new Date(moment(token_expire).format('YYYY-MM-DD'))) {
                resultData = await adminSchema.findOneAndUpdate(
                    { email: email },
                    { $set: { emailValidateToken: uuidv1(), emailTokenExpire: moment(current_day).add(1, 'day'), verificationEmailSentCount: 1 } },
                    { returnDocument: 'after' }
                );
            }
            //checking email sending restriction for a day
            else if (ValidEmail.verificationEmailSentCount >= config.get('verificationEmailSentCount') && token_expire > current_day) {
                return res.send(Response.FailResp('Verification mail sent limit reached,Please try next day.'));
            }
            //increasing the count every time after mail sent
            else if (current_day <= token_expire) {
                resultData = await adminSchema.findOneAndUpdate(
                    { email: email },
                    { $set: { emailValidateToken: uuidv1(), emailTokenExpire: moment(current_day).add(1, 'day'), verificationEmailSentCount: ValidEmail.verificationEmailSentCount + 1 } },
                    { returnDocument: 'after' }
                );
            }
           
                const mailResponse = await MailResponse.sendAdminVerificationMail([resultData]);
                logger.info(`Mail invitation response ${mailResponse}`);
            
            res.send(Response.SuccessResp(`Admin verification mail sent successfully`));
        } catch (err) {
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp('Error updating Admin', err.message));
        }
    }
    async generateOtp(req, res) {
        try {
            const adminNumber = req?.body.number;
            const ValidNumber = await adminSchema.findOne({ phoneNumber: adminNumber });
            if (!ValidNumber) return res.send(Response.FailResp(` number doesnot exist `));

            // if (ValidNumber?.phoneVerified && !ValidNumber?.isTwoFactorEnabled) return res.send(Response.FailResp(`phone already verified `));
        

            let resultData = await adminSchema.findOneAndUpdate(
                    { phoneNumber: adminNumber },
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
                    return res.send(Response.SuccessResp(`Admin phone verification otp sent successfully`));
                })
                .catch(error => {
                    console.error(error)
                    return res.send(Response.FailResp('Something went wrong,Plz try again'))
                });
            
           
        } catch (err) {
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp('Something went wrong', err.message));
        }
    }
    async updatePassword(req, res) {
        try {
            const result = req.verified;
            const { language, email } = result.userData?.userData;
            const { oldPassword,newPassword } = req?.body;
            const userData = await adminSchema.findOne({ email: email });
            
            const checkPassword = await bcrypt.compare(oldPassword, userData.password);
            if (!checkPassword) return res.send(Response.FailResp('Invalid Old Password!'));
            const saltRounds = 10;
            const hashedPassword = await bcrypt.hash(newPassword, saltRounds);
          
            const { error } = await adminValidation.updatePassword(req.body);
            if (error) return res.send(Response.FailResp(`Validation Failed`, error.message));
            const resultData = await adminSchema.findOneAndUpdate({ email: email }, { $set: { password: hashedPassword } }, { returnDocument: 'after' });
            res.send(Response.SuccessResp(`Password reset successfully`, resultData));
        } catch (err) {
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp(`Something went wrong`, err.message));
        }
    }

    async enableTwoFactor(req, res) {
        try {
            const result = req.verified;
            const { language, email } = result.userData?.userData;
            const { twoFactorAuth } = req?.body;
            const userData = await adminSchema.findOne({ email: email });
            
            if (userData.isTwoFactorEnabled) return res.send(Response.FailResp(`2FA already enabled`));
            if (!userData.phoneNumber) return res.send(Response.FailResp(`2FA cant be enabled,Phone Number is not present`));
            const resultData = await adminSchema.findOneAndUpdate({ email: email }, { $set: { isTwoFactorEnabled: twoFactorAuth } }, { returnDocument: 'after' });
            res.send(Response.SuccessResp(`2FA  updated successfully`, resultData));
        } catch (err) {
            
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp(`Something went wrong`, err.message));
        }
    }

    async getLanguages(req, res){
        try{
            const languages = {
                "English"       : "en",
                "Hindi"	        : "hi",
                "French"        : "fr",
                "German"        : "de",
                "Greek"	        : "el",
                "Portuguese"    : "pt",
                "Turkish"       : "tr",
                "Spanish"       : "es",
                "Russian"       : "ru",
                "Indonesian"    : "id",
                "Vietnamese"    : "vi",
                "Thai"	        : "th",
                "Telugu"	    : "te",
                "Tamil"		    : "ta",
                "Malayalam"	    : "ml",
                "Kannada"		: "kn",
                "Bengali"		: "bn",
                "Marathi"		: "mr",
                "Gujarati"		: "gu",
                "Punjabi"		: "pa",
                "Odia"			: "or",
                "Sindhi"		: "sd"
            };
            return res.send(Response.SuccessResp("Language Fetched Successfully",languages));

        }
        catch(err){
            return res.send(Response.FailResp("Something went wrong, Please try again."))

        }
    }

    async getAdminTask(req, res) {
        try {
            const result = req.verified;
            const emp_id = req?.query.emp_id
            const data = req?.body 
            const  date  = req?.body.date ;
            const sortBy = {};
            sortBy[req?.query?.orderBy || 'createdAt'] = req?.query?.sort?.toString() === 'asc' ? 1 : -1;
            const {error} = adminValidation.searchTask(data);
            if(error)  return res.send(Response.FailResp(`Failed`,error));

            let { orgId } = result?.userData?.userData;

            if (!orgId) {
                return  res.send(Response.FailResp(`Failed, Please provide OrgId`));
            }
            const trackQuery={orgId}
            const query = { orgId };

            if(emp_id){
                query.emp_id=emp_id;
                query.taskApproveStatus= { $nin: [0,5] },
                trackQuery.emp_id=emp_id;
            }
            
              
            if (date) {
                const startDate = new Date(`${date}T00:00:00Z`);
                const endDate = new Date(`${date}T23:59:59Z`);
                query.createdAt = { $gte: startDate, $lte: endDate };
                trackQuery.date=date
            }
            const skip = parseInt(req.query.skip) || 0;
            const limit = parseInt(req.query.limit) || 10;
            const tasks = await taskModel.find(query).skip(skip).limit(limit).sort(sortBy).lean();

            let trackingData = await trackSchema.find(trackQuery)
            .select('date distTravelled geologs')
            .populate({
                path: 'geologs.taskId',
                model: taskModel,
                select: 'taskName taskDescription'
            });

        const percentageMapping = {
            1: 100,
            2: 100,
            3: 100,
            4: 100,
            5: 100
        };
        const employeeDetails = await profileModel.findOne({emp_id:emp_id}).select('fullName profilePic role frequency geoLogsStatus snap_duration_limit snap_points_limit createdAt')
        const transportDetails = await transportModel.findOne({emp_id:emp_id}).select('currentMode currentFrequency currentRadius')
        const promises = tasks?.map(async (obj) => {
            const taskPercentage=percentageMapping[obj?.taskApproveStatus] || 0
            const clientId = obj?.clientId;
            const clientData = await clientSchema.find({_id:clientId});
            return { ...obj,clientData ,taskPercentage};
        });

        // Calculate total distance traveled
        // let totalDistance = 0;

        // for (let i = 0; i < trackingData[0].geologs.length - 1; i++) {
        //     totalDistance += haversineDistance(trackingData[0].geologs[i], trackingData[0].geologs[i + 1]);
        // }
        // console.log(`Total distance traveled: ${totalDistance / 1000} km`);
        const modifiedArray = await Promise.all(promises);
        const Result = {employeeDetails,transportDetails,modifiedArray,trackingData}
            res.send(Response.SuccessResp(`Tasks Fetched Successfully`, Result));
        } catch (err) {
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp(`Something went wrong`, err.message));
        }
    }
   
    async getUserCoordinates(req, res) {
        try {
            const result = req.verified;
            const emp_id = req?.query.emp_id
            const data = req?.body 
            const  date  = req?.body.date ;
            const sortBy = {};
            sortBy[req?.query?.orderBy || 'createdAt'] = req?.query?.sort?.toString() === 'asc' ? 1 : -1;
            const {error} = adminValidation.searchTask(data);
            if(error)  return res.send(Response.FailResp(`Failed`,error));

            let { orgId } = result?.userData?.userData;

            if (!orgId) {
                return  res.send(Response.FailResp(`Failed, Please provide OrgId`));
            }
            const trackQuery={orgId}
            const query = { orgId };

            if(emp_id){
                query.emp_id=emp_id;
                query.taskApproveStatus = { $nin: [0,5] },
                trackQuery.emp_id=emp_id;
            }
            
              
            if (date) {
                const startDate = new Date(`${date}T00:00:00Z`);
                const endDate = new Date(`${date}T23:59:59Z`);
                query.createdAt = { $gte: startDate, $lte: endDate };
                trackQuery.date=date
            }

        const transportDetails = await transportModel.findOne({emp_id:emp_id}).select('currentMode currentFrequency currentRadius')
        const employeeDetails = await profileModel.findOne({emp_id:emp_id}).select('fullName profilePic role frequency geoLogsStatus snap_duration_limit snap_points_limit createdAt')
        
        let trackData1 = await trackSchema.find(trackQuery)
        .select('geologs');  
        
        let geologsUnviewedCounts = trackData1.map(track =>
            track.geologs.filter(log => log.viewed === 0).length
        )
        let numberOfCoordinatesToSend = geologsUnviewedCounts[0]/employeeDetails.snap_points_limit
            
            let trackingData = await trackSchema.find(trackQuery)
            .select('date distTravelled geologs')
            .populate({
                path: 'geologs.taskId',
                model: taskModel,
                select: 'taskName taskDescription'
            });
            let temp =[]
            trackingData = trackingData.map(track => {
                let filteredViewedGeologs = track.geologs.filter(log => log.viewed === 1);
                temp.push(...filteredViewedGeologs)
                let filteredNotViewedGeologs = track.geologs.filter(log => log.viewed === 0);
                temp.push(...filteredNotViewedGeologs.slice(0, Math.ceil(numberOfCoordinatesToSend)))
                return {
                    ...track._doc,
                    geologs: temp
                };
            });

        const Result = {employeeDetails,transportDetails,trackingData}
            res.send(Response.SuccessResp(`User Coordinates Fetched Successfully`, Result));
        } catch (err) {
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp(`Something went wrong`, err.message));
        }
    }

    async updateCoordinatesStatus(req,res){
        try {
            const result = req.verified;
            let { orgId } = result?.userData?.userData;
            let data = req.body;
            const isUserExist = await adminSchema.findOne({ email: result?.userData.userData.email });
            //checking user exists or not
            if (!isUserExist) return res.send(Response.FailResp(`Admin not exist.`));

            const {error,value} = adminValidation.updateGeoLogStatus(data);
            if(error)  return res.send(Response.FailResp(`Failed`,error));

            const geologsObjectIds = value.geologsId.map(id =>new ObjectId(id));
            // Find and update the document
            const Result = await trackSchema.updateOne(
              { emp_id: value.emp_id,date:moment().format('YYYY-MM-DD'),orgId:orgId, 'geologs._id': { $in: geologsObjectIds } },
              { $set: { 'geologs.$[elem].viewed': 1 } },
              { arrayFilters: [{ 'elem._id': { $in: geologsObjectIds } }], multi: true }
            );

            res.send(Response.SuccessResp(`User Coordinates Updated Successfully`, Result));
        } catch (err) {
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp(`Something went wrong`, err.message));
        }
    }
    async adminDataFetch(req, res) {
        try {
            const result = req.verified;
            const isUserExist = await adminSchema.findOne({ email: result?.userData.userData.email });
            //checking user exists or not
            if (!isUserExist) return res.send(Response.FailResp(`User not exist.`));
            let  filteredData;
            let { forgotPasswordToken, forgotTokenExpire, password, emailValidateToken, emailTokenExpire, passwordEmailSentCount, verificationEmailSentCount, ...filtereData } =isUserExist.toJSON();
                filteredData = filtereData;
            res.send(Response.SuccessResp(`success`, { userData: filteredData }));
        } catch (err) {
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp('Error in fetch admin details.', err.message));
        }
    }

    async uploadProfileImage(req, res){
        const result = req.verified.userData.userData;
        try{
            if(req.files){
                let item = req.files[0];

                let folderName = 'fieldTracking/admin';
                let fileName = result.email.split('.')[0];
                let prefix = folderName+fileName+'.jpg';

                // Delete files with the specified prefix
                async function deleteFiles(prefix) {
                    try {
                        const [files] = await bucket.getFiles({ prefix });
                        await Promise.all(files.map(file => file.delete()));
                    } catch (err) {
                        throw err;
                    }
                }
                await deleteFiles(prefix);

                let profileURL = await uploadImage(item, folderName);
                res.send(Response.SuccessResp(`Image Uploaded successfully.`, { profileURL: profileURL }));
            } 
        } 
        catch(err){
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp('Error Uploading.', err.message));
        }
    }


    async adminUsersTrackingData(req, res){
        try {
            const { orgId, emp_id } = req.verified.userData.userData;
            const skip = parseInt(req.query.skip, 10) || 0;
            const limit = parseInt(req.query.limit, 10) || 10;
            const employee_id = req.query.employee_id;
            const date = req.body.date || moment().format('YYYY-MM-DD');

            const startDate = moment(date).startOf('day').toDate(); // 00:00:00.000

            const endDate = moment(date).endOf('day').toDate(); // 23:59:59.999
    
            const isAdminDataExist = await adminSchema.findOne({ orgId, emp_id });
            if (!isAdminDataExist) return res.send(Response.FailResp('User not exist.'));
            let {checkIn,checkOut} = req.body
            if(checkIn===true&&checkOut===true){
                return res.send(Response.FailResp('Cannot filter CheckedIn and checkOut simultaneously!'));
            }
    
            const query = { orgId, status: { $nin: [4] } };
            if (employee_id&&employee_id!=='null'){
                query.emp_id = employee_id;
            }
            const allOrgUsers = await userModel.find(query).select('orgId profilePic emp_id fullName').limit(limit).skip(skip);
            if (!allOrgUsers.length) return res.send(Response.FailResp('Users not exist for the admin.'));
            const pipeline = [
                {
                    $match: {
                        ...query
                    }
                },
                {
                    $lookup: {
                        from: 'temptrackschemas',
                        let: { empId: '$emp_id' },
                        pipeline: [
                            {
                                $match: {
                                    $expr: { $eq: ['$emp_id', '$$empId'] },
                                    ...(date ? { updatedAt: { $gte: startDate, $lte: endDate } } : {}) 
                                }
                            }
                        ],
                        as: 'tempTrackData'
                    }
                },
                {
                    $unwind: {
                        path: '$tempTrackData',
                        preserveNullAndEmptyArrays: true
                    }
                },
                {
                    $lookup: {
                        from: 'trackschemas',
                        let: { empId: '$emp_id' },
                        pipeline: [
                            {
                                $match: {
                                    $expr: { $eq: ['$emp_id', '$$empId'] },
                                    ...(date ? { date: date } : {})  
                                }
                            },
                            {
                                $sort: { date: -1 }  
                            }
                        ],
                        as: 'trackingData'
                    }
                },
                {
                    $unwind: {
                        path: '$trackingData',
                        preserveNullAndEmptyArrays: true
                    }
                },
                {
                    $project: {
                        _id: 0,
                        orgId: 1,
                        emp_id: 1,
                        profilePic: 1,
                        fullName: 1,
                        checkIn: {
                            $ifNull: ['$trackingData.checkIn', null]
                        },
                        checkOut: {
                            $ifNull: ['$trackingData.checkOut', null]
                        },
                        lastGeoLog: {
                            $cond: {
                                if: { $gt: [{ $size: { $ifNull: ['$tempTrackData.geologs', []] } }, 0] },  // Check if tempTrackData.geologs exists and is not empty
                                then: { $arrayElemAt: ['$tempTrackData.geologs', -1] },  // Use last geolog from tempTrackData
                                else: { $arrayElemAt: ['$trackingData.geologs', -1] }  // Fallback to trackingData.geologs
                            }
                        }
                    }
                }
            ];
            


            
                      if(checkIn===true&&checkIn){
                      pipeline.push(  {
                        $match: {
                          $or: [
                            { 'checkIn': { $ne: null } },
                          ],
                          trackingData: { $ne: [] } 
                        }
                      })
                    }else if(checkOut===true&&checkOut){
                        pipeline.push({
                            $match: {
                              $or: [
                                { 'checkOut': { $ne: null } }
                              ],
                              trackingData: { $ne: [] } 
                            }
                          })
                    }


                    const calculatingTotalCount = await userModel.aggregate(pipeline);
                    let actualTotalCount = calculatingTotalCount.length;
                    pipeline.push(
                        { $skip: skip },  
                        { $limit: limit } 
                      );
                    // console.log(JSON.stringify(pipeline, null, 2));

                    const usersWithSingleCheckInOrCheckOutOnDate = await userModel.aggregate(pipeline).exec();
                      
                    // const totalCount = await userModel.countDocuments(pipeline);

                      
                      const results = [];
                    for (const item of usersWithSingleCheckInOrCheckOutOnDate) {
                        let Address = null
                        if (item.lastGeoLog!==null&&item.lastGeoLog&&item.lastGeoLog.latitude!==null&&item.lastGeoLog.longitude!==null) {
                            const { latitude, longitude } = item.lastGeoLog;
                            const url = `${config.get('googleMapsGeoCodingApi')}lat=${latitude}&lon=${longitude}`;
                            const geocodeUrl = await axios.get(url); 
                            if (geocodeUrl.statusText === 'OK') {
                                Address = geocodeUrl.data.display_name;
                            }
                        } 
                        item.address=Address
                        
                        results.push(item);
                      }
                      
    
            const finalData = {
                totalCount : actualTotalCount,
                orgStartDate:isAdminDataExist.createdAt,
                userData: results,
            };
    
            res.send(Response.SuccessResp('Successfully fetched user data!', finalData));
    
        } catch (err) {
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp('Error in fetching admin details.', err.message));
        }
    };
    
    async allTaskStats(req, res) {
        try {
            const result = req.verified;
            let { orgId, emp_id } = result?.userData?.userData;
            let isAdminDataExist =await adminSchema.findOne({orgId:orgId,emp_id:emp_id})
            //checking if Admin exists or not
            if (!isAdminDataExist) return res.send(Response.FailResp(`Admin not exist.`));

            const todayStart = moment().startOf('day').format('YYYY-MM-DD');
            const todayEnd = moment().endOf('day').format('YYYY-MM-DD');
            
            const pendingTask = await taskModel.countDocuments({
                orgId:orgId,
                date: {
                    $gte: todayStart,
                    $lte: todayEnd
                },
                taskApproveStatus: { $in: [0] }
            });
            const startedTask = await taskModel.countDocuments({
                orgId:orgId,
                date: {
                    $gte: todayStart,
                    $lte: todayEnd
                },
                taskApproveStatus: { $in: [1] }
            });
            const pausedTask = await taskModel.countDocuments({
                orgId:orgId,
                date: {
                    $gte: todayStart,
                    $lte: todayEnd
                },
                taskApproveStatus: { $in: [2] }
            });
            const resumedTask = await taskModel.countDocuments({
                orgId:orgId,
                date: {
                    $gte: todayStart,
                    $lte: todayEnd
                },
                taskApproveStatus: { $in: [3] }
            });
            const completedTask = await taskModel.countDocuments({
                orgId:orgId,
                date: {
                    $gte: todayStart,
                    $lte: todayEnd
                },
                taskApproveStatus: { $in: [4] }
            });
            
            const overAllTodaysTasks = await taskModel.countDocuments({
                orgId:orgId,
                date: {
                    $gte: todayStart,
                    $lte: todayEnd
                },
                taskApproveStatus: { $nin: [5] }
            });

            const todaysTaskStats = {
                pendingTask:pendingTask|| 0,
                startedTask:startedTask|| 0,
                pausedTask:pausedTask|| 0,
                resumedTask:resumedTask|| 0,
                completedTask:completedTask|| 0,
               
            };
            const TodaysTasks = {
                todaysTaskStats,
                overAllTodaysTasks:overAllTodaysTasks|| 0
            }

            const overAllPendingTask = await taskModel.countDocuments({
                orgId:orgId,
                taskApproveStatus: { $in: [0] }
            });
            const overAllStartedTask = await taskModel.countDocuments({
                orgId:orgId,
                taskApproveStatus: { $in: [1] }
            });
            const overAllPausedTask = await taskModel.countDocuments({
                orgId:orgId,
                taskApproveStatus: { $in: [2] }
            });
            const overAllResumedTask = await taskModel.countDocuments({
                orgId:orgId,
                taskApproveStatus: { $in: [3] }
            });
            const overAllCompletedTask = await taskModel.countDocuments({
                orgId:orgId,
                taskApproveStatus: { $in: [4] }
            });
            
            const overAllTasks = await taskModel.countDocuments({
                orgId:orgId,
                taskApproveStatus: { $nin: [5] }
            });

            const overAllTaskStats = {
                overAllPendingTask:overAllPendingTask|| 0,
                overAllStartedTask:overAllStartedTask||0,
                overAllPausedTask:overAllPausedTask|| 0,
                overAllResumedTask:overAllResumedTask|| 0,
                overAllCompletedTask:overAllCompletedTask|| 0,
               
            };
            const allTasksStats = {
                overAllTaskStats,
                overAllTasks:overAllTasks|| 0
            }
            const data = {
                TodaysTasks,
                allTasksStats
            }
            res.send(Response.SuccessResp(`Successfully fetched today's Task Stats`,data));

        } catch (err) {
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp('Error in fetch admin details.', err.message));
        }

    }


    async getAllAdminFieldTrackingUsers(req, res) {
        try {
            const result = req.verified;
            let { orgId, emp_id } = result?.userData?.userData;
            let skip =req.query.skip || 0;
            let limit = req.query.limit || 10;
            let searchQuery = req.query.searchQuery;
            let {role,location,department,deleted} = req.body;
            const sortBy = {};
            sortBy[req?.query?.orderBy || 'createdAt'] = req?.query?.sort?.toString() === 'asc' ? 1 : -1;
            let isAdminDataExist =await adminSchema.findOne({orgId:orgId,emp_id:emp_id});
            //checking if Admin exists or not
            if (!isAdminDataExist) return res.send(Response.FailResp(`Admin not exist.`));

            let data = {
                searchQuery: searchQuery || '',
                role: role || '',
                location: location || '',
                department: department || '',
            };
            const query = {
                orgId: orgId,
                ...(data.role && { role: data.role }),
                ...(data.location && { location: data.location }),
                ...(data.department && { department: data.department }),
            };
            if(deleted===true){
                query.status=4
            }else{
                query.status=1
            }
            const fieldsToSearch = ['fullName', 'email', 'location','department','role']; 
            
            if (data.searchQuery) {
                    query.$or = fieldsToSearch.map(field => ({
                        [field]: new RegExp(data.searchQuery, 'i')
                    }));
            }
            const allUsers = await userModel.find(query).limit(limit).skip(skip).sort(sortBy);
            const totalCount = await userModel.countDocuments(query);

            const finalData = {
                totalCount,
                allUsers
            }
            
            res.send(Response.SuccessResp(`Successfully fetched Users`,finalData));

        } catch (err) {
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp('Error in fetch admin details.', err.message));
        }
    }
    async dashboardStats(req,res){
    try {
        const result = req.verified;
        let { orgId, emp_id ,timezone} = result?.userData?.userData;
        let isAdminDataExist =await adminSchema.findOne({orgId:orgId,emp_id:emp_id});
        //checking if Admin exists or not
        const query={}
        query.orgId=orgId;
        if (!isAdminDataExist) return res.send(Response.FailResp(`Admin not exist.`));
        const employees = await userModel.countDocuments(query)
        const listOfEmployeesSuspended = await userModel.countDocuments({orgId:orgId,isSuspended:true})

        const startOfDay = moment().startOf('day').format('YYYY-MM-DD HH:mm:ss');
        const endOfDay = moment().endOf('day').format('YYYY-MM-DD HH:mm:ss');

        const presentToday = await trackSchema.countDocuments({
            orgId: orgId,
            checkIn: { $gte: startOfDay, $lt: endOfDay },
        });
        const date = moment().format('YYYY-MM-DD');

        const working = await taskModel.aggregate([
            {
              $match: {
                orgId: orgId,
                taskApproveStatus: { $gte: 1 },
                date: date,
                start_time: { $ne: null }
              }
            },
            {
              $group: {
                _id: "$emp_id",
                count: { $sum: 1 }
              }
            },
            {
              $count: "totalGroupedDocuments"
            }
          ]);
          
          let totalGroupedDocuments = 0;
          if (working.length > 0) {
            totalGroupedDocuments = working[0].totalGroupedDocuments;
          }
        
        
        const absent = employees-presentToday
        const onDuty = totalGroupedDocuments;
        const suspended = listOfEmployeesSuspended;
        let employeeStats={
            employees,
            presentToday,
            onDuty,
            absent,
            suspended
        }
        res.send(Response.SuccessResp(`Successfully fetched Employees Stats`,employeeStats));
    } catch (err) {
        logger.log(`Error in catch ${err}`);
        res.send(Response.FailResp('Error in fetch admin details.', err.message));
    }
    }

    async getEmployeeDetails(req, res) {
        try {
            const result = req.verified;
            let { orgId, emp_id } = result?.userData?.userData;
            let isAdminDataExist = await adminSchema.findOne({ orgId: orgId, emp_id: emp_id });
            const sortBy = {};
            sortBy[req?.query?.orderBy || 'createdAt'] = req?.query?.sort?.toString() === 'asc' ? 1 : -1;
            let searchQuery = req.query.search;

            if (!isAdminDataExist) return res.send(Response.FailResp(`Admin does not exist.`));
    
            const skip = parseInt(req.query.skip) || 0;
            const limit = parseInt(req.query.limit) || 10;
            const employeeStatus = req.query.employeeStatus;
    
            const query = { orgId: orgId };
    
            const startOfDay = moment().startOf('day').format('YYYY-MM-DD HH:mm:ss');
            const endOfDay = moment().endOf('day').format('YYYY-MM-DD HH:mm:ss');
            const date = moment().format('YYYY-MM-DD');
    
            if (employeeStatus === 'presentToday') {

                const presentEMP = await trackSchema.find({
                    orgId: orgId,
                    checkIn: { $gte: startOfDay, $lt: endOfDay },
                }).select('emp_id');
                query.emp_id = { $in: presentEMP.map(emp => emp.emp_id) };
            } else if (employeeStatus === 'onDuty') {

                let onDutyEMP = await taskModel.find({taskApproveStatus:{ $gte: 1 },date,start_time:{ $ne: null }}).select('emp_id');
                query.emp_id = { $in: onDutyEMP.map(emp => emp.emp_id) };
            } else if (employeeStatus === 'absent') {
                const presentTodayCount = await trackSchema.find({
                    orgId: orgId,
                    checkIn: { $gte: startOfDay, $lt: endOfDay }
                });
                query.emp_id = { $nin: presentTodayCount.map(emp => emp.emp_id) };
            } else if (employeeStatus === 'suspended') {
                query.isSuspended = true;
            }

            if (searchQuery) {
                query.$or = [
                    { fullName: { $regex: searchQuery, $options: 'i' } },
                    { age: parseInt(searchQuery) || null },
                    { gender: { $regex: searchQuery, $options: 'i' } },
                    { email: { $regex: searchQuery, $options: 'i' } },
                    { location: { $regex: searchQuery, $options: 'i' } },
                    { department: { $regex: searchQuery, $options: 'i' } },
                    { role: { $regex: searchQuery, $options: 'i' } },
                    { emp_id: { $regex: searchQuery, $options: 'i' } },
                    { address1: { $regex: searchQuery, $options: 'i' } },
                    { address2: { $regex: searchQuery, $options: 'i' } },
                    { city: { $regex: searchQuery, $options: 'i' } },
                    { state: { $regex: searchQuery, $options: 'i' } },
                    { country: { $regex: searchQuery, $options: 'i' } },
                    { zipCode: { $regex: searchQuery, $options: 'i' } },
                    { phoneNumber: { $regex: searchQuery, $options: 'i' } },
                    { timezone: { $regex: searchQuery, $options: 'i' } },
                ];
            }

            const employees = await userModel.find(query)
                .skip(skip)
                .limit(limit)
                .sort(sortBy)
                .select('-snap_duration_limit -snap_points_limit -geoLogsStatus -frequency -isWebEnabled -isBioMetricEnabled -isMobileDeviceEnabled -isGeoFencingOn -passwordEmailSentCount -forgotTokenExpire -forgotPasswordToken -isSuspended -__v -status -password');

            const totalCount = await userModel.countDocuments(query)   
            let finalData = {
                totalCount,
                employees
            } 
    
            res.send(Response.SuccessResp(`Successfully fetched Employee Details`, finalData));
            
        } catch (err) {
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp('Error in fetching employee details.', err.message));
        }
    }
    

    async deleteAdminUsers(req, res) {
        try {
            const result = req.verified;
            let { orgId, emp_id, timezone } = result?.userData?.userData;
            let isAdminDataExist = await adminSchema.findOne({ orgId: orgId, emp_id: emp_id });
            if (!isAdminDataExist) return res.send(Response.FailResp(`Admin not exist.`));
    
            let data = req?.body;
            if (!Array.isArray(data.empIds) || data.empIds.length === 0) {
                return res.send(Response.FailResp(`No user IDs provided.`));
            }
    
            let nonExistentUsers = [];
            let deletedUsers = [];
    
            for (let empId of data.empIds) {
                let userExist = await userModel.findOne({ emp_id: empId, orgId: orgId });
                if (!userExist) {
                    nonExistentUsers.push(empId);
                    continue;
                }
    
                await userModel.deleteOne({ emp_id: empId });
                await trackSchema.deleteMany({ emp_id: empId });
                await taskSchema.deleteMany({ emp_id: empId });
                await clientSchema.deleteMany({ emp_id: empId });
                await testTempTrackingModel.deleteMany({ emp_id: empId });
                await tempTrackingModel.deleteMany({ emp_id: empId });
                await transportModel.deleteMany({ emp_id: empId });
    
                deletedUsers.push(empId);
            }
    
            if (nonExistentUsers.length > 0) {
                return res.send(Response.FailResp(`Users with IDs ${nonExistentUsers.join(', ')} do not exist.`, { nonExistentUsers, deletedUsers }));
            }
    
            return res.send(Response.SuccessResp(`Users Deleted Permanently.`, { deletedUsers }));
        } catch (err) {
            console.log(err);
            res.send(Response.FailResp('Something went wrong', err.message));
        }
    }

    async softDeleteAdminUsers(req, res) {
        try {
            const result = req.verified;
            let { orgId, emp_id, timezone } = result?.userData?.userData;
            let isAdminDataExist = await adminSchema.findOne({ orgId: orgId, emp_id: emp_id });
            if (!isAdminDataExist) return res.send(Response.FailResp(`Admin not exist.`));
    
            let data = req?.body;
            if (!Array.isArray(data.empIds) || data.empIds.length === 0) {
                return res.send(Response.FailResp(`No user IDs provided.`));
            }
    
            let nonExistentUsers = [];
            let deletedUsers = [];
    
            for (let empId of data.empIds) {
                console.log(empId,' ',orgId);
                const result = await userModel.updateOne(
                    { emp_id: empId, orgId: orgId },
                    { status: 4 }
                );
                    console.log(result);
                if (result.matchedCount === 0) {
                    nonExistentUsers.push(empId);
                } else {
                    deletedUsers.push(empId);
                }
            }
            
    
            if (nonExistentUsers.length > 0) {
                return res.send(Response.FailResp(`Users with IDs ${nonExistentUsers.join(', ')} do not exist.`, { nonExistentUsers, deletedUsers }));
            }
    
            return res.send(Response.SuccessResp(`Users Deleted.`, { deletedUsers }));
        } catch (err) {
            console.log(err);
            res.send(Response.FailResp('Something went wrong', err.message));
        }
    }

    async restoreDeletedUsers(req, res) {
        try {
            const result = req.verified;
            let { orgId, emp_id, timezone } = result?.userData?.userData;
            let isAdminDataExist = await adminSchema.findOne({ orgId: orgId, emp_id: emp_id });
            if (!isAdminDataExist) return res.send(Response.FailResp(`Admin not exist.`));
    
            let data = req?.body;
            if (!Array.isArray(data.empIds) || data.empIds.length === 0) {
                return res.send(Response.FailResp(`No user IDs provided.`));
            }
    
            let nonExistentUsers = [];
            let deletedUsers = [];
    
            for (let empId of data.empIds) {
                console.log(empId,' ',orgId);
                const result = await userModel.updateOne(
                    { emp_id: empId, orgId: orgId },
                    { status: 1 }
                );
                    console.log(result);
                if (result.matchedCount === 0) {
                    nonExistentUsers.push(empId);
                } else {
                    deletedUsers.push(empId);
                }
            }
            
    
            if (nonExistentUsers.length > 0) {
                return res.send(Response.FailResp(`Users with IDs ${nonExistentUsers.join(', ')} do not exist.`, { nonExistentUsers, deletedUsers }));
            }
    
            return res.send(Response.SuccessResp(`Users Restored Successfully.`, { deletedUsers }));
        } catch (err) {
            console.log(err);
            res.send(Response.FailResp('Something went wrong', err.message));
        }
    }
    

    async averageWorkingHours(req,res) {
    try {
        const result = req.verified;
        const {orgId,emp_id } = result?.userData?.userData;
            let isAdminExist = await adminSchema.findOne({orgId:orgId ,emp_id:emp_id},{returnDocument:'after'});
            if(!isAdminExist)return res.send(Response.SuccessResp("Admin does not Exists!.")); 


            const startDate = req.query.startDate; 
            const endDate = req.query.endDate;   
            

            function getDatesInRange(startDate, endDate) {
                const dates = [];
                let currentDate = moment(startDate);
                const end = moment(endDate);
                
                while (currentDate.isSameOrBefore(end)) {
                    dates.push(currentDate.format('YYYY-MM-DD'));
                    currentDate = currentDate.add(1, 'days');
                }
                
                return dates;
            }
            
            const userTrackingData = await trackSchema.find({
                date: { $gte: startDate, $lte: endDate },
                orgId: orgId
            });
            
            const groupedData = userTrackingData.reduce((acc, curr) => {
                const dateKey = curr.date;
                if (!acc[dateKey]) {
                    acc[dateKey] = [];
                }
                acc[dateKey].push(curr);
                return acc;
            }, {});
            
            const allDatesInRange = getDatesInRange(startDate, endDate);
            
            const groupedArray = allDatesInRange.map(date => ({
                date: date,
                avgOfAllEmployeesWorkingHours: groupedData[date] ? totalHours(groupedData[date]) : 0
            }));
            
            
            
            res.send(Response.SuccessResp(`Successfully fetched Users`,{averageWorkingHours: groupedArray}));
        } catch (err) {
            logger.log(`Error in fetching data ${err}`);
            res.send(Response.FailResp('Something went wrong!', err.message));
        }

    }

    async adminUsersLocationDetails(req, res) {
        try {
            const result = req.verified;
            let { orgId, emp_id } = result?.userData?.userData;
    
            let employee_id = req.query.employee_id;
            let Date = req.body.date;
            let isAdminDataExist = await adminSchema.findOne({ orgId: orgId, emp_id: emp_id });
    
            
            if (!isAdminDataExist) return res.send(Response.FailResp(`Admin not exist.`));
    
            let query = { orgId: orgId };
            if (employee_id) {
                query.emp_id = employee_id;
            }
    
            const allOrgUsers = await userModel.find(query).select('orgId profilePic emp_id fullName');
            if (!allOrgUsers) return res.send(Response.FailResp(`Users not exist for the admin.`));
    
            let date = Date || moment().format('YYYY-MM-DD');
            const startDate = moment(date).startOf('day').toDate(); // 00:00:00.000

            const endDate = moment(date).endOf('day').toDate(); // 23:59:59.999

            const data = await Promise.all(allOrgUsers.map(async (user) => {
                try {
                    const tempTrackDataOfEmployee = await tempTrackingModel.aggregate([
                        {
                            $match:{
                                emp_id:user.emp_id,
                                updatedAt:{$gte:startDate,$lte:endDate},
                            }
                        },
                        {   $project: {
                            _id: 0,
                            orgId: 1,
                            emp_id: 1,
                            lastGeoLog: { $arrayElemAt: ['$geologs', -1] },
                        }
                    }
                    ])



                    const result = await trackSchema.aggregate([
                        {
                            $match: {
                                emp_id: user.emp_id,
                                date: date,
                                checkIn: { $ne: null }
                            },
                        },
                        {
                            $project: {
                                _id: 0,
                                orgId: 1,
                                emp_id: 1,
                                checkIn: 1,
                                checkOut: 1,
                                lastGeoLog: { $arrayElemAt: ['$geologs', -1] },
                            },
                        }
                    ]);
    
                    if (result.length > 0) {
                        let userData = {
                            user,
                            currentLocation: (tempTrackDataOfEmployee.length&&tempTrackDataOfEmployee[0].lastGeoLog!==undefined)?tempTrackDataOfEmployee[0].lastGeoLog:(result[0]?.lastGeoLog || null),
                            checkIn: result[0]?.checkIn || null,
                            checkOut: result[0]?.checkOut || null,
                        };
                        return userData;
                    } else {
                        return null;
                    }
                } catch (error) {
                    logger.log(`Error fetching user data for emp_id ${user.emp_id}: ${error.message}`);
                    return null;
                }
            }));
    
            const filteredData = data.filter(userData => userData !== null);
    
            const totalCount = await userModel.countDocuments(query);
            const finalData = {
                totalCount,
                orgStartDate:isAdminDataExist.createdAt,
                userData: filteredData,
            };
    
            res.send(Response.SuccessResp(`Successfully fetched userData!`, finalData));
        } catch (err) {
            logger.log(`Error in catch: ${err}`);
            res.send(Response.FailResp('Error in fetch admin details.', err.message));
        }
    }

    async getLocation(req,res){
        try {
            const result = req.verified;
            let { orgId, emp_id } = result?.userData?.userData;
            let skip =parseInt(req.query.skip || 0);
            let limit =parseInt(req.query.limit || 10);
            let searchQuery = req.query.searchQuery;

            let isAdminDataExist = await adminSchema.findOne({ orgId: orgId, emp_id: emp_id });
            if (!isAdminDataExist) return res.send(Response.FailResp(`Admin does not exist.`));


            const uniqueLocationNames = await orgLocationModel.aggregate([
                {
                  $match: {
                    orgId,
                    ...(searchQuery && {
                      locationName: { $regex: searchQuery, $options: "i" }
                    })
                  }
                },
                {
                  $group: {
                    _id: "$locationName",
                    latitude: { $first: "$latitude" },
                    longitude: { $first: "$longitude" },
                    range: { $first: "$range" }
                  }
                },
                {
                  $project: {
                    _id: 0,
                    locationName: "$_id",
                    latitude: 1,
                    longitude: 1,
                    range: 1
                  }
                },
                {
                  $skip: skip
                },
                {
                  $limit: limit
                }
              ]);
              
            res.send(Response.SuccessResp(`Successfully fetched Locations`,{Locations: uniqueLocationNames}));

        } catch (err) {
            logger.log(`Error in catch: ${err}`);
            res.send(Response.FailResp('Error in fetch Location details.', err.message));
        }
    }


    async getDepartment(req, res) {
        try {
            const result = req.verified;
            let { orgId, emp_id } = result?.userData?.userData;
            let skip = parseInt(req.query.skip || 0);
            let limit = parseInt(req.query.limit || 10);
            let searchQuery = req.query.searchQuery;
    
            let isAdminDataExist = await adminSchema.findOne({ orgId: orgId, emp_id: emp_id });
            if (!isAdminDataExist) return res.send(Response.FailResp(`Admin does not exist.`));
    
            const uniqueLocationNames = await userModel.aggregate([
                {
                    $match: {
                        department: { $ne: null },
                        ...(searchQuery && {
                            department: { $regex: searchQuery, $options: "i" }
                        })
                    }
                },
                {
                    $group: {
                        _id: "$department"
                    }
                },
                {
                    $project: {
                        _id: 0,
                        department: "$_id"
                    }
                },
                {
                    $skip: skip
                },
                {
                    $limit: limit
                }
            ]);
            
    
            res.send(Response.SuccessResp(`Successfully fetched Department`, { Department: uniqueLocationNames }));
        } catch (err) {
            logger.log(`Error in catch: ${err}`);
            res.send(Response.FailResp('Error in fetch Department details.', err.message));
        }
    }


    async getUserTimeLine(req,res){
        try{
            const result = req.verified;
            const emp_id = req?.query.emp_id
            const data = req?.body 
            const  date  = req?.body.date ;
            const {error} = adminValidation.searchTask(data);
            if(error)  return res.send(Response.FailResp(`Failed`,error));

            let { orgId } = result?.userData?.userData;

            if (!orgId) {
                return  res.send(Response.FailResp(`Failed, Please provide OrgId`));
            }
            const trackQuery={orgId}

            if(emp_id){
                trackQuery.emp_id=emp_id;
            }
            
            const today = moment().format('YYYY-MM-DD');
            if (date) {
                trackQuery.date=date||today;
            }
            const skip = parseInt(req.query.skip) || 0;
            const limit = parseInt(req.query.limit) || 10;

            let aggregationPipeline = [
                { $match: trackQuery },
                { $unwind: "$geologs" },
            
                { $match: {"geologs.taskId":{$ne:null}} },
            
                {
                    $addFields: {
                        "geologs.taskId": {
                            $convert: {
                                input: "$geologs.taskId",
                                to: "objectId",
                                onError: null, 
                                onNull: null 
                            }
                        }
                    }
                },
                {
                    $lookup: {
                        from: "taskschemas", 
                        localField: "geologs.taskId", 
                        foreignField: "_id", 
                        as: "tasks" 
                    }
                },

                {
                    $addFields: {
                        debugClientId: "$clientId"
                    }
                },
                {
                    $addFields: {
                        "tasks.clientId": {
                            $convert: {
                                input: { $arrayElemAt: ["$tasks.clientId", 0] }, 
                                to: "objectId",
                                onError: null,
                                onNull: null
                            }
                        }
                    }
                },
                {
                    $lookup: {
                        from: "clientschemas", 
                        localField: "tasks.clientId", 
                        foreignField: "_id", 
                        as: "clientDetails" 
                    }
                },

                {
                    $project: {
                        _id: 1,
                        distTravelled:1,
                        "geologs.latitude": 1,
                        "geologs.longitude": 1,
                        "geologs.time": 1,
                        clients: 1,
                        taskDetails: {
                            $arrayElemAt: [
                                {
                                    $map: {
                                        input: "$tasks", 
                                        as: "task", 
                                        in: {
                                            Id: "$$task._id", 
                                            clientId: "$$task.clientId", 
                                            taskId: "$$task.taskId",
                                            orgId: "$$task.orgId",
                                            taskName: "$$task.taskName",
                                            emp_id: "$$task.emp_id",
                                            date: "$$task.date",
                                            time: "$geologs.time",
                                            taskApproveStatus: "$geologs.status",
                                            start_time: "$$task.start_time",
                                            end_time: "$$task.end_time",
                                            taskDescription: "$$task.taskDescription",
                                            empStartTime: "$$task.empStartTime",
                                            empEndTime: "$$task.empEndTime",
                                            files: "$$task.files",
                                            images: "$$task.images",
                                            value: "$$task.value",
                                            taskVolume: "$$task.taskVolume",
                                            tagLogs: "$$task.tagLogs",
                                            createdAt: "$$task.createdAt",
                                        }
                                    }
                                },
                                0
                            ]
                        },
                        clientDetails: {
                            $arrayElemAt: [
                                {
                                    $map: {
                                        input: "$clientDetails",
                                        as: "client",
                                        in: {
                                            Id: "$$client._id",
                                            clientName: "$$client.clientName",
                                            orgId: "$$client.orgId",
                                            client_emp_id: "$$client.emp_id",
                                            contactNumber: "$$client.contactNumber",
                                            clientProfilePic: "$$client.clientProfilePic",
                                            address1: "$$client.address1",
                                            address2: "$$client.address2"
                                        }
                                    }
                                },
                                0
                            ]
                        }
                    }
                },
                // { $skip: skip },
                // { $limit: limit },
            ];
            

            let trackingData = await trackSchema.aggregate(aggregationPipeline).exec();

            
    
            res.send(Response.SuccessResp(`Successfully fetched User TimeLine details!`, trackingData));
        } catch(err){
            logger.log(`Error in catch: ${err}`);
            res.send(Response.FailResp('Error in fetch User TimeLine details! ',err.message));
        }
    }

    async createTask(req, res) {
        const result = req.verified;
        let { orgId, emp_id } = result?.userData?.userData;
        let employeeId = req.query.employeeId;

        try {
            // Check if admin exists
            let isAdminDataExist = await adminSchema.findOne({ orgId: orgId, emp_id: emp_id });
            if (!isAdminDataExist) return res.send(Response.FailResp(`Admin does not exist.`));

            // Validate employeeId
            if (!employeeId) return res.send(Response.FailResp(`Please provide a valid employee Id to create a Task.`));

            // Check if employee exists
            let checkEmployee = await userModel.findOne({ orgId: orgId, emp_id: employeeId, status: { $nin: [2, 3, 4] } });
            if (!checkEmployee) return res.send(Response.FailResp(`Employee with Id ${employeeId} does not exist.`));

            let data = req?.body;

            // Validate task data
            const { value, error } = adminValidation.createTask(data);
            if (error) {
                return res.send(Response.FailResp(error.message, "Validation Failed"));
            }

            // Check if client exists
            const clientExist = await clientSchema.find({ _id: new ObjectId(data.clientId) });
            if (clientExist.length === 0) {
                return res.send(Response.FailResp("Invalid Client Data"));
            }

            // Check for task name duplication
            // const isExist = await taskSchema.find({ orgId: orgId, taskName: data.taskName, clientId: data.clientId });
            // if (isExist.length > 0) {
            //     return res.send(Response.FailResp("Task Name Already Present"));
            // }

            let recurrenceId = uuidv1();
            data.orgId = orgId;
            data.emp_id = employeeId;
            data.recurrenceId = uuidv1();

            // Calculate converted amount if applicable
            if (typeof data.value?.amount === "number") {
                data.value.convertedAmountInUSD = await currencyValue('USD', data.value.amount);
            }
            // Handle recurrence logic
            const { recurrence } = data;
            if (recurrence?.startDate && recurrence?.endDate) {
                const startDate = moment(recurrence.startDate);
                const endDate = moment(recurrence.endDate);
                const excludedDays = recurrence.excludedDays || [];

                let tasksToCreate = [];
                let currentDate = startDate.clone();


                const allDays = [0, 1, 2, 3, 4, 5, 6];
                const daysOfWeek = allDays.filter(day => !excludedDays.includes(day));
                let {startDate:recStartDate,endDate:recendDate} = recurrence;

                while (currentDate.isSameOrBefore(endDate, "day")) {
                    const currentDay = currentDate.day(); // Get the day of the week (0-6, Sunday-Saturday)
                    if (!excludedDays.includes(currentDay)) {
                        const formattedDate = currentDate.format("YYYY-MM-DD"); // Format current date
                        const taskData = {
                            ...data,
                            date: formattedDate,
                            start_time: `${formattedDate} ${data.start_time.split(" ")[1]}`,
                            end_time: `${formattedDate} ${data.end_time.split(" ")[1]}`,
                            recurrenceId: recurrenceId,
                            recurrenceDetails:{TaskCycle:1,startDate:recStartDate,endDate:recendDate,daysOfWeek}

                        };
                        tasksToCreate.push(taskData);
                    }
                    currentDate.add(1, "day"); // Move to the next day
                }
                // Bulk insert tasks
                const createdTasks = await taskSchema.insertMany(tasksToCreate);
                if (createdTasks.length > 0) {
                    return res.send(Response.SuccessResp("Tasks Created Successfully.", createdTasks));
                }
            } else {
                // Create a single task if no recurrence is specified
                const addData = await taskSchema.create(data);
                if (addData) {
                    return res.send(Response.SuccessResp("Task Created Successfully.", addData));
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
        let skip = parseInt(req.query.skip || 0);
        let limit = parseInt(req.query.limit || 10); 
        let generalSearch = req.query.generalSearch;
        let searchClients = req.query.searchClients;
        let searchEmployee = req.query.searchEmployee;
        const sortBy = {};
        sortBy[req?.query?.orderBy || 'createdAt'] = (req?.query?.sort?.toString() || 'desc') === 'asc' ? 1 : -1;
        const date = req.query.date;
        let isAdminDataExist = await adminSchema.findOne({ orgId: orgId, emp_id: emp_id });
        if (!isAdminDataExist) return res.send(Response.FailResp(`Admin does not exist.`));
        let condition = { orgId, taskApproveStatus: { $ne: 5 } };
        if (date) {
            const startDate = date;
            const endDate = date;
            condition.date = { $gte: startDate, $lte: endDate };
        }
        const pipeline = [
            { $match: condition },

            {
                $addFields: {
                    clientIdAsObjectId: { $toObjectId: '$clientId' }
                }
            },
            {
                $lookup: {
                    from: 'userschemas',
                    localField: 'emp_id',
                    foreignField: 'emp_id',
                    as: 'employeeDetails',
                    pipeline: [
                        { $project: { fullName: 1 } }
                    ]
                }
            },
            { $unwind: { path: '$employeeDetails', preserveNullAndEmptyArrays: true } },
            {
                $lookup: {
                    from: 'clientschemas',
                    localField: 'clientIdAsObjectId',
                    foreignField: '_id',
                    as: 'clientData',
                    pipeline: [
                        {
                            $project: {
                                clientName: 1,
                                city: 1,
                                longitude: 1,
                                latitude: 1,
                                _id: 1
                            }
                        },
                    ]
                }
            },
            { $unwind: { path: '$clientData', preserveNullAndEmptyArrays: true } },
            {
                $lookup: {
                    from: 'adminschemas',
                    localField: 'orId',
                    foreignField: 'orId',
                    as: 'adminDetails',
                    pipeline: [
                        { $project: { fullName: 1 } }
                    ]
                }
            },
            { $unwind: { path: '$adminDetails', preserveNullAndEmptyArrays: true } },
        ];
        
        const searchConditions = [];
        
        if (generalSearch) {
            searchConditions.push({
                $or: [
                    { taskName: { $regex: generalSearch, $options: 'i' } },
                    { orgId: { $regex: generalSearch, $options: 'i' } },
                    { emp_id: { $regex: generalSearch, $options: 'i' } },
                    { taskDescription: { $regex: generalSearch, $options: 'i' } },
                    { 'clientData.clientName': { $regex: generalSearch, $options: 'i' } },
                    { 'clientData.address1': { $regex: generalSearch, $options: 'i' } },
                    { 'clientData.address2': { $regex: generalSearch, $options: 'i' } },
                    { 'clientData.city': { $regex: generalSearch, $options: 'i' } },
                ]
            });
        }
        
        if (searchClients) {
            searchConditions.push({
                'clientData.clientName': { $regex: searchClients, $options: 'i' }
            });
        }
        
        if (searchEmployee) {
            searchConditions.push({
                'employeeDetails.fullName': { $regex: searchEmployee, $options: 'i' }
            });
        }
        
        if (searchConditions.length > 0) {
            pipeline.push({ $match: { $and: searchConditions } });
        }
        
        pipeline.push(
            {
                $lookup: {
                    from: 'tags',  
                    localField: 'orgId', 
                    foreignField: 'orgId',  
                    as: 'tagDetails'
                }
            },
            {
                $addFields: {
                    tagLogs: {
                        $map: {
                            input: '$tagLogs',
                            as: 'log',
                            in: {
                                $mergeObjects: [
                                    '$$log', 
                                    {
                                        tagDetail: {
                                            $arrayElemAt: [
                                                {
                                                    $filter: {
                                                        input: '$tagDetails',  
                                                        as: 'tagDetail',
                                                        cond: { $eq: ['$$tagDetail.tagName', '$$log.tagName'] }  
                                                    }
                                                },
                                                0  
                                            ]
                                        }
                                    }
                                ]
                            }
                        }
                    },
                    images: {
                            $map: {
                            input: '$images',
                            as: 'img',
                            in: {
                                url: '$$img.url',
                                description: '$$img.description'
                            }
                            }
                        },
                          files: {
                                $map: {
                                input: '$files',
                                as: 'file',
                                in: {
                                    url: '$$file.url',
                                    name: '$$file.name',
                                    description: '$$file.description'
                                }
                                }
                            }

                        }
                    },

            {
                $group: {
                    _id: '$_id',
                    employeeName: { $first: '$employeeDetails.fullName' },
                    assignedBy: { $first: '$adminDetails.fullName' },
                    orgId: { $first: '$orgId' },
                    taskName: { $first: '$taskName' },
                    tagLogs: { $first: '$tagLogs' },
                    emp_id: { $first: '$emp_id' },
                    date: { $first: '$date' },
                    start_time: { $first: '$start_time' },
                    end_time: { $first: '$end_time' },
                    taskDescription: { $first: '$taskDescription' },
                    taskApproveStatus: { $first: '$taskApproveStatus' },
                    empStartTime: { $first: '$empStartTime' },
                    empEndTime: { $first: '$empEndTime' },
                    createdBy: { $first: '$createdBy' },
                    updatedBy: { $first: '$updatedBy' },
                    createdAt:{$first: '$createdAt'},
                    updatedAt:{$first:'$updatedAt'},
                    files: { $first: '$files' },
                    images: { $first: '$images' },
                    value: { $first: '$value' },
                    taskVolume: { $first: '$taskVolume' },
                    clientName: { $first: '$clientData.clientName' },
                    clientId: {$first:'$clientData._id'},
                    city: { $first: '$clientData.city' },
                    longitude: { $first: '$clientData.longitude' },
                    latitude: { $first: '$clientData.latitude' },
                    recurrenceId: {
                        $first: {
                            $cond: {
                                if: {
                                    $or: [
                                        { $eq: ['$recurrenceDetails', null] }, 
                                        {
                                            $and: [
                                                { $eq: ['$recurrenceDetails.startDate', null] },
                                                { $eq: ['$recurrenceDetails.endDate', null] }
                                            ]
                                        }
                                    ]
                                },
                                then: null,
                                else: '$recurrenceId'
                            }
                        }
                    },
                    recurrenceDetails:{$first:'$recurrenceDetails'}
                }
            },
            {
                $sort:sortBy
            },

        );
        pipeline.push({
            $facet: {
                data: [
                    { $skip: skip },
                    { $limit: limit }
                ],
                totalCount: [
                    { $count: "count" }
                ]
            }
        });
        const result = await taskSchema.aggregate(pipeline);
        const newData = {
            totalCount: result[0].totalCount.length > 0 ? result[0].totalCount[0].count : 0,
            data: result[0].data
        };
        if (result[0].totalCount.length > 0) {
            return res.send(Response.SuccessResp("Task Fetched Successfully", newData));
        }
        return res.send(Response.FailResp("No Data Found"));
    } catch (err) {
        return res.send(Response.FailResp("Something Went Wrong", err));
    }
}

async updateTask(req, res) {
    try {
        
        const result = req.verified;
        let { orgId, emp_id } = result?.userData?.userData;
        let data = req?.body;
        const { value, error } = adminValidation.updateTask(data);
        let isAdminDataExist = await adminSchema.findOne({ orgId: orgId, emp_id: emp_id });
        if (!isAdminDataExist) return res.send(Response.FailResp(`Admin does not exist.`));
        if (error) {
            return res.send(Response.FailResp(error.details,"Validation Failed"))
        }
        let existingTask = await taskSchema.findOne({ _id:new ObjectId(data.taskId) });

        if((existingTask.taskApproveStatus === 4) && data?.start_time !== existingTask?.start_time && data?.end_time !== existingTask?.end_time){
            return res.send(Response.FailResp("Cannot Reschedule Completed Task!."));
        }

        if (data?.start_time !== existingTask?.start_time || data?.end_time !== existingTask?.end_time) {
            data.taskApproveStatus = 0;
        }
        const clientExist = await clientSchema.find({_id: (data.clientId)});
        if(clientExist.length == 0){
            return res.send(Response.FailResp("Invalid Client Data"));
        }
        if (typeof(data.value.amount) === "number") {
            data.value.convertedAmountInUSD = await currencyValue('USD', data.value.amount);
        }
        if(existingTask&&existingTask.recurrenceId===null){
            existingTask = await taskSchema.findOneAndUpdate({ _id: data.taskId },{recurrenceId:uuidv1()},{ returnDocument: 'after' })
        }
        let {recurrence} = req.body;
        let updatedRecurrence
        if (recurrence?.startDate && recurrence?.endDate) {
            const newStartDate = moment(recurrence.startDate);
            const newEndDate = moment(recurrence.endDate);
            let {startDate,endDate} = recurrence;
            const newExcludedDays = recurrence.excludedDays || [];
        
            // Fetch existing tasks for the recurrence
            let existingTasks = await taskSchema.find({ recurrenceId:existingTask.recurrenceId });
        
            // Filter tasks to be deleted based on the new date range and excluded days
            const tasksToDelete = existingTasks.filter((task) => {
                const taskDate = moment(task.date);
                return (
                    (taskDate.isBefore(newStartDate, "day")||
                    (taskDate.isAfter(newEndDate, "day")) && task.taskApproveStatus !== 5) ||
                    (newExcludedDays.includes(taskDate.day()) && task.taskApproveStatus !== 5)
                );
            });
            // Extract task IDs for deletion, but only if taskApproveStatus <= 0
            const deleteIds = tasksToDelete
                .filter((task) => task.taskApproveStatus === 0)
                .map((task) => task._id);
        
            // Remove outdated tasks from the database
            if (deleteIds.length > 0) {
                await taskSchema.deleteMany({ _id: { $in: deleteIds } });
            }
        
            // Generate new tasks based on updated recurrence
            let currentDate = newStartDate.clone();
            let tasksToCreate = [];
            const allDays = [0, 1, 2, 3, 4, 5, 6];
            let daysOfWeek = allDays.filter(day => !newExcludedDays.includes(day));
            while (currentDate.isSameOrBefore(newEndDate, "day")) {
                const currentDay = currentDate.day(); // 0-6 (Sunday-Saturday)
                if (!newExcludedDays.includes(currentDay)) {
                    const formattedDate = currentDate.format("YYYY-MM-DD");
                    const taskData = {
                        ...existingTask,
                        clientId:existingTask.clientId,
                        orgId:existingTask.orgId,
                        taskName:existingTask.taskName,
                        emp_id:existingTask.emp_id,
                        date: formattedDate,
                        start_time: `${formattedDate} ${data.start_time.split(" ")[1]}`,
                        end_time: `${formattedDate} ${data.end_time.split(" ")[1]}`,
                        taskDescription:existingTask.taskDescription,
                        taskApproveStatus:0,
                        empStartTime:existingTask.empStartTime,
                        empEndTime:existingTask.empEndTime,
                        tagLogs:existingTask.tagLogs,
                        createdBy:existingTask.createdBy,
                        updatedBy:existingTask.updatedBy,
                        files:existingTask.files,
                        images:existingTask.images,
                        value:existingTask.value,
                        taskVolume:existingTask.taskVolume,
                        recurrenceId: existingTask.recurrenceId,
                        recurrenceDetails:{TaskCycle:1,startDate,endDate,daysOfWeek,_id:existingTask.recurrenceDetails._id}
                    };
                    // Check if task already exists for this date
                    const taskExists = existingTasks.some(
                        (task) => moment(task.date).isSame(currentDate, "day") && task.taskApproveStatus !== 5
                    );
                    if (!taskExists) {
                        tasksToCreate.push(taskData);
                    }else{
                            let updatedTask = await taskSchema.updateOne(
                                { _id: existingTask._id },
                                { $set: {
                                    recurrenceDetails:{TaskCycle:1,startDate,endDate,daysOfWeek,_id:existingTask.recurrenceDetails._id}
                                } }
                            );
                    }
                }

                currentDate.add(1, "day");
            }
            let updatedTask = await taskSchema.updateMany(
                { recurrenceId:existingTask.recurrenceId },
                {
                  $set: {
                    "recurrenceDetails.TaskCycle": 1,
                    "recurrenceDetails.startDate": startDate,
                    "recurrenceDetails.endDate": endDate,
                    "recurrenceDetails.daysOfWeek": daysOfWeek
                  }
                }
              );
            if (tasksToCreate.length > 0) {
                await taskSchema.insertMany(tasksToCreate);
            }

            
            updatedRecurrence = {
                deleted: deleteIds.length,
                created: tasksToCreate.length,
            }
            // console.log(updatedRecurrence);
        }
        
        data.orgId = orgId;
        data.emp_id = emp_id;
        const existingFiles = Array.isArray(existingTask.files) ? existingTask.files.map(file => file.url) : [];
        const existingImages = Array.isArray(existingTask.images) ? existingTask.images.map(image => image.url) : [];        
        let updateObj = data;
        if (Array.isArray(data?.files)) {
            const newFiles = data.files.map(f => f.url).sort();
            const currentFileUrls = existingFiles.sort();
            const areFilesDifferent = JSON.stringify(newFiles) !== JSON.stringify(currentFileUrls);
            if (areFilesDifferent) {
                if (data.files.length > 5) {
                return res.send(Response.FailResp(`${existingTask.taskName} Task reached maximum limit to upload Documents!`,"Validation Failed"))
            }
                updateObj.$set = updateObj.$set || {};
                updateObj.$set.files = data.files;
            }
        }
        if (Array.isArray(data?.images)) {
            const newImageUrls = data.images.map(f => f.url).sort();
            const currentImageUrls = existingImages.sort();
            const areImagesDifferent = JSON.stringify(newImageUrls) !== JSON.stringify(currentImageUrls);
            if (areImagesDifferent) {
                if (data.images.length > 5) {
                return res.send(Response.FailResp(`${existingTask.taskName} Task reached maximum limit to upload Images!`,"Validation Failed"))
            }
              updateObj.$set = updateObj.$set || {};
              updateObj.$set.images = data.images; 
            }
        }
        if (updateObj.$set && Object.keys(updateObj.$set).length === 0) {
            delete updateObj.$set;
        }
            delete updateObj.files;
            delete updateObj.images;

        if(existingTask.taskApproveStatus>0&&data?.employeeId&&existingTask.emp_id!==data?.employeeId){
            const statuses = ["Pending", "Started", "Paused", "Resumed", "Finished", "Deleted"];
            const getStatus = code => statuses[code] || "Unknown Status";
            return res.send(Response.FailResp(`Task has already ${getStatus(4)}, updating the assigned employee is not permitted.`, "Validation Failed")); 
        }else if(data.employeeId){
            let userExist = await userModel.findOne({emp_id:data.employeeId,orgId});
            if(userExist){
            updateObj.emp_id = data.employeeId;
            }else{
                return res.send(Response.FailResp(`User with emp_id=${data.employeeId} does not exist for the Organization! Please provide valid employeeId`, "Validation Failed"));
            }
        }
        delete updateObj.files
        delete updateObj.images
        const updateTaskData = await taskSchema.findOneAndUpdate(
            { _id: data.taskId },
            updateObj,
             { new: true }, // use `new` to return updated document in mongoose
            { returnDocument: 'after' }
        );
        
        
        if (updateTaskData||updatedRecurrence) {
            return res.send(Response.SuccessResp("Task Updated Successfully",updateTaskData));
        }
        
    } catch (err) {
        console.log(err);
        logger.error(`${err}`);
        return res.send(Response.FailResp("Something Went Wrong", err));
    }
}

async deleteTask(req, res) {
    const result = req.verified;
    let { orgId,emp_id } = result?.userData?.userData;
    try {
        let data = req?.body;
        let isAdminDataExist = await adminSchema.findOne({ orgId: orgId, emp_id: emp_id });
        if (!isAdminDataExist) return res.send(Response.FailResp(`Admin does not exist.`));
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
        console.log(err);
        return res.send(Response.FailResp("Failed to Delete Task", err));
    }

}
   

//Admin Client Services
async createClient(req, res) {
    try {
        const result = req.verified;
        let { orgId, emp_id } = result?.userData?.userData;
        // let employeeId = req.query.employeeId;
        // if (!employeeId) return res.send(Response.FailResp(`Please provide valid employee Id to create a Task.`));

        
        let isAdminDataExist = await adminSchema.findOne({ orgId: orgId, emp_id: emp_id });
        if (!isAdminDataExist) return res.send(Response.FailResp(`Admin does not exist.`));
        // let checkEmployee = await userModel.findOne({ orgId: orgId, emp_id: employeeId, status: { $nin: [2, 3, 4] } });
        // if (!checkEmployee) return res.send(Response.FailResp(`Employee with Id ${employeeId} does not exist.`));        
        const data = req.body;

        const { employeeIds = [] } = req.body;
        const { value, error } = adminValidation.createClient(data);
        if (error) {
            return res.send(Response.FailResp("Validation Error", error.message));
        }
        data.orgId = orgId;
        data.clientId = Math.random();
        data.emp_id = emp_id ;
        
        const clientCompanyExists = await clientSchema.findOne({
            orgId: orgId,
            contactNumber: data.contactNumber
        }).select('contactNumber');
        
        if (clientCompanyExists) {
            return res.send(Response.FailResp("ContactNumber already exist!."));
        }
        const addData = await clientSchema.create(data)
        if (addData) {
            const validEmployees = await userModel.find({
                _id: { $in: employeeIds },
                orgId: orgId, 
                status: { $nin: [2,4] } // Assuming 2, 3, 4 are statuses for inactive employees
            }).select('_id'); // Get only valid IDs

            // Extract the IDs of valid employees
            const validEmployeeIds = validEmployees.map(emp => emp._id.toString());

            // Only assign valid employee IDs
            if (validEmployeeIds.length > 0) {
                await clientSchema.findByIdAndUpdate(
                    addData._id,
                    { $addToSet: { assignedEmployees:  validEmployeeIds  } }, // Use $addToSet to avoid duplicates
                    { new: true }
                );
            }
            const updatedClient = await clientSchema.findById(addData._id);
            return res.send(Response.SuccessResp("Client Created Successfully", updatedClient));
        }
    } catch (err) {
        logger.error(`${err}`);
        return res.send(Response.FailResp("Failed to Add Client", err));
    }
}
async fetchClientInfo(req, res) {
    try {
        const result = req.verified;
        let { orgId, emp_id } = result?.userData?.userData;
        let skip = parseInt(req.query.skip || 0);
        let limit = parseInt(req.query.limit || 10); 
        let searchClients = req.query.searchClients;
        let searchEmployee = req.query.searchEmployee;
        let isAdminDataExist = await adminSchema.findOne({ orgId: orgId, emp_id: emp_id });
        if (!isAdminDataExist) return res.send(Response.FailResp(`Admin does not exist.`));

        let matchCondition = {
            $or: [
                { 
                    orgId: orgId, 
                    status: { $nin: [1, 2] },
                    ...(searchClients ? { clientName: new RegExp(searchClients, 'i') } : {})
                },
                { clientType: 99 }
            ]
        };
        
        
        let pipeline = [
            { $match: matchCondition },
            {
                $lookup: {
                    from: 'userschemas', 
                    localField: 'assignedEmployees', 
                    foreignField: '_id', 
                    as: 'employees' 
                }
            },
            {
                $addFields: {
                    employees: { $arrayElemAt: ["$employees", 0] }
                }
            },
            {
                $lookup: {
                    from: "userschemas",
                    let: { emp_id: "$emp_id", orgId: "$orgId" },
                    pipeline: [
                        {
                            $match: {
                                $expr: {
                                    $and: [
                                        { $eq: ["$emp_id", "$$emp_id"] },
                                        { $eq: ["$orgId", "$$orgId"] },
                                    ]
                                },
                                ...(searchEmployee ? { fullName: new RegExp(searchEmployee, 'i') } : {})
                            }
                        },
                        {
                            $project: {
                                password: 0,
                                passwordEmailSentCount: 0,
                                isGeoFencingOn: 0,
                                isMobileDeviceEnabled: 0,
                                isBioMetricEnabled: 0,
                                isWebEnabled: 0,
                                frequency: 0,
                                geoLogsStatus: 0,
                                snap_points_limit: 0,
                                snap_duration_limit: 0,
                                forgotPasswordToken:0,
                                forgotTokenExpire: 0,
                                createdAt: 0,
                                updatedAt: 0,
                                latitude: 0,
                                longitude: 0,
                                isSuspended: 0,
                                _id: 0,
                                __v: 0
                            }
                        }
                    ],
                    as: "assignedMembers"
                }
            },
            {
                $match: {
                    ...(searchEmployee ? { "assignedMembers.0": { $exists: true } } : {})
                }
            },
            { $group: { _id: null, documents: { $push: "$$ROOT" } } },
            { $unwind: "$documents" },
            { $replaceRoot: { newRoot: "$documents" } },
            {
                $project: {
                    __v: -1,
                    _id:1,
                    clientName:1, 
                    orgId:1,
                    emailId:1,
                    contactNumber:1,
                    clientProfilePic:1,
                    countryCode:1,
                    address1:1,
                    address2:1,
                    city:1,
                    state:1,
                    country:1,
                    zipCode:1,
                    latitude:1,
                    longitude:1,
                    clientType:1,
                    category:1,
                    status:1,
                    createdAt:1,
                    updatedAt:1,
                    updatedBy:1,      
                    assignedMembers:1,
                    employees: {
                        _id: "$employees._id",
                        name: "$employees.fullName",
                        emailId: "$employees.email",
                        emp_id: "$employees.emp_id"
                    }
                }
            },
            { $skip: skip },
            { $limit: limit },

        ];
        
        
        let fetchData = await clientSchema.aggregate(pipeline);
        
        let countOfAllClients = await clientSchema.countDocuments(matchCondition);

        let Clients = {
            totalCount: countOfAllClients+1,
            data: fetchData,
        };

        if (fetchData?.length > 0) return res.send(Response.SuccessResp("Details Fetched Successfully", Clients));
        return res.send(Response.FailResp("No Data Found"));
    } catch (err) {
        return res.send(Response.FailResp("Something Went Wrong", err));
    }
}



async updateClient(req, res) {
    try {
        const result = req.verified;
        let { orgId, _id, emp_id } = result?.userData?.userData;

        let isAdminDataExist = await adminSchema.findOne({ orgId: orgId, emp_id: emp_id });
        if (!isAdminDataExist) return res.send(Response.FailResp(`Admin does not exist.`));

        const clientDetails = req.body;
        const clientId = req.query.clientId;
        clientDetails.updatedBy = _id;

        // Validate input
        const { value, error } = adminValidation.updateClient(clientDetails);
        if (error) {
            return res.send(Response.FailResp("Validation Error", error.message));
        }

        // Check if the client exists
        const findClient = await clientSchema.findOne({ _id: new ObjectId(clientId) });
        if (!findClient) {
            return res.send(Response.FailResp("Invalid ClientId"));
        }

        // Validate employee IDs if present
        if (clientDetails.employeeIds) {
            const validEmployees = await userModel.findOne({
                _id: clientDetails.employeeIds ,
                orgId: orgId,
                status: { $nin: [2, 4] } // Exclude inactive employees
            }).select('_id');

            if(validEmployees){
                clientDetails.assignedEmployees = validEmployees._id;
            }else{
                return res.send(Response.FailResp("Invalid employeeId! Please provide valid employeeId"));
            }
        }

        const updateClient = await clientSchema.findOneAndUpdate(
            { _id: new ObjectId(clientId) },
            { $set: clientDetails },
            { new: true }
        );
        if (updateClient) {
            const updatedClient = await clientSchema.findById(updateClient._id)
            return res.send(Response.SuccessResp("Successfully Updated Client Details", updatedClient));
        }
    } catch (err) {
        logger.error(`${err}`);
        return res.send(Response.FailResp("Failed to Update Client Details", err));
    }
}

async deleteClient(req, res) {
    try {
        const result = req.verified;
        let { orgId, emp_id, timezone } = result?.userData?.userData;
        let isAdminDataExist = await adminSchema.findOne({ orgId: orgId, emp_id: emp_id });
        if (!isAdminDataExist) return res.send(Response.FailResp(`Admin not exist.`));

        let data = req?.body;
        if (!Array.isArray(data.clientIds) || data.clientIds.length === 0) {
            return res.send(Response.FailResp(`No client IDs provided.`));
        }

        let nonExistentUsers = [];
        let deletedUsers = [];

        for (let clientId of data.clientIds) {
            console.log(clientId,' ',orgId);
            const result = await clientSchema.updateOne(
                { _id:new ObjectId(clientId), orgId: orgId },
                { status: 2 }
            );
                console.log(result);
            if (result.matchedCount === 0) {
                nonExistentUsers.push(clientId);
            } else {
                deletedUsers.push(clientId);
            }
        }
        

        if (nonExistentUsers.length > 0) {
            return res.send(Response.FailResp(`Clients with IDs ${nonExistentUsers.join(', ')} do not exist.`, { nonExistentUsers, deletedUsers }));
        }

        return res.send(Response.SuccessResp(`Clients Deleted.`, { deletedUsers }));
    } catch (err) {
        logger.error(`${err}`);
        return res.send(Response.FailResp("Failed to Delete Client Details", err));
    }
}

async allEmployeesAttendance(req,res){
    try{
        const result = req.verified;
        let { orgId, emp_id } = result?.userData?.userData;
        let skip = Number(req.query.skip) || 0;
        let limit = Number(req.query.limit) || 10;
        let isAdminDataExist = await adminSchema.findOne({ orgId: orgId, emp_id: emp_id });
        if (!isAdminDataExist) return res.send(Response.FailResp(`Admin not exist.`));
        
        const users = await userModel.find({ orgId ,status:1}).select('-_id emp_id');
        const empIdSet = new Set(users.map(u => Number(u.emp_id))); // Using Set for O(1) lookup
        let emp_secret_key = config.get("emp_secret_key");
        let empURL = config.get("adminAttendanceURL")
        
        // const skip = req.body.skip ?? 0;
        // const limit  = req.body.limit  ?? 10;
        
        let {date,searchTerm,start_date,end_date} = req.body;
        
        const data = {
            location_id:0,
            department_id:"0",
            role_id:0,
            date,
            ...(searchTerm && { name: searchTerm }),
            skip,
            limit,
            status:1,
            start_date,
            end_date,
            sortColumn: "",
            sortOrder: "",
            employee_type: 0,
            organization_id:Number(orgId),
            is_employee : false,
            loginEmployeeID : null, 
            roleId : null,
            secretKey: emp_secret_key
        };
        
        const response = await axios({ 
            method: 'post',
            maxBodyLength: Infinity,
            url: empURL,
            headers: {
                'Content-Type': 'application/json'
            },
            data 
        });


        data.skip = 0;
        data.limit = users?.length
        const rawData = response?.data?.data || [];
        const filteredData = rawData.filter(entry => empIdSet.has(Number(entry.id)));



        const totalCountResponse = await axios({ 
            method: 'post',
            maxBodyLength: Infinity,
            url: empURL,
            headers: {
                'Content-Type': 'application/json'
            },
            data,
        });
        
                const rawData1 = totalCountResponse?.data?.data || [];
                const filteredData1 = rawData1.filter(entry => empIdSet.has(Number(entry.id)));
        if (response) {
            return res.send(Response.SuccessResp("Attendance fetched successfully",{totalCount:filteredData1?.length,data:filteredData}));
        }


    }catch (err) {
        logger.error(`${err}`);
        return res.send(Response.FailResp("Failed to fetch Attendance Details", err));
    }
}

    
}

const totalHours = (userTrackingData) => {
    return userTrackingData?.reduce((totalHours, employee) => {
        const { geologs ,updatedAt,createdAt} = employee;
        if (!geologs?.length) return totalHours;

        const firstLogTime = createdAt;
        const lastLogTime = updatedAt
        if (!firstLogTime || !lastLogTime || firstLogTime.length <= 8 || lastLogTime.length <= 8) return totalHours;
        const startTime = moment(firstLogTime);
        const endTime = moment(lastLogTime);
        const hours = moment.duration(endTime.diff(startTime)).asHours();
        return totalHours + (isNaN(hours) ? 0 : hours);
    }, 0);
};






const uploadImage = (file, folderName) =>
    new Promise(async (resolve, reject) => {

        const { originalname, buffer } = file;
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

            const blob = storage.bucket(bucketName).file(`${folderName}/${originalname.replace(/ /g, '_')}`);
            const blobStream = blob.createWriteStream({
                resumable: false,
            });
            const timestamp = Date.now();

            blobStream
                .on('finish', async () => {
                    const options = {
                        entity: 'allUsers',
                        role: storage.acl.READER_ROLE,
                    };

                    try {
                        await blob.acl.add(options);
                        const publicUrl = `https://storage.googleapis.com/${bucketName}/${blob.name}?timestamp=${timestamp}`;

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


    function haversineDistance(coord1, coord2) {
        const R = 6371e3; // Radius of the Earth in meters
        const lat1 = coord1.latitude * Math.PI / 180; // Convert degrees to radians
        const lat2 = coord2.latitude * Math.PI / 180; // Convert degrees to radians
        const deltaLat = (coord2.latitude - coord1.latitude) * Math.PI / 180; // Difference in latitude in radians
        const deltaLon = (coord2.longitude - coord1.longitude) * Math.PI / 180; // Difference in longitude in radians
    
        const a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                  Math.cos(lat1) * Math.cos(lat2) *
                  Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    
        const distance = R * c; // Distance in meters
        return distance;
    }

export default new AdminService();
