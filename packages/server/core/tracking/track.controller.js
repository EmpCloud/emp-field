import trackService from "./track.service.js";
class trackControllerService {
    async trackUser(req, res, next) {
        /* #swagger.tags = ['Track']
                           #swagger.description = 'API to track the location'  */
        /* #swagger.security = [{
               "AccessToken": []
        }] */

        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'task Details',
                                required: true,
                                schema: { $ref: "#/definitions/trackUser" }
        }*/

        return await trackService.trackUser(req, res, next);
    }
}
export default new trackControllerService();