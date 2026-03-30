import CategoryController from "./category.controller.js";
import Router from 'express';
const router = Router();
import verifyToken from '../../middleware/verifyToken.js';
router.post('/create', verifyToken, CategoryController.addCategory);
router.get('/fetch', verifyToken, CategoryController.fetchCategory);
router.put('/update', verifyToken, CategoryController.updateCategory);
router.delete('/delete', verifyToken, CategoryController.deleteCategory);
export default router;