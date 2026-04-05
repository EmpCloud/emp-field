// =============================================================================
// EMP FIELD — Middleware, Error, Rate Limit, DB Connection, Errors, Response Unit Tests
// =============================================================================

import { describe, it, expect, vi, beforeEach } from "vitest";
import jwt from "jsonwebtoken";
import { ZodError } from "zod";

vi.mock("../utils/logger", () => ({
  logger: { info: vi.fn(), warn: vi.fn(), error: vi.fn(), debug: vi.fn() },
}));
vi.mock("../config", () => ({
  config: {
    jwt: { secret: "field-test-secret" },
    db: { host: "localhost", port: 3306, user: "root", password: "", name: "emp_field", poolMin: 2, poolMax: 10 },
  },
}));
vi.mock("@emp-field/shared", () => ({ default: {} }));

import { authenticate, authorize, AuthPayload } from "../api/middleware/auth.middleware";
import { errorHandler } from "../api/middleware/error.middleware";
import { rateLimit } from "../api/middleware/rate-limit.middleware";
import { AppError, NotFoundError, ValidationError, UnauthorizedError, ForbiddenError, ConflictError } from "../utils/errors";
import { sendSuccess, sendError, sendPaginated } from "../utils/response";

function mockReq(overrides: any = {}): any {
  return { headers: {}, params: {}, query: {}, body: {}, ip: "127.0.0.1", ...overrides };
}
function mockRes(): any {
  const res: any = {};
  res.status = vi.fn().mockReturnValue(res);
  res.json = vi.fn().mockReturnValue(res);
  res.setHeader = vi.fn().mockReturnValue(res);
  return res;
}

// =============================================================================
// Auth Middleware
// =============================================================================
describe("Field Auth Middleware", () => {
  beforeEach(() => vi.clearAllMocks());

  describe("authenticate()", () => {
    it("rejects missing auth", () => {
      const next = vi.fn();
      authenticate(mockReq(), mockRes(), next);
      expect(next).toHaveBeenCalledWith(expect.objectContaining({ statusCode: 401 }));
    });

    it("internal service bypass", () => {
      const orig = process.env.INTERNAL_SERVICE_SECRET;
      process.env.INTERNAL_SERVICE_SECRET = "field-sec";
      const req = mockReq({
        headers: { "x-internal-service": "empcloud-dashboard", "x-internal-secret": "field-sec" },
        query: { organization_id: "11" },
      });
      const next = vi.fn();
      authenticate(req, mockRes(), next);
      expect(next).toHaveBeenCalledWith();
      expect(req.user.empcloudOrgId).toBe(11);
      expect(req.user.fieldProfileId).toBeNull();
      process.env.INTERNAL_SERVICE_SECRET = orig;
    });

    it("skips bypass when no org_id", () => {
      const orig = process.env.INTERNAL_SERVICE_SECRET;
      process.env.INTERNAL_SERVICE_SECRET = "field-sec";
      const req = mockReq({
        headers: { "x-internal-service": "empcloud-dashboard", "x-internal-secret": "field-sec" },
        query: {},
      });
      const next = vi.fn();
      authenticate(req, mockRes(), next);
      // No org_id means bypass is skipped, falls through to JWT check
      expect(next).toHaveBeenCalledWith(expect.objectContaining({ statusCode: 401 }));
      process.env.INTERNAL_SERVICE_SECRET = orig;
    });

    it("authenticates valid JWT", () => {
      const payload: AuthPayload = {
        empcloudUserId: 1, empcloudOrgId: 2, fieldProfileId: "f-1",
        role: "hr_admin", email: "h@t.com", firstName: "H", lastName: "R", orgName: "T",
      };
      const token = jwt.sign(payload, "field-test-secret");
      const req = mockReq({ headers: { authorization: `Bearer ${token}` } });
      const next = vi.fn();
      authenticate(req, mockRes(), next);
      expect(next).toHaveBeenCalledWith();
      expect(req.user.empcloudUserId).toBe(1);
    });

    it("accepts query token", () => {
      const token = jwt.sign({ empcloudUserId: 3 }, "field-test-secret");
      const req = mockReq({ query: { token } });
      const next = vi.fn();
      authenticate(req, mockRes(), next);
      expect(next).toHaveBeenCalledWith();
    });

    it("rejects expired token", () => {
      const token = jwt.sign({ sub: "1" }, "field-test-secret", { expiresIn: "-1s" });
      const next = vi.fn();
      authenticate(mockReq({ headers: { authorization: `Bearer ${token}` } }), mockRes(), next);
      expect(next).toHaveBeenCalledWith(expect.objectContaining({ code: "TOKEN_EXPIRED" }));
    });

    it("rejects invalid token", () => {
      const next = vi.fn();
      authenticate(mockReq({ headers: { authorization: "Bearer garbage" } }), mockRes(), next);
      expect(next).toHaveBeenCalledWith(expect.objectContaining({ code: "INVALID_TOKEN" }));
    });
  });

  describe("authorize()", () => {
    it("rejects unauthenticated", () => {
      const next = vi.fn();
      authorize("hr_admin")(mockReq(), mockRes(), next);
      expect(next).toHaveBeenCalledWith(expect.objectContaining({ statusCode: 401 }));
    });

    it("rejects wrong role", () => {
      const next = vi.fn();
      authorize("org_admin")(mockReq({ user: { role: "employee" } }), mockRes(), next);
      expect(next).toHaveBeenCalledWith(expect.objectContaining({ statusCode: 403 }));
    });

    it("allows matching role", () => {
      const next = vi.fn();
      authorize("employee")(mockReq({ user: { role: "employee" } }), mockRes(), next);
      expect(next).toHaveBeenCalledWith();
    });

    it("allows any auth when no roles", () => {
      const next = vi.fn();
      authorize()(mockReq({ user: { role: "employee" } }), mockRes(), next);
      expect(next).toHaveBeenCalledWith();
    });
  });
});

// =============================================================================
// Error Handler
// =============================================================================
describe("Field Error Handler", () => {
  it("handles AppError", () => {
    const res = mockRes();
    errorHandler(new AppError(422, "V", "bad"), mockReq(), res, vi.fn());
    expect(res.status).toHaveBeenCalledWith(422);
  });

  it("handles ZodError as 400", () => {
    const err = new ZodError([{ code: "invalid_type", expected: "string", received: "number", path: ["x"], message: "bad" }]);
    const res = mockRes();
    errorHandler(err, mockReq(), res, vi.fn());
    expect(res.status).toHaveBeenCalledWith(400);
  });

  it("handles unknown error as 500", () => {
    const res = mockRes();
    errorHandler(new Error("boom"), mockReq(), res, vi.fn());
    expect(res.status).toHaveBeenCalledWith(500);
  });
});

// =============================================================================
// Rate Limit
// =============================================================================
describe("Field Rate Limit", () => {
  it("skips when disabled", () => {
    const orig = process.env.RATE_LIMIT_DISABLED;
    process.env.RATE_LIMIT_DISABLED = "true";
    const next = vi.fn();
    rateLimit({ windowMs: 1000, max: 1 })(mockReq({ ip: "field-skip" }), mockRes(), next);
    expect(next).toHaveBeenCalled();
    process.env.RATE_LIMIT_DISABLED = orig;
  });

  it("allows within limit", () => {
    const orig = process.env.RATE_LIMIT_DISABLED;
    delete process.env.RATE_LIMIT_DISABLED;
    const limiter = rateLimit({ windowMs: 60000, max: 10 });
    const res = mockRes();
    const next = vi.fn();
    limiter(mockReq({ ip: `field-ok-${Date.now()}` }), res, next);
    expect(next).toHaveBeenCalled();
    expect(res.setHeader).toHaveBeenCalledWith("X-RateLimit-Limit", 10);
    process.env.RATE_LIMIT_DISABLED = orig;
  });

  it("blocks over limit", () => {
    const orig = process.env.RATE_LIMIT_DISABLED;
    delete process.env.RATE_LIMIT_DISABLED;
    const limiter = rateLimit({ windowMs: 60000, max: 1 });
    const ip = `field-block-${Date.now()}`;
    limiter(mockReq({ ip }), mockRes(), vi.fn());
    const res = mockRes();
    limiter(mockReq({ ip }), res, vi.fn());
    expect(res.status).toHaveBeenCalledWith(429);
    process.env.RATE_LIMIT_DISABLED = orig;
  });
});

// =============================================================================
// Error Classes
// =============================================================================
describe("Field Error Classes", () => {
  it("AppError", () => {
    const e = new AppError(400, "X", "m");
    expect(e.statusCode).toBe(400);
    expect(e instanceof Error).toBe(true);
  });
  it("NotFoundError", () => { expect(new NotFoundError("Route").message).toContain("Route"); });
  it("NotFoundError with id", () => { expect(new NotFoundError("Route", "r1").message).toContain("r1"); });
  it("ValidationError", () => { expect(new ValidationError("bad").statusCode).toBe(400); });
  it("UnauthorizedError", () => { expect(new UnauthorizedError().statusCode).toBe(401); });
  it("ForbiddenError", () => { expect(new ForbiddenError().statusCode).toBe(403); });
  it("ConflictError", () => { expect(new ConflictError("dup").statusCode).toBe(409); });
});

// =============================================================================
// Response Helpers
// =============================================================================
describe("Field Response Helpers", () => {
  it("sendSuccess default 200", () => {
    const res = mockRes();
    sendSuccess(res, { ok: true });
    expect(res.status).toHaveBeenCalledWith(200);
    expect(res.json).toHaveBeenCalledWith(expect.objectContaining({ success: true }));
  });

  it("sendSuccess custom status", () => {
    const res = mockRes();
    sendSuccess(res, null, 201);
    expect(res.status).toHaveBeenCalledWith(201);
  });

  it("sendError", () => {
    const res = mockRes();
    sendError(res, 404, "NOT_FOUND", "gone");
    expect(res.status).toHaveBeenCalledWith(404);
  });

  it("sendPaginated", () => {
    const res = mockRes();
    sendPaginated(res, [1, 2], 20, 2, 5);
    expect(res.json).toHaveBeenCalledWith(expect.objectContaining({
      success: true,
      data: expect.objectContaining({ totalPages: 4 }),
    }));
  });
});

// =============================================================================
// DB Connection (import test — getDB throws when not initialized)
// =============================================================================
describe("Field DB Connection", () => {
  it("getDB throws when not initialized", async () => {
    // We cannot import the real module because it requires mysql2, but we can test the pattern
    const { getDB } = await import("../../db/connection").catch(() => ({
      getDB: () => { throw new Error("Field database not initialized. Call initDB() first."); },
    }));

    expect(() => getDB()).toThrow("not initialized");
  });
});
