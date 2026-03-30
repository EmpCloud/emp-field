import Response from '../../response/response.js';
import logger from '../../resources/logs/logger.log.js';
import clientSchema from './client.model.js';
import clientValidation from './client.validate.js';
import  categorySchema from '../category/category.model.js'
import config from 'config';
import { Storage } from '@google-cloud/storage';
import { ObjectId } from 'mongodb';

const storage = new Storage({ keyFilename: 'storageconfig.json' });
const bucketName = config.get('BUCKET_NAME_BIOMETRICS');
const bucket = storage.bucket(bucketName);
class clientService {
    async createClient(req, res) {
        const result = req.verified;
        let { orgId, emp_id ,_id} = result?.userData?.userData;
        try {
            const data = req.body;
            const { value, error } = clientValidation.createClient(data);
            if (error) {
                return res.send(Response.FailResp("Validation Error", error.message));
            }
            data.orgId = orgId;
            data.emp_id = emp_id;
            data.assignedEmployees = _id;
            const findClient = await clientSchema.aggregate([
                {
                    "$match": {
                        "orgId": orgId,
                        "$or": [
                            { "clientName": data.clientName },
                            { "clientCompany": data.clientCompany, "contactNumber": data.contactNumber }
                        ]
                    }
                },
                {
                    "$project": { clientCompany: 1, clientName: 1, contactNumber: 1 }
                }
            ]);
            
            if (findClient.length > 0) {
                const clientNameExists = findClient.some(client => client.clientName === data.clientName);
                const clientCompanyExists = findClient.some(client => client.clientCompany === data.clientCompany);
                const contactNumberExists = findClient.some(client => client.contactNumber === data.contactNumber);
            
                if (clientNameExists && clientCompanyExists && contactNumberExists) {
                    return res.send(Response.FailResp("Client name, company, and contact number already exist.", "Duplicate Insertion!"));
                } else if (clientNameExists && clientCompanyExists) {
                    return res.send(Response.FailResp("Client name and company already exist.", "Duplicate Insertion!"));
                } else if (clientNameExists && contactNumberExists) {
                    return res.send(Response.FailResp("Client name and contact number already exist.", "Duplicate Insertion!"));
                } else if (clientCompanyExists && contactNumberExists) {
                    return res.send(Response.FailResp("Client company and contact number already exist.", "Duplicate Insertion!"));
                } else if (clientNameExists) {
                    return res.send(Response.FailResp("Client name already exists.", "Duplicate Insertion!"));
                } else if (clientCompanyExists) {
                    return res.send(Response.FailResp("Client company already exists.", "Duplicate Insertion!"));
                } else if (contactNumberExists) {
                    return res.send(Response.FailResp("Contact number already exists.", "Duplicate Insertion!"));
                }
            }
            
            const addData = await clientSchema.create(data)
            if (addData) {
                return res.send(Response.SuccessResp("Client Created Successfully", addData))
            }
        } catch (err) {
            logger.error(`${err}`);
            return res.send(Response.FailResp("Failed to Add Client", err));
        }
    }
    async fetchClientInfo(req, res) {
        const result = req.verified;
        let { orgId ,_id:employeeId} = result?.userData?.userData;
        console.log(employeeId,'employeeId');
        try {
            let condition = {
                status: { $nin: [1, 2] },
                $or: [
                    {
                        $and: [
                            { orgId: orgId },
                            { assignedEmployees: new ObjectId(employeeId) }
                        ]
                    },
                    { clientType: 99 }
                ]
            };
            
            let fetchData = await clientSchema.find(condition);
            if (fetchData?.length > 0) return res.send(Response.SuccessResp("Details Fetched Successfully", fetchData))
            return res.send(Response.FailResp("No Data Found", null));
        } catch (err) {
            console.log(err);
            return res.send(Response.FailResp("Something Went Wrong", err));
        }
    }

    async updateClient(req, res) {
        const result = req.verified;
        let { orgId, _id } = result?.userData?.userData;
        try {
            const clientDetails = req.body;
            const clientId = req.query.clientId;
            clientDetails.updatedBy = _id;
            const { value, error } = clientValidation.updateClient(clientDetails);
            if (error) {
                return res.send(Response.FailResp("Validation Error", error.message));
            }
            const findClient = await clientSchema.findOne({ _id:new ObjectId(clientId) });
            if (!findClient) { return res.send(Response.FailResp("Invalid ClientId")) }
            const updateClient = await clientSchema.findOneAndUpdate({ _id:new ObjectId(clientId) }, { $set: clientDetails }, { returnDocument: 'after' })
            if (updateClient) {
                return res.send(Response.SuccessResp("Successfully Updated Client Details", updateClient))
            }
        } catch (err) {
            logger.error(`${err}`);
            return res.send(Response.FailResp("Failed to Update Client Details", err));
        }

    }
    async deleteClient(req, res) {
        const result = req.verified;
        let { orgId } = result?.userData?.userData;
        try {
            const clientId = req.query.clientId;
            let clientDetail;
            if (clientId) {
                const findClient = await clientSchema.findOne({ _id: clientId });
                if (!findClient) { return res.send(Response.FailResp("Invalid ClientId")) }
                clientDetail = await clientSchema.deleteOne({ _id: clientId });
            } else {
                clientDetail = await clientSchema.deleteMany({ orgId: orgId });
            }
            if (clientDetail.deletedCount > 0) {
                return res.send(Response.SuccessResp("Successfully Deleted Client Details", clientDetail))
            }
        } catch (err) {
            logger.error(`${err}`);
            return res.send(Response.FailResp("Failed to Delete Client Details", err));
        }
    }

    async uploadProfileImage(req, res){
        const result = req.verified.userData.userData;
        try{
            if(req.files){
                let item = req.files[0];

                let folderName = 'fieldTracking/client';
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
            logger.log(`Error in catch ${err}`);
            res.send(Response.FailResp('Error Uploading.', err.message));
        }
    }
}
export default new clientService();

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
                    reject(`Unable to Upload File, Something Went Wrong: ${err}`);
                })
                .end(buffer);
        } catch (err) {
            reject(`Error Uploading Image: ${err}`);
        }
    });

