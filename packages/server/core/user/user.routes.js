import Router from 'express';
const router = Router();
import UserController from './user.controller.js';
import verifyToken from '../../middleware/verifyToken.js';

router.post('/create', verifyToken, UserController.createUser); 
router.get('/fetch', verifyToken, UserController.fetchUsers);
router.put('/update', verifyToken, UserController.updateUser);
router.delete('/delete', verifyToken, UserController.deleteUser);
router.get('/fetch-emp-users', verifyToken, UserController.fetchEmpUsers); 
router.post('/add-emp-users', verifyToken, UserController.addEmpUsers);
router.get('/user-frequency-geolocation',verifyToken,UserController.setFrequencyAndGeoLoc);
router.put('/update_Biometric_config',verifyToken, UserController.updateBiometricConfig);



export default router;
