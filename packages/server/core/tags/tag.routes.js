import Router from 'express';
const router = Router();
import verifyToken from '../../middleware/verifyToken.js';
import tagsController from '../tags/tag.controller.js'


router.post('/createTags',verifyToken, tagsController.createTags);
router.post('/updateTags',verifyToken, tagsController.updateTags);
router.post('/updateTagsOrder',verifyToken, tagsController.updateTagsOrder);
router.get('/getTags',verifyToken, tagsController.getTags);
router.delete('/deleteTags',verifyToken, tagsController.deleteTags);
router.get('/getAdminTags',verifyToken, tagsController.getAdminTags);

export default router;
