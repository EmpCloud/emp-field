// ============================================================================
// EMP FIELD — Service Coverage Final Tests
// Targets: geo utilities, analytics, expense, geo-fence, route, work-order, errors
// ============================================================================

process.env.DB_HOST = "localhost";
process.env.DB_PORT = "3306";
process.env.DB_USER = "empcloud";
process.env.DB_PASSWORD = process.env.DB_PASSWORD || "";
process.env.DB_NAME = "emp_field";
process.env.EMPCLOUD_DB_HOST = "localhost";
process.env.EMPCLOUD_DB_PORT = "3306";
process.env.EMPCLOUD_DB_USER = "empcloud";
process.env.EMPCLOUD_DB_PASSWORD = process.env.EMPCLOUD_DB_PASSWORD || "";
process.env.EMPCLOUD_DB_NAME = "empcloud";
process.env.NODE_ENV = "test";
process.env.JWT_SECRET = "test-jwt-secret-cov-final";
process.env.EMPCLOUD_URL = "http://localhost:3000";
process.env.LOG_LEVEL = "error";

import { describe, it, expect, beforeAll, afterAll, vi } from "vitest";
import { initDB, closeDB, getDB } from "../../db/connection";

vi.mock("../../services/notification.service", () => ({
  sendNotification: vi.fn().mockResolvedValue(undefined),
}));

let db: any;

beforeAll(async () => {
  await initDB();
  db = getDB();
}, 30000);

afterAll(async () => {
  await closeDB();
}, 10000);

const ORG = 5;
const U = String(Date.now()).slice(-6);

// ── GEO UTILITIES (pure functions) ───────────────────────────────────────────

describe("Geo utilities", () => {
  let geo: any;

  beforeAll(async () => {
    geo = await import("../../utils/geo");
  });

  it("haversineDistance returns 0 for same point", () => {
    expect(geo.haversineDistance(0, 0, 0, 0)).toBe(0);
  });

  it("haversineDistance NYC to LA approx 3944 km", () => {
    const d = geo.haversineDistance(40.7128, -74.0060, 34.0522, -118.2437);
    expect(d).toBeGreaterThan(3900);
    expect(d).toBeLessThan(4000);
  });

  it("haversineDistanceMeters returns meters", () => {
    const d = geo.haversineDistanceMeters(0, 0, 0, 1);
    expect(d).toBeGreaterThan(100000);
  });

  it("isPointInCircle — inside", () => {
    expect(geo.isPointInCircle(12.9716, 77.5946, 12.9716, 77.5946, 100)).toBe(true);
  });

  it("isPointInCircle — outside", () => {
    expect(geo.isPointInCircle(12.9716, 77.5946, 13.0, 78.0, 100)).toBe(false);
  });

  it("isPointInPolygon — inside triangle", () => {
    const triangle = [
      { lat: 0, lng: 0 },
      { lat: 10, lng: 0 },
      { lat: 5, lng: 10 },
    ];
    expect(geo.isPointInPolygon(3, 3, triangle)).toBe(true);
  });

  it("isPointInPolygon — outside triangle", () => {
    const triangle = [
      { lat: 0, lng: 0 },
      { lat: 10, lng: 0 },
      { lat: 5, lng: 10 },
    ];
    expect(geo.isPointInPolygon(20, 20, triangle)).toBe(false);
  });

  it("isPointInPolygon — inside square", () => {
    const square = [
      { lat: 0, lng: 0 },
      { lat: 0, lng: 10 },
      { lat: 10, lng: 10 },
      { lat: 10, lng: 0 },
    ];
    expect(geo.isPointInPolygon(5, 5, square)).toBe(true);
  });

  it("isPointInPolygon — outside square", () => {
    const square = [
      { lat: 0, lng: 0 },
      { lat: 0, lng: 10 },
      { lat: 10, lng: 10 },
      { lat: 10, lng: 0 },
    ];
    expect(geo.isPointInPolygon(15, 5, square)).toBe(false);
  });
});

// ── ERROR CLASSES ────────────────────────────────────────────────────────────

describe("Field error classes", () => {
  let errors: any;

  beforeAll(async () => {
    errors = await import("../../utils/errors");
  });

  it("NotFoundError with resource and id", () => {
    const err = new errors.NotFoundError("Geo fence", "abc");
    expect(err.statusCode).toBe(404);
    expect(err.message).toContain("abc");
  });

  it("ValidationError", () => {
    const err = new errors.ValidationError("Invalid coords");
    expect(err.statusCode).toBe(400);
  });

  it("ForbiddenError", () => {
    const err = new errors.ForbiddenError();
    expect(err.statusCode).toBe(403);
  });
});

// ── GEO-FENCE SERVICE ────────────────────────────────────────────────────────

describe("Geo-fence service", () => {
  let geoFenceService: any;
  let fenceId: string;

  beforeAll(async () => {
    geoFenceService = await import("../../services/geo-fence.service");
  });

  afterAll(async () => {
    try { if (fenceId) await db("geo_fences").where({ id: fenceId }).del(); } catch {}
  });

  it("create circle geo-fence", async () => {
    const result = await geoFenceService.create(ORG, {
      name: "CovFence-" + U,
      type: "circle",
      center_lat: 12.9716,
      center_lng: 77.5946,
      radius_meters: 500,
    });
    expect(result).toBeDefined();
    expect(result.name).toBe("CovFence-" + U);
    fenceId = result.id;
  });

  it("getById returns fence", async () => {
    const result = await geoFenceService.getById(ORG, fenceId);
    expect(result.name).toContain("CovFence");
  });

  it("getById throws NotFoundError", async () => {
    await expect(geoFenceService.getById(ORG, "nonexistent"))
      .rejects.toThrow();
  });

  it("update geo-fence", async () => {
    const result = await geoFenceService.update(ORG, fenceId, {
      radius_meters: 1000,
    });
    expect(Number(result.radius_meters)).toBe(1000);
  });

  it("list with pagination", async () => {
    const result = await geoFenceService.list(ORG, { page: 1, perPage: 5 });
    expect(result.data.length).toBeGreaterThan(0);
  });

  it("list with active filter", async () => {
    const result = await geoFenceService.list(ORG, { active: true });
    expect(result).toBeDefined();
  });

  it("remove geo-fence", async () => {
    const result = await geoFenceService.remove(ORG, fenceId);
    expect(result.deleted).toBe(true);
    fenceId = "";
  });

  it("remove throws NotFoundError", async () => {
    await expect(geoFenceService.remove(ORG, "nonexistent"))
      .rejects.toThrow();
  });
});

// ── EXPENSE SERVICE — error branches ─────────────────────────────────────────

describe("Expense service — errors", () => {
  let expenseService: any;

  beforeAll(async () => {
    expenseService = await import("../../services/expense.service");
  });

  it("getById throws NotFoundError", async () => {
    await expect(expenseService.getById(ORG, "nonexistent"))
      .rejects.toThrow();
  });

  it("list returns data", async () => {
    const result = await expenseService.list(ORG, {});
    expect(result).toBeDefined();
    expect(Array.isArray(result.data)).toBe(true);
  });
});

// ── ROUTE SERVICE — error branches ───────────────────────────────────────────

describe("Route service — errors", () => {
  let routeService: any;

  beforeAll(async () => {
    routeService = await import("../../services/route.service");
  });

  it("getById throws NotFoundError", async () => {
    await expect(routeService.getById(ORG, "nonexistent"))
      .rejects.toThrow();
  });

  it("list returns data", async () => {
    const result = await routeService.list(ORG, {});
    expect(result).toBeDefined();
    expect(Array.isArray(result.data)).toBe(true);
  });
});

// ── WORK ORDER SERVICE — error branches ──────────────────────────────────────

describe("Work order service — errors", () => {
  let workOrderService: any;

  beforeAll(async () => {
    workOrderService = await import("../../services/work-order.service");
  });

  it("getById throws NotFoundError", async () => {
    await expect(workOrderService.getById(ORG, "nonexistent"))
      .rejects.toThrow();
  });

  it("list returns data", async () => {
    const result = await workOrderService.list(ORG, {});
    expect(result).toBeDefined();
    expect(Array.isArray(result.data)).toBe(true);
  });
});

// ── ANALYTICS SERVICE ────────────────────────────────────────────────────────

describe("Analytics service", () => {
  let analyticsService: any;

  beforeAll(async () => {
    analyticsService = await import("../../services/analytics.service");
  });

  it("getDashboardKPIs returns data", async () => {
    const result = await analyticsService.getDashboardKPIs(ORG);
    expect(result).toBeDefined();
  });

  it("getCoverage returns data", async () => {
    const result = await analyticsService.getCoverage(ORG);
    expect(result).toBeDefined();
  });
});

// ── LOCATION SERVICE ─────────────────────────────────────────────────────────

describe("Location service", () => {
  let locationService: any;

  beforeAll(async () => {
    locationService = await import("../../services/location.service");
  });

  it("getLiveLocations returns data", async () => {
    const result = await locationService.getLiveLocations(ORG);
    expect(Array.isArray(result)).toBe(true);
  });

  it("getTrail returns data", async () => {
    const result = await locationService.getTrail(ORG, 999, {});
    expect(result).toBeDefined();
  });
});
