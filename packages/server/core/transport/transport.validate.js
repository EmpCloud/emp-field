import Joi from 'joi';

class transportValidation {

    transportModeValidate(body) {
        const schema = Joi.object().keys({
            currentMode: Joi.string().valid('bike', 'car'),
        });
        const result = schema.validate(body);
        return result;
    }

    transportFreqRadValidate(body) {
        const schema = Joi.object().keys({
            configMode: Joi.string().valid('bike', 'car'),
            configFrequency: Joi.number(),
            configRadius: Joi.number(),
        });
        const result = schema.validate(body);
        return result;
    }
}

export default new transportValidation();
