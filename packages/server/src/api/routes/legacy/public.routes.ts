// ============================================================================
// LEGACY PUBLIC ROUTES (mirrors commit 3409d88 unauthorized.routes.js)
// ============================================================================

import { Router, Request, Response, NextFunction } from "express";
import { authenticate } from "../../middleware/auth.middleware";
import { publicService } from "../../../services/legacy/public.service";
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

// ----- Public surface (no auth) -------------------------------------------
router.post("/verify-email", wrap(async (req) => publicService.verifyEmail(req.body.userMail)));
router.post("/verify-phone", wrap(async (req) => publicService.verifyPhone(req.body.userNumber)));
router.post(
  "/set-password",
  wrap(async (req) => publicService.setPassword(req.body.email, req.body.password)),
);
router.post(
  "/user-login",
  wrap(async (req) => publicService.userLogin(req.body.userMail, req.body.password)),
);
router.post(
  "/forgot-password",
  wrap(async (req) => publicService.forgotPassword(req.body.email)),
);
router.get(
  "/verifyOTP",
  wrap(async (req) => publicService.verifyOTP(String(req.query.email), String(req.query.otp))),
);
router.put(
  "/reset-password",
  wrap(async (req) =>
    publicService.resetPassword(req.body.email, req.body.token, req.body.password),
  ),
);
router.post(
  "/generate-token",
  wrap(async (req) => publicService.generateToken(req.body.email)),
);
router.post("/phone-verification-otp-generate", wrap(async () => publicService.generateOtp()));

// ----- Authenticated portion of the legacy /unauthorized router -----------
router.put(
  "/update-profile",
  authenticate,
  wrap(async (req) =>
    publicService.updateProfile(req.user!.empcloudOrgId, req.user!.empcloudUserId, req.body),
  ),
);
router.post(
  "/check-in",
  authenticate,
  wrap(async (req) =>
    publicService.attendanceCheckIn(req.user!.empcloudOrgId, req.user!.empcloudUserId, req.body),
  ),
);
router.get(
  "/attendance-history",
  authenticate,
  wrap(async (req) =>
    publicService.getAttendance(req.user!.empcloudOrgId, req.user!.empcloudUserId),
  ),
);

// ----- Imports (no token in old version, but org_id from body) ------------
router.post(
  "/import-users",
  wrap(async (req) =>
    publicService.importUsers(Number(req.body.organization_id), req.body.users || []),
  ),
);
router.post(
  "/import-admin",
  wrap(async (req) =>
    publicService.adminImportUsers(Number(req.body.organization_id), req.body.users || []),
  ),
);
router.post(
  "/delete-user",
  wrap(async (req) =>
    publicService.deleteUserPerm(Number(req.body.organization_id), Number(req.body.user_id)),
  ),
);

// ----- EMP Monitor compatibility ------------------------------------------
router.post(
  "/get-location-list",
  wrap(async (req) => publicService.getLocationList(Number(req.body.organization_id))),
);
router.post(
  "/get-geo-location-details",
  wrap(async (req) =>
    publicService.getGeoLocationDetails(
      Number(req.body.organization_id),
      Number(req.body.employee_id),
    ),
  ),
);
router.post(
  "/update-geo-location-details",
  wrap(async (req) =>
    publicService.updateGeoLocationDetails(
      Number(req.body.organization_id),
      Number(req.body.employee_id),
      req.body,
    ),
  ),
);
router.post(
  "/empmonitor-employee-import",
  wrap(async (req) =>
    publicService.empEmployeeImport(Number(req.body.organization_id), req.body.users || []),
  ),
);

export { router as legacyPublicRoutes };
