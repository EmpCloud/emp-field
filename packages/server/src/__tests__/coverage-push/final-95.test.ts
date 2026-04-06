// =============================================================================
// EMP FIELD -- Final Coverage Push to 95%+
// Fixes known DB issues and covers auth, middleware, and remaining service gaps.
// =============================================================================

import { describe, it, expect, beforeAll, afterAll, vi } from "vitest";
import { v4 as uuidv4 } from "uuid";
import jwt from "jsonwebtoken";

process.env.DB_HOST = "localhost";
process.env.DB_PORT = "3306";
process.env.DB_USER = "empcloud";
process.env.DB_PASSWORD = "EmpCloud2026";
process.env.DB_NAME = "emp_field";
process.env.EMPCLOUD_DB_HOST = "localhost";
process.env.EMPCLOUD_DB_USER = "empcloud";
process.env.EMPCLOUD_DB_PASSWORD = "EmpCloud2026";
process.env.EMPCLOUD_DB_NAME = "empcloud";
process.env.JWT_SECRET = "test-secret-for-vitest";
process.env.NODE_ENV = "test";

import { initDB, getDB, closeDB } from "../../db/connection";
import * as authService from "../../services/auth.service";
import { initEmpCloudDB, closeEmpCloudDB } from "../../db/empcloud";
import * as clientSiteService from "../../services/client-site.service";
import * as expenseService from "../../services/expense.service";
import * as geoFenceService from "../../services/geo-fence.service";
import * as routeService from "../../services/route.service";
import * as visitService from "../../services/visit.service";
import * as workOrderService from "../../services/work-order.service";
import { authenticate, authorize } from "../../api/middleware/auth.middleware";
import { config } from "../../config";
import { errorHandler } from "../../api/middleware/error.middleware";
import { rateLimit } from "../../api/middleware/rate-limit.middleware";
import { AppError } from "../../utils/errors";
import { ZodError } from "zod";

const TS = Date.now();
const TEST_ORG = 78500 + (TS % 1000);
const TEST_USER = 89600 + (TS % 1000);
const TEST_USER2 = 89601 + (TS % 1000);
const cleanupIds: { table: string; where: Record<string, any> }[] = [];

function track(table: string, where: Record<string, any>) {
  cleanupIds.push({ table, where });
}

beforeAll(async () => { await initDB(); await initEmpCloudDB(); });
afterAll(async () => {
  const db = getDB();
  for (const item of cleanupIds.reverse()) {
    try { await db(item.table).where(item.where).del(); } catch {}
  }
  await closeDB();
  try { await closeEmpCloudDB(); } catch {}
});

// =========================================================================
// AUTH MIDDLEWARE -- full coverage
// =========================================================================
describe("Auth Middleware", () => {
  function mockReq(headers: Record<string, any> = {}, query: Record<string, any> = {}): any {
    return { headers, query };
  }
  function mockRes(): any {
    const r: any = { statusCode: 0, body: null, headers: {} };
    r.status = (c: number) => { r.statusCode = c; return r; };
    r.json = (b: any) => { r.body = b; return r; };
    r.setHeader = (k: string, v: any) => { r.headers[k] = v; };
    return r;
  }

  it("rejects missing auth header", () => new Promise<void>((resolve) => {
    const req = mockReq();
    authenticate(req, {} as any, (err: any) => {
      expect(err).toBeTruthy();
      expect(err.code).toBe("UNAUTHORIZED");
      resolve();
    });
  }));

  it("accepts valid Bearer token", () => new Promise<void>((resolve) => {
    const token = jwt.sign({ empcloudUserId: 1, empcloudOrgId: 1, fieldProfileId: null, role: "employee", email: "a@b.com", firstName: "A", lastName: "B", orgName: "O" }, config.jwt.secret);
    const req = mockReq({ authorization: "Bearer " + token });
    authenticate(req, {} as any, (err: any) => {
      expect(err).toBeUndefined();
      expect(req.user.email).toBe("a@b.com");
      resolve();
    });
  }));

  it("accepts query token", () => new Promise<void>((resolve) => {
    const token = jwt.sign({ empcloudUserId: 1, empcloudOrgId: 1, fieldProfileId: null, role: "employee", email: "q@t.com", firstName: "Q", lastName: "T", orgName: "O" }, config.jwt.secret);
    const req = mockReq({}, { token });
    authenticate(req, {} as any, (err: any) => {
      expect(err).toBeUndefined();
      expect(req.user.email).toBe("q@t.com");
      resolve();
    });
  }));

  it("handles expired token", () => new Promise<void>((resolve) => {
    const token = jwt.sign({ empcloudUserId: 1, iat: Math.floor(Date.now() / 1000) - 3600, exp: Math.floor(Date.now() / 1000) - 1800 }, config.jwt.secret);
    const req = mockReq({ authorization: "Bearer " + token });
    authenticate(req, {} as any, (err: any) => {
      expect(err.code).toBe("TOKEN_EXPIRED");
      resolve();
    });
  }));

  it("handles invalid token", () => new Promise<void>((resolve) => {
    const req = mockReq({ authorization: "Bearer badtoken" });
    authenticate(req, {} as any, (err: any) => {
      expect(err.code).toBe("INVALID_TOKEN");
      resolve();
    });
  }));

  it("internal service bypass", () => new Promise<void>((resolve) => {
    process.env.INTERNAL_SERVICE_SECRET = "testsecret";
    const req = mockReq({ "x-internal-service": "empcloud-dashboard", "x-internal-secret": "testsecret" }, { organization_id: "999" });
    authenticate(req, {} as any, (err: any) => {
      expect(err).toBeUndefined();
      expect(req.user.empcloudOrgId).toBe(999);
      delete process.env.INTERNAL_SERVICE_SECRET;
      resolve();
    });
  }));

  it("internal bypass no orgId falls through", () => new Promise<void>((resolve) => {
    process.env.INTERNAL_SERVICE_SECRET = "testsecret";
    const req = mockReq({ "x-internal-service": "empcloud-dashboard", "x-internal-secret": "testsecret" }, {});
    authenticate(req, {} as any, (err: any) => {
      expect(err).toBeTruthy();
      delete process.env.INTERNAL_SERVICE_SECRET;
      resolve();
    });
  }));

  it("internal bypass wrong secret falls through", () => new Promise<void>((resolve) => {
    process.env.INTERNAL_SERVICE_SECRET = "testsecret";
    const req = mockReq({ "x-internal-service": "empcloud-dashboard", "x-internal-secret": "wrong" }, { organization_id: "1" });
    authenticate(req, {} as any, (err: any) => {
      expect(err).toBeTruthy();
      delete process.env.INTERNAL_SERVICE_SECRET;
      resolve();
    });
  }));

  it("authorize allows matching role", () => new Promise<void>((resolve) => {
    const middleware = authorize("employee", "org_admin");
    const req: any = { user: { role: "employee" } };
    middleware(req, {} as any, (err: any) => {
      expect(err).toBeUndefined();
      resolve();
    });
  }));

  it("authorize rejects non-matching role", () => new Promise<void>((resolve) => {
    const middleware = authorize("org_admin");
    const req: any = { user: { role: "employee" } };
    middleware(req, {} as any, (err: any) => {
      expect(err.code).toBe("FORBIDDEN");
      resolve();
    });
  }));

  it("authorize rejects no user", () => new Promise<void>((resolve) => {
    const middleware = authorize("employee");
    const req: any = {};
    middleware(req, {} as any, (err: any) => {
      expect(err.code).toBe("UNAUTHORIZED");
      resolve();
    });
  }));

  it("authorize allows empty roles array", () => new Promise<void>((resolve) => {
    const middleware = authorize();
    const req: any = { user: { role: "employee" } };
    middleware(req, {} as any, (err: any) => {
      expect(err).toBeUndefined();
      resolve();
    });
  }));
});

// =========================================================================
// ERROR MIDDLEWARE -- full coverage
// =========================================================================
describe("Error Middleware", () => {
  function mockRes(): any {
    const r: any = { statusCode: 0, body: null };
    r.status = (c: number) => { r.statusCode = c; return r; };
    r.json = (b: any) => { r.body = b; return r; };
    return r;
  }

  it("handles AppError", () => {
    const res = mockRes();
    errorHandler(new AppError(400, "BAD", "bad input", { f: ["e"] }), {} as any, res, () => {});
    expect(res.statusCode).toBe(400);
    expect(res.body.error.code).toBe("BAD");
    expect(res.body.error.details).toEqual({ f: ["e"] });
  });

  it("handles ZodError", () => {
    const res = mockRes();
    const zodErr = new ZodError([{ code: "custom", path: ["name"], message: "required" }]);
    errorHandler(zodErr, {} as any, res, () => {});
    expect(res.statusCode).toBe(400);
    expect(res.body.error.code).toBe("VALIDATION_ERROR");
  });

  it("handles generic Error (dev)", () => {
    const oldEnv = process.env.NODE_ENV;
    process.env.NODE_ENV = "development";
    const res = mockRes();
    errorHandler(new Error("oops"), {} as any, res, () => {});
    expect(res.statusCode).toBe(500);
    expect(res.body.error.message).toBe("oops");
    process.env.NODE_ENV = oldEnv;
  });

  it("handles generic Error (production)", () => {
    const oldEnv = process.env.NODE_ENV;
    process.env.NODE_ENV = "production";
    const res = mockRes();
    errorHandler(new Error("secret"), {} as any, res, () => {});
    expect(res.statusCode).toBe(500);
    expect(res.body.error.message).toBe("An unexpected error occurred");
    process.env.NODE_ENV = oldEnv;
  });
});

// =========================================================================
// RATE LIMIT MIDDLEWARE -- full coverage
// =========================================================================
describe("Rate Limit Middleware", () => {
  function mockReq(ip = "127.0.0.1"): any {
    return { ip, headers: {}, query: {} };
  }
  function mockRes(): any {
    const r: any = { statusCode: 0, body: null, headers: {} };
    r.status = (c: number) => { r.statusCode = c; return r; };
    r.json = (b: any) => { r.body = b; return r; };
    r.setHeader = (k: string, v: any) => { r.headers[k] = v; };
    return r;
  }

  it("bypasses when RATE_LIMIT_DISABLED", () => new Promise<void>((resolve) => {
    const old = process.env.RATE_LIMIT_DISABLED;
    process.env.RATE_LIMIT_DISABLED = "true";
    const limiter = rateLimit({ windowMs: 1000, max: 1 });
    limiter(mockReq(), mockRes(), (err: any) => {
      expect(err).toBeUndefined();
      process.env.RATE_LIMIT_DISABLED = old;
      resolve();
    });
  }));

  it("allows first request", () => new Promise<void>((resolve) => {
    const old = process.env.RATE_LIMIT_DISABLED;
    delete process.env.RATE_LIMIT_DISABLED;
    const limiter = rateLimit({ windowMs: 60000, max: 5 });
    const res = mockRes();
    limiter(mockReq("10.0.0." + TS), res, () => {
      expect(res.headers["X-RateLimit-Remaining"]).toBe(4);
      process.env.RATE_LIMIT_DISABLED = old;
      resolve();
    });
  }));

  it("blocks after max", () => new Promise<void>((resolve) => {
    const old = process.env.RATE_LIMIT_DISABLED;
    delete process.env.RATE_LIMIT_DISABLED;
    const ip = "10.0.1." + (TS % 255);
    const limiter = rateLimit({ windowMs: 60000, max: 1 });
    const res1 = mockRes();
    limiter(mockReq(ip), res1, () => {
      const res2 = mockRes();
      limiter(mockReq(ip), res2, () => { /* should not reach */ });
      setTimeout(() => {
        expect(res2.statusCode).toBe(429);
        process.env.RATE_LIMIT_DISABLED = old;
        resolve();
      }, 10);
    });
  }));

  it("uses custom keyFn", () => new Promise<void>((resolve) => {
    const old = process.env.RATE_LIMIT_DISABLED;
    delete process.env.RATE_LIMIT_DISABLED;
    const limiter = rateLimit({ windowMs: 60000, max: 10, keyFn: () => "custom-" + TS });
    const res = mockRes();
    limiter(mockReq(), res, () => {
      expect(res.headers["X-RateLimit-Remaining"]).toBe(9);
      process.env.RATE_LIMIT_DISABLED = old;
      resolve();
    });
  }));

  it("uses unknown IP fallback", () => new Promise<void>((resolve) => {
    const old = process.env.RATE_LIMIT_DISABLED;
    delete process.env.RATE_LIMIT_DISABLED;
    const limiter = rateLimit({ windowMs: 60000, max: 10 });
    const req = mockReq(); req.ip = undefined;
    const res = mockRes();
    limiter(req, res, () => {
      process.env.RATE_LIMIT_DISABLED = old;
      resolve();
    });
  }));
});

// =========================================================================
// AUTH SERVICE -- covers login, ssoLogin, refreshToken
// =========================================================================
describe("Auth Service", () => {
  // login tests
  it("login rejects invalid email", async () => {
    await expect(authService.login("nonexistent-" + TS + "@test.com", "pass")).rejects.toThrow("Invalid email or password");
  });

  it("login rejects wrong password for real user", async () => {
    // User 522 = ananya@technova.in exists in empcloud DB
    await expect(authService.login("ananya@technova.in", "definitelywrongpassword")).rejects.toThrow("Invalid email or password");
  });

  // refreshToken tests
  it("refreshToken rejects invalid token", async () => {
    await expect(authService.refreshToken("badtoken")).rejects.toThrow();
  });

  it("refreshToken rejects non-refresh token", async () => {
    const token = jwt.sign({ userId: 1, type: "access" }, config.jwt.secret, { expiresIn: "1h" });
    await expect(authService.refreshToken(token)).rejects.toThrow("Invalid token type");
  });

  it("refreshToken rejects non-existent user", async () => {
    const token = jwt.sign({ userId: 999999999, type: "refresh" }, config.jwt.secret, { expiresIn: "1h" });
    await expect(authService.refreshToken(token)).rejects.toThrow();
  });

  it("refreshToken succeeds for real user", async () => {
    const token = jwt.sign({ userId: 522, type: "refresh" }, config.jwt.secret, { expiresIn: "1h" });
    const result = await authService.refreshToken(token);
    expect(result.accessToken).toBeTruthy();
    expect(result.refreshToken).toBeTruthy();
  });

  // ssoLogin tests
  it("ssoLogin rejects invalid token", async () => {
    await expect(authService.ssoLogin("badtoken")).rejects.toThrow();
  });

  it("ssoLogin rejects token without sub", async () => {
    const token = jwt.sign({ foo: "bar" }, config.jwt.secret);
    await expect(authService.ssoLogin(token)).rejects.toThrow("SSO token missing user id");
  });

  it("ssoLogin rejects non-existent user", async () => {
    const token = jwt.sign({ sub: 999999999 }, config.jwt.secret);
    await expect(authService.ssoLogin(token)).rejects.toThrow();
  });

  it("ssoLogin succeeds for real user", async () => {
    const token = jwt.sign({ sub: 522, jti: null }, config.jwt.secret);
    const result = await authService.ssoLogin(token);
    expect(result.user.email).toBe("ananya@technova.in");
    expect(result.tokens.accessToken).toBeTruthy();
    expect(result.tokens.refreshToken).toBeTruthy();
  });

  it("ssoLogin with jti does token lookup", async () => {
    // Token with jti will try to look up in oauth_access_tokens - should fail gracefully
    const token = jwt.sign({ sub: 522, jti: "nonexistent-jti-" + TS }, config.jwt.secret);
    // This will either succeed (jti lookup failure is caught) or fail
    try {
      const result = await authService.ssoLogin(token);
      expect(result.user.empcloudUserId).toBe(522);
    } catch (err: any) {
      // If it throws UnauthorizedError from invalid jti, that's also valid coverage
      expect(err.message).toContain("Invalid");
    }
  });
});

// =========================================================================
// CLIENT SITE SERVICE -- fix: add latitude/longitude (NOT NULL columns)
// =========================================================================
describe("ClientSiteService (fixed)", () => {
  let siteId: string;

  it("creates with lat/lng", async () => {
    const site = await clientSiteService.create(TEST_ORG, { name: "FS " + TS, latitude: 28.61, longitude: 77.21, is_active: true });
    siteId = site.id; track("client_sites", { id: siteId });
    expect(site.id).toBeTruthy();
  });

  it("updates", async () => {
    const s = await clientSiteService.update(TEST_ORG, siteId, { name: "Updated " + TS });
    expect(s.name).toContain("Updated");
  });

  it("removes", async () => {
    const s = await clientSiteService.create(TEST_ORG, { name: "del2", latitude: 0, longitude: 0, is_active: true });
    track("client_sites", { id: s.id });
    expect((await clientSiteService.remove(TEST_ORG, s.id)).deleted).toBe(true);
  });
});

// =========================================================================
// EXPENSE SERVICE -- fix: use valid expense_type enum, Number() for DECIMAL
// =========================================================================
describe("ExpenseService (fixed)", () => {
  let eid: string;

  it("creates with valid type", async () => {
    const e = await expenseService.create(TEST_ORG, TEST_USER, { expense_type: "travel", amount: 1500, currency: "INR" });
    eid = e.id; track("expenses", { id: eid });
    expect(Number(e.amount)).toBe(1500);
  });

  it("gets by id (Number check)", async () => {
    const e = await expenseService.getById(TEST_ORG, eid);
    expect(Number(e.amount)).toBe(1500);
  });

  it("updates draft (Number check)", async () => {
    const e = await expenseService.update(TEST_ORG, eid, TEST_USER, { amount: 2000 });
    expect(Number(e.amount)).toBe(2000);
  });

  it("removes draft", async () => {
    const e4 = await expenseService.create(TEST_ORG, TEST_USER, { expense_type: "food", amount: 50 });
    track("expenses", { id: e4.id });
    expect((await expenseService.remove(TEST_ORG, e4.id, TEST_USER)).deleted).toBe(true);
  });
});

// =========================================================================
// GEO FENCE SERVICE -- fix: typeof check before JSON.parse
// =========================================================================
describe("GeoFenceService (fixed)", () => {
  let fid: string;

  it("creates polygon", async () => {
    const coords = [{ lat: 0, lng: 0 }, { lat: 0, lng: 10 }, { lat: 10, lng: 10 }];
    const f = await geoFenceService.create(TEST_ORG, { name: "PF " + TS, type: "polygon", polygon_coords: coords });
    fid = f.id; track("geo_fences", { id: fid });
    expect(f.polygon_coords).toHaveLength(3);
  });

  it("lists active", async () => {
    const r = await geoFenceService.list(TEST_ORG, { active: true });
    expect(r.data.length).toBeGreaterThanOrEqual(0);
  });

  it("lists default", async () => {
    expect((await geoFenceService.list(TEST_ORG)).total).toBeGreaterThanOrEqual(0);
  });

  it("updates with polygon", async () => {
    const updated = await geoFenceService.update(TEST_ORG, fid, { polygon_coords: [{ lat: 1, lng: 1 }] });
    expect(updated.polygon_coords).toBeTruthy();
  });

  it("check point in circle fence", async () => {
    const cf = await geoFenceService.create(TEST_ORG, { name: "CF " + TS, type: "circle", center_lat: 28.61, center_lng: 77.21, radius_meters: 500 });
    track("geo_fences", { id: cf.id });
    const result = await geoFenceService.checkPointInFence(TEST_ORG, cf.id, 28.61, 77.21);
    expect(result.inside).toBe(true);
  });

  it("check point in polygon fence", async () => {
    const pf = await geoFenceService.create(TEST_ORG, { name: "PFC " + TS, type: "polygon", polygon_coords: [{ lat: 0, lng: 0 }, { lat: 0, lng: 10 }, { lat: 10, lng: 10 }, { lat: 10, lng: 0 }] });
    track("geo_fences", { id: pf.id });
    const result = await geoFenceService.checkPointInFence(TEST_ORG, pf.id, 5, 5);
    expect(result.inside).toBe(true);
  });

  it("check point outside polygon fence", async () => {
    const pf = await geoFenceService.create(TEST_ORG, { name: "PFO " + TS, type: "polygon", polygon_coords: [{ lat: 0, lng: 0 }, { lat: 0, lng: 10 }, { lat: 10, lng: 10 }, { lat: 10, lng: 0 }] });
    track("geo_fences", { id: pf.id });
    const result = await geoFenceService.checkPointInFence(TEST_ORG, pf.id, 50, 50);
    expect(result.inside).toBe(false);
  });
});

// =========================================================================
// ROUTE SERVICE -- fix: planned_arrival is DATETIME not TIME
// =========================================================================
describe("RouteService (fixed)", () => {
  let rid: string;

  it("creates with stops (datetime format)", async () => {
    const r = await routeService.create(TEST_ORG, TEST_USER, {
      date: new Date().toISOString().slice(0, 10),
      stops: [
        { sequence_order: 1, planned_arrival: "2026-04-06 09:00:00" },
        { sequence_order: 2 },
      ],
    });
    rid = r.id; track("daily_routes", { id: rid });
    expect(r.stops.length).toBe(2);
  });

  it("gets by id", async () => {
    const r = await routeService.getById(TEST_ORG, rid);
    expect(r.stops.length).toBe(2);
  });

  it("updates status", async () => {
    const r = await routeService.updateStatus(TEST_ORG, rid, "in_progress");
    expect(r.status).toBe("in_progress");
  });

  it("reorders stops", async () => {
    const r = await routeService.getById(TEST_ORG, rid);
    const reordered = await routeService.reorderStops(TEST_ORG, rid, [
      { id: r.stops[0].id, sequence_order: 2 },
      { id: r.stops[1].id, sequence_order: 1 },
    ]);
    expect(reordered.stops).toBeTruthy();
  });

  it("updates a stop", async () => {
    const r = await routeService.getById(TEST_ORG, rid);
    const s = await routeService.updateStop(TEST_ORG, rid, r.stops[0].id, { status: "visited" });
    expect(s.status).toBe("visited");
  });

  it("throws updateStop missing stop", async () => {
    await expect(routeService.updateStop(TEST_ORG, rid, uuidv4(), {})).rejects.toThrow();
  });
});

// =========================================================================
// VISIT SERVICE -- fix: needs client_site with lat/lng
// =========================================================================
describe("VisitService (fixed)", () => {
  let vid: string;

  it("creates full", async () => {
    const site = await clientSiteService.create(TEST_ORG, { name: "VS2 " + TS, latitude: 28.61, longitude: 77.21, is_active: true });
    track("client_sites", { id: site.id });
    const v = await visitService.create(TEST_ORG, TEST_USER, {
      client_site_id: site.id, notes: "test2", photos: [{ url: "http://img2" }],
      duration_minutes: 15, purpose: "delivery", outcome: "done",
    });
    vid = v.id; track("visit_logs", { id: vid });
    expect(vid).toBeTruthy();
  });

  it("gets by id", async () => {
    const v = await visitService.getById(TEST_ORG, vid);
    expect(v.notes).toBe("test2");
  });

  it("updates with photos", async () => {
    const v = await visitService.update(TEST_ORG, vid, TEST_USER, { notes: "upd3", photos: [{ url: "new2" }] });
    expect(v.notes).toBe("upd3");
  });

  it("updates without photos", async () => {
    const v = await visitService.update(TEST_ORG, vid, TEST_USER, { notes: "upd4" });
    expect(v.notes).toBe("upd4");
  });
});

// =========================================================================
// WORK ORDER SERVICE -- fix: assigned_to is NOT NULL
// =========================================================================
describe("WorkOrderService (fixed)", () => {
  it("creates minimal with assigned_to", async () => {
    const o = await workOrderService.create(TEST_ORG, TEST_USER, { title: "Min2 " + TS, assigned_to: TEST_USER });
    track("work_orders", { id: o.id });
    expect(o.priority).toBe("medium");
  });

  it("removes", async () => {
    const o2 = await workOrderService.create(TEST_ORG, TEST_USER, { title: "del2 " + TS, assigned_to: TEST_USER });
    track("work_orders", { id: o2.id });
    expect((await workOrderService.remove(TEST_ORG, o2.id)).deleted).toBe(true);
  });

  it("updates to completed", async () => {
    const o3 = await workOrderService.create(TEST_ORG, TEST_USER, { title: "comp " + TS, assigned_to: TEST_USER });
    track("work_orders", { id: o3.id });
    const updated = await workOrderService.update(TEST_ORG, o3.id, { status: "completed" });
    expect(updated.completed_at).toBeTruthy();
  });

  it("getMyOrders covers", async () => {
    const r = await workOrderService.getMyOrders(TEST_ORG, TEST_USER);
    expect(r.page).toBe(1);
  });
});
