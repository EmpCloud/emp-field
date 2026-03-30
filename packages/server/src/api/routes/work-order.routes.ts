import { Router, Request, Response, NextFunction } from "express";
import { authenticate, authorize } from "../middleware/auth.middleware";
import * as workOrderService from "../../services/work-order.service";
import { createWorkOrderSchema, updateWorkOrderSchema } from "@emp-field/shared";
import { sendSuccess, sendPaginated } from "../../utils/response";
import { ValidationError } from "../../utils/errors";

const router = Router();
router.use(authenticate);

// GET / — list work orders
router.get("/", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await workOrderService.list(req.user!.empcloudOrgId, {
      page: Number(req.query.page) || 1,
      perPage: Number(req.query.perPage) || 20,
      status: req.query.status as string | undefined,
      priority: req.query.priority as string | undefined,
      assignedTo: req.query.assignedTo ? Number(req.query.assignedTo) : undefined,
    });
    sendPaginated(res, result.data, result.total, result.page, result.perPage);
  } catch (err) { next(err); }
});

// GET /my — my work orders
router.get("/my", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await workOrderService.getMyOrders(req.user!.empcloudOrgId, req.user!.empcloudUserId, {
      page: Number(req.query.page) || 1,
      perPage: Number(req.query.perPage) || 20,
      status: req.query.status as string | undefined,
    });
    sendPaginated(res, result.data, result.total, result.page, result.perPage);
  } catch (err) { next(err); }
});

// GET /:id
router.get("/:id", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const order = await workOrderService.getById(req.user!.empcloudOrgId, req.params.id as string);
    sendSuccess(res, order);
  } catch (err) { next(err); }
});

// POST / — create (admin/manager)
router.post("/", authorize("super_admin", "org_admin", "hr_admin", "hr_manager"), async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = createWorkOrderSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const order = await workOrderService.create(req.user!.empcloudOrgId, req.user!.empcloudUserId, parsed.data);
    sendSuccess(res, order, 201);
  } catch (err) { next(err); }
});

// PUT /:id — update
router.put("/:id", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = updateWorkOrderSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const order = await workOrderService.update(req.user!.empcloudOrgId, req.params.id as string, parsed.data);
    sendSuccess(res, order);
  } catch (err) { next(err); }
});

// DELETE /:id (admin only)
router.delete("/:id", authorize("super_admin", "org_admin", "hr_admin"), async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await workOrderService.remove(req.user!.empcloudOrgId, req.params.id as string);
    sendSuccess(res, result);
  } catch (err) { next(err); }
});

export { router as workOrderRoutes };
