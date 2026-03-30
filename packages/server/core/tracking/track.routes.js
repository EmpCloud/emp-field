import TrackController from "./track.controller.js";
import Router from 'express';
const router = Router();
import verifyToken from '../../middleware/verifyToken.js';

router.post('/get-location',verifyToken,TrackController.trackUser);
export default router;