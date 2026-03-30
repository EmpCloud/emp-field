import Response from '../../response/response.js';
import jwt from 'jsonwebtoken';
import Logger from '../../resources/logs/logger.log.js';
import UserSchema from '../profile/profile.model.js';
import ProfileValidation from '../profile/profile.validate.js';
import config from 'config';
import axios from 'axios';
import adminSchema from '../../core/admin/admin.model.js'

import { Storage } from '@google-cloud/storage';

const storage = new Storage({ keyFilename: 'storageconfig.json' });
const bucketName = config.get('BUCKET_NAME_BIOMETRICS');
const bucket = storage.bucket(bucketName);

class ProfileService {
    async fetchProfile(req, res){
        const result = req.verified;
        try{
            const {email} = result?.userData?.userData;
            let resultData = await UserSchema.find({ email: email})
            .select({
                fullName: 1,
                age: 1,
                gender: 1,
                email: 1,
                profilePic: 1,
                address: 1,
                phoneNumber: 1,
                address1: 1,
                address2: 1,
                zipCode: 1,
                state: 1,
                country: 1,
                city: 1,
                latitude:1,
                longitude:1
            });
            res.send(Response.SuccessResp('Profile Details Fetched Successfully.',{resultData}))
        }
        catch(err){
            Logger.error(`Error in catch ${err}`);
            res.send(Response.FailResp('Unable to Fetch Profile Details', err));
        }
    }


    async updateProfile(req, res){
        try{
            const result = req.verified;
            const {email} = result?.userData?.userData;
                const data = req?.body;
                const { value, error } = ProfileValidation.updateUser(req?.body);
                if (error) return res.send(Response.FailResp('Validation Failed', error.details[0]?.message));
                let resultData = await UserSchema.findOneAndUpdate({ email: (email) }, { $set: data }, { returnDocument: 'after' });
                let { forgotPasswordToken, forgotTokenExpire, ...filteredData } = resultData.toJSON();
                res.send(Response.SuccessResp(`User Profile Updated Successfully.`, {resultData:[ filteredData]}));
        }
        catch(err){
            Logger.error(`Error in catch ${err}`);
            res.send(Response.FailResp('Unable to Update Profile Details', err));
        }
    }

    async uploadProfileImage(req, res){
        const result = req.verified.userData.userData;
        try{
            if(req.files){
                let item = req.files[0];

                let folderName = 'fieldTracking';
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
                res.send(Response.SuccessResp(`Image Uploaded Successfully.`, { profileURL: profileURL }));
            } 
        } 
        catch(err){
           console.log(err)
        }
    }

    async updateSnapDetails(req, res){
        try{
            const result = req.verified;
            const {email,orgId,emp_id} = result?.userData?.userData;
            let { Employee_orgId, Employee_id } = req.query;
            let isAdminExist = await adminSchema.findOne({orgId:orgId ,emp_id:emp_id},{returnDocument:'after'});
            if(!isAdminExist)return res.send(Response.SuccessResp("Admin does not Exists!.")); 
                const data = req?.body;
                let resultData = await UserSchema.findOneAndUpdate({ emp_id:Employee_id ,orgId:Employee_orgId}, { $set: data }, { returnDocument: 'after' });
                let { forgotPasswordToken, forgotTokenExpire, ...filteredData } = resultData.toJSON();
                res.send(Response.SuccessResp(`User Snap Limit Updated Successfully.`, {resultData:[ filteredData]}));
        }
        catch(err){
            Logger.error(`Error in catch ${err}`);
            res.send(Response.FailResp('Something Went Wrong', err));
        }
    }
}

export default new ProfileService();

const uploadImage = (file, folderName) =>
    new Promise(async (resolve, reject) => {

        const { originalname, buffer } = file;
        try {
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