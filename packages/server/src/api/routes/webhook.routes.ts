import { Router, Request, Response, NextFunction } from "express";
import { verifyEmpCloudWebhook } from "../middleware/webhook-verify.middleware";
import { handleEvent, EmpCloudEvent } from "../../services/webhook.service";
import { sendSuccess } from "../../utils/response";
import { AppError } from "../../utils/errors";

const router = Router();

// POST /empcloud — receive events from the EmpCloud core.
router.post("/empcloud", verifyEmpCloudWebhook, async (req: Request, res: Response, next: NextFunction) => {
  try {
    const body = req.body as EmpCloudEvent;
    if (!body?.type || !body?.payload) {
      throw new AppError(400, "INVALID_PAYLOAD", "Expected { type, payload }");
    }
    await handleEvent(body);
    sendSuccess(res, { received: true });
  } catch (err) {
    next(err);
  }
});

export { router as webhookRoutes };
