import taskService from "./task.service.js";
class taskControllerSeivice {
    async createTask(req, res, next) {
        /* #swagger.tags = ['Task']
                           #swagger.description = 'This routes is used for creating Task Details'  */
        /* #swagger.security = [{
               "AccessToken": []
        }] */

        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'task Details',
                                required: true,
                                schema: { $ref: "#/definitions/CreateTask" }
        }*/

        return await taskService.createTask(req, res, next);
    }
    async fetchTask(req, res, next) {
        /* #swagger.tags = ['Task']
                           #swagger.description = 'This routes is used to fetch Task details'  */
        /* #swagger.security = [{
                  "AccessToken": []
        }] */
        return await taskService.fetchTask(req, res, next);
    }
    async updateTask(req, res, next) {
        /* #swagger.tags = ['Task']
                           #swagger.description = 'This routes is used for update the Task detailes' */
        /* #swagger.security = [{
                  "AccessToken": []
        }]*/
        /*  #swagger.parameters['data'] = {
                in: 'body',
                required: true,
                schema: { $ref: "#/definitions/UpdateTask" }
        }*/
        return await taskService.updateTask(req, res, next);
    }
    async  deleteTask(req, res, next) {
        /* #swagger.tags = ['Task']
                           #swagger.description = 'This routes is used for delete the Task'  */
        /* #swagger.security = [{
               "AccessToken": []
        }] */
        /*  #swagger.parameters['data'] = {
                in: 'body',
                required: true,
                schema: { $ref: "#/definitions/DeleteTask" }
        }*/

        return await taskService.deleteTask(req, res, next);
    }

    async updateStatus(req, res, next){
        /* #swagger.tags = ['Task']
                           #swagger.description = 'API to update the Task Status' */
        /* #swagger.security = [{
                  "AccessToken": []
        }]*/
        /*  #swagger.parameters['data'] = {
                in: 'body',
                required: true,
                schema: { $ref: "#/definitions/updateTaskStatus" }
        }*/
        return await taskService.updateStatus(req, res, next);
    }
    async approveTask(req, res, next) {
        /* #swagger.tags = ['Task']
                           #swagger.description = 'This routes is used for update the Task approvel' */
        /* #swagger.security = [{
                  "AccessToken": []
        }]*/
        /* #swagger.parameters['taskApproveStatus'] = {
                in: 'query',
                enum:["1"]
        }*/
        /*  #swagger.parameters['data'] = {
                in: 'body',
                required: true,
                schema: { $ref: "#/definitions/taskIds" }
        }*/
        return await taskService.approveTask(req, res, next);
    }

    async uploadTaskData(req, res, next) {
        /* 	#swagger.tags = ['Task']
                        #swagger.description = 'Which Uplaod multimedia file' */
        /* #swagger.security = [{
               "AccessToken": []
        }] */
        /*
      #swagger.consumes = ['multipart/form-data'] 
      #swagger.parameters['files'] = {
                in: 'formData',
                type: 'array',
                minItems: 1,
                maxItems: 10,
                required: true,
                "collectionFormat": "multi",
                description: 'The file to upload',
                items: { type: 'file' }
                }*/


        return await taskService.uploadTaskData(req, res, next);
    }

    async deleteDocs(req,res,next) {
        /* 	#swagger.tags = ['Task']
                        #swagger.description = 'Provide doc ImageId's or TagId's to delete document' */
        /* #swagger.security = [{
               "AccessToken": []
        }] */ 
        /*  #swagger.parameters['data'] = {
                in: 'body',
                required: true,
                schema: { $ref: "#/definitions/deleteDocuments" }
        }*/  
        return await taskService.deleteDocs(req, res, next);             
    }

    async filterTask(req, res, next){
        /* 	#swagger.tags = ['Task']
                        #swagger.description = 'Which Uplaod multimedia file' */
        /* #swagger.security = [{
               "AccessToken": []
        }] */
        /*  #swagger.parameters['data'] = {
                in: 'body',
                required: true,
                schema: { $ref: "#/definitions/filterTask" }
        }*/
        return await taskService.filterTask(req, res, next);
    }

//Notification Controller's
    async getNotification(req, res, next){
        /* 	#swagger.tags = ['AndroidNotification']
                        #swagger.description = 'Which Uplaod multimedia file' */
        /* #swagger.security = [{
               "AccessToken": []
        }] */
        return await taskService.getNotification(req, res, next);
    }

}
export default new taskControllerSeivice();