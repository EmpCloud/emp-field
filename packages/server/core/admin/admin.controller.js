import AdminService from './admin.service.js';

class AdminController {
  async addAdmin(req, res, next) {
    /* #swagger.tags = ['Admin']
                           #swagger.description = 'This routes is used for creating an organization with Admin Details'  */

    /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Admin  Details',
                                required: true,
                                schema: { $ref: "#/definitions/AdminDetails" }
        }*/

    return await AdminService.addAdmin(req, res, next);
  }

  async fetchAdmin(req, res, next) {
    /* #swagger.tags = ['Admin']
                           #swagger.description = 'This routes is used for fetching an admin details with access token'  */

    /*	#swagger.parameters['data'] = {
                                  in: 'body',
                                  description: 'Admin  Details',
                                  required: true,
                                  schema: { $ref: "#/definitions/AdminCreds" }
        }*/

    return await AdminService.fetchAdmin(req, res, next);
  }

  async verifyPhone(req, res, next) {
    /* #swagger.tags = ['Admin']
                           #swagger.description = 'This routes is used for sending verify mail to admin mail Id'  */
    /*	#swagger.parameters['data'] = {
                                 in: 'body',
                                 description: 'Admin Email Verification',
                                 required: true,
                                 schema: { $ref: "#/definitions/PhoneVerification" }
        }*/

    return await AdminService.verifyPhone(req, res, next);
  }
  async verifyEmail(req, res, next) {
    /* #swagger.tags = ['Admin']
                           #swagger.description = 'This routes is used for sending verify mail to admin mail Id'  */
    /*	#swagger.parameters['data'] = {
                                 in: 'body',
                                 description: 'Admin Email Verification',
                                 required: true,
                                 schema: { $ref: "#/definitions/MailVerification" }
        }*/

    return await AdminService.verifyEmail(req, res, next);
  }
  async updatePhone(req, res, next) {
    /* #swagger.tags = ['Admin']
                           #swagger.description = 'This routes is used to add/update phoneNumber'  */

    /*	#swagger.parameters['phone'] = {
                                in: 'body',
                                description: 'Admin phone Updation',
                                required: true,
                                schema: { $ref: "#/definitions/NumberUpdation" }
                                
        } */

    return await AdminService.updatePhone(req, res, next);
  }
  async updateAdmin(req, res, next) {
    /* #swagger.tags = ['Admin']
                           #swagger.description = 'This routes is used for update admin details'  */
    /* #swagger.security = [{
                  "AccessToken": []
        }] */

    /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Admin  Details',
                                required: true,
                                schema: { $ref: "#/definitions/UpdateAdmin" }
        } */

    return await AdminService.updateAdmin(req, res, next);
  }
  async updateCoordinatesStatus(req, res, next) {
    /* #swagger.tags = ['Admin']
                           #swagger.description = 'This routes is used for update Users GeoLogs Status'  */
    /* #swagger.security = [{
                  "AccessToken": []
        }] */

    /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Update User GeoLogs Status',
                                required: true,
                                schema: { $ref: "#/definitions/UpdateUserGeologsStatus" }
        } */

    return await AdminService.updateCoordinatesStatus(req, res, next);
  }  

  async allOrgEmployee(req, res, next) {
    /* #swagger.tags = ['Admin']
                           #swagger.description = 'Api to get all organisation Employee Data'  */
    /* #swagger.security = [{
                  "AccessToken": []
        }] */
    /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Api to get all organisation Employee Data',
                                required: true,
                                schema: { $ref: "#/definitions/allOrgEmployee" }
        } */

    return await AdminService.allOrgEmployee(req, res, next);
  }

  async forgotPassword(req, res, next) {
    /* #swagger.tags = ['Admin']
                           #swagger.description = 'This routes is used to send verification Mail and verificationToken for reset Password'  */

    /*	#swagger.parameters['email'] = {
                                in: 'query',
                                description: 'Admin email',
                                required: true,
                                
        } */

    return await AdminService.forgotPassword(req, res, next);
  }
  async resetPassword(req, res, next) {
    /* #swagger.tags = ['Admin']
                           #swagger.description = 'This routes is used for resetting Admin New password'  */

    /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Admin Reset Password Details',
                                required: true,
                                schema: { $ref: "#/definitions/AdminResetPassword" }
        } */

    return await AdminService.resetPassword(req, res, next);
  }

  async generateToken(req, res, next) {
    /* #swagger.tags = ['Admin']
                           #swagger.description = 'Admin email token generation API'  */
    /*	#swagger.parameters['data'] = {
                                  in: 'body',
                                  description: 'Admin Email ',
                                  required: true,
                                  schema: { $ref: "#/definitions/AdminEmail" }
          } */

    return await AdminService.generateToken(req, res, next);
  }
  async generateOtp(req, res, next) {
    /* #swagger.tags = ['Admin']
                           #swagger.description = 'Admin phone verification otp generation API'  */
    /*	#swagger.parameters['data'] = {
                                  in: 'body',
                                  description: 'Admin Number ',
                                  required: true,
                                  schema: { $ref: "#/definitions/AdminNumber" }
          } */

    return await AdminService.generateOtp(req, res, next);
  }
  async updatePassword(req, res, next) {
    /* #swagger.tags = ['Admin']
                           #swagger.description = 'Admin update password API'  */
    /* #swagger.security = [{
               "AccessToken": []
        }] */
    /*	#swagger.parameters['data'] = {
                                  in: 'body',
                                  description: 'Admin Password ',
                                  required: true,
                                  schema: { $ref: "#/definitions/AdminPassword" }
          } */

    return await AdminService.updatePassword(req, res, next);
  }
  async enableTwoFactor(req, res, next) {
    /* #swagger.tags = ['Admin']
                           #swagger.description = 'Admin 2FA enable/disable API'  */
    /* #swagger.security = [{
               "AccessToken": []
        }] */
    /*	#swagger.parameters['data'] = {
                                  in: 'body',
                                  description: 'Admin Password ',
                                  required: true,
                                  schema: { $ref: "#/definitions/update2FA" }
          } */

    return await AdminService.enableTwoFactor(req, res, next);
  }

  async getLanguages(req, res, next){
    /* #swagger.tags = ['Admin']
                       #swagger.description = 'Language List API'  */

    return await AdminService.getLanguages(req, res, next);
}



  async getAdminTask(req, res, next) {
    /* #swagger.tags = ['Admin']
                           #swagger.description = 'Admin 2FA enable/disable API'  */
    /* #swagger.security = [{
               "AccessToken": []
        }] */
    /*	#swagger.parameters['emp_id'] = {
                                in: 'query',
                                description: 'Enter  emp_id of Employee.',
                                required: false,
                                
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
        /*	#swagger.parameters['orderBy'] = {
                             in: 'query',
                             description: 'keyword to be ordered',
                             enum: ['taskApproveStatus'],
        } */        
        /*	#swagger.parameters['sort'] = {
                        in: 'query',
                        description: 'sorting parameters(asc or desc)',
                        enum: ['asc', 'desc'],
    }*/          
    /*	#swagger.parameters['data'] = {
                                  in: 'body',
                                  description: 'Provide Date  to filter DateWise task.',
                                  required: false,
                                  schema: { $ref: "#/definitions/date" }
          } */

    return await AdminService.getAdminTask(req, res, next);
  }

  async getUserCoordinates(req, res, next) {
    /* #swagger.tags = ['Admin']
                           #swagger.description = 'Admin 2FA enable/disable API'  */
    /* #swagger.security = [{
               "AccessToken": []
        }] */
    /*	#swagger.parameters['emp_id'] = {
                                in: 'query',
                                description: 'Enter  emp_id of Employee.',
                                required: false,
                                
        } */
        /*	#swagger.parameters['orderBy'] = {
                             in: 'query',
                             description: 'keyword to be ordered',
                             enum: ['taskApproveStatus'],
        } */        
        /*	#swagger.parameters['sort'] = {
                        in: 'query',
                        description: 'sorting parameters(asc or desc)',
                        enum: ['asc', 'desc'],
    }*/          
    /*	#swagger.parameters['data'] = {
                                  in: 'body',
                                  description: 'Provide Date  to filter DateWise task.',
                                  required: false,
                                  schema: { $ref: "#/definitions/date" }
          } */

    return await AdminService.getUserCoordinates(req, res, next);
  }
  async adminDataFetch(req, res, next) {
    /* #swagger.tags = ['Admin']
                           #swagger.description = 'This routes is used for fetching Admin details Only'  */

    /* #swagger.security = [{
               "AccessToken": []
        }] */

    return await AdminService.adminDataFetch(req, res, next);
  }
  async uploadProfileImage(req, res, next){
    /* #swagger.tags = ['Admin']
                        #swagger.description = 'API to upload Admin Profile Image' */
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
    return await AdminService.uploadProfileImage(req, res, next);
}

  async getAllAdminFieldTrackingUsers(req, res, next){
    /* #swagger.tags = ['Admin']
                        #swagger.description = 'API to Fetch all Existing Users of Admin' */   
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
                             enum: ['createdAt'],
        } */        
        /*	#swagger.parameters['sort'] = {
                        in: 'query',
                        description: 'sorting parameters(asc or desc)',
                        enum: ['asc', 'desc'],
    }*/        
    /*	#swagger.parameters['searchQuery'] = {
                            in: 'query',
                            description: 'Provide SearchQuery',
        } */        
    /*	#swagger.parameters['data'] = {
                                  in: 'body',
                                  description: 'Provide ',
                                  required: false,
                                  schema: { $ref: "#/definitions/filterData" }
          } */         
        return await AdminService.getAllAdminFieldTrackingUsers(req, res, next);                     
  }

  async deleteUser(req, res, next) {
    /* #swagger.tags = ['Admin']
                       #swagger.description = 'This routes is used for delete the User'  */
    /* #swagger.security = [{
           "AccessToken": []
    }] */
    /* #swagger.parameters['userId'] = {
            in: 'query',
            required: true
    }*/

    return await AdminService.deleteUser(req, res, next);
}
async deleteAdminUsers(req,res,next){

  /* #swagger.tags = ['Admin']
                #swagger.description = 'API to Permanently delete User from FieldTracking' */
                  /* #swagger.security = [{
   "AccessToken": []
}] */ 
/*	#swagger.parameters['data'] = {
                     in: 'body',
                     description: 'Import Users',
                     required: true,
                     schema: { $ref: "#/definitions/deleteUserPerm" }
} */
return await AdminService.deleteAdminUsers(req,res,next);
}

async softDeleteAdminUsers(req,res,next){

  /* #swagger.tags = ['Admin']
                #swagger.description = 'API to delete User from FieldTracking' */
                  /* #swagger.security = [{
   "AccessToken": []
}] */ 
/*	#swagger.parameters['data'] = {
                     in: 'body',
                     description: 'Import Users',
                     required: true,
                     schema: { $ref: "#/definitions/deleteUserPerm" }
} */
return await AdminService.softDeleteAdminUsers(req,res,next);
}
async restoreDeletedUsers(req,res,next){
     /* #swagger.tags = ['Admin']
                #swagger.description = 'API to Restore Soft-delete User from FieldTracking' */
                  /* #swagger.security = [{
   "AccessToken": []
}] */ 
/*	#swagger.parameters['data'] = {
                     in: 'body',
                     description: 'Import Users',
                     required: true,
                     schema: { $ref: "#/definitions/deleteUserPerm" }
} */

  return await AdminService.restoreDeletedUsers(req,res,next);
}

async getLocation(req,res,next){
    /* #swagger.tags = ['Admin']
                #swagger.description = 'API to get Location List' */
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
                            type:'string',
                            description:'Search Location',
    } */        
 return await AdminService.getLocation(req,res,next);
}

async getDepartment(req,res,next){
  /* #swagger.tags = ['Admin']
              #swagger.description = 'API to get Department List' */
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
                            type:'string',
                            description:'search department',
  } */   
return await AdminService.getDepartment(req,res,next);
}

async getUserTimeLine(req,res,next){
  /* #swagger.tags = ['Admin']
              #swagger.description = 'API to get Department List' */
                /* #swagger.security = [{
 "AccessToken": []
}] */ 
    /*	#swagger.parameters['emp_id'] = {
                                in: 'query',
                                description: 'Enter  emp_id of Employee.',
                                required: false,
                                
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
    /*	#swagger.parameters['data'] = {
                                  in: 'body',
                                  description: 'Provide Date  to filter DateWise task.',
                                  required: false,
                                  schema: { $ref: "#/definitions/date" }
          } */
  return await AdminService.getUserTimeLine(req,res,next);
}
async allTaskStats(req, res, next){
  /* #swagger.tags = ['Admin_Dashboard']
                      #swagger.description = 'API to upload Admin Profile Image' */   
  /* #swagger.security = [{
             "AccessToken": []
      }] */    
      return await AdminService.allTaskStats(req, res, next);                     
}
async dashboardStats(req, res, next) {
  /* #swagger.tags = ['Admin_Dashboard']
                         #swagger.description = 'Api to get dashboard Header Stats'  */
  /* #swagger.security = [{
                "AccessToken": []
      }] */

  return await AdminService.dashboardStats(req, res, next);
}
async getEmployeeDetails(req,res,next){
  /* #swagger.tags = ['Admin_Dashboard']
                         #swagger.description = 'Api to get Dashboard Stats Details'  */
  /* #swagger.security = [{
                "AccessToken": []
      }] */ 
  /*	#swagger.parameters['employeeStatus'] = {
                          in: 'query',
                          type:'string',
                          description: 'Fetch Employees based on status',
                          enum: ['AllEmployees', 'presentToday','onDuty','absent','suspended',]
      } */ 
    /*	#swagger.parameters['search'] = {
                            in: 'query',
                            description: 'Provide SearchQuery',
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
        /*	#swagger.parameters['orderBy'] = {
                             in: 'query',
                             description: 'keyword to be ordered',
                             enum: ['createdAt'],
        } */ 
        /*	#swagger.parameters['sort'] = {
                        in: 'query',
                        description: 'sorting parameters(asc or desc)',
                        enum: ['asc', 'desc'],
    }*/      

  return await AdminService.getEmployeeDetails(req,res,next);       
}
async adminUsersTrackingData(req, res, next){
  /* #swagger.tags = ['Admin_Dashboard']
                      #swagger.description = 'API to upload Admin Profile Image' */   
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
  /*	#swagger.parameters['employee_id'] = {
                          in: 'query',
                          description: 'Provide emp_id',
      } */         
  /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Provide ',
                                required: false,
                                schema: { $ref: "#/definitions/userFilter" }
        } */           
      return await AdminService.adminUsersTrackingData(req, res, next);                     
}
async adminUsersLocationDetails(req, res, next){
  /* #swagger.tags = ['Admin_Dashboard']
                      #swagger.description = 'API to upload Admin Profile Image' */   
  /* #swagger.security = [{
             "AccessToken": []
      }] */    
  /*	#swagger.parameters['employee_id'] = {
                          in: 'query',
                          description: 'Provide emp_id',
      } */         
    /*	#swagger.parameters['data'] = {
                                  in: 'body',
                                  description: 'Provide date',
                                  required: false,
                                  schema: { $ref: "#/definitions/userLocationFilter" }
    }*/         
    return await AdminService.adminUsersLocationDetails(req, res, next);                     
}
async averageWorkingHours(req, res, next) {
  /* #swagger.tags = ['Admin_Dashboard']
                     #swagger.description = 'This routes is used for getting'  */
  /* #swagger.security = [{
         "AccessToken": []
  }] */
    /* #swagger.parameters['startDate'] = {
            in: 'query',
            required: true
    }*/  
    /* #swagger.parameters['endDate'] = {
            in: 'query',
            required: true
    }*/      
  return await AdminService.averageWorkingHours(req, res, next);
}




///Admin Task Controller's
async createTask(req, res, next) {
  /* #swagger.tags = ['AdminTask']
                     #swagger.description = 'This routes is used for creating Task Details'  */
  /* #swagger.security = [{
         "AccessToken": []
  }] */

  /*	#swagger.parameters['employeeId'] = {
                          in: 'query',
                          required: true,
                          description: 'Provide emp_id',
      } */   

  /*	#swagger.parameters['data'] = {
                          in: 'body',
                          description: 'task Details',
                          required: true,
                          schema: { $ref: "#/definitions/CreateTask" }
  }*/

  return await AdminService.createTask(req, res, next);
}
async fetchTask(req, res, next) {
  /* #swagger.tags = ['AdminTask']
                     #swagger.description = 'This routes is used to fetch Task details'  */
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
    /*	#swagger.parameters['generalSearch'] = {
                            in: 'query',
                            description: 'Provide SearchQuery',
        } */       
    /*	#swagger.parameters['searchEmployee'] = {
                            in: 'query',
                            description: 'Provide SearchQuery',
        } */  
    /*	#swagger.parameters['searchClients'] = {
                            in: 'query',
                            description: 'Provide SearchQuery',
        } */   
    /*	#swagger.parameters['date'] = {
                            in: 'query',
                            description: 'Provide date to filter',
        } */  
        /*	#swagger.parameters['orderBy'] = {
                             in: 'query',
                             description: 'By default sorting is based on updatedAt',
                             enum: ['taskApproveStatus','updatedAt','createdAt'],
        } */           
        /*	#swagger.parameters['sort'] = {
                        in: 'query',
                        description: 'sorting parameters(asc or desc)',
                        enum: ['asc', 'desc'],
    }*/                        
  return await AdminService.fetchTask(req, res, next);
}
async updateTask(req, res, next) {
  /* #swagger.tags = ['AdminTask']
                     #swagger.description = 'This routes is used for update the Task detailes' */
  /* #swagger.security = [{
            "AccessToken": []
  }]*/
  /*  #swagger.parameters['data'] = {
          in: 'body',
          required: true,
          schema: { $ref: "#/definitions/UpdateTask" }
  }*/
  return await AdminService.updateTask(req, res, next);
}
async  deleteTask(req, res, next) {
  /* #swagger.tags = ['AdminTask']
                     #swagger.description = 'This routes is used for delete the Task'  */
  /* #swagger.security = [{
         "AccessToken": []
  }] */
  /*  #swagger.parameters['data'] = {
          in: 'body',
          required: true,
          schema: { $ref: "#/definitions/DeleteTask" }
  }*/

  return await AdminService.deleteTask(req, res, next);
}


//Admin Client Controller's
async createClient(req, res, next) {
  /* #swagger.tags = ['AdminClient']
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

  return await AdminService.createClient(req, res, next);
}
async fetchClientInfo(req, res, next) {
  /* #swagger.tags = ['AdminClient']
                     #swagger.description = 'API to fetch all Client Lists'  */
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
    /*	#swagger.parameters['searchEmployee'] = {
                            in: 'query',
                            description: 'Provide SearchQuery',
        } */  
    /*	#swagger.parameters['searchClients'] = {
                            in: 'query',
                            description: 'Provide SearchQuery',
        } */                  
  return await AdminService.fetchClientInfo(req, res, next);
}

async updateClient(req, res, next) {
  /* #swagger.tags = ['AdminClient']
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
  return await AdminService.updateClient(req, res, next);
}
async deleteClient(req, res, next) {
  /* #swagger.tags = ['AdminClient']
                     #swagger.description = 'This routes is used for delete the Client'  */
  /* #swagger.security = [{
         "AccessToken": []
  }] */
/*	#swagger.parameters['data'] = {
                     in: 'body',
                     description: 'Provide Client _id',
                     required: true,
                     schema: { $ref: "#/definitions/deleteCLientIds" }
} */

  return await AdminService.deleteClient(req, res, next);
}


async allEmployeesAttendance(req,res,next){
    /* #swagger.tags = ['AllEmployeesAttendance']
                     #swagger.description = 'This routes is used for fetching Attendance of all the Employees'  */
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

/*	#swagger.parameters['data'] = {
                     in: 'body',
                     description: 'Provide Admin required details',
                     required: true,
                     schema: { $ref: "#/definitions/allEmployeesAttendance" }
} */
  return await AdminService.allEmployeesAttendance(req,res,next);
}

}
export default new AdminController();
