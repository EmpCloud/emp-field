import Router from 'express';
const router = Router()
import holidayController from './holiday.controller.js'
import verifyToken from '../../middleware/verifyToken.js';

router.post('/create-holiday',verifyToken,holidayController.createHoliday)
router.get('/get-holiday',verifyToken,holidayController.getHoliday);
router.put('/update-holiday',verifyToken,holidayController.updateHoliday)
router.delete('/delete-holiday',verifyToken,holidayController.deleteHoliday)



export default router;