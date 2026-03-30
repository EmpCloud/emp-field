import tagsServices from "./tag.service.js";
class TagsController {
    async createTags(req, res, next) {
        /* #swagger.tags = ['Tags']
                           #swagger.description = 'This routes is used for creating Tags'  */
        /* #swagger.security = [{
               "AccessToken": []
        }] */

        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'task Details',
                                required: true,
                                schema: { $ref: "#/definitions/CreateTags" }
        }*/

        return await tagsServices.createTags(req, res, next);
    }    
    async updateTags(req, res, next) {
        /* #swagger.tags = ['Tags']
                           #swagger.description = 'This routes is used for updating Tags'  */
        /* #swagger.security = [{
               "AccessToken": []
        }] */

        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'task Details',
                                required: true,
                                schema: { $ref: "#/definitions/updateTags" }
        }*/

        return await tagsServices.updateTags(req, res, next);
    }
    async updateTagsOrder(req, res, next) {
        /* #swagger.tags = ['Tags']
                           #swagger.description = 'This routes is used for updating Tags'  */
        /* #swagger.security = [{
               "AccessToken": []
        }] */

        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'task Details',
                                required: true,
                                schema: { $ref: "#/definitions/updatedStagesOrder" }
        }*/

        return await tagsServices.updateTagsOrder(req, res, next);
    }      
    async getTags(req,res,next) {
                /* #swagger.tags = ['Tags']
                           #swagger.description = 'This routes is used for Getting Tags'  */
        /* #swagger.security = [{
               "AccessToken": []
        }] */
        /*	#swagger.parameters['orderBy'] = {
                             in: 'query',
                             description: 'keyword to be ordered',
                             enum: ['order'],
        } */   
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
        /*	#swagger.parameters['sort'] = {
                        in: 'query',
                        description: 'sorting parameters(asc or desc)',
                        enum: ['asc', 'desc'],
    }*/         

        return await tagsServices.getTags(req, res, next);

    } 
    async deleteTags(req,res,next) {
        /* #swagger.tags = ['Tags']
                   #swagger.description = 'This routes is used for updating Tags'  */
        /* #swagger.security = [{
       "AccessToken": []
        }] */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'task Details',
                                required: true,
                                schema: { $ref: "#/definitions/deleteTags" }
        }*/
        return await tagsServices.deleteTags(req, res, next);

    } 
    async getAdminTags(req,res,next) {
        /* #swagger.tags = ['Tags']
                   #swagger.description = 'This routes is used for Getting Tags'  */
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
                             enum: ['tagName','tagDescription','createdAt','color'],
        } */                      
        /*	#swagger.parameters['sort'] = {
                        in: 'query',
                        description: 'sorting parameters(asc or desc)',
                        enum: ['asc', 'desc'],
    }*/  
    /*	#swagger.parameters['search'] = {
                            in: 'query',
                            description: 'Provide SearchQuery',
        } */     

    return await tagsServices.getAdminTags(req, res, next);

    } 
   
}
export default new TagsController();