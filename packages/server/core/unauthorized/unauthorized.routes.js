import Router from 'express';
const router = Router();
import unauthorizedController from './unauthorized.controller.js';
import verifyToken from '../../middleware/verifyToken.js';

router.post('/verify-email', unauthorizedController.verifyUser);
router.post('/verify-phone', unauthorizedController.verifyPhone);
router.post('/set-password', unauthorizedController.setPassword);
router.post('/user-login', unauthorizedController.UserLogin);
router.post('/forgot-password', unauthorizedController.forgotPassword);
router.get('/verifyOTP', unauthorizedController.verifyOTP);
router.put('/reset-password', unauthorizedController.resetPassword);
router.post('/generate-token', unauthorizedController.generateToken);
router.post('/phone-verification-otp-generate', unauthorizedController.generateOtp);

router.put('/update-profile',verifyToken,unauthorizedController.updateProfile);
router.post('/check-in',verifyToken,unauthorizedController.attendance);
router.get('/attendance-history',verifyToken, unauthorizedController.getAttendance);
router.post('/import-users',unauthorizedController.importUsers);
router.post('/import-admin',unauthorizedController.adminImportUsers);
router.post('/delete-user',unauthorizedController.deleteUserPerm);

//EMP Monitor APi's
router.post('/get-location-list',unauthorizedController.getLocationList);
router.post('/get-geo-location-details',unauthorizedController.getGeoLocationDetails);
router.post('/update-geo-location-details',unauthorizedController.updateGeoLocationDetails);
router.post('/empmonitor-employee-import',unauthorizedController.empEmployeeImport);
export default router;
