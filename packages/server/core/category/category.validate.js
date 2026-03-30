import Joi from 'joi';
class categoryValidateService{
    createCategory(body) {
        const schema = Joi.object().keys({
            categoryName: Joi.string().min(1).max(20).trim(true)
        })
        const result = schema.validate(body);
        return result;
    }
    updateCategory(body) {
        const schema = Joi.object().keys({
            categoryName: Joi.string().min(1).max(20).trim(true)
        })
        const result = schema.validate(body);
        return result;

    }
}
export default new categoryValidateService();