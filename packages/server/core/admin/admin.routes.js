import Router from 'express';
const router = Router();
import adminController from './admin.controller.js';
import verifyToken from '../../middleware/verifyToken.js';
import  Multer from 'multer';

let processFile = Multer({
    storage: Multer.memoryStorage(),
}).array('files');


router.post('/sign-up', adminController.addAdmin);
router.post('/sign-in', adminController.fetchAdmin);
// router.post('/verify-email', adminController.verifyEmail);
router.post('/update-phone',adminController.updatePhone);
router.post('/verify-phone', adminController.verifyPhone);
router.post('/forgot-password-mail', adminController.forgotPassword);
router.post('/reset-password', adminController.resetPassword);
router.post('/email-verification-token-generate', adminController.generateToken);
router.post('/phone-verification-otp-generate', adminController.generateOtp);
router.post('/admin-task',verifyToken,adminController.getAdminTask);
router.post('/getUserCoordinates',verifyToken,adminController.getUserCoordinates);
router.put('/update-Coordinates-Status',verifyToken,adminController.updateCoordinatesStatus)
router.post('/adminUploadProfileImage',processFile,verifyToken,adminController.uploadProfileImage);
router.put('/update', verifyToken, adminController.updateAdmin);
router.put('/update-password', verifyToken, adminController.updatePassword);
router.put('/two-factor-auth',verifyToken,adminController.enableTwoFactor);
router.get('/admin-fetch',verifyToken,adminController.adminDataFetch)
router.get('/get-languages',adminController.getLanguages);
router.post('/getAllAdminFieldTrackingUsers',verifyToken,adminController.getAllAdminFieldTrackingUsers);
router.post('/allOrgEmployee',verifyToken, adminController.allOrgEmployee);
// router.delete('/user-delete',verifyToken, adminController.deleteUser);
router.delete('/permanent-delete-user',verifyToken,adminController.deleteAdminUsers);
router.delete('/soft-delete-user',verifyToken,adminController.softDeleteAdminUsers);
router.post('/restore-softDelete-Users',verifyToken,adminController.restoreDeletedUsers)
router.get('/getLocation',verifyToken,adminController.getLocation)
router.get('/getDepartment',verifyToken,adminController.getDepartment)
router.post('/getUserTimeLine',verifyToken,adminController.getUserTimeLine)

//Admin Dashboard
router.get('/get-dashboard-stats',verifyToken, adminController.dashboardStats);
router.post('/getEmployeeDetails',verifyToken,adminController.getEmployeeDetails)
router.get('/allTaskStats',verifyToken,adminController.allTaskStats);
router.post('/allUsers-Tracking-Data',verifyToken,adminController.adminUsersTrackingData);
router.post('/users-LocationDetails',verifyToken,adminController.adminUsersLocationDetails);
router.get('/average-working-hours',verifyToken, adminController.averageWorkingHours);



//Admin Task Api's
router.post('/createTask', verifyToken, adminController.createTask);
router.get('/fetchTask',verifyToken,adminController.fetchTask);
router.put('/updateTask', verifyToken,adminController.updateTask);
router.delete('/deleteTask', verifyToken,adminController.deleteTask);



//Admin Client Api's
router.post('/createClient', verifyToken, adminController.createClient);
router.get('/fetchClient',verifyToken,adminController.fetchClientInfo)
router.put('/updateClient', verifyToken,adminController.updateClient);
router.delete('/deleteClient', verifyToken,adminController.deleteClient);


router.post('/allEmployeesAttendance',verifyToken, adminController.allEmployeesAttendance);


export default router;



