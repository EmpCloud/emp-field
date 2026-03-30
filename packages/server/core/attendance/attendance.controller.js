import AttendanceService from './attendance.service.js';

class AttendanceController {


    async fetchAttendance(req, res, next){
        /* #swagger.tags = ['Attendance']
                            #swagger.description = 'API to fetch the attendance Details' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Attendance Details',
                                required: true,
                                schema: { $ref: "#/definitions/attendanceRange" }
        } */
        return await AttendanceService.fetchAttendance(req, res, next);
    }

    async attendanceRequest(req, res, next){
        /* #swagger.tags = ['Attendance']
                            #swagger.description = 'API to fetch the attendance Details' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Attendance Details',
                                required: true,
                                schema: { $ref: "#/definitions/attendanceRequest" }
        } */
        return await AttendanceService.attendanceRequest(req, res, next);
    }

    async markAttendance(req, res, next){
        /* #swagger.tags = ['Attendance']
                            #swagger.description = 'API to mark attendance' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        /*	#swagger.parameters['data'] = {
                                in: 'body',
                                description: 'Attendance Details',
                                required: true,
                                schema: { $ref: "#/definitions/markAttendance" }
        } */
        return await AttendanceService.markAttendance(req, res, next);
    }

    async getAttendance(req, res, next){
        /* #swagger.tags = ['Attendance']
                            #swagger.description = 'API to fetch the mark attendance' */
        /* #swagger.security = [{
                   "AccessToken": []
            }] */
        return await AttendanceService.getAttendance(req, res, next);
    }

  
}
export default new AttendanceController();