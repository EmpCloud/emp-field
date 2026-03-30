import Router from 'express';
const router = Router();
import verifyToken from '../../middleware/verifyToken.js';

import reportsController from './reports.controller.js';

router.post('/get-Consolidated-Reports',verifyToken, reportsController.fetchConsolidatedReport);
router.post('/updateGeoFencing',verifyToken, reportsController.updateGeoFencing);
router.post('/getUserDetails',verifyToken,reportsController.getUserDetails);
router.post('/taskListDetails',verifyToken,reportsController.taskListDetails);
router.post('/userStats',verifyToken,reportsController.userStats);
router.post('/taskStatus',verifyToken,reportsController.taskStatus);
router.post('/taskStages',verifyToken,reportsController.taskStages);
router.post('/clientDetails',verifyToken,reportsController.clientDetails);
router.post('/distanceTraveled',verifyToken,reportsController.distanceTraveled);
router.post('/getIndividualAttendanceData',verifyToken,reportsController.getIndividualAttendanceData);
export default router;
