
import transportService from "./transport.service.js";

class transportController {




async empModeOfTransport(req,res,next){
    /* #swagger.tags = ['Profile']
                      #swagger.description = 'API to import Admin from EmpMonitor' */
        /* #swagger.security = [{
               "AccessToken": []
        }] */                                      
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Update Mode, Frequency or Range ',
                                required: true,
                                schema: { $ref: "#/definitions/empModeOfTransport" }
        } */                      

    return await transportService.empModeOfTransport(req, res, next);
                      
}
async empUpdFreqRad(req,res,next){
    /* #swagger.tags = ['Admin']
                      #swagger.description = 'API to import Admin from EmpMonitor' */
        /* #swagger.security = [{
               "AccessToken": []
        }] */   
        /*	#swagger.parameters['Employee_id'] = {
                              in: 'query',
                              required:true,
                              description: 'Enter the emp_id of employee',                     
        }*/ 
        /*	#swagger.parameters['Employee_orgId'] = {
                              in: 'query',
                              required:true,
                              description: 'Enter the Employee orgId',                     
        }*/                            
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Update Mode, Frequency or Range ',
                                schema: { $ref: "#/definitions/empUpdFreqRad" }
        } */                      

    return await transportService.empUpdFreqRad(req, res, next);
                      
}

}
export default new transportController();