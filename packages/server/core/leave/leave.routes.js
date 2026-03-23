import Router from 'express';
const router = Router()
import leaveController from './leave.controller.js'
import verifyToken from '../../middleware/verifyToken.js';

router.post('/create-leave-type',verifyToken,leaveController.createLeaveType)
router.get('/get-leave-type',verifyToken,leaveController.getLeaveType);
router.put('/update-leave-type',verifyToken,leaveController.updateLeaveType)
router.delete('/delete-leave-type',verifyToken,leaveController.deleteLeaveType)
router.post('/get-leaves',verifyToken,leaveController.getLeaves);
router.post('/create-leave',verifyToken,leaveController.createLeave);
router.post('/fetch-leave-type',verifyToken,leaveController.leaveTypeOption);
router.post('/update-leaves',verifyToken,leaveController.updateLeaves);
router.post('/delete-leaves',verifyToken,leaveController.deleteLeaves);


export default router;