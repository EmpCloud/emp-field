// ============================================================================
// WEBHOOK SIGNATURE VERIFICATION
// ============================================================================
//
// EmpCloud core fires webhooks to /api/v1/webhooks/empcloud signed with HMAC
// SHA-256 using a shared secret. This middleware verifies the signature
// before handing off to the route handler.
// ============================================================================

import crypto from "crypto";
import { Request, Response, NextFunction } from "express";
import { AppError } from "../../utils/errors";

const SECRET = process.env.EMPCLOUD_WEBHOOK_SECRET || "";

function timingSafeEqual(a: string, b: string): boolean {
  const ab = Buffer.from(a);
  const bb = Buffer.from(b);
  if (ab.length !== bb.length) return false;
  return crypto.timingSafeEqual(ab, bb);
}

export function verifyEmpCloudWebhook(req: Request, _res: Response, next: NextFunction) {
  if (!SECRET) {
    // Fail closed in production; permissive in dev for local testing.
    if (process.env.NODE_ENV === "production") {
      return next(new AppError(500, "WEBHOOK_NOT_CONFIGURED", "Webhook secret not set"));
    }
    return next();
  }

  const signature = req.header("x-empcloud-signature");
  if (!signature) {
    return next(new AppError(401, "MISSING_SIGNATURE", "Webhook signature missing"));
  }

  const raw = JSON.stringify(req.body ?? {});
  const expected =
    "sha256=" + crypto.createHmac("sha256", SECRET).update(raw).digest("hex");

  if (!timingSafeEqual(signature, expected)) {
    return next(new AppError(401, "INVALID_SIGNATURE", "Webhook signature mismatch"));
  }

  next();
}
