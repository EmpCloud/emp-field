import { Router, Request, Response, NextFunction } from "express";
import { authorize } from "../middleware/auth.middleware";
import { protectRoute } from "../middleware/rbac.middleware";
import * as settingsService from "../../services/settings.service";
import { updateSettingsSchema } from "@emp-field/shared";
import { sendSuccess } from "../../utils/response";
import { ValidationError } from "../../utils/errors";

const router = Router();
router.use(protectRoute);

// GET / — get field settings
router.get("/", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const settings = await settingsService.get(req.user!.empcloudOrgId);
    sendSuccess(res, settings);
  } catch (err) { next(err); }
});

// PUT / — update field settings (admin only)
router.put("/", authorize("super_admin", "org_admin", "hr_admin"), async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = updateSettingsSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const settings = await settingsService.update(req.user!.empcloudOrgId, parsed.data);
    sendSuccess(res, settings);
  } catch (err) { next(err); }
});

export { router as settingsRoutes };
