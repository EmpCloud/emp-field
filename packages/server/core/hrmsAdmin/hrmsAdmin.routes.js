import Router from 'express';
const router = Router()
import hrmsAdminController from './hrmsAdmin.controller.js'
import verifyToken from '../../middleware/verifyToken.js';

//update location configuration
router.post('/location-update',verifyToken,hrmsAdminController.updateLocation);
router.post('/get-location-details',verifyToken,hrmsAdminController.getLocationDetails);
router.post('/get-employee-conf',verifyToken,hrmsAdminController.getEmployeeConf);
router.post('/update-employee-conf',verifyToken,hrmsAdminController.updateEmployeeConf);
router.get('/org-location-list',verifyToken,hrmsAdminController.orgLocationList);

router.post('/updateEmployeeLocation',verifyToken,hrmsAdminController.updateEmployeeLocation);
router.get('/getEmployeeLocation',verifyToken,hrmsAdminController.getEmployeeLocation)





export default router;