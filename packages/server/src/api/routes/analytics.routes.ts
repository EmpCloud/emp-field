import { Router, Request, Response, NextFunction } from "express";
import { authorize } from "../middleware/auth.middleware";
import { protectRoute } from "../middleware/rbac.middleware";
import * as analyticsService from "../../services/analytics.service";
import { sendSuccess } from "../../utils/response";

const router = Router();
router.use(protectRoute);

// GET /dashboard — dashboard KPIs
router.get("/dashboard", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const kpis = await analyticsService.getDashboardKPIs(req.user!.empcloudOrgId);
    sendSuccess(res, kpis);
  } catch (err) { next(err); }
});

// GET /productivity — productivity metrics
router.get("/productivity", authorize("super_admin", "org_admin", "hr_admin", "hr_manager"), async (req: Request, res: Response, next: NextFunction) => {
  try {
    const data = await analyticsService.getProductivity(req.user!.empcloudOrgId, {
      startDate: req.query.startDate as string | undefined,
      endDate: req.query.endDate as string | undefined,
    });
    sendSuccess(res, data);
  } catch (err) { next(err); }
});

// GET /coverage — site coverage
router.get("/coverage", authorize("super_admin", "org_admin", "hr_admin", "hr_manager"), async (req: Request, res: Response, next: NextFunction) => {
  try {
    const data = await analyticsService.getCoverage(req.user!.empcloudOrgId);
    sendSuccess(res, data);
  } catch (err) { next(err); }
});

export { router as analyticsRoutes };
