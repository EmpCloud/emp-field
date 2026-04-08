// =============================================================================
// FIELD ROUTE DEEP — route planning, stops, work orders, client sites
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

async function seedClientSite(name: string): Promise<string> {
  const id = uuidv4();
  await db("client_sites").insert({
    id, organization_id: ORG_ID, name: `${name}-${TS}`,
    latitude: 12.9716, longitude: 77.5946, radius_meters: 200,
    is_active: true,
  });
  cleanup.push({ table: "client_sites", id });
  return id;
}

// ==========================================================================
// CLIENT SITES
// ==========================================================================
describe("Client Sites CRUD", () => {
  it("should create a client site with full details", async () => {
    const id = uuidv4();
    await db("client_sites").insert({
      id, organization_id: ORG_ID, name: `Acme Corp-${TS}`,
      address: "123 Main St", city: "Bangalore", state: "Karnataka", country: "India",
      latitude: 12.9716, longitude: 77.5946, radius_meters: 300,
      contact_name: "John Doe", contact_phone: "+911234567890",
      contact_email: "john@acme.com", category: "enterprise", is_active: true,
    });
    cleanup.push({ table: "client_sites", id });

    const row = await db("client_sites").where({ id }).first();
    expect(row.name).toBe(`Acme Corp-${TS}`);
    expect(row.city).toBe("Bangalore");
    expect(row.contact_name).toBe("John Doe");
    expect(row.radius_meters).toBe(300);
  });

  it("should update a client site", async () => {
    const id = await seedClientSite("Update");
    await db("client_sites").where({ id }).update({
      address: "456 New Road", radius_meters: 500, category: "mid_market",
    });

    const row = await db("client_sites").where({ id }).first();
    expect(row.address).toBe("456 New Road");
    expect(row.radius_meters).toBe(500);
  });

  it("should deactivate a client site", async () => {
    const id = await seedClientSite("Deact");
    await db("client_sites").where({ id }).update({ is_active: false });

    const row = await db("client_sites").where({ id }).first();
    expect(row.is_active).toBe(0);
  });

  it("should list active client sites for org", async () => {
    await seedClientSite("List1");
    await seedClientSite("List2");

    const sites = await db("client_sites")
      .where({ organization_id: ORG_ID, is_active: true });
    expect(sites.length).toBeGreaterThanOrEqual(2);
  });
});

// ==========================================================================
// DAILY ROUTES & STOPS
// ==========================================================================
describe("Daily Routes & Stops", () => {
  it("should create a planned daily route with stops", async () => {
    const site1 = await seedClientSite("Stop1");
    const site2 = await seedClientSite("Stop2");
    const site3 = await seedClientSite("Stop3");

    const routeId = uuidv4();
    await db("daily_routes").insert({
      id: routeId, organization_id: ORG_ID, user_id: USER_ID,
      date: "2026-04-04", status: "planned", total_distance_km: 0,
    });
    cleanup.push({ table: "daily_routes", id: routeId });

    const stops = [
      { id: uuidv4(), daily_route_id: routeId, client_site_id: site1, sequence_order: 1, planned_arrival: new Date("2026-04-04T09:00:00"), status: "pending" },
      { id: uuidv4(), daily_route_id: routeId, client_site_id: site2, sequence_order: 2, planned_arrival: new Date("2026-04-04T11:00:00"), status: "pending" },
      { id: uuidv4(), daily_route_id: routeId, client_site_id: site3, sequence_order: 3, planned_arrival: new Date("2026-04-04T14:00:00"), status: "pending" },
    ];
    await db("route_stops").insert(stops);
    for (const s of stops) cleanup.push({ table: "route_stops", id: s.id });

    const routeStops = await db("route_stops")
      .where({ daily_route_id: routeId })
      .orderBy("sequence_order");
    expect(routeStops.length).toBe(3);
    expect(routeStops[0].sequence_order).toBe(1);
    expect(routeStops[2].sequence_order).toBe(3);
  });

  it("should mark stops as visited with actual arrival", async () => {
    const siteId = await seedClientSite("Visit");
    const routeId = uuidv4();
    await db("daily_routes").insert({
      id: routeId, organization_id: ORG_ID, user_id: USER_ID,
      date: "2026-04-04", status: "in_progress",
    });
    cleanup.push({ table: "daily_routes", id: routeId });

    const stopId = uuidv4();
    await db("route_stops").insert({
      id: stopId, daily_route_id: routeId, client_site_id: siteId,
      sequence_order: 1, status: "pending",
    });
    cleanup.push({ table: "route_stops", id: stopId });

    await db("route_stops").where({ id: stopId }).update({
      status: "visited", actual_arrival: new Date("2026-04-04T09:15:00"),
      notes: "Arrived 15 min late",
    });

    const row = await db("route_stops").where({ id: stopId }).first();
    expect(row.status).toBe("visited");
    expect(row.notes).toBe("Arrived 15 min late");
  });

  it("should skip a stop", async () => {
    const siteId = await seedClientSite("Skip");
    const routeId = uuidv4();
    await db("daily_routes").insert({
      id: routeId, organization_id: ORG_ID, user_id: USER_ID,
      date: "2026-04-04", status: "in_progress",
    });
    cleanup.push({ table: "daily_routes", id: routeId });

    const stopId = uuidv4();
    await db("route_stops").insert({
      id: stopId, daily_route_id: routeId, client_site_id: siteId,
      sequence_order: 1, status: "pending",
    });
    cleanup.push({ table: "route_stops", id: stopId });

    await db("route_stops").where({ id: stopId }).update({
      status: "skipped", notes: "Client unavailable",
    });

    const row = await db("route_stops").where({ id: stopId }).first();
    expect(row.status).toBe("skipped");
  });

  it("should complete a daily route with total distance", async () => {
    const routeId = uuidv4();
    await db("daily_routes").insert({
      id: routeId, organization_id: ORG_ID, user_id: USER_ID,
      date: "2026-04-04", status: "in_progress", total_distance_km: 0,
    });
    cleanup.push({ table: "daily_routes", id: routeId });

    await db("daily_routes").where({ id: routeId }).update({
      status: "completed", total_distance_km: 45.5,
    });

    const row = await db("daily_routes").where({ id: routeId }).first();
    expect(row.status).toBe("completed");
    expect(Number(row.total_distance_km)).toBeCloseTo(45.5);
  });
});

// ==========================================================================
// WORK ORDERS
// ==========================================================================
describe("Work Orders", () => {
  it("should create a work order assigned to a user", async () => {
    const siteId = await seedClientSite("WO");
    const woId = uuidv4();
    await db("work_orders").insert({
      id: woId, organization_id: ORG_ID, assigned_to: USER_ID,
      client_site_id: siteId, title: `Install equipment-${TS}`,
      description: "Install network equipment at client site",
      priority: "high", status: "pending",
      due_date: "2026-04-10", created_by: USER_ID,
    });
    cleanup.push({ table: "work_orders", id: woId });

    const row = await db("work_orders").where({ id: woId }).first();
    expect(row.priority).toBe("high");
    expect(row.status).toBe("pending");
  });

  it("should advance work order: pending -> in_progress -> completed", async () => {
    const woId = uuidv4();
    await db("work_orders").insert({
      id: woId, organization_id: ORG_ID, assigned_to: USER_ID,
      title: `Lifecycle-${TS}`, priority: "medium", status: "pending",
      created_by: USER_ID,
    });
    cleanup.push({ table: "work_orders", id: woId });

    await db("work_orders").where({ id: woId }).update({ status: "in_progress" });
    let row = await db("work_orders").where({ id: woId }).first();
    expect(row.status).toBe("in_progress");

    await db("work_orders").where({ id: woId }).update({
      status: "completed", completed_at: new Date(),
    });
    row = await db("work_orders").where({ id: woId }).first();
    expect(row.status).toBe("completed");
    expect(row.completed_at).toBeTruthy();
  });

  it("should cancel a work order", async () => {
    const woId = uuidv4();
    await db("work_orders").insert({
      id: woId, organization_id: ORG_ID, assigned_to: USER_ID,
      title: `Cancel-${TS}`, priority: "low", status: "pending",
      created_by: USER_ID,
    });
    cleanup.push({ table: "work_orders", id: woId });

    await db("work_orders").where({ id: woId }).update({ status: "cancelled" });
    const row = await db("work_orders").where({ id: woId }).first();
    expect(row.status).toBe("cancelled");
  });

  it("should filter work orders by priority", async () => {
    const ids: string[] = [];
    for (const priority of ["low", "medium", "high", "urgent"] as const) {
      const id = uuidv4();
      await db("work_orders").insert({
        id, organization_id: ORG_ID, assigned_to: USER_ID,
        title: `${priority}-${TS}`, priority, status: "pending",
        created_by: USER_ID,
      });
      ids.push(id);
      cleanup.push({ table: "work_orders", id });
    }

    const urgent = await db("work_orders")
      .where({ organization_id: ORG_ID, priority: "urgent" })
      .whereIn("id", ids);
    expect(urgent.length).toBe(1);
  });

  it("should list overdue work orders", async () => {
    const woId = uuidv4();
    await db("work_orders").insert({
      id: woId, organization_id: ORG_ID, assigned_to: USER_ID,
      title: `Overdue-${TS}`, priority: "high", status: "pending",
      due_date: "2026-01-01", created_by: USER_ID,
    });
    cleanup.push({ table: "work_orders", id: woId });

    const overdue = await db("work_orders")
      .where({ organization_id: ORG_ID, status: "pending" })
      .where("due_date", "<", new Date())
      .whereIn("id", [woId]);
    expect(overdue.length).toBe(1);
  });
});
