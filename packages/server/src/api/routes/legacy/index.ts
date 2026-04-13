// ============================================================================
// LEGACY ROUTE BUNDLE
// ============================================================================
//
// One file mounting every old commit-3409d88 module under the same paths
// they were exposed at originally, so existing JS clients can hit them.
// ============================================================================

import { Router, Request, Response, NextFunction } from "express";
import { authenticate } from "../../middleware/auth.middleware";
import { sendSuccess } from "../../../utils/response";
import {
  holidayService,
  leaveService,
  roleService,
  profileService,
  categoryService,
  fieldTagService,
  transportService,
  trackingService,
  hrmsAdminService,
  autoEmailReportService,
  legacyClientService,
  legacyUserService,
  attendanceService,
  taskService,
  reportsService,
} from "../../../services/legacy/misc.service";

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

// ---------------------------------------------------------------------------
// HOLIDAY  (mounted at /api/v1/legacy/holiday)
// ---------------------------------------------------------------------------
const holiday = Router();
holiday.use(authenticate);
holiday.post(
  "/create-holiday",
  wrap(async (req) => holidayService.create(req.user!.empcloudOrgId, req.body)),
);
holiday.get("/get-holiday", wrap(async (req) => holidayService.list(req.user!.empcloudOrgId)));
holiday.put(
  "/update-holiday",
  wrap(async (req) => holidayService.update(req.user!.empcloudOrgId, req.body.id, req.body)),
);
holiday.delete(
  "/delete-holiday",
  wrap(async (req) =>
    holidayService.remove(req.user!.empcloudOrgId, String(req.query.id || req.body.id)),
  ),
);

// ---------------------------------------------------------------------------
// LEAVE
// ---------------------------------------------------------------------------
const leave = Router();
leave.use(authenticate);
leave.post(
  "/create-leave-type",
  wrap(async (req) => leaveService.createType(req.user!.empcloudOrgId, req.body)),
);
leave.get("/get-leave-type", wrap(async (req) => leaveService.listTypes(req.user!.empcloudOrgId)));
leave.put(
  "/update-leave-type",
  wrap(async (req) => leaveService.updateType(req.user!.empcloudOrgId, req.body.id, req.body)),
);
leave.delete(
  "/delete-leave-type",
  wrap(async (req) =>
    leaveService.deleteType(req.user!.empcloudOrgId, String(req.query.id || req.body.id)),
  ),
);
leave.post("/get-leaves", wrap(async (req) => leaveService.listLeaves(req.user!.empcloudOrgId, req.body)));
leave.post(
  "/create-leave",
  wrap(async (req) =>
    leaveService.createLeave(req.user!.empcloudOrgId, req.user!.empcloudUserId, req.body),
  ),
);
leave.post("/fetch-leave-type", wrap(async (req) => leaveService.leaveTypeOption(req.user!.empcloudOrgId)));
leave.post(
  "/update-leaves",
  wrap(async (req) => leaveService.updateLeave(req.user!.empcloudOrgId, req.body.id, req.body)),
);
leave.post(
  "/delete-leaves",
  wrap(async (req) => leaveService.deleteLeave(req.user!.empcloudOrgId, req.body.id)),
);

// ---------------------------------------------------------------------------
// ROLE
// ---------------------------------------------------------------------------
const role = Router();
role.use(authenticate);
role.post("/create", wrap(async (req) => roleService.create(req.user!.empcloudOrgId, req.body.role)));
role.get("/get", wrap(async (req) => roleService.list(req.user!.empcloudOrgId)));
role.put(
  "/update",
  wrap(async (req) => roleService.update(req.user!.empcloudOrgId, req.body.id, req.body.role)),
);
role.delete(
  "/delete",
  wrap(async (req) => roleService.remove(req.user!.empcloudOrgId, String(req.query.id || req.body.id))),
);

// ---------------------------------------------------------------------------
// PROFILE
// ---------------------------------------------------------------------------
const profile = Router();
profile.use(authenticate);
profile.get(
  "/fetchProfile",
  wrap(async (req) => profileService.fetch(req.user!.empcloudOrgId, req.user!.empcloudUserId)),
);
profile.post(
  "/updateProfile",
  wrap(async (req) => profileService.update(req.user!.empcloudOrgId, req.user!.empcloudUserId, req.body)),
);
profile.post(
  "/uploadProfileImage",
  wrap(async (req) =>
    profileService.uploadImage(req.user!.empcloudOrgId, req.user!.empcloudUserId, req.body.url),
  ),
);
profile.post(
  "/update-snap-details",
  wrap(async (req) =>
    profileService.updateSnap(
      req.user!.empcloudOrgId,
      req.user!.empcloudUserId,
      Number(req.body.snap_points_limit),
      Number(req.body.snap_duration_limit),
    ),
  ),
);

// ---------------------------------------------------------------------------
// CATEGORY
// ---------------------------------------------------------------------------
const category = Router();
category.use(authenticate);
category.post(
  "/create",
  wrap(async (req) => categoryService.create(req.user!.empcloudOrgId, req.body.category_name)),
);
category.get("/fetch", wrap(async (req) => categoryService.list(req.user!.empcloudOrgId)));
category.put(
  "/update",
  wrap(async (req) =>
    categoryService.update(req.user!.empcloudOrgId, req.body.id, req.body.category_name),
  ),
);
category.delete(
  "/delete",
  wrap(async (req) =>
    categoryService.remove(req.user!.empcloudOrgId, String(req.query.id || req.body.id)),
  ),
);

// ---------------------------------------------------------------------------
// TAGS
// ---------------------------------------------------------------------------
const tag = Router();
tag.use(authenticate);
tag.post(
  "/createTags",
  wrap(async (req) =>
    fieldTagService.create(req.user!.empcloudOrgId, req.user!.empcloudUserId, req.body),
  ),
);
tag.post(
  "/updateTags",
  wrap(async (req) =>
    fieldTagService.update(req.user!.empcloudOrgId, req.user!.empcloudUserId, req.body.id, req.body),
  ),
);
tag.post(
  "/updateTagsOrder",
  wrap(async (req) => fieldTagService.updateOrder(req.user!.empcloudOrgId, req.body.items || [])),
);
tag.get("/getTags", wrap(async (req) => fieldTagService.list(req.user!.empcloudOrgId)));
tag.delete(
  "/deleteTags",
  wrap(async (req) =>
    fieldTagService.remove(req.user!.empcloudOrgId, String(req.query.id || req.body.id)),
  ),
);
tag.get("/getAdminTags", wrap(async (req) => fieldTagService.listAdmin(req.user!.empcloudOrgId)));

// ---------------------------------------------------------------------------
// TRANSPORT
// ---------------------------------------------------------------------------
const transport = Router();
transport.use(authenticate);
transport.post(
  "/Update-Emp-mode-of-transport",
  wrap(async (req) => transportService.updateMode(req.user!.empcloudOrgId, req.body.emp_id, req.body.mode)),
);
transport.post(
  "/Update-Emp-Mot-Frequency-Radius",
  wrap(async (req) =>
    transportService.updateFreqRadius(
      req.user!.empcloudOrgId,
      req.body.emp_id,
      Number(req.body.frequency),
      Number(req.body.radius),
    ),
  ),
);

// ---------------------------------------------------------------------------
// TRACKING
// ---------------------------------------------------------------------------
const tracking = Router();
tracking.use(authenticate);
tracking.post(
  "/get-location",
  wrap(async (req) =>
    trackingService.getLocation(req.user!.empcloudOrgId, req.body.emp_id, req.body.date),
  ),
);

// ---------------------------------------------------------------------------
// HRMS ADMIN
// ---------------------------------------------------------------------------
const hrms = Router();
hrms.use(authenticate);
hrms.post(
  "/location-update",
  wrap(async (req) => hrmsAdminService.updateLocation(req.user!.empcloudOrgId, req.body)),
);
hrms.post(
  "/get-location-details",
  wrap(async (req) => hrmsAdminService.getLocationDetails(req.user!.empcloudOrgId)),
);
hrms.post(
  "/get-employee-conf",
  wrap(async (req) =>
    hrmsAdminService.getEmployeeConf(req.user!.empcloudOrgId, Number(req.body.employee_id)),
  ),
);
hrms.post(
  "/update-employee-conf",
  wrap(async (req) =>
    hrmsAdminService.updateEmployeeConf(
      req.user!.empcloudOrgId,
      Number(req.body.employee_id),
      req.body,
    ),
  ),
);
hrms.get(
  "/org-location-list",
  wrap(async (req) => hrmsAdminService.orgLocationList(req.user!.empcloudOrgId)),
);
hrms.post(
  "/updateEmployeeLocation",
  wrap(async (req) =>
    hrmsAdminService.updateEmployeeLocation(
      req.user!.empcloudOrgId,
      Number(req.body.employee_id),
      req.body,
    ),
  ),
);
hrms.get(
  "/getEmployeeLocation",
  wrap(async (req) =>
    hrmsAdminService.getEmployeeLocation(req.user!.empcloudOrgId, Number(req.query.employee_id)),
  ),
);

// ---------------------------------------------------------------------------
// AUTO EMAIL REPORT
// ---------------------------------------------------------------------------
const autoEmail = Router();
autoEmail.use(authenticate);
autoEmail.post(
  "/createAutoEmailReport",
  wrap(async (req) => autoEmailReportService.create(req.user!.empcloudOrgId, req.body)),
);
autoEmail.post("/get", wrap(async (req) => autoEmailReportService.list(req.user!.empcloudOrgId)));
autoEmail.post(
  "/Update",
  wrap(async (req) => autoEmailReportService.update(req.user!.empcloudOrgId, req.body.id, req.body)),
);
autoEmail.post(
  "/delete",
  wrap(async (req) => autoEmailReportService.remove(req.user!.empcloudOrgId, req.body.id)),
);

// ---------------------------------------------------------------------------
// LEGACY CLIENT
// ---------------------------------------------------------------------------
const legacyClient = Router();
legacyClient.use(authenticate);
legacyClient.post(
  "/create",
  wrap(async (req) => legacyClientService.create(req.user!.empcloudOrgId, req.body)),
);
legacyClient.get("/fetch", wrap(async (req) => legacyClientService.list(req.user!.empcloudOrgId)));
legacyClient.put(
  "/update",
  wrap(async (req) => legacyClientService.update(req.user!.empcloudOrgId, req.body.id, req.body)),
);
legacyClient.delete(
  "/delete",
  wrap(async (req) =>
    legacyClientService.remove(req.user!.empcloudOrgId, String(req.query.id || req.body.id)),
  ),
);
legacyClient.post(
  "/clientUploadProfileImage",
  wrap(async (req) =>
    legacyClientService.uploadProfileImage(req.user!.empcloudOrgId, req.body.id, req.body.url),
  ),
);

// ---------------------------------------------------------------------------
// LEGACY USER
// ---------------------------------------------------------------------------
const legacyUser = Router();
legacyUser.use(authenticate);
legacyUser.post(
  "/create",
  wrap(async (req) => legacyUserService.create(req.user!.empcloudOrgId, req.body)),
);
legacyUser.get("/fetch", wrap(async (req) => legacyUserService.fetch(req.user!.empcloudOrgId)));
legacyUser.put(
  "/update",
  wrap(async (req) => legacyUserService.update(req.user!.empcloudOrgId, Number(req.body.user_id), req.body)),
);
legacyUser.delete(
  "/delete",
  wrap(async (req) =>
    legacyUserService.remove(req.user!.empcloudOrgId, Number(req.query.user_id || req.body.user_id)),
  ),
);
legacyUser.get(
  "/fetch-emp-users",
  wrap(async (req) => legacyUserService.fetchEmpUsers(req.user!.empcloudOrgId)),
);
legacyUser.post(
  "/add-emp-users",
  wrap(async (req) => legacyUserService.addEmpUsers(req.user!.empcloudOrgId, req.body.users || [])),
);
legacyUser.get(
  "/user-frequency-geolocation",
  wrap(async (req) =>
    legacyUserService.setFrequencyAndGeoLoc(
      req.user!.empcloudOrgId,
      Number(req.query.user_id),
      Number(req.query.frequency),
      Number(req.query.geo_on),
    ),
  ),
);
legacyUser.put(
  "/update_Biometric_config",
  wrap(async (req) =>
    legacyUserService.updateBiometricConfig(
      req.user!.empcloudOrgId,
      Number(req.body.user_id),
      Number(req.body.enabled),
    ),
  ),
);

// ---------------------------------------------------------------------------
// ATTENDANCE
// ---------------------------------------------------------------------------
const attendance = Router();
attendance.use(authenticate);
attendance.post(
  "/fetch-attendance",
  wrap(async (req) => attendanceService.fetch(req.user!.empcloudOrgId, req.body)),
);
attendance.post(
  "/mark-attendance",
  wrap(async (req) =>
    attendanceService.mark(req.user!.empcloudOrgId, req.user!.empcloudUserId, req.body),
  ),
);
attendance.get(
  "/attendance",
  wrap(async (req) => attendanceService.getMine(req.user!.empcloudOrgId, req.user!.empcloudUserId)),
);
attendance.post(
  "/attendance-request",
  wrap(async (req) =>
    attendanceService.createRequest(req.user!.empcloudOrgId, req.user!.empcloudUserId, req.body),
  ),
);

// ---------------------------------------------------------------------------
// TASK (legacy)
// ---------------------------------------------------------------------------
const task = Router();
task.use(authenticate);
task.post(
  "/create",
  wrap(async (req) => taskService.create(req.user!.empcloudOrgId, req.body)),
);
task.get("/fetch", wrap(async (req) => taskService.fetch(req.user!.empcloudOrgId, req.query)));
task.put(
  "/update",
  wrap(async (req) => taskService.update(req.user!.empcloudOrgId, req.body.id, req.body)),
);
task.delete(
  "/delete",
  wrap(async (req) => taskService.remove(req.user!.empcloudOrgId, String(req.query.id || req.body.id))),
);
task.put(
  "/update-approve",
  wrap(async (req) =>
    taskService.approve(req.user!.empcloudOrgId, req.body.id, Number(req.body.task_approve_status)),
  ),
);
task.post(
  "/update-taskStatus",
  wrap(async (req) =>
    taskService.updateStatus(req.user!.empcloudOrgId, req.body.id, req.body.emp_start_time, req.body.emp_end_time),
  ),
);
task.post(
  "/uploadTask-files",
  wrap(async (req) =>
    taskService.uploadTaskFiles(
      req.user!.empcloudOrgId,
      req.body.id,
      req.body.files || [],
      req.body.images || [],
    ),
  ),
);
task.delete(
  "/deleteDocument",
  wrap(async (req) =>
    taskService.deleteDoc(req.user!.empcloudOrgId, String(req.body.id), String(req.body.url)),
  ),
);
task.post(
  "/filterTask",
  wrap(async (req) => taskService.filterTask(req.user!.empcloudOrgId, req.body)),
);
task.get(
  "/getNotification",
  wrap(async (req) => taskService.getNotification(req.user!.empcloudOrgId)),
);

// ---------------------------------------------------------------------------
// REPORTS
// ---------------------------------------------------------------------------
const reports = Router();
reports.use(authenticate);
reports.post(
  "/get-Consolidated-Reports",
  wrap(async (req) => reportsService.consolidated(req.user!.empcloudOrgId, req.body)),
);
reports.post(
  "/updateGeoFencing",
  wrap(async (req) =>
    reportsService.updateGeoFencing(
      req.user!.empcloudOrgId,
      Number(req.body.employee_id),
      Number(req.body.enabled),
    ),
  ),
);
reports.post(
  "/getUserDetails",
  wrap(async (req) => reportsService.getUserDetails(req.user!.empcloudOrgId, Number(req.body.user_id))),
);
reports.post(
  "/taskListDetails",
  wrap(async (req) => reportsService.taskListDetails(req.user!.empcloudOrgId, req.body)),
);
reports.post(
  "/userStats",
  wrap(async (req) => reportsService.userStats(req.user!.empcloudOrgId, Number(req.body.user_id))),
);
reports.post("/taskStatus", wrap(async (req) => reportsService.taskStatus(req.user!.empcloudOrgId)));
reports.post("/taskStages", wrap(async (req) => reportsService.taskStages(req.user!.empcloudOrgId)));
reports.post("/clientDetails", wrap(async (req) => reportsService.clientDetails(req.user!.empcloudOrgId)));
reports.post(
  "/distanceTraveled",
  wrap(async (req) => reportsService.distanceTraveled(req.user!.empcloudOrgId, req.body)),
);
reports.post(
  "/getIndividualAttendanceData",
  wrap(async (req) =>
    reportsService.getIndividualAttendance(
      req.user!.empcloudOrgId,
      Number(req.body.user_id),
      req.body,
    ),
  ),
);

// ---------------------------------------------------------------------------
// EXPORT BUNDLE
// ---------------------------------------------------------------------------
export const legacyRouters = {
  holiday,
  leave,
  role,
  profile,
  category,
  tag,
  transport,
  tracking,
  hrms,
  autoEmail,
  legacyClient,
  legacyUser,
  attendance,
  task,
  reports,
};
