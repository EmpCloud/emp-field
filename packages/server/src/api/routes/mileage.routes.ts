import { Router, Request, Response, NextFunction } from "express";
import { protectRoute } from "../middleware/rbac.middleware";
import * as mileageService from "../../services/mileage.service";
import { createMileageSchema } from "@emp-field/shared";
import { sendSuccess, sendPaginated } from "../../utils/response";
import { ValidationError } from "../../utils/errors";

const router = Router();
router.use(protectRoute);

// GET / — list my mileage logs
router.get("/", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await mileageService.list(req.user!.empcloudOrgId, req.user!.empcloudUserId, {
      page: Number(req.query.page) || 1,
      perPage: Number(req.query.perPage) || 20,
      startDate: req.query.startDate as string | undefined,
      endDate: req.query.endDate as string | undefined,
    });
    sendPaginated(res, result.data, result.total, result.page, result.perPage);
  } catch (err) { next(err); }
});

// POST / — create mileage log
router.post("/", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = createMileageSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const log = await mileageService.create(req.user!.empcloudOrgId, req.user!.empcloudUserId, parsed.data);
    sendSuccess(res, log, 201);
  } catch (err) { next(err); }
});

// GET /summary — mileage summary
router.get("/summary", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const summary = await mileageService.getSummary(req.user!.empcloudOrgId, req.user!.empcloudUserId, {
      startDate: req.query.startDate as string | undefined,
      endDate: req.query.endDate as string | undefined,
    });
    sendSuccess(res, summary);
  } catch (err) { next(err); }
});

export { router as mileageRoutes };
