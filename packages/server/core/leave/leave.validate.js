import Joi from 'joi';

class leaveValidation {
    createLeaveType(body) {
        const schema = Joi.object().keys({
            name: Joi.string().required().max(50),
            duration: Joi.number().required(),
            no_of_days: Joi.number().required(),
            carry_forward: Joi.number().required().max(2).default(0),
        });
        const result = schema.validate(body);
        return result;
    }
    updateLeaveType(body) {
        const schema = Joi.object().keys({
            name: Joi.string().max(50),
            duration: Joi.number(),
            no_of_days: Joi.number(),
            carry_forward: Joi.number().max(2),
        });
        const result = schema.validate(body);
        return result;
    }
  
}
export default new leaveValidation();
