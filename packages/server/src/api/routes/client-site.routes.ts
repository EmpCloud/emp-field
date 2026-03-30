import { Router, Request, Response, NextFunction } from "express";
import { authenticate, authorize } from "../middleware/auth.middleware";
import * as clientSiteService from "../../services/client-site.service";
import { createClientSiteSchema, updateClientSiteSchema } from "@emp-field/shared";
import { sendSuccess, sendPaginated } from "../../utils/response";
import { ValidationError } from "../../utils/errors";

const router = Router();

// All routes require authentication
router.use(authenticate);

// GET / — list client sites
router.get("/", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const orgId = req.user!.empcloudOrgId;
    const result = await clientSiteService.list(orgId, {
      page: Number(req.query.page) || 1,
      perPage: Number(req.query.perPage) || 20,
      category: req.query.category as string | undefined,
      active: req.query.active !== undefined ? req.query.active === "true" : undefined,
    });
    sendPaginated(res, result.data, result.total, result.page, result.perPage);
  } catch (err) { next(err); }
});

// GET /:id
router.get("/:id", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const site = await clientSiteService.getById(req.user!.empcloudOrgId, req.params.id as string);
    sendSuccess(res, site);
  } catch (err) { next(err); }
});

// POST / — create (admin only)
router.post("/", authorize("super_admin", "org_admin", "hr_admin", "hr_manager"), async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = createClientSiteSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const site = await clientSiteService.create(req.user!.empcloudOrgId, parsed.data);
    sendSuccess(res, site, 201);
  } catch (err) { next(err); }
});

// PUT /:id — update (admin only)
router.put("/:id", authorize("super_admin", "org_admin", "hr_admin", "hr_manager"), async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = updateClientSiteSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const site = await clientSiteService.update(req.user!.empcloudOrgId, req.params.id as string, parsed.data);
    sendSuccess(res, site);
  } catch (err) { next(err); }
});

// DELETE /:id — delete (admin only)
router.delete("/:id", authorize("super_admin", "org_admin", "hr_admin"), async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await clientSiteService.remove(req.user!.empcloudOrgId, req.params.id as string);
    sendSuccess(res, result);
  } catch (err) { next(err); }
});

export { router as clientSiteRoutes };
