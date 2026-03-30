import Joi from 'joi';
class UserValidation {
    createUser(body) {
        const schema = Joi.object()
            .keys({
                fullName: Joi.string().trim(true).min(3).required(),
                age: Joi.number().integer().positive(),
                gender: Joi.string(),
                profilePic: Joi.string().trim(true).allow(null),
                password: Joi.string().trim(true).required(),
                countryCode: Joi.string()
                    .max(10)
                    .regex(/^\+[0-9]*$/)
                    .trim(true)
                    .allow(null)
                    .messages({ 'string.pattern.base': 'Country Code must start with + and contain only number' }),
                phoneNumber: Joi.number().integer().min(10 ** 9).max(10 ** 10 - 1).messages({
                    'number.min': 'Mobile number should be 10 digit.',
                    'number.max': 'Mobile number should be 10 digit'
                }),
                email: Joi.string().email().required(),
                orgId: Joi.string().required(),
                role: Joi.string(),
                location:Joi.string().default(null),
                department:Joi.string().default(null),
                address1: Joi.string().trim(true).min(4).max(250),
                address2: Joi.string().trim(true).min(4).max(250),
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
                .trim(true)
            })
            .required();
        const result = schema.validate(body);
        return result;
    }
    updateUser(body) {
        const schema = Joi.object()
            .keys({
                fullName: Joi.string().trim(true).min(3),
                age: Joi.number().integer().positive(),
                gender: Joi.string(),
                profilePic: Joi.string().trim(true).allow(null),
                username: Joi.string(),
                role: Joi.string(),
                location:Joi.string().default(null),
                department:Joi.string().default(null),
                address1: Joi.string().trim(true).min(4).max(250),
                address2: Joi.string().trim(true).min(4).max(250),
                countryCode: Joi.string()
                    .max(10)
                    .regex(/^\+[0-9]*$/)
                    .trim(true)
                    .allow(null)
                    .messages({ 'string.pattern.base': 'Country Code must start with + and contain only number' }),
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
                language: Joi.string()
            })
            .required();
        const result = schema.validate(body);
        return result;
    }
    validateUserState(body){
        const JoiSchema = Joi.object({
            isSuspended: Joi.boolean().required(),
        })
        return JoiSchema.validate(body);
    }
    createEmpUser(body) {
        const schema = Joi.object()
            .keys({
                fullName: Joi.string().trim(true).min(3).required(),
                age: Joi.number().integer().positive(),
                gender: Joi.string(),
                password: Joi.string().trim(true),
                emp_id : Joi.number().integer().required(),
                email: Joi.string().email().required(),
                userType: Joi.number().default(1),
                empMonitor : Joi.boolean.default(true),
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
    updateUserBiometricConfig(body) {
        const schema = Joi.object()
            .keys({
                email: Joi.string().email().required(),
                isBiometricUser: Joi.boolean().required(),
            })
            .required();
        const result = schema.validate(body);
        return result;
    }

}
export default new UserValidation();
