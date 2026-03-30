
import roleService from './role.service.js';
class roleController {

    async create(req, res, next) {
        /* #swagger.tags = ['Roles']
                            #swagger.description = 'This routes is used for create the Roles' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Role Details',
                                required: true,
                                schema: { $ref: "#/definitions/CreateRole" }
        } */
        return await roleService.create(req, res, next);
    }

    async get(req, res, next) {
        /* #swagger.tags = ['Roles']
                            #swagger.description = 'This routes is used to fetch roles'  */
        /* #swagger.security = [{
                  "AccessToken": []
        }] */
       /*	#swagger.parameters['skip'] = {
                           in: 'query',
                           type:'integer',
                           description: 'Skip Values',
       } */
    /*	#swagger.parameters['limit'] = {
                            in: 'query',
                            type:'integer',
                            description: 'results limit',
        } */ 
     /* #swagger.parameters['searchQuery'] = {
                            in: 'query',
                            type: 'string',
                            description:'search Roles',
     } */            
        return await roleService.get(req, res, next)
    }

    async update(req, res, next) {
        /* #swagger.tags = ['Roles']
                            #swagger.description = 'This routes is used to update the Role' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /* #swagger.parameters['roleId'] = {
              in: 'query',
              required: true
      }*/
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Role Details',
                                required: true,
                                schema: { $ref: "#/definitions/UpdateRole" }
        } */
        return await roleService.update(req, res, next);
    }

    async delete(req, res, next) {
        /* #swagger.tags = ['Roles']
                            #swagger.description = 'This routes is used to delete the Role' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /* #swagger.parameters['roleId'] = {
              in: 'query',
              required: true
      }*/
        return await roleService.delete(req, res, next);
    }


}

export default new roleController()