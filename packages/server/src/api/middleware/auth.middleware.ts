import { Request, Response, NextFunction } from "express";
import jwt from "jsonwebtoken";
import { config } from "../../config";
import { AppError } from "../../utils/errors";
import { loadPublicKey } from "../../utils/jwt-keys";

export interface AuthPayload {
  empcloudUserId: number;
  empcloudOrgId: number;
  fieldProfileId: string | null;
  role: "super_admin" | "org_admin" | "hr_admin" | "hr_manager" | "employee";
  email: string;
  firstName: string;
  lastName: string;
  orgName: string;
}

declare global {
  namespace Express {
    interface Request {
      user?: AuthPayload;
    }
  }
}

export function authenticate(req: Request, _res: Response, next: NextFunction) {
  // Internal service bypass for dashboard widget data fetching
  const internalService = req.headers["x-internal-service"];
  const internalSecret = req.headers["x-internal-secret"];
  const expectedSecret = process.env.INTERNAL_SERVICE_SECRET || "";
  if (internalService === "empcloud-dashboard" && expectedSecret && internalSecret === expectedSecret) {
    const orgId = Number(req.query.organization_id);
    if (orgId) {
      req.user = {
        empcloudUserId: 0,
        empcloudOrgId: orgId,
        fieldProfileId: null,
        role: "org_admin",
        email: "system@empcloud.internal",
        firstName: "System",
        lastName: "Service",
        orgName: "System",
      };
      return next();
    }
  }

  const header = req.headers.authorization;
  const queryToken = req.query.token as string | undefined;

  if (!header?.startsWith("Bearer ") && !queryToken) {
    return next(new AppError(401, "UNAUTHORIZED", "Missing or invalid authorization header"));
  }

  const token = queryToken || header!.slice(7);
  const publicKey = loadPublicKey();

  try {
    let payload: AuthPayload;
    if (publicKey) {
      // Prefer RS256 from EmpCloud OAuth2; fall back to HS256 on signature mismatch.
      try {
        payload = jwt.verify(token, publicKey, {
          algorithms: ["RS256"],
          issuer: config.jwt.issuer,
        }) as AuthPayload;
      } catch (rsErr: any) {
        if (rsErr.name === "TokenExpiredError") throw rsErr;
        payload = jwt.verify(token, config.jwt.secret, { algorithms: ["HS256"] }) as AuthPayload;
      }
    } else {
      payload = jwt.verify(token, config.jwt.secret, { algorithms: ["HS256"] }) as AuthPayload;
    }
    req.user = payload;
    next();
  } catch (err: any) {
    if (err.name === "TokenExpiredError") {
      return next(new AppError(401, "TOKEN_EXPIRED", "Access token has expired"));
    }
    return next(new AppError(401, "INVALID_TOKEN", "Invalid access token"));
  }
}

export function authorize(...roles: AuthPayload["role"][]) {
  return (req: Request, _res: Response, next: NextFunction) => {
    if (!req.user) {
      return next(new AppError(401, "UNAUTHORIZED", "Not authenticated"));
    }
    if (roles.length > 0 && !roles.includes(req.user.role)) {
      return next(
        new AppError(403, "FORBIDDEN", "You do not have permission to perform this action"),
      );
    }
    next();
  };
}
