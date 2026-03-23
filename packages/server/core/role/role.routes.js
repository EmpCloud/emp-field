import Router from 'express';
const router = Router()
import roleController from './role.controller.js'
import verifyToken from '../../middleware/verifyToken.js';

router.post('/create',verifyToken,roleController.create)
router.get('/get',verifyToken,roleController.get);
router.put('/update',verifyToken,roleController.update)
router.delete('/delete',verifyToken,roleController.delete)



export default router;