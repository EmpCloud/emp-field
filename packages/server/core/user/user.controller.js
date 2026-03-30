import UserService from './user.service.js';

class UserController {
    async createUser(req, res, next) {
        /* #swagger.tags = ['Users']
                            #swagger.description = 'This routes is used for create the Users' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'User Details',
                                required: true,
                                schema: { $ref: "#/definitions/CreateUser" }
        } */
        return await UserService.createUser(req, res, next);
    }
    async fetchUsers(req, res, next) {
        /* #swagger.tags = ['Users']
                           #swagger.description = 'This routes is used to fetch User details'  */
        /* #swagger.security = [{
                  "AccessToken": []
        }] */
       /*	#swagger.parameters['fullName'] = {
                           in: 'query',
                           description: 'Enter  Employee fullName.',
                           required: false,
                                
        }    
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
        return await UserService.fetchUsers(req, res, next);
    }
    async updateUser(req, res, next) {
        /* #swagger.tags = ['Users']
                           #swagger.description = 'This routes is used for update the Users' */
        /* #swagger.security = [{
                  "AccessToken": []
        }]*/
        /* #swagger.parameters['userId'] = {
                in: 'query',
                required: true
        }*/
        /*  #swagger.parameters['data'] = {
                in: 'body',
                required: true,
                schema: { $ref: "#/definitions/UpdateUser" }
        }*/
        return await UserService.updateUser(req, res, next);
    }
    async deleteUser(req, res, next) {
        /* #swagger.tags = ['Users']
                           #swagger.description = 'This routes is used for delete the User'  */
        /* #swagger.security = [{
               "AccessToken": []
        }] */
        /* #swagger.parameters['userId'] = {
                in: 'query',
                required: true
        }*/

        return await UserService.deleteUser(req, res, next);
    }
    async fetchEmpUsers(req, res, next) {
        /* #swagger.tags = ['Users']
                           #swagger.description = 'This routes is used the emp Users'  */
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
        /*	#swagger.parameters['orderBy'] = {
                         in: 'query',
                         description: 'keyword to be ordered',
                         enum:["firstName","lastName","email", "role", "department", "emp_id"],
     } */
        /*	#swagger.parameters['sort'] = {
                        in: 'query',
                        description: 'sorting parameters(asc or desc)',
                        enum: ['asc', 'desc'],
    } */
    /*	#swagger.parameters['search'] = {
                        in: 'query',
                        description: 'keyword to be searched',
    } */

        return await UserService.fetchEmpUsers(req, res, next);
    }

    async addEmpUsers(req, res, next) {
        /* #swagger.tags = ['Users']
                            #swagger.description = 'This routes is used for adding the emp Users' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'User Details',
                                required: true,
                                schema: { $ref: "#/definitions/AddEmpUser" }
        } */
        return await UserService.addEmpUsers(req, res, next);
    }
    async setFrequencyAndGeoLoc(req,res,next){
        /* #swagger.tags = ['Users']
                            #swagger.description = 'API to Update User Details' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
    /*	#swagger.parameters['emp_id'] = {
                        in: 'query',
                        type:'string',
                        description: 'Provide emp_id of employee!.',
    } */              
    /*	#swagger.parameters['frequency'] = {
                        in: 'query',
                        type:'integer',
                        description: 'Provide frequency.',
    } */  
    /*	#swagger.parameters['geoLocation'] = {
                        in: 'query',
                        type:'boolean',
                        description: 'Provide boolean value for GeoLocation.',
    } */           
      return await UserService.setFrequencyAndGeoLoc(req, res, next);
      }

      async updateBiometricConfig(req,res,next){
        /* #swagger.tags = ['Users']
                            #swagger.description = 'API to Update Biometric Details' */
        /* #swagger.security = [{
                   "AccessToken": []
        }] */
        /*  #swagger.parameters['data'] = {
                in: 'body',
                required: true,
                schema: { $ref: "#/definitions/UpdateUserBiometricConfig" }
        }*/
        return await UserService.updateBiometricConfig(req, res, next);

      }

}
export default new UserController();
