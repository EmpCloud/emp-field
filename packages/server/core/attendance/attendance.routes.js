import Router from 'express';
const router = Router();
import AttendanceController  from './attendance.controller.js';
import verifyToken from '../../middleware/verifyToken.js';



router.post('/fetch-attendance',verifyToken, AttendanceController.fetchAttendance);
router.post('/mark-attendance',verifyToken, AttendanceController.markAttendance);
router.get('/attendance',verifyToken, AttendanceController.getAttendance);
router.post('/attendance-request',verifyToken,AttendanceController.attendanceRequest);



export default router;