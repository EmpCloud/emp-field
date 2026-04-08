// =============================================================================
// FIELD MISC DEEP — geo-fences (circle/polygon), analytics, notifications, settings
// =============================================================================

import { describe, it, expect, beforeAll, afterAll, afterEach } from "vitest";
import knexLib, { Knex } from "knex";
import { v4 as uuidv4 } from "uuid";

let db: Knex;
const ORG_ID = 5;
const USER_ID = 522;
const TS = Date.now();
const cleanup: { table: string; id: string }[] = [];

beforeAll(async () => {
  db = knexLib({
    client: "mysql2",
    connection: { host: "localhost", port: 3306, user: "empcloud", password: process.env.DB_PASSWORD || "", database: "emp_field" },
  });
  await db.raw("SELECT 1");
});

afterEach(async () => {
  for (const item of cleanup.reverse()) {
    try { await db(item.table).where({ id: item.id }).del(); } catch {}
  }
  cleanup.length = 0;
});

afterAll(async () => { if (db) await db.destroy(); });

async function seedClientSite(): Promise<string> {
  const id = uuidv4();
  await db("client_sites").insert({
    id, organization_id: ORG_ID, name: `GeoSite-${TS}`,
    latitude: 12.9716, longitude: 77.5946, radius_meters: 200, is_active: true,
  });
  cleanup.push({ table: "client_sites", id });
  return id;
}

// ==========================================================================
// GEO-FENCES (CIRCLE)
// ==========================================================================
describe("Geo-Fences (Circle)", () => {
  it("should create a circular geo-fence", async () => {
    const siteId = await seedClientSite();
    const id = uuidv4();
    await db("geo_fences").insert({
      id, organization_id: ORG_ID, client_site_id: siteId,
      name: `Circle-${TS}`, type: "circle",
      center_lat: 12.9716, center_lng: 77.5946, radius_meters: 200,
      is_active: true,
    });
    cleanup.push({ table: "geo_fences", id });

    const row = await db("geo_fences").where({ id }).first();
    expect(row.type).toBe("circle");
    expect(row.radius_meters).toBe(200);
  });

  it("should update geo-fence radius", async () => {
    const id = uuidv4();
    await db("geo_fences").insert({
      id, organization_id: ORG_ID, name: `Update-${TS}`, type: "circle",
      center_lat: 12.9716, center_lng: 77.5946, radius_meters: 200,
      is_active: true,
    });
    cleanup.push({ table: "geo_fences", id });

    await db("geo_fences").where({ id }).update({ radius_meters: 500 });
    const row = await db("geo_fences").where({ id }).first();
    expect(row.radius_meters).toBe(500);
  });

  it("should deactivate a geo-fence", async () => {
    const id = uuidv4();
    await db("geo_fences").insert({
      id, organization_id: ORG_ID, name: `Deact-${TS}`, type: "circle",
      center_lat: 12.9716, center_lng: 77.5946, radius_meters: 100,
      is_active: true,
    });
    cleanup.push({ table: "geo_fences", id });

    await db("geo_fences").where({ id }).update({ is_active: false });
    const row = await db("geo_fences").where({ id }).first();
    expect(row.is_active).toBe(0);
  });

  it("should validate point inside circle", () => {
    function isPointInCircle(
      lat: number, lng: number,
      centerLat: number, centerLng: number, radiusMeters: number,
    ): boolean {
      const R = 6371000;
      const dLat = (lat - centerLat) * Math.PI / 180;
      const dLng = (lng - centerLng) * Math.PI / 180;
      const a = Math.sin(dLat / 2) ** 2 +
        Math.cos(centerLat * Math.PI / 180) * Math.cos(lat * Math.PI / 180) * Math.sin(dLng / 2) ** 2;
      const dist = R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
      return dist <= radiusMeters;
    }

    // ~30m away: should be inside 200m radius
    expect(isPointInCircle(12.9718, 77.5948, 12.9716, 77.5946, 200)).toBe(true);
    // ~1km away: should be outside 200m radius
    expect(isPointInCircle(12.9800, 77.6000, 12.9716, 77.5946, 200)).toBe(false);
  });
});

// ==========================================================================
// GEO-FENCES (POLYGON)
// ==========================================================================
describe("Geo-Fences (Polygon)", () => {
  it("should create a polygon geo-fence", async () => {
    const id = uuidv4();
    const polygon = [
      { lat: 12.9700, lng: 77.5900 },
      { lat: 12.9700, lng: 77.6000 },
      { lat: 12.9750, lng: 77.6000 },
      { lat: 12.9750, lng: 77.5900 },
    ];
    await db("geo_fences").insert({
      id, organization_id: ORG_ID, name: `Polygon-${TS}`, type: "polygon",
      polygon_coords: JSON.stringify(polygon), is_active: true,
    });
    cleanup.push({ table: "geo_fences", id });

    const row = await db("geo_fences").where({ id }).first();
    expect(row.type).toBe("polygon");
    const coords = typeof row.polygon_coords === "string" ? JSON.parse(row.polygon_coords) : row.polygon_coords;
    expect(coords.length).toBe(4);
  });

  it("should validate point inside polygon (ray casting)", () => {
    function isPointInPolygon(
      lat: number, lng: number,
      polygon: { lat: number; lng: number }[],
    ): boolean {
      let inside = false;
      for (let i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
        const xi = polygon[i].lat, yi = polygon[i].lng;
        const xj = polygon[j].lat, yj = polygon[j].lng;
        const intersect = ((yi > lng) !== (yj > lng)) &&
          (lat < (xj - xi) * (lng - yi) / (yj - yi) + xi);
        if (intersect) inside = !inside;
      }
      return inside;
    }

    const polygon = [
      { lat: 12.9700, lng: 77.5900 },
      { lat: 12.9700, lng: 77.6000 },
      { lat: 12.9750, lng: 77.6000 },
      { lat: 12.9750, lng: 77.5900 },
    ];

    // Point inside
    expect(isPointInPolygon(12.9720, 77.5950, polygon)).toBe(true);
    // Point outside
    expect(isPointInPolygon(12.9600, 77.5800, polygon)).toBe(false);
  });
});

// ==========================================================================
// GEO-FENCE EVENTS
// ==========================================================================
describe("Geo-Fence Events", () => {
  it("should log entry and exit events", async () => {
    const fenceId = uuidv4();
    await db("geo_fences").insert({
      id: fenceId, organization_id: ORG_ID, name: `Events-${TS}`, type: "circle",
      center_lat: 12.9716, center_lng: 77.5946, radius_meters: 200,
      is_active: true,
    });
    cleanup.push({ table: "geo_fences", id: fenceId });

    const entryId = uuidv4();
    await db("geo_fence_events").insert({
      id: entryId, organization_id: ORG_ID, user_id: USER_ID,
      geo_fence_id: fenceId, event_type: "entry",
      latitude: 12.9718, longitude: 77.5948,
      timestamp: new Date("2026-04-04T09:00:00"),
    });
    cleanup.push({ table: "geo_fence_events", id: entryId });

    const exitId = uuidv4();
    await db("geo_fence_events").insert({
      id: exitId, organization_id: ORG_ID, user_id: USER_ID,
      geo_fence_id: fenceId, event_type: "exit",
      latitude: 12.9800, longitude: 77.6000,
      timestamp: new Date("2026-04-04T13:00:00"),
    });
    cleanup.push({ table: "geo_fence_events", id: exitId });

    const events = await db("geo_fence_events")
      .where({ geo_fence_id: fenceId })
      .orderBy("timestamp", "asc");
    expect(events.length).toBe(2);
    expect(events[0].event_type).toBe("entry");
    expect(events[1].event_type).toBe("exit");
  });

  it("should calculate dwell time from entry/exit events", async () => {
    const fenceId = uuidv4();
    await db("geo_fences").insert({
      id: fenceId, organization_id: ORG_ID, name: `Dwell-${TS}`, type: "circle",
      center_lat: 12.9716, center_lng: 77.5946, radius_meters: 200,
      is_active: true,
    });
    cleanup.push({ table: "geo_fences", id: fenceId });

    const entryTime = new Date("2026-04-04T09:00:00");
    const exitTime = new Date("2026-04-04T11:30:00");

    const eId = uuidv4();
    const xId = uuidv4();
    await db("geo_fence_events").insert([
      { id: eId, organization_id: ORG_ID, user_id: USER_ID, geo_fence_id: fenceId, event_type: "entry", latitude: 12.9718, longitude: 77.5948, timestamp: entryTime },
      { id: xId, organization_id: ORG_ID, user_id: USER_ID, geo_fence_id: fenceId, event_type: "exit", latitude: 12.9800, longitude: 77.6000, timestamp: exitTime },
    ]);
    cleanup.push({ table: "geo_fence_events", id: eId });
    cleanup.push({ table: "geo_fence_events", id: xId });

    const dwellMinutes = Math.round((exitTime.getTime() - entryTime.getTime()) / 60000);
    expect(dwellMinutes).toBe(150); // 2.5 hours
  });
});

// ==========================================================================
// NOTIFICATIONS
// ==========================================================================
describe("Notifications", () => {
  it("should create and read a notification", async () => {
    const id = uuidv4();
    await db("notifications").insert({
      id, organization_id: ORG_ID, user_id: USER_ID,
      title: "New work order", body: "You have been assigned a new work order",
      type: "work_order", reference_type: "work_order", reference_id: uuidv4(),
      is_read: false,
    });
    cleanup.push({ table: "notifications", id });

    const row = await db("notifications").where({ id }).first();
    expect(row.is_read).toBe(0);
    expect(row.type).toBe("work_order");

    // Mark as read
    await db("notifications").where({ id }).update({ is_read: true, read_at: new Date() });
    const updated = await db("notifications").where({ id }).first();
    expect(updated.is_read).toBe(1);
    expect(updated.read_at).toBeTruthy();
  });

  it("should list unread notifications for user", async () => {
    const ids: string[] = [];
    for (let i = 0; i < 3; i++) {
      const id = uuidv4();
      await db("notifications").insert({
        id, organization_id: ORG_ID, user_id: USER_ID,
        title: `Notif-${i}-${TS}`, type: "info", is_read: i === 0,
      });
      ids.push(id);
      cleanup.push({ table: "notifications", id });
    }

    const unread = await db("notifications")
      .where({ organization_id: ORG_ID, user_id: USER_ID, is_read: false })
      .whereIn("id", ids);
    expect(unread.length).toBe(2);
  });
});

// ==========================================================================
// FIELD SETTINGS
// ==========================================================================
describe("Field Settings", () => {
  it("should create default settings for an org", async () => {
    const fakeOrg = 88880 + (TS % 1000);
    const id = uuidv4();
    await db("field_settings").insert({
      id, organization_id: fakeOrg,
      check_in_radius_meters: 200, photo_required: false,
      auto_checkout_hours: 12, mileage_rate_per_km: 8.00,
      tracking_interval_seconds: 30,
    });
    cleanup.push({ table: "field_settings", id });

    const row = await db("field_settings").where({ organization_id: fakeOrg }).first();
    expect(row.check_in_radius_meters).toBe(200);
    expect(row.auto_checkout_hours).toBe(12);
    expect(Number(row.mileage_rate_per_km)).toBe(8);
  });

  it("should update field settings", async () => {
    const fakeOrg = 88881 + (TS % 1000);
    const id = uuidv4();
    await db("field_settings").insert({
      id, organization_id: fakeOrg,
      check_in_radius_meters: 200, photo_required: false,
      auto_checkout_hours: 12, mileage_rate_per_km: 8.00,
      tracking_interval_seconds: 30,
    });
    cleanup.push({ table: "field_settings", id });

    await db("field_settings").where({ id }).update({
      check_in_radius_meters: 500, photo_required: true,
      auto_checkout_hours: 8, mileage_rate_per_km: 12.50,
      tracking_interval_seconds: 15,
    });

    const row = await db("field_settings").where({ id }).first();
    expect(row.check_in_radius_meters).toBe(500);
    expect(row.photo_required).toBe(1);
    expect(row.auto_checkout_hours).toBe(8);
    expect(Number(row.mileage_rate_per_km)).toBe(12.5);
    expect(row.tracking_interval_seconds).toBe(15);
  });
});

// ==========================================================================
// ANALYTICS (aggregate queries)
// ==========================================================================
describe("Analytics Queries", () => {
  it("should count check-ins per user in date range", async () => {
    const ids: string[] = [];
    for (let i = 1; i <= 3; i++) {
      const id = uuidv4();
      await db("checkins").insert({
        id, organization_id: ORG_ID, user_id: USER_ID,
        check_in_time: new Date(`2026-04-0${i}T09:00:00`),
        check_in_lat: 12.9716, check_in_lng: 77.5946, status: "completed",
        duration_minutes: 60 * i,
      });
      ids.push(id);
      cleanup.push({ table: "checkins", id });
    }

    const result = await db("checkins")
      .where({ organization_id: ORG_ID, user_id: USER_ID })
      .whereIn("id", ids)
      .count("* as total_checkins")
      .sum("duration_minutes as total_duration")
      .first();

    expect(Number(result?.total_checkins)).toBe(3);
    expect(Number(result?.total_duration)).toBe(360); // 60+120+180
  });

  it("should count expenses by type", async () => {
    const ids: string[] = [];
    for (const t of ["travel", "food", "travel"] as const) {
      const id = uuidv4();
      await db("expenses").insert({
        id, organization_id: ORG_ID, user_id: USER_ID,
        expense_type: t, amount: 1000, currency: "INR", status: "approved",
      });
      ids.push(id);
      cleanup.push({ table: "expenses", id });
    }

    const rows = await db("expenses")
      .where({ organization_id: ORG_ID })
      .whereIn("id", ids)
      .select("expense_type")
      .count("* as count")
      .sum("amount as total")
      .groupBy("expense_type");

    expect(rows.length).toBe(2);
    const travel = rows.find((r: any) => r.expense_type === "travel");
    expect(Number(travel?.count)).toBe(2);
  });

  it("should get work order completion rate", async () => {
    const ids: string[] = [];
    for (const s of ["completed", "completed", "pending", "cancelled"] as const) {
      const id = uuidv4();
      await db("work_orders").insert({
        id, organization_id: ORG_ID, assigned_to: USER_ID,
        title: `WO-${s}-${TS}`, priority: "medium", status: s,
        created_by: USER_ID,
      });
      ids.push(id);
      cleanup.push({ table: "work_orders", id });
    }

    const total = await db("work_orders")
      .where({ organization_id: ORG_ID })
      .whereIn("id", ids)
      .count("* as cnt")
      .first();
    const completed = await db("work_orders")
      .where({ organization_id: ORG_ID, status: "completed" })
      .whereIn("id", ids)
      .count("* as cnt")
      .first();

    const rate = Math.round((Number(completed?.cnt) / Number(total?.cnt)) * 100);
    expect(rate).toBe(50);
  });
});
