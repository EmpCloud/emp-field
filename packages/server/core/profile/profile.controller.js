import ProfileService from './profile.service.js';

class ProfileController {

    async fetchProfile(req, res, next){
        /* #swagger.tags = ['Profile']
                            #swagger.description = 'API to get profile Details' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        return await ProfileService.fetchProfile(req, res, next);
    }

    async updateProfile(req, res, next){
        /* #swagger.tags = ['Profile']
                            #swagger.description = 'API to update the Profile Details' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Profile Details',
                                required: true,
                                schema: { $ref: "#/definitions/updateProfileDetail" }
        } */
        return await ProfileService.updateProfile(req, res, next);
    }

    async uploadProfileImage(req, res, next){
        /* #swagger.tags = ['Profile']
                            #swagger.description = 'API to upload Profile Image' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /*	#swagger.consumes = ['multipart/form-data'] 
                #swagger.parameters['files'] = {
                    in: 'formData',
                    type: 'array',
                    minItems: 1,
                    maxItems: 5,
                    required: true,
                    "collectionFormat": "multi",
                    description: 'The file to upload',
                    items: { type: 'file' }
                }*/
        return await ProfileService.uploadProfileImage(req, res, next);
    }

    async updateSnapDetails(req, res, next){
        /* #swagger.tags = ['Profile']
                            #swagger.description = 'API to user Snapped Details' */
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
                                description: 'Profile Details',
                                required: true,
                                schema: { $ref: "#/definitions/updateSnapDetails" }
        } */
        return await ProfileService.updateSnapDetails(req, res, next);
    }
    
}
export default new ProfileController();