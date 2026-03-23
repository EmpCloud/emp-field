import Joi from 'joi';

class TagValidation {
    createTag(body) {
        const schema = Joi.object().keys({
            tagName: Joi.string()
                .trim(true)
                .min(3)
                .max(50)
                .required()
                .messages({
                    'string.empty': 'Tag name is required',
                    'string.min': 'Tag name must be at least 3 characters long',
                    'string.max': 'Tag name must be less than or equal to 50 characters long'
                }),
                
            tagDescription: Joi.string()
                .trim(true)
                .max(250)
                .allow(null)
                .messages({ 'string.max': 'Description can be a maximum of 250 characters' }),
                
            color: Joi.string()
                .trim(true)
                .regex(/^#[0-9A-F]{6}$/i)
                .allow(null)
                .messages({ 'string.pattern.base': 'Color must be a valid hex code (e.g., #FFFFFF)' }),


            updatedBy: Joi.string()
                .trim(true)
                .allow(null),
                
            isActive: Joi.boolean()
                .default(true)
                .required()
                .messages({ 'any.required': 'isActive status is required' }),
        });

        const result = schema.validate(body);
        return result;
    }

    updateTag(body) {
        const schema = Joi.object().keys({
            tagId: Joi.string().required(),
            tagName: Joi.string()
                .trim(true)
                .min(3)
                .max(50)
                .messages({
                    'string.empty': 'Tag name is required',
                    'string.min': 'Tag name must be at least 3 characters long',
                    'string.max': 'Tag name must be less than or equal to 50 characters long'
                }),
            tagDescription: Joi.string()
                .trim(true)
                .max(250)
                .allow(null)
                .messages({ 'string.max': 'Description can be a maximum of 250 characters' }),
            color: Joi.string()
                .trim(true)
                .regex(/^#[0-9A-F]{6}$/i)
                .allow(null)
                .messages({ 'string.pattern.base': 'Color must be a valid hex code (e.g., #FFFFFF)' }),
            updatedBy: Joi.string()
                .trim(true)
                .allow(null),
            isActive: Joi.boolean()
                .default(true)
                .messages({ 'any.required': 'isActive status is required' }),
        });
    
        const result = schema.validate(body);
        return result;
    }
    updateStagesOrder(body) {
        const schema = Joi.object().keys({
            updatedStagesOrder: Joi.array()
            .items(
                Joi.object().keys({
                    _id: Joi.string().required().messages({
                        'string.empty': 'Tag ID is required',
                    }),
                    order: Joi.number()
                        .integer()
                        .min(1)
                        .max(10)
                        .required()
                        .messages({
                            'number.base': 'Order must be a number',
                            'number.integer': 'Order must be an integer',
                            'number.min': 'Order must be at least 1',
                            'number.max': 'Order must be at most 10',
                            'any.required': 'Order is required',
                        }),
                })
            )
            .allow(null)
            .messages({ 'array.base': 'updatedTagsOrder must be an array of tag objects' }),
        });
    
        const result = schema.validate(body);
        return result;
    }
}

export default new TagValidation();
