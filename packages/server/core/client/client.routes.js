import ClientController from "./client.controller.js";
import Router from 'express';
const router = Router();
import verifyToken from '../../middleware/verifyToken.js';
import  Multer from 'multer';


let processFile = Multer({
    storage: Multer.memoryStorage(),
}).array('files');


router.post('/create', verifyToken, ClientController.createClient);
router.get('/fetch',verifyToken,ClientController.fetchClientInfo)
router.put('/update', verifyToken,ClientController.updateClient);
router.delete('/delete', verifyToken,ClientController.deleteClient);
router.post('/clientUploadProfileImage',processFile,verifyToken,ClientController.uploadProfileImage);
export default router;