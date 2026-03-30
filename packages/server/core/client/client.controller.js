import clientService from "./client.service.js";
class clientControllerSeivice {
    async createClient(req, res, next) {
        /* #swagger.tags = ['Client']
                           #swagger.description = 'This routes is used for creating Client Details'  */
        /* #swagger.security = [{
               "AccessToken": []
        }] */

        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'client  Details',
                                required: true,
                                schema: { $ref: "#/definitions/clientDetails" }
        }*/

        return await clientService.createClient(req, res, next);
    }
    async fetchClientInfo(req, res, next) {
        /* #swagger.tags = ['Client']
                           #swagger.description = 'API to fetch all Client Lists'  */
        /* #swagger.security = [{
                  "AccessToken": []
        }] */
        return await clientService.fetchClientInfo(req, res, next);
    }
    
    async updateClient(req, res, next) {
        /* #swagger.tags = ['Client']
                           #swagger.description = 'This routes is used for update the Client detailes' */
        /* #swagger.security = [{
                  "AccessToken": []
        }]*/
        /* #swagger.parameters['clientId'] = {
                in: 'query',
                required: true
        }*/
        /*  #swagger.parameters['data'] = {
                in: 'body',
                required: true,
                schema: { $ref: "#/definitions/clientDetails" }
        }*/
        return await clientService.updateClient(req, res, next);
    }
    async deleteClient(req, res, next) {
        /* #swagger.tags = ['Client']
                           #swagger.description = 'This routes is used for delete the Client'  */
        /* #swagger.security = [{
               "AccessToken": []
        }] */
        /* #swagger.parameters['clientId'] = {
                in: 'query',
                required: false,
                description: 'client Id',
        }*/

        return await clientService.deleteClient(req, res, next);
    }

    async uploadProfileImage(req, res, next){
        /* #swagger.tags = ['Client']
                            #swagger.description = 'API to upload Client Profile Image' */
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
        return await clientService.uploadProfileImage(req, res, next);
    }

}
export default new clientControllerSeivice();