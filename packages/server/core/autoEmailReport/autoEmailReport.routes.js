import Router from 'express';
const router = Router();
import autoReportEmailController from './autoEmailReport.controller.js';
import verifyToken from '../../middleware/verifyToken.js';

router.post('/createAutoEmailReport',verifyToken,autoReportEmailController.createAutoEmailReport);
router.post('/get',verifyToken, autoReportEmailController.fetchReportDetails);
router.post('/Update', verifyToken,autoReportEmailController.updateReport);
router.post('/delete',verifyToken,autoReportEmailController.deleteReport);


export default router;