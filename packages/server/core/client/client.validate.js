import Joi from 'joi';
class clientValidation {
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
            emailId: Joi.string().email().allow(null).allow(''),
            clientProfilePic:Joi.string().allow(null).allow(''),
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
                .allow("")
                .allow(null)
                .default(null),
            timezone: Joi.string(),
            clientType:Joi.number(),
            status:Joi.number(),
            updatedBy:Joi.string(),
            createdBy:Joi.string(),
            category: Joi.string()
            .trim(true)
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
            emailId: Joi.string().email().allow(null).allow(''),
            clientProfilePic:Joi.string().allow(null).allow(''),
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
                .allow("")
                .allow(null)
                .default(null),
            timezone: Joi.string(),
            clientType:Joi.number(),
            status:Joi.number(),
            updatedBy:Joi.string(),
            createdBy:Joi.string(),
            category: Joi.string()
            .trim(true)
        })
        const result = schema.validate(body);
        return result;

    }
}
export default new clientValidation();
