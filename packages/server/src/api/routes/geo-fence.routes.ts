import { Router, Request, Response, NextFunction } from "express";
import { authenticate, authorize } from "../middleware/auth.middleware";
import * as geoFenceService from "../../services/geo-fence.service";
import { createGeoFenceSchema, updateGeoFenceSchema, checkPointInFenceSchema } from "@emp-field/shared";
import { sendSuccess, sendPaginated } from "../../utils/response";
import { ValidationError } from "../../utils/errors";

const router = Router();
router.use(authenticate);

// GET / — list geo fences
router.get("/", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await geoFenceService.list(req.user!.empcloudOrgId, {
      page: Number(req.query.page) || 1,
      perPage: Number(req.query.perPage) || 20,
      active: req.query.active !== undefined ? req.query.active === "true" : undefined,
    });
    sendPaginated(res, result.data, result.total, result.page, result.perPage);
  } catch (err) { next(err); }
});

// GET /:id
router.get("/:id", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const fence = await geoFenceService.getById(req.user!.empcloudOrgId, req.params.id as string);
    sendSuccess(res, fence);
  } catch (err) { next(err); }
});

// POST / — create (admin/manager)
router.post("/", authorize("super_admin", "org_admin", "hr_admin", "hr_manager"), async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = createGeoFenceSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const fence = await geoFenceService.create(req.user!.empcloudOrgId, parsed.data);
    sendSuccess(res, fence, 201);
  } catch (err) { next(err); }
});

// PUT /:id
router.put("/:id", authorize("super_admin", "org_admin", "hr_admin", "hr_manager"), async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = updateGeoFenceSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const fence = await geoFenceService.update(req.user!.empcloudOrgId, req.params.id as string, parsed.data);
    sendSuccess(res, fence);
  } catch (err) { next(err); }
});

// DELETE /:id
router.delete("/:id", authorize("super_admin", "org_admin", "hr_admin"), async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await geoFenceService.remove(req.user!.empcloudOrgId, req.params.id as string);
    sendSuccess(res, result);
  } catch (err) { next(err); }
});

// POST /check — check if point is in a fence
router.post("/check", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = checkPointInFenceSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const result = await geoFenceService.checkPointInFence(
      req.user!.empcloudOrgId,
      parsed.data.geo_fence_id,
      parsed.data.latitude,
      parsed.data.longitude,
    );
    sendSuccess(res, result);
  } catch (err) { next(err); }
});

// GET /events — list geo fence events
router.get("/events", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await geoFenceService.getEvents(req.user!.empcloudOrgId, {
      userId: req.query.userId ? Number(req.query.userId) : undefined,
      geoFenceId: req.query.geoFenceId as string | undefined,
      page: Number(req.query.page) || 1,
      perPage: Number(req.query.perPage) || 20,
    });
    sendPaginated(res, result.data, result.total, result.page, result.perPage);
  } catch (err) { next(err); }
});

export { router as geoFenceRoutes };
