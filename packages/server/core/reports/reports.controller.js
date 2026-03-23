import reportsService from "./reports.service.js";
class ReportsController {
    async fetchConsolidatedReport(req, res, next){
        /* #swagger.tags = ['Reports']
                            #swagger.description = 'API to get Consolidated Report Details' */
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
    /*	#swagger.parameters['searchQuery'] = {
                            in: 'query',
                            description: 'Provide SearchQuery',
        } */ 
        /*	#swagger.parameters['orderBy'] = {
                             in: 'query',
                             description: 'keyword to be ordered',
                             enum: ['fullName','email','createdAt','department','location','role','pendingTaskCount','startTaskCount','pauseTaskCount','resumeTaskCount','finishTaskCount','deleteTaskCount'],
        } */        
        /*	#swagger.parameters['sort'] = {
                        in: 'query',
                        description: 'sorting parameters(asc or desc)',
                        enum: ['asc', 'desc'],
    }*/                     
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Filter Details',
                                required: true,
                                schema: { $ref: "#/definitions/filterReport" }
        } */        
        return await reportsService.fetchConsolidatedReport(req, res, next);
    }    
   
    async getUserDetails(req,res,next){
        /* #swagger.tags = ['Reports']
                            #swagger.description = 'API to get user Report Details' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */ 
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Filter Details',
                                required: true,
                                schema: { $ref: "#/definitions/filterUserReport" }
        } */  
    return await reportsService.getUserDetails(req,res,next);                             
    }

    async taskListDetails(req,res,next){
        /* #swagger.tags = ['Reports']
                            #swagger.description = 'API to get taskListDetails Report Details' */
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
    /*	#swagger.parameters['exportTaskDetails'] = {
                            in: 'query',
                            type:'boolean',
                            required:true,
                            description: 'true for exportTaskDetails and vice versa!',
        } */                 
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Filter Details',
                                required: true,
                                schema: { $ref: "#/definitions/filterUserReport" }
        } */          

        return await reportsService.taskListDetails(req,res,next);         
    }

    async userStats(req,res,next){
        /* #swagger.tags = ['Reports']
                            #swagger.description = 'API to get userStats Report Details' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */  
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Filter Details',
                                required: true,
                                schema: { $ref: "#/definitions/statsClientFilter" }
        } */                            
    return await reportsService.userStats(req,res,next);         

    }

    async updateGeoFencing(req,res,next){
        /* #swagger.tags = ['Reports']
                            #swagger.description = 'API to Update User Details' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
    /*	#swagger.parameters['emp_id'] = {
                        in: 'query',
                        type:'string',
                        description: 'Provide emp_id of employee!.',
    } */               
    /*	#swagger.parameters['geoFencing'] = {
                        in: 'query',
                        type:'boolean',
                        description: 'Provide boolean value for geoFencing.',
    } */   
    return await reportsService.updateGeoFencing(req,res,next);          
    }

    async taskStatus(req,res,next){
        /* #swagger.tags = ['Reports']
                            #swagger.description = 'API to Update User Details' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
    /*	#swagger.parameters['employee_Id'] = {
                        in: 'query',
                        type:'string',
                        required:true,
                        description: 'Provide emp_id=29372 of an employee!.',
    } */              
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Filter Details',
                                required: true,
                                schema: { $ref: "#/definitions/reportTaskStatus" }
        } */                    
     return await reportsService.taskStatus(req,res,next);               
    }

    async taskStages(req,res,next){
        /* #swagger.tags = ['Reports']
                            #swagger.description = 'API to Update User Details' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
    /*	#swagger.parameters['employee_Id'] = {
                        in: 'query',
                        type:'string',
                        required:true,
                        description: 'Provide emp_id=29372 of an employee!.',
    } */             
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Filter Details',
                                required: true,
                                schema: { $ref: "#/definitions/reportTaskStages" }
        } */ 
    return await reportsService.taskStages(req,res,next);
    }

    async clientDetails(req,res,next){
                /* #swagger.tags = ['Reports']
                            #swagger.description = 'API to Get Client Details' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
    /*	#swagger.parameters['employee_Id'] = {
                        in: 'query',
                        type:'string',
                        required:true,
                        description: 'Provide emp_id=29372 of an employee!.',
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
    /*	#swagger.parameters['exportClient'] = {
                            in: 'query',
                            type:'boolean',
                            required:true,
                            description: 'true for export and vice versa!',
        } */  
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Filter Details',
                                required: true,
                                schema: { $ref: "#/definitions/reportClientFilter" }
        } */                
    return await reportsService.clientDetails(req,res,next);
    }

    async distanceTraveled(req,res,next){
        /* #swagger.tags = ['Reports']
                            #swagger.description = 'API to Update User Details' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
    /*	#swagger.parameters['employee_Id'] = {
                        in: 'query',
                        type:'string',
                        required:true,
                        description: 'Provide emp_id=29372 of an employee!.',
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
    /*	#swagger.parameters['exportDistanceTraveled'] = {
                            in: 'query',
                            type:'boolean',
                            required:true,
                            description: 'true for exportDistanceTraveled and vice versa!',
        } */  
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Filter Details',
                                required: true,
                                schema: { $ref: "#/definitions/reportDistanceTraveled" }
        } */               
    return await reportsService.distanceTraveled(req,res,next);    
    }


    async getIndividualAttendanceData(req, res, next){
         /* #swagger.tags = ['Reports']
                            #swagger.description = 'API to get user Report Details' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */ 
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Filter Details',
                                required: true,
                                schema: { $ref: "#/definitions/getIndividualAttendanceData" }
        } */
        return await reportsService.getIndividualAttendanceData(req,res,next);
    }
}
export default new ReportsController();