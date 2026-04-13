// ============================================================================
// RBAC MIDDLEWARE STACK
// ============================================================================
//
// Standard EmpCloud middleware chain (after `authenticate()`):
//   1. checkOrgScope    — ensure a tenant is attached and not cross-tenant
//   2. checkPermission  — role/permission gate per action
//   3. checkSubscription — verify the org has the `field` module enabled
//
// ============================================================================

import { Request, Response, NextFunction, RequestHandler } from "express";
import { AppError } from "../../utils/errors";
import { getEmpCloudDB } from "../../db/empcloud";
import { logger } from "../../utils/logger";
import { authenticate } from "./auth.middleware";

// ---------------------------------------------------------------------------
// Org scope — blocks requests that target another org via query/body/params
// ---------------------------------------------------------------------------
export function checkOrgScope(req: Request, _res: Response, next: NextFunction) {
  const user = req.user;
  if (!user?.empcloudOrgId) {
    return next(new AppError(401, "UNAUTHORIZED", "Missing organization scope"));
  }

  const claimed =
    (req.body && (req.body.organization_id ?? req.body.organizationId)) ||
    (req.query && (req.query.organization_id ?? req.query.organizationId));

  if (claimed !== undefined && Number(claimed) !== user.empcloudOrgId) {
    return next(new AppError(403, "CROSS_TENANT", "Cross-tenant access denied"));
  }

  next();
}

// ---------------------------------------------------------------------------
// Permission gate — simple role map, extensible to per-permission strings
// ---------------------------------------------------------------------------
type Role = NonNullable<Request["user"]>["role"];

const ROLE_RANK: Record<Role, number> = {
  super_admin: 100,
  org_admin: 80,
  hr_admin: 60,
  hr_manager: 40,
  employee: 20,
};

export function checkPermission(minRole: Role) {
  return (req: Request, _res: Response, next: NextFunction) => {
    if (!req.user) {
      return next(new AppError(401, "UNAUTHORIZED", "Not authenticated"));
    }
    const userRank = ROLE_RANK[req.user.role] ?? 0;
    const needed = ROLE_RANK[minRole] ?? 0;
    if (userRank < needed) {
      return next(
        new AppError(403, "FORBIDDEN", `Requires role '${minRole}' or higher`),
      );
    }
    next();
  };
}

// ---------------------------------------------------------------------------
// Subscription gate — ensures the org has the `field` module active
// ---------------------------------------------------------------------------
const subscriptionCache = new Map<number, { enabled: boolean; expiresAt: number }>();
const CACHE_TTL_MS = 60_000;

export async function checkSubscription(
  req: Request,
  _res: Response,
  next: NextFunction,
) {
  const user = req.user;
  if (!user?.empcloudOrgId) {
    return next(new AppError(401, "UNAUTHORIZED", "Not authenticated"));
  }

  // Super admins always pass.
  if (user.role === "super_admin") return next();

  const now = Date.now();
  const cached = subscriptionCache.get(user.empcloudOrgId);
  if (cached && cached.expiresAt > now) {
    if (!cached.enabled) {
      return next(new AppError(402, "SUBSCRIPTION_REQUIRED", "Field module is not enabled"));
    }
    return next();
  }

  try {
    const db = getEmpCloudDB();
    const row = await db("org_subscriptions")
      .where({ org_id: user.empcloudOrgId, module_slug: "field" })
      .first("status");
    const enabled = row?.status === "active";
    subscriptionCache.set(user.empcloudOrgId, { enabled, expiresAt: now + CACHE_TTL_MS });
    if (!enabled) {
      return next(new AppError(402, "SUBSCRIPTION_REQUIRED", "Field module is not enabled"));
    }
    next();
  } catch (err) {
    // Fail open in dev if the master DB isn't reachable; fail closed otherwise.
    if (process.env.NODE_ENV !== "production") {
      logger.warn("checkSubscription: EmpCloud DB unavailable, allowing in dev");
      return next();
    }
    logger.error("checkSubscription error:", err);
    return next(new AppError(503, "SUBSCRIPTION_CHECK_FAILED", "Unable to verify subscription"));
  }
}

// ---------------------------------------------------------------------------
// Standard protection stack — apply on every protected router
//   authenticate → checkOrgScope → checkSubscription
// ---------------------------------------------------------------------------
export const protectRoute: RequestHandler[] = [
  authenticate,
  checkOrgScope,
  checkSubscription,
];
