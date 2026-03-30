import Router from 'express';
const router = Router();
import ProfileController  from './profile.controller.js';
import verifyToken from '../../middleware/verifyToken.js';
import  Multer from 'multer';
import profileController from './profile.controller.js';

let processFile = Multer({
    storage: Multer.memoryStorage(),
}).array('files');

router.get('/fetchProfile',verifyToken, ProfileController.fetchProfile);
router.post('/updateProfile',verifyToken, ProfileController.updateProfile);
router.post('/uploadProfileImage',processFile,verifyToken,ProfileController.uploadProfileImage);
router.post('/update-snap-details',verifyToken,ProfileController.updateSnapDetails);




export default router;