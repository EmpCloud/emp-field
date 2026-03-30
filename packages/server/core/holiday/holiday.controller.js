
import holidayService from './holiday.service.js';
class leaveController {
 
    async createHoliday(req, res, next) {
        /* #swagger.tags = ['Holiday']
                            #swagger.description = 'This routes is used for create the Holiday' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Holiday  Details',
                                required: true,
                                schema: { $ref: "#/definitions/CreateHoliday" }
        } */
        return await holidayService.createHoliday(req, res, next);
    }

    async getHoliday(req,res,next){
       /* #swagger.tags = ['Holiday']
                           #swagger.description = 'This routes is used to fetch Holiday'  */
        /* #swagger.security = [{
                  "AccessToken": []
        }] */
        return await holidayService.getHoliday(req,res,next)
    }

    async updateHoliday(req, res, next) {
        /* #swagger.tags = ['Holiday']
                            #swagger.description = 'This routes is used to update the Holiday' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /* #swagger.parameters['holidayId'] = {
                in: 'query',
                required: true
        }*/
        
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Holiday Type Details',
                                required: true,
                                schema: { $ref: "#/definitions/UpdateHoliday" }
        } */
        return await holidayService.updateHoliday(req, res, next);
    }

    async deleteHoliday(req, res, next) {
        /* #swagger.tags = ['Holiday']
                            #swagger.description = 'This routes is used to delete the Holiday' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
          /* #swagger.parameters['holidayId'] = {
                in: 'query',
                required: true
        }*/
        return await holidayService.deleteHoliday(req, res, next);
    }


}

export default new leaveController()