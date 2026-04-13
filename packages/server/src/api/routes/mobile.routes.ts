import { Router, Request, Response, NextFunction } from "express";
import { protectRoute } from "../middleware/rbac.middleware";
import * as mobileService from "../../services/mobile.service";
import { mobileSyncSchema } from "@emp-field/shared";
import { sendSuccess } from "../../utils/response";
import { ValidationError } from "../../utils/errors";

const router = Router();
router.use(protectRoute);

// GET /today — one-call digest for the mobile home screen
router.get("/today", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const data = await mobileService.getToday(
      req.user!.empcloudOrgId,
      req.user!.empcloudUserId,
    );
    sendSuccess(res, data);
  } catch (err) {
    next(err);
  }
});

// POST /sync — offline batch upload from mobile clients
router.post("/sync", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = mobileSyncSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError(
        "Invalid input",
        parsed.error.flatten().fieldErrors as Record<string, string[]>,
      );
    }
    const report = await mobileService.sync(
      req.user!.empcloudOrgId,
      req.user!.empcloudUserId,
      parsed.data,
    );
    sendSuccess(res, report);
  } catch (err) {
    next(err);
  }
});

export { router as mobileRoutes };
