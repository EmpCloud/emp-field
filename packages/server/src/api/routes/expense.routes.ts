import { Router, Request, Response, NextFunction } from "express";
import { authorize } from "../middleware/auth.middleware";
import { protectRoute } from "../middleware/rbac.middleware";
import * as expenseService from "../../services/expense.service";
import { createExpenseSchema, updateExpenseSchema } from "@emp-field/shared";
import { sendSuccess, sendPaginated } from "../../utils/response";
import { ValidationError } from "../../utils/errors";

const router = Router();
router.use(protectRoute);

// GET / — list expenses
router.get("/", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await expenseService.list(req.user!.empcloudOrgId, {
      userId: req.query.userId ? Number(req.query.userId) : undefined,
      page: Number(req.query.page) || 1,
      perPage: Number(req.query.perPage) || 20,
      status: req.query.status as string | undefined,
      expenseType: req.query.expenseType as string | undefined,
    });
    sendPaginated(res, result.data, result.total, result.page, result.perPage);
  } catch (err) { next(err); }
});

// GET /my — my expenses
router.get("/my", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await expenseService.list(req.user!.empcloudOrgId, {
      userId: req.user!.empcloudUserId,
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
    const expense = await expenseService.getById(req.user!.empcloudOrgId, req.params.id as string);
    sendSuccess(res, expense);
  } catch (err) { next(err); }
});

// POST / — create
router.post("/", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = createExpenseSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const expense = await expenseService.create(req.user!.empcloudOrgId, req.user!.empcloudUserId, parsed.data);
    sendSuccess(res, expense, 201);
  } catch (err) { next(err); }
});

// PUT /:id — update (own drafts only)
router.put("/:id", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = updateExpenseSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Invalid input", parsed.error.flatten().fieldErrors as Record<string, string[]>);
    }
    const expense = await expenseService.update(req.user!.empcloudOrgId, req.params.id as string, req.user!.empcloudUserId, parsed.data);
    sendSuccess(res, expense);
  } catch (err) { next(err); }
});

// POST /:id/submit
router.post("/:id/submit", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const expense = await expenseService.submit(req.user!.empcloudOrgId, req.params.id as string, req.user!.empcloudUserId);
    sendSuccess(res, expense);
  } catch (err) { next(err); }
});

// POST /:id/approve (admin/manager)
router.post("/:id/approve", authorize("super_admin", "org_admin", "hr_admin", "hr_manager"), async (req: Request, res: Response, next: NextFunction) => {
  try {
    const expense = await expenseService.approve(req.user!.empcloudOrgId, req.params.id as string, req.user!.empcloudUserId);
    sendSuccess(res, expense);
  } catch (err) { next(err); }
});

// POST /:id/reject (admin/manager)
router.post("/:id/reject", authorize("super_admin", "org_admin", "hr_admin", "hr_manager"), async (req: Request, res: Response, next: NextFunction) => {
  try {
    const expense = await expenseService.reject(req.user!.empcloudOrgId, req.params.id as string, req.user!.empcloudUserId);
    sendSuccess(res, expense);
  } catch (err) { next(err); }
});

// DELETE /:id — delete own draft
router.delete("/:id", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await expenseService.remove(req.user!.empcloudOrgId, req.params.id as string, req.user!.empcloudUserId);
    sendSuccess(res, result);
  } catch (err) { next(err); }
});

export { router as expenseRoutes };
