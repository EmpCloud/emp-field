import Joi from 'joi';
class ProfileValidation {
    updateUser(body) {
        const schema = Joi.object()
            .keys({
                fullName: Joi.string().trim(true).min(3),
                age: Joi.number().integer().positive(),
                gender: Joi.string(),
                latitude:Joi.string(),
                longitude:Joi.string(),
                profilePic: Joi.string().trim(true).allow(null),
                role: Joi.string(),
                location:Joi.string().default(null),
                department:Joi.string().default(null),
                email: Joi.string().email(),
                countryCode: Joi.string()
                    .max(10)
                    .regex(/^\+[0-9]*$/)
                    .trim(true)
                    .allow(null)
                    .messages({ 'string.pattern.base': 'Country Code must start with + and contain only number' }),
                address1: Joi.string().trim(true).min(4).max(250),
                address2: Joi.string().trim(true).min(4).max(250),
                phoneNumber: Joi.number()
                                .integer()
                                .allow("")
                                .allow(null)
                                .default(null),
                city: Joi.string()
                    .trim(true)
                    .allow(null),
                state: Joi.string()
                    .trim(true).allow(null),
                country: Joi.string()
                    .trim(true),
                zipCode: Joi.string()
                    .allow("")
                    .allow(null)
                    .default(null),
                timezone: Joi.string()
                    .trim(true),
            });
        const result = schema.validate(body);
        return result;
    }
}
export default new ProfileValidation();
