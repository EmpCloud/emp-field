// =============================================================================
// FIELD CHECKIN DEEP — check-in/out, geo validation, duration calc, auto-close
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
    connection: { host: "localhost", port: 3306, user: "empcloud", password: "EmpCloud2026", database: "emp_field" },
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
    id, organization_id: ORG_ID, name: `Site-${TS}`,
    latitude: 12.9716, longitude: 77.5946, radius_meters: 200,
    address: "Bangalore", is_active: true,
  });
  cleanup.push({ table: "client_sites", id });
  return id;
}

// ==========================================================================
// CHECK-IN / CHECK-OUT
// ==========================================================================
describe("Check-in / Check-out", () => {
  it("should create a check-in with geo coordinates", async () => {
    const siteId = await seedClientSite();
    const id = uuidv4();
    await db("checkins").insert({
      id, organization_id: ORG_ID, user_id: USER_ID, client_site_id: siteId,
      check_in_time: new Date("2026-04-04T09:00:00"),
      check_in_lat: 12.9716, check_in_lng: 77.5946,
      status: "active", notes: "Morning check-in",
    });
    cleanup.push({ table: "checkins", id });

    const row = await db("checkins").where({ id }).first();
    expect(row.status).toBe("active");
    expect(Number(row.check_in_lat)).toBeCloseTo(12.9716, 3);
  });

  it("should complete check-out with duration calculation", async () => {
    const siteId = await seedClientSite();
    const id = uuidv4();
    const checkInTime = new Date("2026-04-04T09:00:00");
    const checkOutTime = new Date("2026-04-04T13:30:00");
    const durationMinutes = Math.round((checkOutTime.getTime() - checkInTime.getTime()) / 60000);

    await db("checkins").insert({
      id, organization_id: ORG_ID, user_id: USER_ID, client_site_id: siteId,
      check_in_time: checkInTime, check_in_lat: 12.9716, check_in_lng: 77.5946,
      status: "active",
    });
    cleanup.push({ table: "checkins", id });

    await db("checkins").where({ id }).update({
      check_out_time: checkOutTime, check_out_lat: 12.9720, check_out_lng: 77.5950,
      status: "completed", duration_minutes: durationMinutes,
    });

    const row = await db("checkins").where({ id }).first();
    expect(row.status).toBe("completed");
    expect(row.duration_minutes).toBe(270); // 4.5 hours
  });

  it("should auto-close check-in after threshold", async () => {
    const id = uuidv4();
    await db("checkins").insert({
      id, organization_id: ORG_ID, user_id: USER_ID,
      check_in_time: new Date("2026-04-03T09:00:00"),
      check_in_lat: 12.9716, check_in_lng: 77.5946,
      status: "active",
    });
    cleanup.push({ table: "checkins", id });

    // Auto-close: set status to auto_closed with 12h duration
    await db("checkins").where({ id }).update({
      status: "auto_closed", duration_minutes: 720,
      check_out_time: new Date("2026-04-03T21:00:00"),
    });

    const row = await db("checkins").where({ id }).first();
    expect(row.status).toBe("auto_closed");
    expect(row.duration_minutes).toBe(720);
  });

  it("should check-in without a client site", async () => {
    const id = uuidv4();
    await db("checkins").insert({
      id, organization_id: ORG_ID, user_id: USER_ID,
      check_in_time: new Date("2026-04-04T10:00:00"),
      check_in_lat: 28.6139, check_in_lng: 77.2090,
      status: "active", notes: "Ad-hoc visit",
    });
    cleanup.push({ table: "checkins", id });

    const row = await db("checkins").where({ id }).first();
    expect(row.client_site_id).toBeNull();
    expect(row.notes).toBe("Ad-hoc visit");
  });

  it("should add a photo URL to check-in", async () => {
    const id = uuidv4();
    await db("checkins").insert({
      id, organization_id: ORG_ID, user_id: USER_ID,
      check_in_time: new Date("2026-04-04T09:00:00"),
      check_in_lat: 12.9716, check_in_lng: 77.5946,
      photo_url: "/photos/checkin-001.jpg", status: "active",
    });
    cleanup.push({ table: "checkins", id });

    const row = await db("checkins").where({ id }).first();
    expect(row.photo_url).toBe("/photos/checkin-001.jpg");
  });
});

// ==========================================================================
// GEO VALIDATION
// ==========================================================================
describe("Geo Validation (Haversine)", () => {
  function haversineDistance(lat1: number, lng1: number, lat2: number, lng2: number): number {
    const R = 6371000; // meters
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLng = (lng2 - lng1) * Math.PI / 180;
    const a = Math.sin(dLat / 2) ** 2 +
      Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) * Math.sin(dLng / 2) ** 2;
    return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  }

  it("should calculate distance within radius (valid check-in)", () => {
    // Site at 12.9716, 77.5946 with 200m radius
    // Check-in at 12.9718, 77.5948 (~30m away)
    const dist = haversineDistance(12.9716, 77.5946, 12.9718, 77.5948);
    expect(dist).toBeLessThan(200);
  });

  it("should detect distance outside radius (invalid check-in)", () => {
    // Check-in at 12.9800, 77.6000 (~1km away)
    const dist = haversineDistance(12.9716, 77.5946, 12.9800, 77.6000);
    expect(dist).toBeGreaterThan(200);
  });

  it("should return 0 for same coordinates", () => {
    const dist = haversineDistance(12.9716, 77.5946, 12.9716, 77.5946);
    expect(dist).toBe(0);
  });

  it("should handle antipodal points", () => {
    const dist = haversineDistance(0, 0, 0, 180);
    expect(dist).toBeGreaterThan(20000000); // ~20,000 km
  });
});

// ==========================================================================
// VISIT LOGS
// ==========================================================================
describe("Visit Logs", () => {
  it("should create a visit log linked to a check-in", async () => {
    const siteId = await seedClientSite();
    const checkinId = uuidv4();
    await db("checkins").insert({
      id: checkinId, organization_id: ORG_ID, user_id: USER_ID, client_site_id: siteId,
      check_in_time: new Date("2026-04-04T09:00:00"),
      check_in_lat: 12.9716, check_in_lng: 77.5946, status: "completed",
      check_out_time: new Date("2026-04-04T10:30:00"), duration_minutes: 90,
    });
    cleanup.push({ table: "checkins", id: checkinId });

    const visitId = uuidv4();
    await db("visit_logs").insert({
      id: visitId, organization_id: ORG_ID, user_id: USER_ID,
      client_site_id: siteId, checkin_id: checkinId,
      purpose: "Quarterly review", outcome: "Signed renewal",
      notes: "Client satisfied", duration_minutes: 90,
      photos: JSON.stringify(["/photos/visit-001.jpg"]),
    });
    cleanup.push({ table: "visit_logs", id: visitId });

    const row = await db("visit_logs").where({ id: visitId }).first();
    expect(row.purpose).toBe("Quarterly review");
    expect(row.outcome).toBe("Signed renewal");
    const photos = typeof row.photos === "string" ? JSON.parse(row.photos) : row.photos;
    expect(photos.length).toBe(1);
  });

  it("should create a visit log without check-in", async () => {
    const siteId = await seedClientSite();
    const visitId = uuidv4();
    await db("visit_logs").insert({
      id: visitId, organization_id: ORG_ID, user_id: USER_ID,
      client_site_id: siteId, purpose: "Cold call", duration_minutes: 30,
    });
    cleanup.push({ table: "visit_logs", id: visitId });

    const row = await db("visit_logs").where({ id: visitId }).first();
    expect(row.checkin_id).toBeNull();
    expect(row.duration_minutes).toBe(30);
  });
});

// ==========================================================================
// LOCATION TRAILS
// ==========================================================================
describe("Location Trails", () => {
  it("should record location trail points", async () => {
    const ids: string[] = [];
    for (let i = 0; i < 5; i++) {
      const id = uuidv4();
      await db("location_trails").insert({
        id, organization_id: ORG_ID, user_id: USER_ID,
        latitude: 12.9716 + i * 0.001, longitude: 77.5946 + i * 0.001,
        accuracy: 15.5, battery_percent: 80 - i * 5,
        timestamp: new Date(Date.now() - (4 - i) * 60000),
      });
      ids.push(id);
      cleanup.push({ table: "location_trails", id });
    }

    const trails = await db("location_trails")
      .where({ organization_id: ORG_ID, user_id: USER_ID })
      .whereIn("id", ids)
      .orderBy("timestamp", "asc");
    expect(trails.length).toBe(5);
    expect(Number(trails[0].battery_percent)).toBe(80);
  });
});
