import categoryService from './category.service.js'
class categoryControllerService {
    async addCategory(req, res, next) {
        /* #swagger.tags = ['Category']
                           #swagger.description = 'This routes is used for creating Category Details'  */
        /* #swagger.security = [{
               "AccessToken": []
        }] */

        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'category Details',
                                required: true,
                                schema: { $ref: "#/definitions/CreateCategory" }
        }*/

        return await categoryService.addCategory(req, res, next);
    }
    async fetchCategory(req, res, next) {
        /* #swagger.tags = ['Category']
                           #swagger.description = 'This routes is used to fetch Category details'  */
        /* #swagger.security = [{
                  "AccessToken": []
        }] */
        /*
        #swagger.parameters['categoryId'] = {
                           in: 'query',
                           description: 'category Id',
       } 
        */
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
        return await categoryService.fetchCategory(req, res, next);
    }
    async updateCategory(req, res, next) {
        /* #swagger.tags = ['Category']
                           #swagger.description = 'This routes is used for update the Category detailes' */
        /* #swagger.security = [{
                  "AccessToken": []
        }]*/
        /* #swagger.parameters['categoryId'] = {
                in: 'query',
                required: true
        }*/
        /*  #swagger.parameters['data'] = {
                in: 'body',
                required: true,
                schema: { $ref: "#/definitions/UpdateCategory" }
        }*/
        return await categoryService.updateCategory(req, res, next);
    }
    async deleteCategory(req, res, next) {
        /* #swagger.tags = ['Category']
                           #swagger.description = 'This routes is used for delete the Category'  */
        /* #swagger.security = [{
               "AccessToken": []
        }] */
        /* #swagger.parameters['categoryId'] = {
                in: 'query',
                required: false,
                description: 'category Id',
        }*/

        return await categoryService.deleteCategory(req, res, next);
    }

}
export default new categoryControllerService();