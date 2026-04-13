// ============================================================================
// AUDIT LOGGING MIDDLEWARE
// ============================================================================
//
// Captures every sensitive write (POST/PUT/PATCH/DELETE) into audit_logs.
// Fires after the response is sent so it never slows the request path.
// ============================================================================

import { Request, Response, NextFunction } from "express";
import { v4 as uuidv4 } from "uuid";
import { getDB } from "../../db/connection";
import { logger } from "../../utils/logger";

const WRITE_METHODS = new Set(["POST", "PUT", "PATCH", "DELETE"]);

// Never log request bodies for these paths (credentials / tokens).
const SENSITIVE_PATHS = [/\/auth\//i, /\/refresh/i, /\/password/i];

function isSensitive(path: string): boolean {
  return SENSITIVE_PATHS.some((re) => re.test(path));
}

function resourceFromPath(path: string): string {
  const parts = path.split("/").filter(Boolean);
  const idx = parts.findIndex((p) => p === "v1");
  return parts[idx + 1] ?? "unknown";
}

function actionFromMethod(method: string): string {
  switch (method) {
    case "POST":
      return "create";
    case "PUT":
    case "PATCH":
      return "update";
    case "DELETE":
      return "delete";
    default:
      return method.toLowerCase();
  }
}

export function auditLogger(req: Request, res: Response, next: NextFunction) {
  if (!WRITE_METHODS.has(req.method)) return next();

  res.on("finish", () => {
    try {
      const user = req.user;
      if (!user?.empcloudOrgId) return;

      const db = getDB();
      const entry = {
        id: uuidv4(),
        organization_id: user.empcloudOrgId,
        user_id: user.empcloudUserId || null,
        action: actionFromMethod(req.method),
        resource: resourceFromPath(req.originalUrl || req.url),
        resource_id: (req.params?.id as string) ?? null,
        method: req.method,
        path: (req.originalUrl || req.url).slice(0, 500),
        status_code: res.statusCode,
        ip_address: req.ip?.slice(0, 45) ?? null,
        user_agent: (req.headers["user-agent"] ?? "").toString().slice(0, 500),
        request_body: isSensitive(req.path) ? null : JSON.stringify(req.body ?? {}),
        response_meta: null,
      };

      db("audit_logs")
        .insert(entry)
        .catch((err) => logger.error("audit insert failed:", err));
    } catch (err) {
      logger.error("audit middleware error:", err);
    }
  });

  next();
}
