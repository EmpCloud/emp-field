import { Router, Request, Response, NextFunction } from "express";
import { authenticate, authorize } from "../middleware/auth.middleware";
import * as locationService from "../../services/location.service";
import { batchLocationSchema } from "@emp-field/shared";
import { sendSuccess } from "../../utils/response";
import { ValidationError } from "../../utils/errors";

const router = Router();
router.use(authenticate);

// POST /batch — batch upload location points
router.post("/batch", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = batchLocationSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const result = await locationService.batchUpload(
      req.user!.empcloudOrgId,
      req.user!.empcloudUserId,
      parsed.data.points,
    );
    sendSuccess(res, result, 201);
  } catch (err) { next(err); }
});

// GET /live — live locations for org (admin/manager)
router.get("/live", authorize("super_admin", "org_admin", "hr_admin", "hr_manager"), async (req: Request, res: Response, next: NextFunction) => {
  try {
    const data = await locationService.getLiveLocations(req.user!.empcloudOrgId);
    sendSuccess(res, data);
  } catch (err) { next(err); }
});

// GET /trail/:userId — location trail for a user
router.get("/trail/:userId", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const data = await locationService.getTrail(req.user!.empcloudOrgId, Number(req.params.userId as string), {
      startDate: req.query.startDate as string | undefined,
      endDate: req.query.endDate as string | undefined,
      limit: req.query.limit ? Number(req.query.limit) : undefined,
    });
    sendSuccess(res, data);
  } catch (err) { next(err); }
});

// GET /my — my location trail
router.get("/my", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const data = await locationService.getTrail(req.user!.empcloudOrgId, req.user!.empcloudUserId, {
      startDate: req.query.startDate as string | undefined,
      endDate: req.query.endDate as string | undefined,
      limit: req.query.limit ? Number(req.query.limit) : undefined,
    });
    sendSuccess(res, data);
  } catch (err) { next(err); }
});

export { router as locationRoutes };
