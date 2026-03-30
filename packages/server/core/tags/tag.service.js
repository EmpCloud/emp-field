import Response from '../../response/response.js';
import Logger from '../../resources/logs/logger.log.js';
// import config from 'config';
import adminSchema from '../../core/admin/admin.model.js'
import tagValidate from '../tags/tag.validate.js';
import tagModel from '../tags/tag.model.js';
import userModel from '../profile/profile.model.js'
import { ObjectId } from 'mongodb';


class TagsService {
    async createTags(req, res){
        try{
            const result = req.verified;
            let { orgId } = result?.userData?.userData;

            const isUserExist = await adminSchema.findOne({ email: result?.userData.userData.email });
            if (!isUserExist) return res.send(Response.FailResp(`Admin does not exist.`));

            const { value,error } = tagValidate.createTag(req?.body);
            if (error) return res.send(Response.validationFailResp(error.message,'Validation Failed'));
            let ExistingTags = await tagModel.find({orgId,isActive:true}).sort({order:-1});
            if (ExistingTags.length>9) return res.send(Response.validationFailResp('Stage Limit Reached!','Validation Failed'));

            value.order = (ExistingTags[0]?.order||0) + 1
            value.orgId = orgId
            value.createdBy = isUserExist._id
            let duplicateTag =await tagModel.findOne({tagName:value.tagName});
            if(duplicateTag) return res.send(Response.validationFailResp(`TagName ${value.tagName} already Exists`,'Duplicate Insertion'));
            let tagCreated = await tagModel.create(value)

            if(tagCreated){
                res.send(Response.SuccessResp(`${value.tagName} Stages Created successfully.`, { tagCreated }));
            }

        }catch(err){
            Logger.error(`Error in catch ${err}`);
            res.send(Response.FailResp('Unable to Fetch Details', err));
        }
    }

    async updateTags(req, res) {
        try {
            const result = req.verified;
            let { orgId } = result?.userData?.userData;
    
            const isUserExist = await adminSchema.findOne({ email: result?.userData.userData.email });
            if (!isUserExist) return res.send(Response.FailResp(`Admin does not exist.`));
    
            const { value, error } = tagValidate.updateTag(req?.body);
            if (error) return res.send(Response.validationFailResp('Validation Failed', error.message));
            
            value.updatedBy = isUserExist._id;
            let duplicateTag = await tagModel.findOne({tagName:value.tagName});
            if(duplicateTag) return res.send(Response.validationFailResp('Duplicate Insertion', `tagName ${value.tagName} already Exists`));
            let updatedData = await tagModel.findOneAndUpdate(
                { _id:new ObjectId(value.tagId), orgId: orgId }, 
                { $set: value },  
                { new: true }  
            );
    
            if (!updatedData) return res.send(Response.FailResp('Stages not found or update failed.'));
    
            return res.send(Response.SuccessResp('Stages updated successfully.', updatedData));
        } catch (err) {
            Logger.error(`Error in catch: ${err}`);
            console.log(err);
            return res.send(Response.FailResp('Unable to update tag.', err));
        }
    }

    async updateTagsOrder(req, res) {
        try {
            const result = req.verified;
            const { orgId } = result?.userData?.userData;
    
            const isUserExist = await adminSchema.findOne({ email: result?.userData.userData.email });
            if (!isUserExist) {
                return res.send(Response.FailResp('Admin does not exist.'));
            }
    
            const { value, error } = tagValidate.updateStagesOrder(req.body);
            if (error) {
                return res.send(Response.validationFailResp('Validation Failed', error.message));
            }
    
            if (Array.isArray(value.updatedStagesOrder)) {
                const updatedTagIds = [];
                const newOrders = {};
    
                // Determine the tags to be updated and their new orders
                value.updatedStagesOrder.forEach(tag => {
                    updatedTagIds.push(tag._id);
                    newOrders[tag._id] = tag.order;
                });
    
                // Get the current tags and their orders
                const currentTags = await tagModel.find({ _id: { $in: updatedTagIds }, orgId ,isActive:true});
                const currentOrderMap = {};
    
                currentTags.forEach(tag => {
                    currentOrderMap[tag._id] = tag.order;
                });
                // Adjust orders of other tags
                const maxOrder = Math.max(...Object.values(newOrders));
                for (const tagId of Object.keys(newOrders)) {
                    const newOrder = newOrders[tagId];
    
                    // Shift orders if moving tag to a new position
                    if (newOrder < currentOrderMap[tagId]) {
                        // Moving tag up in the order
                        await tagModel.updateMany(
                            { orgId, order: { $gte: newOrder, $lt: currentOrderMap[tagId] } },
                            { $inc: { order: 1 } }
                        );
                    } else if (newOrder > currentOrderMap[tagId]) {
                        // Moving tag down in the order
                        await tagModel.updateMany(
                            { orgId, order: { $gt: currentOrderMap[tagId], $lte: newOrder } },
                            { $inc: { order: -1 } }
                        );
                    }
                }
    
                // Now update the tags with the new order
                const bulkOps = value.updatedStagesOrder.map(tag => ({
                    updateOne: {
                        filter: { _id: new ObjectId(tag._id), orgId },
                        update: { $set: { order: newOrders[tag._id] } }
                    }
                }));
    
                await tagModel.bulkWrite(bulkOps);
    
                const updatedData = await tagModel.find({ _id: { $in: updatedTagIds }, orgId });
    
                return res.send(Response.SuccessResp('Stages updated successfully.', updatedData));
            } else {
                return res.send(Response.FailResp('No Stages to update.'));
            }
        } catch (err) {
            Logger.error(`Error in updateStagesOrder: ${err}`);
            return res.send(Response.FailResp('Unable to update stages.', err.message));
        }
    }
    
    
    


    async getTags(req,res) {
        try {
            const result = req.verified;
            let { orgId } = result?.userData?.userData;
            const sortBy = {};
            sortBy[req?.query?.orderBy || 'createdAt'] = req?.query?.sort?.toString() === 'asc' ? 1 : -1;
            let skip = req.query.skip || 0;
            let limit = req.query.limit || 10;
            const isUserExist = await userModel.findOne({ email: result?.userData.userData.email });
            if (!isUserExist) return res.send(Response.FailResp(`User does not exist.`));
                
            let allTags = await tagModel.find({orgId,isActive:true}).sort(sortBy).skip(skip).limit(limit);
            return res.send(Response.SuccessResp('Stages fetched successfully.', allTags));
        } catch (err) {
            Logger.error(`Error in catch: ${err}`);
            console.log(err);
            return res.send(Response.FailResp('Unable to update tag.', err));
        }
    }

    async deleteTags(req, res) {
        try {
            const result = req.verified;
            const { orgId } = result?.userData?.userData;
            let { tagIds } = req.body;
    
            if (!Array.isArray(tagIds) || tagIds.length === 0) {
                return res.send(Response.FailResp('No stages provided for deletion.'));
            }
    
            // Verify admin existence
            const isUserExist = await adminSchema.findOne({ email: result?.userData?.userData?.email });
            if (!isUserExist) return res.send(Response.FailResp('Admin does not exist.'));
    
            // Convert tagIds to ObjectId format
            tagIds = tagIds.map(id => new ObjectId(id));
    
            // Soft delete by setting isActive to false
            const resultDelete = await tagModel.updateMany(
                { _id: { $in: tagIds }, orgId: orgId, isActive: true },
                { $set: { isActive: false } }
            );
    
            if (resultDelete.modifiedCount === 0) {
                return res.send(Response.FailResp('No stages were deleted.'));
            }
    
            // Find the lowest order in the deleted tags to determine where to start reordering
            const deletedTags = await tagModel.find({ _id: { $in: tagIds }, orgId: orgId }).sort({ order: 1 });
            const minOrder = deletedTags.length ? deletedTags[0].order : null;
    
            // Reorder tags if there's a valid minimum order to start from
            if (minOrder !== null) {
                await tagModel.updateMany(
                    { orgId, isActive: true, order: { $gt: minOrder } },
                    { $inc: { order: -1 } }
                );
            }
    
            return res.send(Response.SuccessResp('Stages deleted and reordered successfully.', { deletedCount: resultDelete.modifiedCount }));
        } catch (err) {
            Logger.error(`Error in deleteTags: ${err}`);
            return res.send(Response.FailResp('Unable to delete tags.', err.message));
        }
    }
    

    async getAdminTags(req,res) {
        try {
            const result = req.verified;
            let { orgId } = result?.userData?.userData;
    
            const isUserExist = await adminSchema.findOne({ email: result?.userData.userData.email });
            let skip = parseInt(req.query.skip) || 0;
            let limit = parseInt(req.query.limit) || 10;
            if (!isUserExist) return res.send(Response.FailResp(`Admin does not exist.`));
            const sortBy = {};
            sortBy[req?.query?.orderBy || 'createdAt'] = req?.query?.sort?.toString() === 'asc' ? 1 : -1;

            const { search = '' } = req.query;

            let searchCriteria = { orgId ,isActive:true};
            if (search) {
                searchCriteria = {
                    ...searchCriteria,
                    $or: [
                        { tagName: { $regex: search, $options: 'i' } },
                        { tagDescription: { $regex: search, $options: 'i' } },
                        { color: { $regex: search, $options: 'i' } },
                        { orgId: { $regex: search, $options: 'i' } },
                    ],
                    
                };
            }
                
            let totalCount = await tagModel.countDocuments(searchCriteria);
            let allTags = await tagModel.find(searchCriteria).skip(skip).limit(limit).sort(sortBy);
            let finalResult = {
                totalCount,
                allTags
            }
            return res.send(Response.SuccessResp('Stages fetched successfully.', finalResult));
        } catch (err) {
            Logger.error(`Error in catch: ${err}`);
            console.log(err);
            return res.send(Response.FailResp('Unable to update Stage.', err));
        }
    }    
    

}  
export default new TagsService();      