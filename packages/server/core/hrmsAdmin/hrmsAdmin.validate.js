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

    validateEmployeeLocation(body){
        const schema = Joi.object().keys({
            employeeId: Joi.string().required(),
            address: Joi.string().allow(null, '').optional(),
            latitude: Joi.number()
            .when("geo_fencing", {
              is: true,
              then: Joi.number().required().messages({
                "any.required": `"latitude" is required when geo_fencing is true`,
                "number.base": `"Please provide latitude in Number`,
              }),
              otherwise: Joi.number().allow(null).messages({
                "number.base": `"latitude" must be a number or null`,
              }),
            }),
    
          longitude: Joi.number()
            .when("geo_fencing", {
              is: true,
              then: Joi.number().required().messages({
                "any.required": `"longitude" is required when geo_fencing is true`,
                "number.base": `"Please provide Longitude in Number`,
              }),
              otherwise: Joi.number().allow(null).messages({
                "number.base": `"longitude" must be a number or null`,
              }),
            }),
            range: Joi.number().default(50).optional(),
            geo_fencing: Joi.boolean().default(true).optional(),
            isMobEnabled: Joi.boolean().default(true).optional()
        });
        const result = schema.validate(body);
        return result;
    }
  
}
export default new holidayValidation();
