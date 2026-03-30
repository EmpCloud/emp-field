import { Router, Request, Response, NextFunction } from "express";
import { authenticate } from "../middleware/auth.middleware";
import * as checkinService from "../../services/checkin.service";
import { checkInSchema, checkOutSchema } from "@emp-field/shared";
import { sendSuccess, sendPaginated } from "../../utils/response";
import { ValidationError } from "../../utils/errors";

const router = Router();
router.use(authenticate);

// POST / — check in
router.post("/", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = checkInSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const checkin = await checkinService.checkIn(req.user!.empcloudOrgId, req.user!.empcloudUserId, parsed.data);
    sendSuccess(res, checkin, 201);
  } catch (err) { next(err); }
});

// POST /:id/checkout — check out
router.post("/:id/checkout", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = checkOutSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const checkin = await checkinService.checkOut(req.user!.empcloudOrgId, req.user!.empcloudUserId, req.params.id as string, parsed.data);
    sendSuccess(res, checkin);
  } catch (err) { next(err); }
});

// GET /active — get active check-in
router.get("/active", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const checkin = await checkinService.getActive(req.user!.empcloudOrgId, req.user!.empcloudUserId);
    sendSuccess(res, checkin);
  } catch (err) { next(err); }
});

// GET /today — today's check-ins for current user
router.get("/today", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const data = await checkinService.getToday(req.user!.empcloudOrgId, req.user!.empcloudUserId);
    sendSuccess(res, data);
  } catch (err) { next(err); }
});

// GET / — list check-ins (with filters)
router.get("/", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await checkinService.list(req.user!.empcloudOrgId, {
      userId: req.query.userId ? Number(req.query.userId) : undefined,
      page: Number(req.query.page) || 1,
      perPage: Number(req.query.perPage) || 20,
      status: req.query.status as string | undefined,
      startDate: req.query.startDate as string | undefined,
      endDate: req.query.endDate as string | undefined,
    });
    sendPaginated(res, result.data, result.total, result.page, result.perPage);
  } catch (err) { next(err); }
});

// GET /:id
router.get("/:id", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const checkin = await checkinService.getById(req.user!.empcloudOrgId, req.params.id as string);
    sendSuccess(res, checkin);
  } catch (err) { next(err); }
});

export { router as checkinRoutes };
