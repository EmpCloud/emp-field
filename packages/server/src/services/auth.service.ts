// ============================================================================
// AUTH SERVICE
// Handles SSO login, token refresh for EMP Field.
// Users are stored in the EmpCloud master database.
// ============================================================================

import bcrypt from "bcryptjs";
import jwt from "jsonwebtoken";
import { config } from "../config";
import { logger } from "../utils/logger";
import {
  findUserByEmail,
  findUserById,
  findOrgById,
  getEmpCloudDB,
} from "../db/empcloud";
import { UnauthorizedError, ValidationError, ConflictError } from "../utils/errors";
import type { AuthPayload } from "../api/middleware/auth.middleware";

interface LoginResult {
  user: {
    empcloudUserId: number;
    empcloudOrgId: number;
    fieldProfileId: string | null;
    role: string;
    email: string;
    firstName: string;
    lastName: string;
    orgName: string;
  };
  tokens: {
    accessToken: string;
    refreshToken: string;
  };
}

function signAccessToken(payload: AuthPayload): string {
  return jwt.sign(payload, config.jwt.secret, {
    expiresIn: config.jwt.accessExpiry as any,
  });
}

function signRefreshToken(userId: number): string {
  return jwt.sign({ userId, type: "refresh" }, config.jwt.secret, {
    expiresIn: config.jwt.refreshExpiry as any,
  });
}

export async function login(email: string, password: string): Promise<LoginResult> {
  const user = await findUserByEmail(email);
  if (!user) {
    throw new UnauthorizedError("Invalid email or password");
  }

  if (!user.password) {
    throw new UnauthorizedError("Password not set for this account");
  }

  const valid = await bcrypt.compare(password, user.password);
  if (!valid) {
    throw new UnauthorizedError("Invalid email or password");
  }

  const org = await findOrgById(user.organization_id);
  if (!org || !org.is_active) {
    throw new UnauthorizedError("Organization is inactive");
  }

  const payload: AuthPayload = {
    empcloudUserId: user.id,
    empcloudOrgId: user.organization_id,
    fieldProfileId: null,
    role: user.role as AuthPayload["role"],
    email: user.email,
    firstName: user.first_name,
    lastName: user.last_name,
    orgName: org.name,
  };

  const accessToken = signAccessToken(payload);
  const refreshToken = signRefreshToken(user.id);

  logger.info(`User logged in: ${user.email} (org: ${org.name})`);

  return {
    user: payload,
    tokens: { accessToken, refreshToken },
  };
}

/**
 * SSO login: exchange an EMP Cloud RS256 JWT for a Field-specific HS256 JWT.
 */
export async function ssoLogin(empcloudToken: string): Promise<LoginResult> {
  const decoded = jwt.decode(empcloudToken);
  if (!decoded || typeof decoded === "string") {
    throw new UnauthorizedError("Invalid SSO token");
  }

  const userId = Number(decoded.sub);
  if (!userId) {
    throw new UnauthorizedError("SSO token missing user id");
  }

  // Best-effort token validation
  try {
    if (decoded.jti) {
      const empcloudDb = getEmpCloudDB();
      const gracePeriod = new Date(Date.now() - 60 * 60 * 1000);
      const tokenRow = await empcloudDb("oauth_access_tokens")
        .where({ jti: decoded.jti })
        .whereNull("revoked_at")
        .where("expires_at", ">", gracePeriod)
        .first();
      if (!tokenRow) {
        throw new UnauthorizedError("Invalid or expired SSO token");
      }
    }
  } catch (err: any) {
    if (err instanceof UnauthorizedError) throw err;
  }

  const user = await findUserById(userId);
  if (!user || user.status !== 1) {
    throw new UnauthorizedError("User not found or inactive");
  }

  const org = await findOrgById(user.organization_id);
  if (!org || !org.is_active) {
    throw new UnauthorizedError("Organization is inactive");
  }

  const payload: AuthPayload = {
    empcloudUserId: user.id,
    empcloudOrgId: user.organization_id,
    fieldProfileId: null,
    role: user.role as AuthPayload["role"],
    email: user.email,
    firstName: user.first_name,
    lastName: user.last_name,
    orgName: org.name,
  };

  const accessToken = signAccessToken(payload);
  const refreshTokenValue = signRefreshToken(user.id);

  logger.info(`SSO login: ${user.email} (org: ${org.name})`);

  return {
    user: payload,
    tokens: { accessToken, refreshToken: refreshTokenValue },
  };
}

export async function refreshToken(token: string): Promise<{ accessToken: string; refreshToken: string }> {
  let decoded: any;
  try {
    decoded = jwt.verify(token, config.jwt.secret);
  } catch {
    throw new UnauthorizedError("Invalid or expired refresh token");
  }

  if (decoded.type !== "refresh") {
    throw new UnauthorizedError("Invalid token type");
  }

  const user = await findUserById(decoded.userId);
  if (!user || user.status !== 1) {
    throw new UnauthorizedError("User not found or inactive");
  }

  const org = await findOrgById(user.organization_id);
  if (!org || !org.is_active) {
    throw new UnauthorizedError("Organization is inactive");
  }

  const payload: AuthPayload = {
    empcloudUserId: user.id,
    empcloudOrgId: user.organization_id,
    fieldProfileId: null,
    role: user.role as AuthPayload["role"],
    email: user.email,
    firstName: user.first_name,
    lastName: user.last_name,
    orgName: org.name,
  };

  const newAccessToken = signAccessToken(payload);
  const newRefreshToken = signRefreshToken(user.id);

  return { accessToken: newAccessToken, refreshToken: newRefreshToken };
}
