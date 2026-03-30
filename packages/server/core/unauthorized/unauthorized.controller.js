import unauthorizedService from './unauthorized.service.js';

class unauthorizedController {
    async verifyUser(req, res, next) {
        /* #swagger.tags = ['Open-User']
                           #swagger.description = 'User email verification API'  */
        /*	#swagger.parameters['data'] = {
                                  in: 'body',
                                  description: 'User Email Verification',
                                  required: true,
                                  schema: { $ref: "#/definitions/UserVerification" }
          } */

        return await unauthorizedService.verifyUser(req, res, next);
    }
    async verifyPhone(req, res, next) {
        /* #swagger.tags = ['Open-User']
                           #swagger.description = 'This routes is used for sending verify mail to admin mail Id'  */
        /*	#swagger.parameters['data'] = {
                                 in: 'body',
                                 description: 'Admin Email Verification',
                                 required: true,
                                 schema: { $ref: "#/definitions/userPhoneVerification" }
        }*/

        return await unauthorizedService.verifyPhone(req, res, next);
    }
    async setPassword(req, res, next) {
        /* #swagger.tags = ['Open-User']
                           #swagger.description = 'Set the Password for the User'  */
        /*	#swagger.parameters['data'] = {
                                  in: 'body',
                                  description: 'Sets the Password for the User',
                                  required: true,
                                  schema: { $ref: "#/definitions/UserPasswordSet" }
          } */

        return await unauthorizedService.setPassword(req, res, next);
    }
    async UserLogin(req, res, next) {
        /* #swagger.tags = ['Open-User']
                           #swagger.description = 'User Login API'  */
        /*	#swagger.parameters['data'] = {
                                  in: 'body',
                                  description: 'User Login API',
                                  required: true,
                                  schema: { $ref: "#/definitions/UserCreds" }
          } */

        return await unauthorizedService.UserLogin(req, res, next);
    }
    async resetPassword(req, res, next) {
        /* #swagger.tags = ['Open-User']
                           #swagger.description = 'Update the Admin details'  */

        /*	#swagger.parameters['data'] = {
                                  in: 'body',
                                  description: 'User Login API',
                                  required: true,
                                  schema: { $ref: "#/definitions/resetPassword" }
          } */
        return await unauthorizedService.resetPassword(req, res, next);
    }

    async forgotPassword(req, res, next) {
        /* #swagger.tags = ['Open-User']
                           #swagger.description = 'Recover the Admin password '  */
        /*	#swagger.parameters['data'] = {
                                  in: 'body',
                                  description: 'User Email ',
                                  required: true,
                                  schema: { $ref: "#/definitions/forgetPassword" }
        } */
        return await unauthorizedService.forgotPassword(req, res, next);
    }
    async generateOtp(req, res, next) {
        /* #swagger.tags = ['Open-User']
                           #swagger.description = 'User phone verification otp generation API'  */
        /*	#swagger.parameters['data'] = {
                                  in: 'body',
                                  description: 'User Number ',
                                  required: true,
                                  schema: { $ref: "#/definitions/AdminNumber" }
          } */

        return await unauthorizedService.generateOtp(req, res, next);
    }    
    async verifyOTP(req,res,next){
        /* #swagger.tags = ['Open-User']
                           #swagger.description = 'Recover the Admin password '  */     
                /*	#swagger.parameters['email'] = {
                              in: 'query',
                              required:true,
                              description: 'Enter User Email!',                                      
        }*/
                /*	#swagger.parameters['verifyOtp'] = {
                              in: 'query',
                              required:true,
                              description: 'Enter the OTP!',                                      
        }*/        
        return await unauthorizedService.verifyOTP(req, res, next);
    }
    async generateToken(req, res, next) {
        /* #swagger.tags = ['Open-User']
                           #swagger.description = 'User email token generation API'  */
        /*	#swagger.parameters['data'] = {
                                  in: 'body',
                                  description: 'User Email ',
                                  required: true,
                                  schema: { $ref: "#/definitions/forgetPassword" }
          } */

        return await unauthorizedService.generateToken(req, res, next);
    }
    async updateProfile(req, res, next) {
        /* #swagger.tags = ['Open-User']
                           #swagger.description = 'This routes is used for update the Users' */
        /* #swagger.security = [{
                  "AccessToken": []
        }]*/
        /*  #swagger.parameters['data'] = {
                in: 'body',
                required: true,
                schema: { $ref: "#/definitions/updateProfile" }
        }*/
        return await unauthorizedService.updateProfile(req, res, next);
    }
    async attendance(req, res, next) {
        /* #swagger.tags = ['Open-User']
                           #swagger.description = 'This routes is used for checkIn & checkOut' */
        /* #swagger.security = [{
                  "AccessToken": []
        }]*/
        /*  #swagger.parameters['data'] = {
                in: 'body',
                required: true,
                schema: { $ref: "#/definitions/userAttendance" }
        }*/
        return await unauthorizedService.attendance(req, res, next);
    }
    async getAttendance(req, res, next) {
        /* #swagger.tags = ['Open-User']
                           #swagger.description = 'This routes is used for fetching attendance history' */
        /* #swagger.security = [{
                  "AccessToken": []
        }]*/
        /*	#swagger.parameters['startDate'] = {
                              in: 'query',
                              
                              description: 'Enter the start date',                                      
        }*/
        /*	#swagger.parameters['endDate'] = {
                              in: 'query',
                              description: 'Enter the end date',                     
        }*/
        /*	#swagger.parameters['userId'] = {
                              in: 'query',
                              description: 'Enter the user Id',
                              
        }*/
        return await unauthorizedService.getAttendance(req, res, next);
    }

    async importUsers(req, res, next){
         /* #swagger.tags = ['Open-User']
                           #swagger.description = 'API to import Users from EmpMonitor' */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Import Users',
                                required: true,
                                schema: { $ref: "#/definitions/importEmpUsers" }
        } */
        return await unauthorizedService.importUsers(req, res, next);
    }

    async adminImportUsers(req, res, next){
        /* #swagger.tags = ['Open-User']
                          #swagger.description = 'API to import Admin from EmpMonitor' */
       /*	#swagger.parameters['data'] = {
                               in: 'body',
                               description: 'Import Users',
                               required: true,
                               schema: { $ref: "#/definitions/adminImportUsers" }
       } */
       return await unauthorizedService.adminImportUsers(req, res, next);
    }

    async deleteUserPerm(req, res, next){
        /* #swagger.tags = ['Open-User']
                          #swagger.description = 'API to import Admin from EmpMonitor' */
       /*	#swagger.parameters['data'] = {
                               in: 'body',
                               description: 'Import Users',
                               required: true,
                               schema: { $ref: "#/definitions/deleteUserPerm" }
       } */
       return await unauthorizedService.deleteUserPerm(req, res, next);
    }

    async getLocationList(req, res, next){
        /* #swagger.tags = ['Open-User']
                          #swagger.description = 'API to get Organisation Location List' */
       /*	#swagger.parameters['data'] = {
                               in: 'body',
                               description: 'Import Users',
                               required: true,
                               schema: { $ref: "#/definitions/getLocationList" }
       } */
       return await unauthorizedService.getLocationList(req, res, next);
    }

    async getGeoLocationDetails(req, res, next){
        /* #swagger.tags = ['Open-User']
                          #swagger.description = 'API to get Organisation Location Details' */
       /*	#swagger.parameters['data'] = {
                               in: 'body',
                               description: 'Import Users',
                               required: true,
                               schema: { $ref: "#/definitions/getGeoLocationDetails" }
       } */
       return await unauthorizedService.getGeoLocationDetails(req, res, next);
    }

    async updateGeoLocationDetails(req, res, next){
        /* #swagger.tags = ['Open-User']
                          #swagger.description = 'API to update Organisation Location Details' */
       /*	#swagger.parameters['data'] = {
                               in: 'body',
                               description: 'Import Users',
                               required: true,
                               schema: { $ref: "#/definitions/updateGeoLocationDetails" }
       } */
       return await unauthorizedService.updateGeoLocationDetails(req, res, next);
    }

    async empEmployeeImport(req, res, next){
        /* #swagger.tags = ['Open-User']
                          #swagger.description = 'API to update Organisation Location Details' */
       /*	#swagger.parameters['data'] = {
                               in: 'body',
                               description: 'Import Users',
                               required: true,
                               schema: { $ref: "#/definitions/empEmployeeImport" }
       } */
       return await unauthorizedService.empEmployeeImport(req, res, next);
    }

}
export default new unauthorizedController();
