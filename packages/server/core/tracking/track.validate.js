import Joi from 'joi';
class taskValidateService {
    insertLocation(body) {
        const schema = Joi.object().keys({
            distTravelled:Joi.number(),
            date: Joi.date().required(true),
            time:Joi.string(),
            latitude: Joi.number(),
            longitude: Joi.number(),
            status: Joi.number(),
            taskId:Joi.string().allow(null)
        })
        const result = schema.validate(body);
        return result;
    }
}
export default new taskValidateService();