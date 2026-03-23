import Joi from 'joi';

class UserValidation {
    verifyUser(body) {
        const schema = Joi.object().keys({
            activationLink: Joi.string().required().trim(true),
            userMail: Joi.string().required().trim(true),
            invitation: Joi.string().valid(0, 1, 2).required().trim(true),
        });
        const result = schema.validate(body);
        return result;
    }
    verifyUserCreds(body) {
        const schema = Joi.object().keys({
            userMail: Joi.string().required().trim(true),
            password: Joi.string().required().trim(true),
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
    setPassword(body) {
        const schema = Joi.object().keys({
            mail: Joi.string().email().required().trim(true),
            password: Joi.string().required().trim(true)
        });
        const result = schema.validate(body);
        return result;
    }
    verifyPhone(body) {
        const schema = Joi.object().keys({
            userNumber: Joi.string().required().trim(true)
        });
        const result = schema.validate(body);
        return result;
    }
    resetPassword(body) {
        const schema = Joi.object().keys({
            email: Joi.string().required().trim(true),
            verifyToken: Joi.string().required().trim(true),
            newPassword: Joi.string().required().trim(true),
        });
        const result = schema.validate(body);
        return result;
    }
    updateUser(body) {
        const schema = Joi.object().keys({
            fullName: Joi.string().trim(true).min(1).required(),
            age: Joi.number().integer().positive(),
            gender: Joi.string(),
            profilePic: Joi.string().trim(true),
            address: Joi.string(),
            zipCode: Joi.string(),
            city:Joi.string(),
            state:Joi.string(),
            country: Joi.string()

        });
        const result = schema.validate(body);
        return result;
    }
    updateTracking(body) {
        const schema = Joi.object().keys({
            userId: Joi.string().required(true),
            orgId: Joi.string().required(true),
            trackingType: Joi.number().integer().valid(1,2).default(1),
            date: Joi.date()
                .iso()
                .min(new Date().toISOString().split('T')[0])
                .max(new Date().toISOString().split('T')[0])
                .required(true),
            latitude: Joi.string().trim(true),
            longitude: Joi.string().trim(true)
        });
        const result = schema.validate(body);
        return result;
    }
    getAttendance(body) {
        const schema = Joi.object().keys({
            startDate: Joi.date().iso(),
            endDate: Joi.date().iso(),
            userId: Joi.string(),
        });
        const result = schema.validate(body);
        return result;
    }
    createUser(body) {
        const schema = Joi.object().keys({
            fullName: Joi.string().trim(true).min(1).required(),
            email: Joi.string().lowercase(),
            phoneNumber: Joi.string().default(null).allow(null),
            age: Joi.number().integer().positive().default(0),
            gender: Joi.string().default(null),
            password: Joi.string().default(null),
            role: Joi.string(),
            location:Joi.string().default(null),
            department:Joi.string().default(null),
            profilePic: Joi.string().trim(true).default(null),
            address1: Joi.string().allow(null),
            address2: Joi.string().default(null),
            zipCode: Joi.string().default(null),
            city:Joi.string().default(null),
            state:Joi.string().default(null),
            country: Joi.string().default(null),
            orgId: Joi.string(),
            emp_id: Joi.string(),
            timezone: Joi.string()

        });
        const result = schema.validate(body);
        return result;
    }
}
export default new UserValidation();
