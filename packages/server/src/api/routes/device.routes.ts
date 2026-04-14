import { Router, Request, Response, NextFunction } from "express";
import { protectRoute } from "../middleware/rbac.middleware";
import * as deviceService from "../../services/device.service";
import { registerDeviceSchema } from "@emp-field/shared";
import { sendSuccess } from "../../utils/response";
import { ValidationError } from "../../utils/errors";

const router = Router();
router.use(protectRoute);

// POST / — register or refresh a push device
router.post("/", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = registerDeviceSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError(
        "Invalid input",
        parsed.error.flatten().fieldErrors as Record<string, string[]>,
      );
    }
    const device = await deviceService.register(
      req.user!.empcloudOrgId,
      req.user!.empcloudUserId,
      parsed.data,
    );
    sendSuccess(res, device, 201);
  } catch (err) {
    next(err);
  }
});

// GET / — list my registered devices
router.get("/", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const devices = await deviceService.listForUser(
      req.user!.empcloudOrgId,
      req.user!.empcloudUserId,
    );
    sendSuccess(res, devices);
  } catch (err) {
    next(err);
  }
});

// POST /heartbeat — keep a device marked as live
router.post("/heartbeat", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const token = String(req.body?.push_token ?? "");
    if (!token) throw new ValidationError("push_token is required");
    await deviceService.heartbeat(
      req.user!.empcloudOrgId,
      req.user!.empcloudUserId,
      token,
    );
    sendSuccess(res, { ok: true });
  } catch (err) {
    next(err);
  }
});

// DELETE /:id — unregister a device
router.delete("/:id", async (req: Request, res: Response, next: NextFunction) => {
  try {
    await deviceService.unregister(
      req.user!.empcloudOrgId,
      req.user!.empcloudUserId,
      req.params.id as string,
    );
    sendSuccess(res, { ok: true });
  } catch (err) {
    next(err);
  }
});

export { router as deviceRoutes };
