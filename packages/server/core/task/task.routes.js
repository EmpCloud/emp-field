import TaskController from "./task.controller.js";
import Router from 'express';
const router = Router();
import verifyToken from '../../middleware/verifyToken.js';


import Multer from 'multer';

let processFile = Multer({
    storage: Multer.memoryStorage(),
}).array('files');

router.post('/create', verifyToken, TaskController.createTask);
router.get('/fetch',verifyToken,TaskController.fetchTask);
router.put('/update', verifyToken,TaskController.updateTask);
router.delete('/delete', verifyToken,TaskController.deleteTask);
router.put('/update-approve',verifyToken,TaskController.approveTask);
router.post('/update-taskStatus',verifyToken,TaskController.updateStatus);
router.post('/uploadTask-files',processFile,verifyToken,TaskController.uploadTaskData);
router.delete('/deleteDocument',verifyToken,TaskController.deleteDocs)
router.post('/filterTask',verifyToken,TaskController.filterTask);


// Notification's API
router.get('/getNotification', verifyToken,TaskController.getNotification);


export default router;