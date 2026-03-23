
import Joi from 'joi';

class AdminValidation {
    createAdmin(body) {
        const schema = Joi.object()
            .keys({
                fullName: Joi.string().trim(true).min(3).required(),
                age: Joi.number().integer().positive(),
                gender: Joi.string(),   
                profilePic: Joi.string().trim(true).allow(null),
                password: Joi.string().trim(true).required(),
                countryCode: Joi.string()
                .trim(true)
                .allow(null)
                .allow(''),
                phoneNumber: Joi.number().integer().min(10 ** 9).max(10 ** 10 - 1).messages({
                    'number.min': 'Mobile number should be 10 digit.',
                    'number.max': 'Mobile number should be 10 digit'
                }),
                email: Joi.string().email().required(),
                username: Joi.string(),
                orgName: Joi.number().integer().positive().required(),
                userType: Joi.number().valid(0,1)
                    .required(),
                address: Joi.string().trim(true).min(4).max(100),
                city: Joi.string()
                    .trim(true)
                    .allow(null),
                state: Joi.string()
                    .trim(true).allow(null),
                country: Joi.string()
                    .trim(true),
                zipCode: Joi.string()
                    .trim(true)
                    .min(4)
                    .max(6)
                    .regex(/^[0-9]*$/)
                    .messages({ 'string.pattern.base': 'zip code can contain only number' }),
                timezone: Joi.string()
                .trim(true),
                language: Joi.string().default('en')
            })
            .required();
        const result = schema.validate(body);
        return result;
    }
  
    updateAdmin(body) {
        const schema = Joi.object()
            .keys({
                fullName: Joi.string().trim(true).min(3),
                age: Joi.number().integer().positive(),
                gender: Joi.string(),
                profilePic: Joi.string().trim(true).allow(null),
                countryCode: Joi.string()
                .trim(true)
                .allow(null)
                .allow(''),
                address1: Joi.string().trim(true).min(4).max(100),
                address2: Joi.string().trim(true).min(4).max(100),
                role:Joi.string().allow(null),
                emp_id:Joi.string(),
                orgId:Joi.string(),
                latitude:Joi.string(),
                longitude:Joi.string(),
                city: Joi.string()
                    .trim(true)
                    .allow(null),
                state: Joi.string()
                    .trim(true).allow(null),
                country: Joi.string()
                    .trim(true),
                zipCode: Joi.string()
                    .trim(true)
                    .min(4)
                    .max(6)
                    .regex(/^[0-9]*$/)
                    .messages({ 'string.pattern.base': 'zip code can contain only number' }),
                phoneNumber:Joi.number().allow(null),
                isSuspended:Joi.boolean(),
                orgRadius:Joi.number(),
                timezone: Joi.string()
                    .trim(true),
                language: Joi.string()
            })
            .required();
        const result = schema.validate(body);
        return result;
    }

    fetchAdmin(body) {
        const schema = Joi.object()
            .keys({
                email: Joi.string().email().required(),
                password: Joi.string().required(),
            })
            .required();
        const result = schema.validate(body);
        return result;
    }
    updateAdminPhoneNumber(body) {
        const schema = Joi.object()
            .keys({
                adminEmail: Joi.string().email().required(),
                newNumber: Joi.number().integer().min(10 ** 9).max(10 ** 10 - 1).messages({
                    'number.min': 'Mobile number should be 10 digit.',
                    'number.max': 'Mobile number should be 10 digit'
                }),
            })
            .required();
        const result = schema.validate(body);
        return result;
    }
    verifyEmail(body) {
        const schema = Joi.object().keys({
            activationLink: Joi.string().required().trim(true),
            adminMail: Joi.string().email().required().trim(true)
        });
        const result = schema.validate(body);
        return result;
    }
    verifyPhone(body) {
        const schema = Joi.object().keys({
            activationOtp: Joi.string().required().trim(true),
            adminNumber:  Joi.number().integer().min(10 ** 9).max(10 ** 10 - 1).messages({
                'number.min': 'Mobile number should be 10 digit.',
                'number.max': 'Mobile number should be 10 digit'
            }),
        });
        const result = schema.validate(body);
        return result;
    }
    forgotPasswordValidation(body) {
        const schema = Joi.object().keys({
            email: Joi.string().required().trim(true)
        });
        const result = schema.validate(body);
        return result;
    }
    resetPassword(body) {
        const schema = Joi.object().keys({
            email: Joi.string().required().trim(true),
            token: Joi.string().required().trim(true),
            newPassword: Joi.string().required().trim(true),
        });
        const result = schema.validate(body);
        return result;
    }
    updatePassword(body) {
        const schema = Joi.object()
            .keys({
                oldPassword: Joi.string().required(),
                newPassword: Joi.string().min(6).max(25).required(),
            })
            .required();
        const result = schema.validate(body);
        return result;
    }
    searchTask(body) {
        const schema = Joi.object().keys({
            date: Joi.date(),
        })
        const result = schema.validate(body);
        return result;
    }
    updateGeoLogStatus(body){
        const schema = Joi.object({
            emp_id: Joi.number().required(),
            geologsId: Joi.array().items(Joi.string().required()).required() // Updated to handle an array of strings
          });
        
          // Validate the input body against the schema
          const result = schema.validate(body);
        
          return result;
        
    }
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
            files: Joi.array().items(fileSchema).min(0).max(5).optional(), // max 5 files
            images: Joi.array().items(imageSchema).min(0).max(5).optional(),
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
            taskId: Joi.string(),
            clientId: Joi.string(),
            employeeId: Joi.string(),
            taskName: Joi.string().required(true),
            date:Joi.string().required(true),
            start_time: Joi.string().required(true),
            end_time: Joi.string().required(true),
            taskDescription: Joi.string(),
            taskApproveStatus: Joi.number().default(0),//1:approve,2:pending
            files: Joi.array().items(fileSchema).min(0).max(5).optional(),
            images: Joi.array().items(imageSchema).min(0).max(5).optional(),
            tagLogs: Joi.array().items(tagsSchema).optional(),
            taskVolume: Joi.number().allow(null),
            value: valueSchema.optional(),
            recurrence:recurrenceSchema.optional()
        })
        const result = schema.validate(body);
        return result;

    }

    //client validation
    createClient(body) {
        const schema = Joi.object().keys({
            clientName: Joi.string().trim(true).min(3).required(),
            latitude:Joi.number(),
            longitude: Joi.number(),
            countryCode: Joi.string()
            .trim(true)
            .allow(null)
            .allow(''),
            phoneNumber: Joi.string().max(15).allow(null),
            emp_id:Joi.string(),
            orgId:Joi.string(),
            clientId:Joi.string(),
            emailId: Joi.string().email().allow(null),
            clientProfilePic:Joi.string().allow(null),
            contactNumber: Joi.string().max(15).allow(null),
            address1: Joi.string().trim(true).min(4).max(250),
            address2: Joi.string().allow(''),
            country: Joi.string()
            .trim(true),
            city: Joi.string()
                .trim(true)
                .allow(null),
            state: Joi.string()
                .trim(true).allow(null),
            zipCode: Joi.string()
                .trim(true)
                .regex(/^[a-zA-Z0-9\s]*$/)
                .allow('')
                .allow(null)
                .messages({ 'string.pattern.base': 'zip code can contain only number' }),
            timezone: Joi.string(),
            clientType:Joi.number(),
            status:Joi.string(),
            updatedBy:Joi.string(),
            createdBy:Joi.string(),
            category: Joi.string()
            .trim(true),
            employeeIds: Joi.string()
            .allow(null) 
            .default(null) 

        })
        const result = schema.validate(body);
        return result;
    }
    updateClient(body) {
        const schema = Joi.object().keys({
            clientName: Joi.string().trim(true).min(3).required(),
            latitude:Joi.number(),
            longitude: Joi.number(),
            countryCode: Joi.string()
            .trim(true)
            .allow(null)
            .allow(''),
            phoneNumber: Joi.string().max(15).allow(null),
            emailId: Joi.string().email().allow(null),
            clientProfilePic:Joi.string().allow(null),
            orgId: Joi.string(),
            emp_id:Joi.string(),
            contactNumber: Joi.string().max(15).allow(null),
            address1: Joi.string().trim(true).min(4).max(250),
            address2: Joi.string().allow(''),
            country: Joi.string()
            .trim(true),
            city: Joi.string()
                .trim(true)
                .allow(null),
            state: Joi.string()
                .trim(true).allow(null),
            zipCode: Joi.string()
                .trim(true)
                .regex(/^[a-zA-Z0-9\s]*$/)
                .allow(null)
                .allow('')
                .messages({ 'string.pattern.base': 'zip code can contain only number' }),
            timezone: Joi.string(),
            clientType:Joi.number(),
            status:Joi.string(),
            updatedBy:Joi.string(),
            createdBy:Joi.string(),
            category: Joi.string()
            .trim(true),
            employeeIds: Joi.string()
            .allow(null) 
            .default(null) 
        })
        const result = schema.validate(body);
        return result;

    }
}
export default new AdminValidation();
