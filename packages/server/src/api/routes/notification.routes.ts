import { Router, Request, Response, NextFunction } from "express";
import { authenticate } from "../middleware/auth.middleware";
import * as notificationService from "../../services/notification.service";
import { sendSuccess, sendPaginated } from "../../utils/response";

const router = Router();
router.use(authenticate);

// GET / — list my notifications
router.get("/", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await notificationService.list(req.user!.empcloudOrgId, req.user!.empcloudUserId, {
      page: Number(req.query.page) || 1,
      perPage: Number(req.query.perPage) || 20,
      unreadOnly: req.query.unread === "true",
    });
    sendPaginated(res, result.data, result.total, result.page, result.perPage);
  } catch (err) { next(err); }
});

// GET /unread-count — get unread count
router.get("/unread-count", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await notificationService.getUnreadCount(req.user!.empcloudOrgId, req.user!.empcloudUserId);
    sendSuccess(res, result);
  } catch (err) { next(err); }
});

// POST /:id/read — mark as read
router.post("/:id/read", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const notification = await notificationService.markRead(req.user!.empcloudOrgId, req.user!.empcloudUserId, req.params.id as string);
    sendSuccess(res, notification);
  } catch (err) { next(err); }
});

// POST /read-all — mark all as read
router.post("/read-all", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const result = await notificationService.markAllRead(req.user!.empcloudOrgId, req.user!.empcloudUserId);
    sendSuccess(res, result);
  } catch (err) { next(err); }
});

export { router as notificationRoutes };
