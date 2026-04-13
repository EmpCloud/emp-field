import { Router, Request, Response, NextFunction } from "express";
import { protectRoute } from "../middleware/rbac.middleware";
import * as visitService from "../../services/visit.service";
import { createVisitLogSchema, updateVisitLogSchema } from "@emp-field/shared";
import { sendSuccess, sendPaginated } from "../../utils/response";
import { ValidationError } from "../../utils/errors";

const router = Router();
router.use(protectRoute);

// GET / — list visit logs
router.get("/", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await visitService.list(req.user!.empcloudOrgId, {
      userId: req.query.userId ? Number(req.query.userId) : undefined,
      clientSiteId: req.query.clientSiteId as string | undefined,
      page: Number(req.query.page) || 1,
      perPage: Number(req.query.perPage) || 20,
    });
    sendPaginated(res, result.data, result.total, result.page, result.perPage);
  } catch (err) { next(err); }
});

// GET /:id
router.get("/:id", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const visit = await visitService.getById(req.user!.empcloudOrgId, req.params.id as string);
    sendSuccess(res, visit);
  } catch (err) { next(err); }
});

// POST / — create
router.post("/", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = createVisitLogSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const visit = await visitService.create(req.user!.empcloudOrgId, req.user!.empcloudUserId, parsed.data);
    sendSuccess(res, visit, 201);
  } catch (err) { next(err); }
});

// PUT /:id — update own visit log
router.put("/:id", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = updateVisitLogSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const visit = await visitService.update(req.user!.empcloudOrgId, req.params.id as string, req.user!.empcloudUserId, parsed.data);
    sendSuccess(res, visit);
  } catch (err) { next(err); }
});

// DELETE /:id — delete own visit log
router.delete("/:id", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await visitService.remove(req.user!.empcloudOrgId, req.params.id as string, req.user!.empcloudUserId);
    sendSuccess(res, result);
  } catch (err) { next(err); }
});

export { router as visitRoutes };
