// =============================================================================
// EMP FIELD -- Real DB Unit Tests (Service-Level Coverage)
// Imports actual service modules to achieve line coverage.
// =============================================================================

import { describe, it, expect, beforeAll, afterAll } from "vitest";
import { v4 as uuidv4 } from "uuid";

// Set env vars BEFORE importing services
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
import * as settingsService from "../../services/settings.service";
import * as clientSiteService from "../../services/client-site.service";
import * as checkinService from "../../services/checkin.service";
import * as expenseService from "../../services/expense.service";
import * as geoFenceService from "../../services/geo-fence.service";
import * as locationService from "../../services/location.service";
import * as mileageService from "../../services/mileage.service";
import * as notificationService from "../../services/notification.service";
import * as routeService from "../../services/route.service";
import * as visitService from "../../services/visit.service";
import * as workOrderService from "../../services/work-order.service";
import * as analyticsService from "../../services/analytics.service";
import { haversineDistance, haversineDistanceMeters, isPointInCircle, isPointInPolygon } from "../../utils/geo";
import { AppError, NotFoundError, ValidationError, UnauthorizedError, ForbiddenError, ConflictError } from "../../utils/errors";
import { sendSuccess, sendError, sendPaginated } from "../../utils/response";

const TS = Date.now();
const TEST_ORG = 77700 + (TS % 1000);
const TEST_USER = 88800 + (TS % 1000);
const TEST_USER2 = 88801 + (TS % 1000);
const cleanupIds: { table: string; where: Record<string, any> }[] = [];

function track(table: string, where: Record<string, any>) {
  cleanupIds.push({ table, where });
}

beforeAll(async () => {
  await initDB();
});

afterAll(async () => {
  const db = getDB();
  for (const item of cleanupIds.reverse()) {
    try { await db(item.table).where(item.where).del(); } catch {}
  }
  try { await db("field_settings").where({ organization_id: TEST_ORG }).del(); } catch {}
  await closeDB();
});

// =========================================================================
// UTILS -- Geo
// =========================================================================
describe("Geo Utilities", () => {
  it("haversineDistance km", () => {
    const d = haversineDistance(28.6139, 77.2090, 19.0760, 72.8777);
    expect(d).toBeGreaterThan(1100);
    expect(d).toBeLessThan(1300);
  });
  it("haversineDistanceMeters", () => {
    const d = haversineDistanceMeters(28.6139, 77.2090, 28.6140, 77.2091);
    expect(d).toBeGreaterThan(0);
    expect(d).toBeLessThan(50);
  });
  it("isPointInCircle true", () => {
    expect(isPointInCircle(28.6139, 77.2090, 28.6139, 77.2090, 100)).toBe(true);
  });
  it("isPointInCircle false", () => {
    expect(isPointInCircle(28.6139, 77.2090, 19.0760, 72.8777, 100)).toBe(false);
  });
  it("isPointInPolygon true", () => {
    const p = [{ lat: 0, lng: 0 }, { lat: 0, lng: 10 }, { lat: 10, lng: 10 }, { lat: 10, lng: 0 }];
    expect(isPointInPolygon(5, 5, p)).toBe(true);
  });
  it("isPointInPolygon false", () => {
    const p = [{ lat: 0, lng: 0 }, { lat: 0, lng: 10 }, { lat: 10, lng: 10 }, { lat: 10, lng: 0 }];
    expect(isPointInPolygon(15, 15, p)).toBe(false);
  });
});

// =========================================================================
// UTILS -- Errors
// =========================================================================
describe("Error classes", () => {
  it("AppError with details", () => {
    const e = new AppError(500, "INTERNAL", "broke", { x: ["y"] });
    expect(e.statusCode).toBe(500);
    expect(e.details).toEqual({ x: ["y"] });
    expect(e.name).toBe("AppError");
  });
  it("NotFoundError with id", () => { expect(new NotFoundError("W", "1").statusCode).toBe(404); });
  it("NotFoundError without id", () => { expect(new NotFoundError("W").statusCode).toBe(404); });
  it("ValidationError with details", () => {
    const e = new ValidationError("bad", { f: ["err"] });
    expect(e.statusCode).toBe(400);
    expect(e.details).toEqual({ f: ["err"] });
  });
  it("UnauthorizedError default", () => { expect(new UnauthorizedError().statusCode).toBe(401); });
  it("UnauthorizedError custom", () => { expect(new UnauthorizedError("no").message).toBe("no"); });
  it("ForbiddenError default", () => { expect(new ForbiddenError().statusCode).toBe(403); });
  it("ForbiddenError custom", () => { expect(new ForbiddenError("nope").message).toBe("nope"); });
  it("ConflictError", () => { expect(new ConflictError("dup").statusCode).toBe(409); });
});

// =========================================================================
// UTILS -- Response helpers
// =========================================================================
describe("Response helpers", () => {
  function mockRes() {
    const r: any = { statusCode: 0, body: null };
    r.status = (c: number) => { r.statusCode = c; return r; };
    r.json = (b: any) => { r.body = b; return r; };
    return r;
  }
  it("sendSuccess 200", () => {
    const res = mockRes(); sendSuccess(res, { x: 1 });
    expect(res.statusCode).toBe(200);
    expect(res.body.success).toBe(true);
  });
  it("sendSuccess custom status", () => {
    const res = mockRes(); sendSuccess(res, {}, 201);
    expect(res.statusCode).toBe(201);
  });
  it("sendError", () => {
    const res = mockRes(); sendError(res, 400, "BAD", "bad");
    expect(res.body.success).toBe(false);
    expect(res.body.error.code).toBe("BAD");
  });
  it("sendPaginated", () => {
    const res = mockRes(); sendPaginated(res, [1, 2], 10, 1, 3);
    expect(res.body.data.totalPages).toBe(4);
  });
});

// =========================================================================
// SETTINGS SERVICE
// =========================================================================
describe("SettingsService", () => {
  it("auto-creates defaults", async () => {
    const s = await settingsService.get(TEST_ORG);
    expect(s.check_in_radius_meters).toBe(200);
  });
  it("returns existing on second call", async () => {
    expect((await settingsService.get(TEST_ORG)).organization_id).toBe(TEST_ORG);
  });
  it("updates settings", async () => {
    const s = await settingsService.update(TEST_ORG, { check_in_radius_meters: 500 });
    expect(s.check_in_radius_meters).toBe(500);
  });
});

// =========================================================================
// CLIENT SITE SERVICE
// =========================================================================
describe("ClientSiteService", () => {
  let siteId: string;
  it("creates", async () => {
    const site = await clientSiteService.create(TEST_ORG, { name: "Site " + TS, address: "123", latitude: 28.61, longitude: 77.21, category: "office", is_active: true });
    siteId = site.id; track("client_sites", { id: siteId });
    expect(site.id).toBeTruthy();
  });
  it("lists with filters", async () => {
    const r = await clientSiteService.list(TEST_ORG, { category: "office", active: true });
    expect(r.data.length).toBeGreaterThanOrEqual(1);
  });
  it("lists default", async () => { expect((await clientSiteService.list(TEST_ORG)).total).toBeGreaterThanOrEqual(1); });
  it("gets by id", async () => { expect((await clientSiteService.getById(TEST_ORG, siteId)).name).toContain("Site"); });
  it("throws missing", async () => { await expect(clientSiteService.getById(TEST_ORG, uuidv4())).rejects.toThrow(); });
  it("updates", async () => { expect((await clientSiteService.update(TEST_ORG, siteId, { name: "Upd " + TS })).name).toContain("Upd"); });
  it("throws update missing", async () => { await expect(clientSiteService.update(TEST_ORG, uuidv4(), { name: "x" })).rejects.toThrow(); });
  it("removes", async () => {
    const s = await clientSiteService.create(TEST_ORG, { name: "del", latitude: 0, longitude: 0, is_active: true });
    expect((await clientSiteService.remove(TEST_ORG, s.id)).deleted).toBe(true);
  });
  it("throws remove missing", async () => { await expect(clientSiteService.remove(TEST_ORG, uuidv4())).rejects.toThrow(); });
});

// =========================================================================
// CHECK-IN SERVICE
// =========================================================================
describe("CheckinService", () => {
  let cid: string;
  it("checks in", async () => {
    const c = await checkinService.checkIn(TEST_ORG, TEST_USER, { check_in_lat: 28.61, check_in_lng: 77.21, notes: "test", photo_url: "http://img" });
    cid = c.id; track("checkins", { id: cid });
    expect(c.status).toBe("active");
  });
  it("prevents double", async () => { await expect(checkinService.checkIn(TEST_ORG, TEST_USER, { check_in_lat: 0, check_in_lng: 0 })).rejects.toThrow(); });
  it("gets active", async () => { expect(await checkinService.getActive(TEST_ORG, TEST_USER)).toBeTruthy(); });
  it("gets today", async () => { expect((await checkinService.getToday(TEST_ORG, TEST_USER)).length).toBeGreaterThanOrEqual(1); });
  it("gets by id", async () => { expect((await checkinService.getById(TEST_ORG, cid)).id).toBe(cid); });
  it("throws getById missing", async () => { await expect(checkinService.getById(TEST_ORG, uuidv4())).rejects.toThrow(); });
  it("lists with all filters", async () => {
    const today = new Date().toISOString().slice(0, 10);
    const r = await checkinService.list(TEST_ORG, { userId: TEST_USER, status: "active", startDate: today, endDate: today, page: 1, perPage: 5 });
    expect(r.data.length).toBeGreaterThanOrEqual(1);
  });
  it("lists default", async () => { expect((await checkinService.list(TEST_ORG)).page).toBe(1); });
  it("checks out", async () => {
    const c = await checkinService.checkOut(TEST_ORG, TEST_USER, cid, { check_out_lat: 28.62, check_out_lng: 77.22, notes: "done" });
    expect(c.status).toBe("completed");
    expect(c.duration_minutes).toBeGreaterThanOrEqual(0);
  });
  it("throws checkout non-active", async () => { await expect(checkinService.checkOut(TEST_ORG, TEST_USER, cid, {})).rejects.toThrow(); });
});

// =========================================================================
// EXPENSE SERVICE
// =========================================================================
describe("ExpenseService", () => {
  let eid: string;
  it("creates", async () => {
    const e = await expenseService.create(TEST_ORG, TEST_USER, { expense_type: "travel", amount: 1500, description: "test", receipt_url: "http://r", currency: "USD" });
    eid = e.id; track("expenses", { id: eid });
    expect(e.status).toBe("draft");
  });
  it("lists all filters", async () => {
    const r = await expenseService.list(TEST_ORG, { userId: TEST_USER, status: "draft", expenseType: "travel" });
    expect(r.data.length).toBeGreaterThanOrEqual(1);
  });
  it("lists default", async () => { expect((await expenseService.list(TEST_ORG)).page).toBe(1); });
  it("gets by id", async () => { expect(Number((await expenseService.getById(TEST_ORG, eid)).amount)).toBe(1500); });
  it("throws missing", async () => { await expect(expenseService.getById(TEST_ORG, uuidv4())).rejects.toThrow(); });
  it("updates draft", async () => { expect(Number((await expenseService.update(TEST_ORG, eid, TEST_USER, { amount: 2000 })).amount)).toBe(2000); });
  it("throws update non-draft", async () => {
    const db = getDB();
    await db("expenses").where({ id: eid }).update({ status: "submitted" });
    await expect(expenseService.update(TEST_ORG, eid, TEST_USER, {})).rejects.toThrow();
    await db("expenses").where({ id: eid }).update({ status: "draft" });
  });
  it("throws update missing", async () => { await expect(expenseService.update(TEST_ORG, uuidv4(), TEST_USER, {})).rejects.toThrow(); });
  it("submits", async () => { expect((await expenseService.submit(TEST_ORG, eid, TEST_USER)).status).toBe("submitted"); });
  it("throws submit non-draft", async () => { await expect(expenseService.submit(TEST_ORG, eid, TEST_USER)).rejects.toThrow(); });
  it("approves", async () => { expect((await expenseService.approve(TEST_ORG, eid, TEST_USER2)).status).toBe("approved"); });
  it("throws approve non-submitted", async () => { await expect(expenseService.approve(TEST_ORG, eid, TEST_USER2)).rejects.toThrow(); });
  it("throws approve missing", async () => { await expect(expenseService.approve(TEST_ORG, uuidv4(), TEST_USER2)).rejects.toThrow(); });
  it("rejects submitted", async () => {
    const e2 = await expenseService.create(TEST_ORG, TEST_USER, { expense_type: "food", amount: 500 });
    track("expenses", { id: e2.id });
    await expenseService.submit(TEST_ORG, e2.id, TEST_USER);
    expect((await expenseService.reject(TEST_ORG, e2.id, TEST_USER2)).status).toBe("rejected");
  });
  it("throws reject non-submitted", async () => {
    const e3 = await expenseService.create(TEST_ORG, TEST_USER, { expense_type: "food", amount: 100 });
    track("expenses", { id: e3.id });
    await expect(expenseService.reject(TEST_ORG, e3.id, TEST_USER2)).rejects.toThrow();
  });
  it("throws reject missing", async () => { await expect(expenseService.reject(TEST_ORG, uuidv4(), TEST_USER2)).rejects.toThrow(); });
  it("removes draft", async () => {
    const e4 = await expenseService.create(TEST_ORG, TEST_USER, { expense_type: "other", amount: 50 });
    expect((await expenseService.remove(TEST_ORG, e4.id, TEST_USER)).deleted).toBe(true);
  });
  it("throws remove non-draft", async () => { await expect(expenseService.remove(TEST_ORG, eid, TEST_USER)).rejects.toThrow(); });
  it("throws remove missing", async () => { await expect(expenseService.remove(TEST_ORG, uuidv4(), TEST_USER)).rejects.toThrow(); });
});

// =========================================================================
// GEO FENCE SERVICE
// =========================================================================
describe("GeoFenceService", () => {
  let fid: string;
  it("creates circle", async () => {
    const f = await geoFenceService.create(TEST_ORG, { name: "C " + TS, type: "circle", center_lat: 28.61, center_lng: 77.21, radius_meters: 200, is_active: true });
    fid = f.id; track("geo_fences", { id: fid });
  });
  it("creates polygon", async () => {
    const f = await geoFenceService.create(TEST_ORG, { name: "P " + TS, type: "polygon", polygon_coords: [{ lat: 0, lng: 0 }, { lat: 0, lng: 10 }, { lat: 10, lng: 10 }] });
    track("geo_fences", { id: f.id });
    expect(f.polygon_coords).toHaveLength(3);
  });
  it("creates without polygon", async () => {
    const f = await geoFenceService.create(TEST_ORG, { name: "N " + TS, type: "circle", client_site_id: null });
    track("geo_fences", { id: f.id });
    expect(f.polygon_coords).toBeNull();
  });
  it("lists active", async () => { expect((await geoFenceService.list(TEST_ORG, { active: true })).data.length).toBeGreaterThanOrEqual(1); });
  it("lists default", async () => { expect((await geoFenceService.list(TEST_ORG)).total).toBeGreaterThanOrEqual(1); });
  it("gets by id", async () => { expect((await geoFenceService.getById(TEST_ORG, fid)).type).toBe("circle"); });
  it("throws missing", async () => { await expect(geoFenceService.getById(TEST_ORG, uuidv4())).rejects.toThrow(); });
  it("updates", async () => { expect((await geoFenceService.update(TEST_ORG, fid, { radius_meters: 300 })).radius_meters).toBe(300); });
  it("updates with polygon", async () => { expect((await geoFenceService.update(TEST_ORG, fid, { polygon_coords: [{ lat: 1, lng: 1 }] })).polygon_coords).toBeTruthy(); });
  it("throws update missing", async () => { await expect(geoFenceService.update(TEST_ORG, uuidv4(), {})).rejects.toThrow(); });
  it("check point in fence", async () => { expect(await geoFenceService.checkPointInFence(TEST_ORG, fid, 28.61, 77.21)).toHaveProperty("inside"); });
  it("records event", async () => {
    const ev = await geoFenceService.recordEvent(TEST_ORG, TEST_USER, { geo_fence_id: fid, event_type: "entry", latitude: 28.61, longitude: 77.21 });
    track("geo_fence_events", { id: ev.id });
  });
  it("gets events with filters", async () => { expect((await geoFenceService.getEvents(TEST_ORG, { userId: TEST_USER, geoFenceId: fid })).data.length).toBeGreaterThanOrEqual(1); });
  it("gets events default", async () => { expect((await geoFenceService.getEvents(TEST_ORG)).page).toBe(1); });
  it("removes", async () => {
    const f2 = await geoFenceService.create(TEST_ORG, { name: "del", type: "circle" });
    expect((await geoFenceService.remove(TEST_ORG, f2.id)).deleted).toBe(true);
  });
  it("throws remove missing", async () => { await expect(geoFenceService.remove(TEST_ORG, uuidv4())).rejects.toThrow(); });
});

// =========================================================================
// LOCATION SERVICE
// =========================================================================
describe("LocationService", () => {
  it("batch uploads", async () => {
    const now = new Date().toISOString();
    const r = await locationService.batchUpload(TEST_ORG, TEST_USER, [
      { latitude: 28.61, longitude: 77.21, accuracy: 10, battery_percent: 80, timestamp: now },
      { latitude: 28.62, longitude: 77.22, timestamp: now },
    ]);
    expect(r.uploaded).toBe(2);
    await getDB()("location_trails").where({ organization_id: TEST_ORG, user_id: TEST_USER }).del();
  });
  it("gets trail with filters", async () => {
    const now = new Date().toISOString();
    await locationService.batchUpload(TEST_ORG, TEST_USER, [{ latitude: 28.61, longitude: 77.21, timestamp: now }]);
    const today = new Date().toISOString().slice(0, 10);
    const trail = await locationService.getTrail(TEST_ORG, TEST_USER, { startDate: today, endDate: today, limit: 10 });
    expect(trail.length).toBeGreaterThanOrEqual(1);
    await getDB()("location_trails").where({ organization_id: TEST_ORG, user_id: TEST_USER }).del();
  });
  it("gets trail default", async () => { expect(Array.isArray(await locationService.getTrail(TEST_ORG, TEST_USER))).toBe(true); });
  it("gets live locations", async () => { expect(Array.isArray(await locationService.getLiveLocations(TEST_ORG))).toBe(true); });
});

// =========================================================================
// MILEAGE SERVICE
// =========================================================================
describe("MileageService", () => {
  it("creates with calculated distance", async () => {
    const m = await mileageService.create(TEST_ORG, TEST_USER, { date: new Date().toISOString().slice(0, 10), start_lat: 28.61, start_lng: 77.21, end_lat: 28.70, end_lng: 77.10 });
    track("mileage_logs", { id: m.id });
    expect(Number(m.distance_km)).toBeGreaterThan(0);
  });
  it("creates with explicit distance_km", async () => {
    const m = await mileageService.create(TEST_ORG, TEST_USER, { date: new Date().toISOString().slice(0, 10), distance_km: 25, start_lat: 28.6, start_lng: 77.2, end_lat: 28.7, end_lng: 77.3 });
    track("mileage_logs", { id: m.id });
    expect(Number(m.distance_km)).toBe(25);
  });
  it("lists with date range", async () => {
    const today = new Date().toISOString().slice(0, 10);
    expect((await mileageService.list(TEST_ORG, TEST_USER, { startDate: today, endDate: today })).data.length).toBeGreaterThanOrEqual(1);
  });
  it("lists default", async () => { expect((await mileageService.list(TEST_ORG, TEST_USER)).page).toBe(1); });
  it("gets summary with range", async () => {
    const today = new Date().toISOString().slice(0, 10);
    expect((await mileageService.getSummary(TEST_ORG, TEST_USER, { startDate: today, endDate: today })).total_trips).toBeGreaterThanOrEqual(1);
  });
  it("gets summary default", async () => { expect((await mileageService.getSummary(TEST_ORG, TEST_USER)).total_trips).toBeGreaterThanOrEqual(0); });
});

// =========================================================================
// NOTIFICATION SERVICE
// =========================================================================
describe("NotificationService", () => {
  let nid: string;
  it("creates full", async () => {
    const n = await notificationService.create(TEST_ORG, { user_id: TEST_USER, title: "N " + TS, body: "b", type: "info", reference_type: "test", reference_id: uuidv4() });
    nid = n.id; track("notifications", { id: nid });
    expect(n.is_read).toBeFalsy();
  });
  it("creates minimal", async () => {
    const n = await notificationService.create(TEST_ORG, { user_id: TEST_USER, title: "min" });
    track("notifications", { id: n.id });
  });
  it("lists unread", async () => { expect((await notificationService.list(TEST_ORG, TEST_USER, { unreadOnly: true })).data.length).toBeGreaterThanOrEqual(1); });
  it("lists default", async () => { expect((await notificationService.list(TEST_ORG, TEST_USER)).page).toBe(1); });
  it("unread count", async () => { expect((await notificationService.getUnreadCount(TEST_ORG, TEST_USER)).count).toBeGreaterThanOrEqual(1); });
  it("marks read", async () => { expect(await notificationService.markRead(TEST_ORG, TEST_USER, nid)).toBeTruthy(); });
  it("marks all read", async () => {
    await notificationService.create(TEST_ORG, { user_id: TEST_USER, title: "n2" }).then(n => track("notifications", { id: n.id }));
    expect((await notificationService.markAllRead(TEST_ORG, TEST_USER)).updated).toBeGreaterThanOrEqual(0);
  });
});

// =========================================================================
// ROUTE SERVICE
// =========================================================================
describe("RouteService", () => {
  let rid: string;
  it("creates with stops", async () => {
    const r = await routeService.create(TEST_ORG, TEST_USER, { date: new Date().toISOString().slice(0, 10), stops: [{ sequence_order: 1, planned_arrival: "2026-04-06 09:00:00" }, { sequence_order: 2 }] });
    rid = r.id; track("daily_routes", { id: rid });
    expect(r.stops.length).toBe(2);
  });
  it("creates empty stops", async () => {
    const r = await routeService.create(TEST_ORG, TEST_USER, { date: new Date().toISOString().slice(0, 10), stops: [] });
    track("daily_routes", { id: r.id });
    expect(r.stops.length).toBe(0);
  });
  it("lists with filters", async () => {
    const today = new Date().toISOString().slice(0, 10);
    expect((await routeService.list(TEST_ORG, { userId: TEST_USER, date: today, status: "planned" })).data.length).toBeGreaterThanOrEqual(1);
  });
  it("lists default", async () => { expect((await routeService.list(TEST_ORG)).page).toBe(1); });
  it("gets by id", async () => { expect((await routeService.getById(TEST_ORG, rid)).stops.length).toBe(2); });
  it("throws missing", async () => { await expect(routeService.getById(TEST_ORG, uuidv4())).rejects.toThrow(); });
  it("updates status", async () => { expect((await routeService.updateStatus(TEST_ORG, rid, "in_progress")).status).toBe("in_progress"); });
  it("throws update status missing", async () => { await expect(routeService.updateStatus(TEST_ORG, uuidv4(), "x")).rejects.toThrow(); });
  it("reorders stops", async () => {
    const r = await routeService.getById(TEST_ORG, rid);
    await routeService.reorderStops(TEST_ORG, rid, [{ id: r.stops[0].id, sequence_order: 2 }, { id: r.stops[1].id, sequence_order: 1 }]);
  });
  it("throws reorder missing", async () => { await expect(routeService.reorderStops(TEST_ORG, uuidv4(), [])).rejects.toThrow(); });
  it("updates a stop", async () => {
    const r = await routeService.getById(TEST_ORG, rid);
    expect((await routeService.updateStop(TEST_ORG, rid, r.stops[0].id, { status: "visited" })).status).toBe("visited");
  });
  it("throws updateStop missing route", async () => { await expect(routeService.updateStop(TEST_ORG, uuidv4(), uuidv4(), {})).rejects.toThrow(); });
  it("throws updateStop missing stop", async () => { await expect(routeService.updateStop(TEST_ORG, rid, uuidv4(), {})).rejects.toThrow(); });
  it("removes", async () => {
    const r2 = await routeService.create(TEST_ORG, TEST_USER, { date: "2099-12-31", stops: [{ sequence_order: 1, planned_arrival: "2099-12-31 08:00:00" }] });
    expect((await routeService.remove(TEST_ORG, r2.id)).deleted).toBe(true);
  });
  it("throws remove missing", async () => { await expect(routeService.remove(TEST_ORG, uuidv4())).rejects.toThrow(); });
});

// =========================================================================
// VISIT SERVICE
// =========================================================================
describe("VisitService", () => {
  let vid: string;
  it("creates full", async () => {
    const site = await clientSiteService.create(TEST_ORG, { name: "VS " + TS, latitude: 28.61, longitude: 77.21, is_active: true });
    track("client_sites", { id: site.id });
    const v = await visitService.create(TEST_ORG, TEST_USER, { client_site_id: site.id, notes: "test", photos: [{ url: "http://img" }], duration_minutes: 30, purpose: "sales", outcome: "ok" });
    vid = v.id; track("visit_logs", { id: vid });
  });
  it("creates minimal", async () => {
    const v = await visitService.create(TEST_ORG, TEST_USER, {});
    track("visit_logs", { id: v.id });
    expect(v.photos).toBeNull();
  });
  it("lists with filters", async () => { expect((await visitService.list(TEST_ORG, { userId: TEST_USER })).data.length).toBeGreaterThanOrEqual(1); });
  it("lists with clientSiteId", async () => { expect((await visitService.list(TEST_ORG, { clientSiteId: uuidv4() })).data.length).toBe(0); });
  it("lists default", async () => { expect((await visitService.list(TEST_ORG)).page).toBe(1); });
  it("gets by id", async () => { expect((await visitService.getById(TEST_ORG, vid)).notes).toBe("test"); });
  it("throws missing", async () => { await expect(visitService.getById(TEST_ORG, uuidv4())).rejects.toThrow(); });
  it("updates with photos", async () => { expect((await visitService.update(TEST_ORG, vid, TEST_USER, { notes: "upd", photos: [{ url: "new" }] })).notes).toBe("upd"); });
  it("updates without photos", async () => { expect((await visitService.update(TEST_ORG, vid, TEST_USER, { notes: "upd2" })).notes).toBe("upd2"); });
  it("throws update missing", async () => { await expect(visitService.update(TEST_ORG, uuidv4(), TEST_USER, {})).rejects.toThrow(); });
  it("removes", async () => {
    const v2 = await visitService.create(TEST_ORG, TEST_USER, {});
    expect((await visitService.remove(TEST_ORG, v2.id, TEST_USER)).deleted).toBe(true);
  });
  it("throws remove missing", async () => { await expect(visitService.remove(TEST_ORG, uuidv4(), TEST_USER)).rejects.toThrow(); });
});

// =========================================================================
// WORK ORDER SERVICE
// =========================================================================
describe("WorkOrderService", () => {
  let oid: string;
  it("creates full", async () => {
    const o = await workOrderService.create(TEST_ORG, TEST_USER, { title: "WO " + TS, description: "test", priority: "high", assigned_to: TEST_USER, client_site_id: null, due_date: "2099-12-31", status: "pending" });
    oid = o.id; track("work_orders", { id: oid });
    expect(o.status).toBe("pending");
  });
  it("creates minimal", async () => {
    const o = await workOrderService.create(TEST_ORG, TEST_USER, { title: "Min " + TS, assigned_to: TEST_USER });
    track("work_orders", { id: o.id });
    expect(o.priority).toBe("medium");
  });
  it("lists all filters", async () => { expect((await workOrderService.list(TEST_ORG, { status: "pending", priority: "high", assignedTo: TEST_USER })).data.length).toBeGreaterThanOrEqual(1); });
  it("lists default", async () => { expect((await workOrderService.list(TEST_ORG)).page).toBe(1); });
  it("gets by id", async () => { expect((await workOrderService.getById(TEST_ORG, oid)).title).toContain("WO"); });
  it("throws missing", async () => { await expect(workOrderService.getById(TEST_ORG, uuidv4())).rejects.toThrow(); });
  it("updates", async () => { expect((await workOrderService.update(TEST_ORG, oid, { description: "upd" })).description).toBe("upd"); });
  it("updates to completed", async () => {
    const o = await workOrderService.update(TEST_ORG, oid, { status: "completed" });
    expect(o.completed_at).toBeTruthy();
  });
  it("throws update missing", async () => { await expect(workOrderService.update(TEST_ORG, uuidv4(), {})).rejects.toThrow(); });
  it("getMyOrders with status", async () => { expect((await workOrderService.getMyOrders(TEST_ORG, TEST_USER, { status: "completed" })).data.length).toBeGreaterThanOrEqual(0); });
  it("getMyOrders default", async () => { expect((await workOrderService.getMyOrders(TEST_ORG, TEST_USER)).page).toBe(1); });
  it("removes", async () => {
    const o2 = await workOrderService.create(TEST_ORG, TEST_USER, { title: "del", assigned_to: TEST_USER });
    expect((await workOrderService.remove(TEST_ORG, o2.id)).deleted).toBe(true);
  });
  it("throws remove missing", async () => { await expect(workOrderService.remove(TEST_ORG, uuidv4())).rejects.toThrow(); });
});

// =========================================================================
// ANALYTICS SERVICE
// =========================================================================
describe("AnalyticsService", () => {
  it("dashboard KPIs", async () => {
    const kpis = await analyticsService.getDashboardKPIs(TEST_ORG);
    expect(kpis).toHaveProperty("active_checkins");
    expect(kpis).toHaveProperty("today_visits");
  });
  it("productivity with range", async () => {
    const today = new Date().toISOString().slice(0, 10);
    expect(Array.isArray(await analyticsService.getProductivity(TEST_ORG, { startDate: today, endDate: today }))).toBe(true);
  });
  it("productivity default", async () => { expect(Array.isArray(await analyticsService.getProductivity(TEST_ORG))).toBe(true); });
  it("coverage", async () => { expect(await analyticsService.getCoverage(TEST_ORG)).toHaveProperty("coverage_percent"); });
});
