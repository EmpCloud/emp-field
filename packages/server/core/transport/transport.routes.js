import Router from 'express';
const router = Router();
import transportController from './transport.controller.js';
import verifyToken from '../../middleware/verifyToken.js';


router.post('/Update-Emp-mode-of-transport',verifyToken,transportController.empModeOfTransport)
router.post('/Update-Emp-Mot-Frequency-Radius',verifyToken,transportController.empUpdFreqRad)



export default router;
