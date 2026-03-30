import Joi from 'joi';

class holidayValidation {
    getHolidays(body) {
        const schema = Joi.object().keys({
            date: Joi.date().default(null),
        });

        const result = schema.validate(body);
        return result;
    }

    addNewHolidays(body) {
        const schema = Joi.object().keys({   
          
               name: Joi.string().required().max(50),
               date: Joi.date().iso().required(),
               orgId: Joi.string().required().max(50),
            })
    
            const result = schema.validate(body);
            return result;
    }

    updateHoliday(body) {
        const schema = Joi.object().keys({
            name: Joi.string().max(50),
            date: Joi.date().iso(),  
        });
        const result = schema.validate(body);
        return result;
    }
  
}
export default new holidayValidation();
