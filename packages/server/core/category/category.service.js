import categorySchema from "./category.model.js";
import Response from '../../response/response.js';
import logger from '../../resources/logs/logger.log.js';
import categoryValidate from './category.validate.js'
class categoryService {
    async addCategory(req, res) {
        const result = req.verified;
        let { orgId } = result.userData?.userData;
        try {
            let data = req.body.categoryName;
            let actaulData = data.toLowerCase();
            let createData = {
                categoryName: actaulData
            };
            const { value, error } = categoryValidate.createCategory(createData);
            if (error) {
                return res.send(Response.FailResp("Validation Failed", error))
            }
            const isExist = await categorySchema.find({ orgId: orgId, categoryName: actaulData });
            if (isExist.length > 0) {
                return res.send(Response.FailResp("Category Name already present"));
            }
            createData.orgId = orgId;
            const addData = await categorySchema.create(createData);
            if (addData) {

                return res.send(Response.SuccessResp("Category created successfully.", addData));
            }
        } catch (err) {
            logger.error(`${err}`);
            return res.send(Response.FailResp("Failed to add category", err));
        }
    }
    async fetchCategory(req, res) {
        const result = req.verified;
        let { orgId } = result.userData?.userData;
        try {
            let categoryId = req.query.categoryId;
            let skipValue = req.query.skip || 0;
            let limitValue = req.query.limit || 10;
            let condition = {};
            if (categoryId) {
                condition._id = categoryId;
                const isExist = await categorySchema.find({ _id: categoryId });
                if (isExist.length == 0) return res.send(Response.FailResp("Invalid category Id"));
            } else {
                condition.orgId = orgId;
            }
            const countCategory = await categorySchema.countDocuments({ orgId: orgId });
            const fetchData = await categorySchema.find(condition).skip(skipValue).limit(limitValue);
            if (fetchData) {
                const response = {
                    totalCategory: countCategory,
                    data: fetchData
                };
                return res.send(Response.SuccessResp("Category fetched successfully.", response));
            }
        } catch (err) {
            logger.error(`${err}`);
            return res.send(Response.FailResp("Failed to fetch category detailes", err));
        }
    }
    async updateCategory(req, res) {
        const result = req.verified;
        let { orgId } = result.userData?.userData;
        try {
            let categoryId = req.query.categoryId;
            let data = req.body.categoryName;
            const { value, error } = categoryValidate.updateCategory(req.body);
            if (error) {
                return res.send(Response.FailResp("Validation Failed", error))
            }
            const isExist = await categorySchema.find({ _id: categoryId });
            if (isExist.length == 0) return res.send(Response.FailResp("Invalid category Id"));
            const isNameExist = await categorySchema.find({ orgId: orgId, categoryName: data.toLowerCase() });
            if (isNameExist.length > 0) return res.send(Response.FailResp("Category already exist"));
            const updateData = await categorySchema.findOneAndUpdate({ _id: categoryId }, { $set: { categoryName: data.toLowerCase() } }, { returnDocument: 'after' });
            if (updateData) {
                return res.send(Response.SuccessResp("Category updated successfully.", updateData));
            }
        } catch (err) {
            logger.error(`${err}`);
            return res.send(Response.FailResp("Failed to update category detailes", err));
        }
    }
    async deleteCategory(req, res) {
        const result = req?.verified;
        let { orgId } = result?.userData?.userData;
        try {
            let categoryId = req?.query?.categoryId;
            let deleteData, condition = {};
            if (categoryId) {
                condition._id = categoryId;
                const isExist = await categorySchema.find({ _id: categoryId });
                if (isExist.length == 0) return res.send(Response.FailResp("Invalid category Id"));
                deleteData = await categorySchema.deleteOne(condition);
            } else {
                condition.orgId = orgId;
                deleteData = await categorySchema.deleteMany(condition);
            }
            if (deleteData.deletedCount > 0) {
                return res.send(Response.SuccessResp("Category fetched successfully.", deleteData));
            }
        } catch (err) {
            logger.error(`${err}`);
            return res.send(Response.FailResp("Failed to fetch category detailes", err));
        }
    }
}
export default new categoryService();