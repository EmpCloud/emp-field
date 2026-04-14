import { Router, Request, Response, NextFunction } from "express";
import { authorize } from "../middleware/auth.middleware";
import { protectRoute } from "../middleware/rbac.middleware";
import * as routeService from "../../services/route.service";
import { createDailyRouteSchema, updateRouteStatusSchema, reorderStopsSchema, updateRouteStopSchema } from "@emp-field/shared";
import { sendSuccess, sendPaginated } from "../../utils/response";
import { ValidationError } from "../../utils/errors";

const router = Router();
router.use(protectRoute);

// GET / — list routes
router.get("/", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await routeService.list(req.user!.empcloudOrgId, {
      userId: req.query.userId ? Number(req.query.userId) : undefined,
      page: Number(req.query.page) || 1,
      perPage: Number(req.query.perPage) || 20,
      date: req.query.date as string | undefined,
      status: req.query.status as string | undefined,
    });
    sendPaginated(res, result.data, result.total, result.page, result.perPage);
  } catch (err) { next(err); }
});

// GET /my — my routes
router.get("/my", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await routeService.list(req.user!.empcloudOrgId, {
      userId: req.user!.empcloudUserId,
      page: Number(req.query.page) || 1,
      perPage: Number(req.query.perPage) || 20,
      date: req.query.date as string | undefined,
      status: req.query.status as string | undefined,
    });
    sendPaginated(res, result.data, result.total, result.page, result.perPage);
  } catch (err) { next(err); }
});

// GET /:id — get route with stops
router.get("/:id", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const route = await routeService.getById(req.user!.empcloudOrgId, req.params.id as string);
    sendSuccess(res, route);
  } catch (err) { next(err); }
});

// POST / — create daily route with stops
router.post("/", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = createDailyRouteSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const route = await routeService.create(req.user!.empcloudOrgId, req.user!.empcloudUserId, parsed.data);
    sendSuccess(res, route, 201);
  } catch (err) { next(err); }
});

// PUT /:id/status — update route status
router.put("/:id/status", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = updateRouteStatusSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const route = await routeService.updateStatus(req.user!.empcloudOrgId, req.params.id as string, parsed.data.status);
    sendSuccess(res, route);
  } catch (err) { next(err); }
});

// PUT /:id/reorder — reorder stops
router.put("/:id/reorder", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = reorderStopsSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const route = await routeService.reorderStops(req.user!.empcloudOrgId, req.params.id as string, parsed.data.stops);
    sendSuccess(res, route);
  } catch (err) { next(err); }
});

// PUT /:id/stops/:stopId — update a stop
router.put("/:id/stops/:stopId", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = updateRouteStopSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const stop = await routeService.updateStop(req.user!.empcloudOrgId, req.params.id as string, req.params.stopId as string, parsed.data);
    sendSuccess(res, stop);
  } catch (err) { next(err); }
});

// DELETE /:id
router.delete("/:id", authorize("super_admin", "org_admin", "hr_admin", "hr_manager"), async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await routeService.remove(req.user!.empcloudOrgId, req.params.id as string);
    sendSuccess(res, result);
  } catch (err) { next(err); }
});

export { router as routeRoutes };
