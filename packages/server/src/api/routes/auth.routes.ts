// ============================================================================
// AUTH ROUTES
// POST /login, POST /sso, POST /refresh-token
// ============================================================================

import { Router, Request, Response, NextFunction } from "express";
import { z } from "zod";
import * as authService from "../../services/auth.service";
import { sendSuccess } from "../../utils/response";
import { ValidationError } from "../../utils/errors";

const router = Router();

const loginSchema = z.object({
  email: z.string().email("Invalid email address"),
  password: z.string().min(1, "Password is required"),
});

const refreshSchema = z.object({
  refreshToken: z.string().min(1, "Refresh token is required"),
});

const ssoSchema = z.object({
  token: z.string().min(1, "SSO token is required"),
});

router.post("/login", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = loginSchema.safeParse(req.body);
    if (!parsed.success) {
      const details: Record<string, string[]> = {};
      for (const issue of parsed.error.issues) {
        const key = issue.path.join(".");
        details[key] = details[key] || [];
        details[key].push(issue.message);
      }
      throw new ValidationError("Invalid input", details);
    }
    const result = await authService.login(parsed.data.email, parsed.data.password);
    sendSuccess(res, result);
  } catch (err) {
    next(err);
  }
});

router.post("/sso", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = ssoSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("SSO token is required");
    }
    const result = await authService.ssoLogin(parsed.data.token);
    sendSuccess(res, result);
  } catch (err) {
    next(err);
  }
});

router.post("/refresh-token", async (req: Request, res: Response, next: NextFunction) => {
  try {
    const parsed = refreshSchema.safeParse(req.body);
    if (!parsed.success) {
      throw new ValidationError("Refresh token is required");
    }
    const tokens = await authService.refreshToken(parsed.data.refreshToken);
    sendSuccess(res, tokens);
  } catch (err) {
    next(err);
  }
});

export { router as authRoutes };
