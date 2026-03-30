
import hrmsAdminService from './hrmsAdmin.service.js';
class hrmsAdminController {
 
    async updateLocation(req, res, next) {
        /* #swagger.tags = ['HrmsAdmin']
                            #swagger.description = 'Api to update office location' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Update Organisation Location',
                                required: true,
                                schema: { $ref: "#/definitions/updateLocation" }
        } */
        return await hrmsAdminService.updateLocation(req, res, next);
    }

    async getLocationDetails(req, res, next) {
        /* #swagger.tags = ['HrmsAdmin']
                            #swagger.description = 'Api to Organisation Location Details' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Api to Organisation Location Details',
                                required: true,
                                schema: { $ref: "#/definitions/getLocationDetails" }
        } */
        return await hrmsAdminService.getLocationDetails(req, res, next);
    }

    async getEmployeeConf(req, res, next) {
        /* #swagger.tags = ['HrmsAdmin']
                            #swagger.description = 'Api to Get the Employee Confiuration Details' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Api to Get the Organisation Location Details',
                                required: true,
                                schema: { $ref: "#/definitions/getEmployeeConf" }
        } */
        return await hrmsAdminService.getEmployeeConf(req, res, next);
    }

    async updateEmployeeConf(req, res, next) {
        /* #swagger.tags = ['HrmsAdmin']
                            #swagger.description = 'Api to Update the Organisation Location Details' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Api to Update the Organisation Location Details',
                                required: true,
                                schema: { $ref: "#/definitions/updateEmployeeConf" }
        } */
        return await hrmsAdminService.updateEmployeeConf(req, res, next);
    }

    async orgLocationList(req, res, next) {
        /* #swagger.tags = ['HrmsAdmin']
                            #swagger.description = 'Api to get Organisation Location List' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        return await hrmsAdminService.orgLocationList(req, res, next);
    }

    async updateEmployeeLocation(req,res,next){
        /* #swagger.tags = ['HrmsAdmin']
                            #swagger.description = 'Api to create Individual Employee Location Details' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Api to create Individual Employee Location Details',
                                required: true,
                                schema: { $ref: "#/definitions/createEmployeeLocation" }
        } */    

    return await hrmsAdminService.updateEmployeeLocation(req,res,next);
    }

    async getEmployeeLocation(req,res,next){
        /* #swagger.tags = ['HrmsAdmin']
                            #swagger.description = 'Api to get Individual Employee Location Details' */
        /* #swagger.security = [{
                   "AccessToken": []
        }] */
        /*	#swagger.parameters['employeeId'] = {
                                in: 'query',
                                description: 'Enter  _id of Employee.',
                                required: true,
                                
        } */
        return await hrmsAdminService.getEmployeeLocation(req,res,next);
    }
}

export default new hrmsAdminController()