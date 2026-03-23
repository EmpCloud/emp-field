
import leaveService from './leave.service.js';
class leaveController {
 
    async createLeaveType(req, res, next) {
        /* #swagger.tags = ['Leave']
                            #swagger.description = 'This routes is used for create the leave type' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Leave Type Details',
                                required: true,
                                schema: { $ref: "#/definitions/CreateLeaveType" }
        } */
        return await leaveService.createLeaveType(req, res, next);
    }

    async getLeaveType(req,res,next){
       /* #swagger.tags = ['Leave']
                           #swagger.description = 'This routes is used to fetch leave type'  */
        /* #swagger.security = [{
                  "AccessToken": []
        }] */
        return await leaveService.getLeaveType(req,res,next)
    }

    async updateLeaveType(req, res, next) {
        /* #swagger.tags = ['Leave']
                            #swagger.description = 'This routes is used to update the leave type' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /* #swagger.parameters['leaveId'] = {
                in: 'query',
                required: true
        }*/
        
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Leave Type Details',
                                required: true,
                                schema: { $ref: "#/definitions/UpdateLeaveType" }
        } */
        return await leaveService.updateLeaveType(req, res, next);
    }

    async deleteLeaveType(req, res, next) {
        /* #swagger.tags = ['Leave']
                            #swagger.description = 'This routes is used to delete the leave type' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
          /* #swagger.parameters['leaveId'] = {
                in: 'query',
                required: true
        }*/
        return await leaveService.deleteLeaveType(req, res, next);
    }

    async getLeaves(req, res, next){
        /* #swagger.tags = ['Leave']
                            #swagger.description = 'API to fetch Employee Leaves' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Leave Type Details',
                                required: true,
                                schema: { $ref: "#/definitions/getLeaves" }
        } */
        return await leaveService.getLeaves(req, res, next);
    }

    async createLeave(req, res, next) {
        /* #swagger.tags = ['Leave']
                            #swagger.description = 'This routes is used for create the leave' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'API to Create Leave Request',
                                required: true,
                                schema: { $ref: "#/definitions/CreateLeaves" }
        } */
        return await leaveService.createLeave(req, res, next);
    }

    async leaveTypeOption(req, res, next) {
        /* #swagger.tags = ['Leave']
                            #swagger.description = 'This routes is used get the leave type' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        return await leaveService.leaveTypeOption(req, res, next);
    }

    async updateLeaves(req, res, next) {
        /* #swagger.tags = ['Leave']
                            #swagger.description = 'This routes is used to update Leave Request' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'API to Update the Leave Request',
                                required: true,
                                schema: { $ref: "#/definitions/UpdateLeaves" }
        } */
        return await leaveService.updateLeaves(req, res, next);
    }

    async deleteLeaves(req, res, next) {
        /* #swagger.tags = ['Leave']
                            #swagger.description = 'This routes is used to delete Leave Request' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'API to delete the Leave Request',
                                required: true,
                                schema: { $ref: "#/definitions/DeleteLeaves" }
        } */
        return await leaveService.deleteLeaves(req, res, next);
    }
}

export default new leaveController()