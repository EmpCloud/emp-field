import Joi from 'joi';
class taskValidateService {
    createTask(body) {
        const fileSchema = Joi.object({
            url: Joi.string().uri().allow(null),
        });
    
        const imageSchema = Joi.object({
            url: Joi.string().uri().allow(null),
            description: Joi.string().allow(null)
        });
        const valueSchema = Joi.object({
            currency: Joi.string().allow(null), 
            amount: Joi.number().allow(null)    
        });
        const tagsSchema = Joi.object({
            tagName: Joi.string().allow(null),
            time: Joi.string().allow(null),
        });
        const recurrenceSchema = Joi.object({
            startDate:Joi.string().required().allow(null),
            endDate:Joi.string().required().allow(null),
            excludedDays: Joi.array()
            .items(Joi.number().integer().min(0).max(6))
            .unique()
            .default([])
            .messages({
                'array.base': `"excludedDays" must be an array.`,
                'array.unique': `"excludedDays" must not contain duplicate values.`,
                'number.base': `"excludedDays" items must be integers.`,
                'number.min': `"excludedDays" items must be between 0 (Sunday) and 6 (Saturday).`,
                'number.max': `"excludedDays" items must be between 0 (Sunday) and 6 (Saturday).`,
            })
        });
        const schema = Joi.object().keys({
            clientId: Joi.string(),
            taskName: Joi.string().required(true),
            date:Joi.string().required(true),
            start_time: Joi.string().required(true),
            end_time: Joi.string().required(true),
            taskDescription: Joi.string(),
            taskApproveStatus: Joi.number().default(0),//1:approve,2:pending
            files: Joi.array().items(fileSchema).max(5).optional(),
            images: Joi.array().items(imageSchema).max(5).optional(),
            tagLogs: Joi.array().items(tagsSchema).optional(),
            taskVolume: Joi.number().allow(null),
            value: valueSchema.optional(),
            recurrence:recurrenceSchema.optional()

        })
        const result = schema.validate(body);
        return result;
    }
    updateTask(body) {
        const fileSchema = Joi.object({
            url: Joi.string().uri().allow(null),
        });
    
        const imageSchema = Joi.object({
            url: Joi.string().uri().allow(null),
            description: Joi.string().allow(null)
        });
        const valueSchema = Joi.object({
            currency: Joi.string().allow(null), 
            amount: Joi.number().allow(null)    
        });
        const tagsSchema = Joi.object({
            tagName: Joi.string().allow(null),
            time: Joi.string().allow(null),
        });
        const recurrenceSchema = Joi.object({
            startDate: Joi.string().required(true).allow(null),
            endDate: Joi.string().required(true).allow(null),
        })
        const schema = Joi.object().keys({
            taskId: Joi.string(),
            clientId: Joi.string(),
            tagId:Joi.string(),
            taskName: Joi.string().required(true),
            date:Joi.string().required(true),
            start_time: Joi.string().required(true),
            end_time: Joi.string().required(true),
            taskDescription: Joi.string(),
            taskApproveStatus: Joi.number().default(0),//1:approve,2:pending
            files: Joi.array().items(fileSchema).optional(),
            images: Joi.array().items(imageSchema).optional(),
            tagLogs: Joi.array().items(tagsSchema).optional(),
            taskVolume: Joi.number().allow(null),
            value: valueSchema.optional(),
            recurrence:recurrenceSchema.optional()
        })
        const result = schema.validate(body);
        return result;

    }
    deleteDocument(body) {
        const schema = Joi.object().keys({
            taskId: Joi.string().required(),
            docIds: Joi.array().items(Joi.string()).required()
        })
        const result = schema.validate(body);
        return result;

    }
}
export default new taskValidateService();