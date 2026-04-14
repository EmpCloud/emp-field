// ============================================================================
// LEGACY ADMIN ROUTES (mirrors commit 3409d88 admin.routes.js)
// ============================================================================

import { Router, Request, Response, NextFunction } from "express";
import { authenticate } from "../../middleware/auth.middleware";
import * as adminService from "../../../services/legacy/admin.service";
import { sendSuccess } from "../../../utils/response";

const router = Router();

const wrap =
  (fn: (req: Request, res: Response) => Promise<any>) =>
  async (req: Request, res: Response, next: NextFunction) => {
    try {
      const result = await fn(req, res);
      if (result !== undefined) sendSuccess(res, result);
    } catch (err) {
      next(err);
    }
  };

// ----- Public auth surface ------------------------------------------------
router.post(
  "/sign-up",
  wrap(async (req) => adminService.signUp(req.body)),
);
router.post(
  "/sign-in",
  wrap(async (req) => adminService.signIn(req.body.email, req.body.password)),
);
router.post(
  "/update-phone",
  wrap(async (req) => adminService.updatePhone(req.body.organization_id, req.body.adminId, req.body.phone_number)),
);
router.post(
  "/verify-phone",
  wrap(async (req) => adminService.verifyPhone(req.body.organization_id, req.body.adminId, req.body.otp)),
);
router.post(
  "/forgot-password-mail",
  wrap(async (req) => adminService.forgotPassword(req.body.email)),
);
router.post(
  "/reset-password",
  wrap(async (req) => adminService.resetPassword(req.body.email, req.body.token, req.body.password)),
);
router.post(
  "/email-verification-token-generate",
  wrap(async (req) => adminService.generateToken(req.body.email)),
);
router.post(
  "/phone-verification-otp-generate",
  wrap(async () => adminService.generateOtp()),
);

// ----- Authenticated endpoints --------------------------------------------
router.use(authenticate);

router.post(
  "/admin-task",
  wrap(async (req) => adminService.getAdminTask(req.user!.empcloudOrgId)),
);
router.post(
  "/getUserCoordinates",
  wrap(async (req) =>
    adminService.getUserCoordinates(req.user!.empcloudOrgId, Number(req.body.userId), req.body.date),
  ),
);
router.put(
  "/update-Coordinates-Status",
  wrap(async (req) =>
    adminService.updateCoordinatesStatus(req.user!.empcloudOrgId, Number(req.body.userId), req.body.date),
  ),
);
router.post(
  "/adminUploadProfileImage",
  wrap(async (req) =>
    adminService.uploadProfileImage(req.user!.empcloudOrgId, req.body.adminId, req.body.url),
  ),
);
router.put(
  "/update",
  wrap(async (req) =>
    adminService.update(req.user!.empcloudOrgId, req.body.adminId, req.body),
  ),
);
router.put(
  "/update-password",
  wrap(async (req) =>
    adminService.updatePassword(
      req.user!.empcloudOrgId,
      req.body.adminId,
      req.body.oldPassword,
      req.body.newPassword,
    ),
  ),
);
router.put(
  "/two-factor-auth",
  wrap(async (req) =>
    adminService.enableTwoFactor(req.user!.empcloudOrgId, req.body.adminId, !!req.body.enabled),
  ),
);
router.get(
  "/admin-fetch",
  wrap(async (req) => adminService.adminDataFetch(req.user!.empcloudOrgId, String(req.query.adminId))),
);
router.get("/get-languages", wrap(async () => adminService.getLanguages()));
router.post(
  "/getAllAdminFieldTrackingUsers",
  wrap(async (req) => adminService.getAllAdminFieldTrackingUsers(req.user!.empcloudOrgId)),
);
router.post(
  "/allOrgEmployee",
  wrap(async (req) => adminService.allOrgEmployee(req.user!.empcloudOrgId)),
);
router.delete(
  "/permanent-delete-user",
  wrap(async (req) => adminService.deleteAdminUsers(req.user!.empcloudOrgId, req.body.ids || [])),
);
router.delete(
  "/soft-delete-user",
  wrap(async (req) => adminService.softDeleteAdminUsers(req.user!.empcloudOrgId, req.body.ids || [])),
);
router.post(
  "/restore-softDelete-Users",
  wrap(async (req) => adminService.restoreDeletedUsers(req.user!.empcloudOrgId, req.body.ids || [])),
);
router.get("/getLocation", wrap(async (req) => adminService.getLocation(req.user!.empcloudOrgId)));
router.get("/getDepartment", wrap(async (req) => adminService.getDepartment(req.user!.empcloudOrgId)));
router.post(
  "/getUserTimeLine",
  wrap(async (req) =>
    adminService.getUserTimeLine(req.user!.empcloudOrgId, Number(req.body.userId), req.body.date),
  ),
);

// ----- Dashboard ----------------------------------------------------------
router.get(
  "/get-dashboard-stats",
  wrap(async (req) => adminService.dashboardStats(req.user!.empcloudOrgId)),
);
router.post(
  "/getEmployeeDetails",
  wrap(async (req) =>
    adminService.getEmployeeDetails(req.user!.empcloudOrgId, Number(req.body.userId)),
  ),
);
router.get(
  "/allTaskStats",
  wrap(async (req) => adminService.allTaskStats(req.user!.empcloudOrgId)),
);
router.post(
  "/allUsers-Tracking-Data",
  wrap(async (req) => adminService.adminUsersTrackingData(req.user!.empcloudOrgId, req.body.date)),
);
router.post(
  "/users-LocationDetails",
  wrap(async (req) =>
    adminService.adminUsersLocationDetails(req.user!.empcloudOrgId, Number(req.body.userId)),
  ),
);
router.get(
  "/average-working-hours",
  wrap(async (req) => adminService.averageWorkingHours(req.user!.empcloudOrgId)),
);

// ----- Admin Tasks --------------------------------------------------------
router.post(
  "/createTask",
  wrap(async (req) => adminService.createTask(req.user!.empcloudOrgId, req.body)),
);
router.get("/fetchTask", wrap(async (req) => adminService.fetchTask(req.user!.empcloudOrgId)));
router.put(
  "/updateTask",
  wrap(async (req) => adminService.updateTask(req.user!.empcloudOrgId, req.body.id, req.body)),
);
router.delete(
  "/deleteTask",
  wrap(async (req) => adminService.deleteTask(req.user!.empcloudOrgId, String(req.query.id || req.body.id))),
);

// ----- Admin Clients ------------------------------------------------------
router.post(
  "/createClient",
  wrap(async (req) => adminService.createClient(req.user!.empcloudOrgId, req.body)),
);
router.get("/fetchClient", wrap(async (req) => adminService.fetchClientInfo(req.user!.empcloudOrgId)));
router.put(
  "/updateClient",
  wrap(async (req) => adminService.updateClient(req.user!.empcloudOrgId, req.body.id, req.body)),
);
router.delete(
  "/deleteClient",
  wrap(async (req) =>
    adminService.deleteClient(req.user!.empcloudOrgId, String(req.query.id || req.body.id)),
  ),
);

router.post(
  "/allEmployeesAttendance",
  wrap(async (req) => adminService.allEmployeesAttendance(req.user!.empcloudOrgId, req.body.date)),
);

export { router as legacyAdminRoutes };
