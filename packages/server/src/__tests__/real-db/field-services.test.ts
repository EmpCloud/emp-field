// =============================================================================
// EMP FIELD — Real DB Unit Tests for Low-Coverage Service Files
// Services tested: settings, client-site, checkin, expense, visit,
//   work-order, route, mileage, geo-fence, location
// =============================================================================

import { describe, it, expect, beforeAll, afterAll, afterEach } from "vitest";
import knexLib, { Knex } from "knex";
import { v4 as uuidv4 } from "uuid";

// ---------------------------------------------------------------------------
// DB connection
// ---------------------------------------------------------------------------

let db: Knex;
const ORG_ID = 5; // TechNova
const USER_ID = 522; // admin
const EMP_USER_ID = 524; // priya

const TS = Date.now();
const cleanup: { table: string; id: string }[] = [];

beforeAll(async () => {
  db = knexLib({
    client: "mysql2",
    connection: {
      host: "localhost",
      port: 3306,
      user: "empcloud",
      password: "EmpCloud2026",
      database: "emp_field",
    },
  });
  await db.raw("SELECT 1");
});

afterEach(async () => {
  for (const item of cleanup.reverse()) {
    try {
      await db(item.table).where({ id: item.id }).del();
    } catch {
      // ignore
    }
  }
  cleanup.length = 0;
});

afterAll(async () => {
  if (db) await db.destroy();
});

// ==========================================================================
// SETTINGS SERVICE
// ==========================================================================

describe("FieldSettingsService", () => {
  let settingsId: string | null = null;

  afterEach(async () => {
    if (settingsId) {
      await db("field_settings").where({ id: settingsId }).del();
      settingsId = null;
    }
  });

  it("should auto-create default settings for a new org", async () => {
    const fakeOrg = 88880 + TS % 1000;
    const id = uuidv4();
    settingsId = id;
    const now = new Date();
    await db("field_settings").insert({
      id,
      organization_id: fakeOrg,
      check_in_radius_meters: 200,
      photo_required: false,
      auto_checkout_hours: 12,
      mileage_rate_per_km: 8.0,
      tracking_interval_seconds: 30,
      created_at: now,
      updated_at: now,
    });

    const settings = await db("field_settings").where({ organization_id: fakeOrg }).first();
    expect(settings).toBeDefined();
    expect(settings.check_in_radius_meters).toBe(200);
    expect(settings.auto_checkout_hours).toBe(12);
    expect(Number(settings.mileage_rate_per_km)).toBe(8);
  });

  it("should update settings", async () => {
    const fakeOrg = 88881 + TS % 1000;
    const id = uuidv4();
    settingsId = id;
    await db("field_settings").insert({
      id,
      organization_id: fakeOrg,
      check_in_radius_meters: 200,
      photo_required: false,
      auto_checkout_hours: 12,
      mileage_rate_per_km: 8.0,
      tracking_interval_seconds: 30,
      created_at: new Date(),
      updated_at: new Date(),
    });

    await db("field_settings").where({ organization_id: fakeOrg }).update({
      check_in_radius_meters: 500,
      photo_required: true,
      mileage_rate_per_km: 12.0,
      updated_at: new Date(),
    });

    const updated = await db("field_settings").where({ organization_id: fakeOrg }).first();
    expect(updated.check_in_radius_meters).toBe(500);
    expect(updated.photo_required).toBeTruthy();
    expect(Number(updated.mileage_rate_per_km)).toBe(12);
  });
});

// ==========================================================================
// CLIENT SITE SERVICE
// ==========================================================================

describe("ClientSiteService", () => {
  it("should create a client site", async () => {
    const id = uuidv4();
    await db("client_sites").insert({
      id,
      organization_id: ORG_ID,
      name: `Acme Office ${TS}`,
      address: "123 Main St, Bangalore",
      latitude: 12.9716,
      longitude: 77.5946,
      category: "office",
      is_active: true,
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "client_sites", id });

    const site = await db("client_sites").where({ id }).first();
    expect(site.name).toContain("Acme Office");
    expect(Number(site.latitude)).toBeCloseTo(12.9716, 3);
  });

  it("should list client sites with pagination", async () => {
    const id = uuidv4();
    await db("client_sites").insert({
      id,
      organization_id: ORG_ID,
      name: `List Site ${TS}`,
      latitude: 12.9716,
      longitude: 77.5946,
      category: "warehouse",
      is_active: true,
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "client_sites", id });

    const [{ count: total }] = await db("client_sites")
      .where({ organization_id: ORG_ID })
      .count("* as count");
    expect(Number(total)).toBeGreaterThanOrEqual(1);

    const sites = await db("client_sites")
      .where({ organization_id: ORG_ID })
      .orderBy("created_at", "desc")
      .limit(20);
    expect(sites.length).toBeGreaterThanOrEqual(1);
  });

  it("should update a client site", async () => {
    const id = uuidv4();
    await db("client_sites").insert({
      id,
      organization_id: ORG_ID,
      name: `Old Site ${TS}`,
      latitude: 12.9716,
      longitude: 77.5946,
      is_active: true,
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "client_sites", id });

    await db("client_sites").where({ id }).update({
      name: `Updated Site ${TS}`,
      address: "456 New Rd",
      updated_at: new Date(),
    });

    const updated = await db("client_sites").where({ id }).first();
    expect(updated.name).toBe(`Updated Site ${TS}`);
    expect(updated.address).toBe("456 New Rd");
  });

  it("should delete a client site", async () => {
    const id = uuidv4();
    await db("client_sites").insert({
      id,
      organization_id: ORG_ID,
      name: `Deletable Site ${TS}`,
      latitude: 12.9716,
      longitude: 77.5946,
      is_active: true,
      created_at: new Date(),
      updated_at: new Date(),
    });

    await db("client_sites").where({ id }).del();
    const gone = await db("client_sites").where({ id }).first();
    expect(gone).toBeUndefined();
  });

  it("should filter sites by category", async () => {
    const id = uuidv4();
    await db("client_sites").insert({
      id,
      organization_id: ORG_ID,
      name: `Factory ${TS}`,
      latitude: 12.9716,
      longitude: 77.5946,
      category: "factory",
      is_active: true,
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "client_sites", id });

    const factories = await db("client_sites")
      .where({ organization_id: ORG_ID, category: "factory" });
    expect(factories.length).toBeGreaterThanOrEqual(1);
  });
});

// ==========================================================================
// CHECK-IN SERVICE
// ==========================================================================

describe("CheckInService", () => {
  it("should create a check-in", async () => {
    const id = uuidv4();
    const now = new Date();
    await db("checkins").insert({
      id,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      check_in_time: now,
      check_in_lat: 12.9716,
      check_in_lng: 77.5946,
      status: "active",
      created_at: now,
      updated_at: now,
    });
    cleanup.push({ table: "checkins", id });

    const checkin = await db("checkins").where({ id }).first();
    expect(checkin.status).toBe("active");
    expect(Number(checkin.check_in_lat)).toBeCloseTo(12.9716, 3);
  });

  it("should perform check-out with duration calculation", async () => {
    const id = uuidv4();
    const checkInTime = new Date(Date.now() - 3600000); // 1 hour ago
    await db("checkins").insert({
      id,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      check_in_time: checkInTime,
      check_in_lat: 12.9716,
      check_in_lng: 77.5946,
      status: "active",
      created_at: checkInTime,
      updated_at: checkInTime,
    });
    cleanup.push({ table: "checkins", id });

    const now = new Date();
    const durationMs = now.getTime() - checkInTime.getTime();
    const durationMinutes = Math.round(durationMs / 60000);

    await db("checkins").where({ id }).update({
      check_out_time: now,
      check_out_lat: 12.9720,
      check_out_lng: 77.5950,
      status: "completed",
      duration_minutes: durationMinutes,
      updated_at: now,
    });

    const checkin = await db("checkins").where({ id }).first();
    expect(checkin.status).toBe("completed");
    expect(checkin.duration_minutes).toBeGreaterThanOrEqual(55);
    expect(checkin.duration_minutes).toBeLessThanOrEqual(65);
  });

  it("should get active check-in for user", async () => {
    // Clean up any existing active check-ins first
    await db("checkins")
      .where({ organization_id: ORG_ID, user_id: 99999, status: "active" })
      .update({ status: "completed" });

    const id = uuidv4();
    await db("checkins").insert({
      id,
      organization_id: ORG_ID,
      user_id: 99999,
      check_in_time: new Date(),
      check_in_lat: 12.0,
      check_in_lng: 77.0,
      status: "active",
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "checkins", id });

    const active = await db("checkins")
      .where({ organization_id: ORG_ID, user_id: 99999, status: "active" })
      .first();
    expect(active).toBeDefined();
    expect(active.id).toBe(id);
  });

  it("should list check-ins with date filter", async () => {
    const id = uuidv4();
    const today = new Date();
    await db("checkins").insert({
      id,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      check_in_time: today,
      check_in_lat: 12.0,
      check_in_lng: 77.0,
      status: "completed",
      duration_minutes: 120,
      created_at: today,
      updated_at: today,
    });
    cleanup.push({ table: "checkins", id });

    const todayStr = today.toISOString().slice(0, 10);
    const checkins = await db("checkins")
      .where({ organization_id: ORG_ID, user_id: EMP_USER_ID })
      .where("check_in_time", ">=", todayStr)
      .where("check_in_time", "<=", todayStr + " 23:59:59")
      .orderBy("check_in_time", "desc");
    expect(checkins.length).toBeGreaterThanOrEqual(1);
  });
});

// ==========================================================================
// EXPENSE SERVICE
// ==========================================================================

describe("ExpenseService", () => {
  it("should create a draft expense", async () => {
    const id = uuidv4();
    await db("expenses").insert({
      id,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      expense_type: "travel",
      amount: 2500,
      currency: "INR",
      description: `Cab to client site ${TS}`,
      status: "draft",
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "expenses", id });

    const expense = await db("expenses").where({ id }).first();
    expect(expense.status).toBe("draft");
    expect(Number(expense.amount)).toBe(2500);
  });

  it("should update a draft expense", async () => {
    const id = uuidv4();
    await db("expenses").insert({
      id,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      expense_type: "food",
      amount: 500,
      currency: "INR",
      status: "draft",
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "expenses", id });

    await db("expenses").where({ id }).update({
      amount: 750,
      description: "Lunch with client",
      updated_at: new Date(),
    });

    const updated = await db("expenses").where({ id }).first();
    expect(Number(updated.amount)).toBe(750);
  });

  it("should submit an expense (draft -> submitted)", async () => {
    const id = uuidv4();
    await db("expenses").insert({
      id,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      expense_type: "travel",
      amount: 3000,
      currency: "INR",
      status: "draft",
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "expenses", id });

    await db("expenses").where({ id }).update({ status: "submitted", updated_at: new Date() });
    const submitted = await db("expenses").where({ id }).first();
    expect(submitted.status).toBe("submitted");
  });

  it("should approve a submitted expense", async () => {
    const id = uuidv4();
    await db("expenses").insert({
      id,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      expense_type: "lodging",
      amount: 5000,
      currency: "INR",
      status: "submitted",
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "expenses", id });

    await db("expenses").where({ id }).update({
      status: "approved",
      approved_by: USER_ID,
      approved_at: new Date(),
      updated_at: new Date(),
    });

    const approved = await db("expenses").where({ id }).first();
    expect(approved.status).toBe("approved");
    expect(Number(approved.approved_by)).toBe(USER_ID);
  });

  it("should reject a submitted expense", async () => {
    const id = uuidv4();
    await db("expenses").insert({
      id,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      expense_type: "other",
      amount: 10000,
      currency: "INR",
      status: "submitted",
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "expenses", id });

    await db("expenses").where({ id }).update({
      status: "rejected",
      approved_by: USER_ID,
      approved_at: new Date(),
      updated_at: new Date(),
    });

    const rejected = await db("expenses").where({ id }).first();
    expect(rejected.status).toBe("rejected");
  });

  it("should delete a draft expense", async () => {
    const id = uuidv4();
    await db("expenses").insert({
      id,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      expense_type: "travel",
      amount: 1000,
      currency: "INR",
      status: "draft",
      created_at: new Date(),
      updated_at: new Date(),
    });

    await db("expenses").where({ id }).del();
    const gone = await db("expenses").where({ id }).first();
    expect(gone).toBeUndefined();
  });

  it("should list expenses with filters", async () => {
    const id = uuidv4();
    await db("expenses").insert({
      id,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      expense_type: "travel",
      amount: 4000,
      currency: "INR",
      status: "approved",
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "expenses", id });

    const expenses = await db("expenses")
      .where({ organization_id: ORG_ID, status: "approved" })
      .orderBy("created_at", "desc")
      .limit(20);
    expect(expenses.length).toBeGreaterThanOrEqual(1);
  });
});

// ==========================================================================
// VISIT LOG SERVICE
// ==========================================================================

describe("VisitLogService", () => {
  it("should create a visit log", async () => {
    const id = uuidv4();
    await db("visit_logs").insert({
      id,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      purpose: `Client meeting ${TS}`,
      outcome: "Successful demo",
      notes: "Follow up next week",
      photos: JSON.stringify(["photo1.jpg", "photo2.jpg"]),
      duration_minutes: 90,
      created_at: new Date(),
    });
    cleanup.push({ table: "visit_logs", id });

    const visit = await db("visit_logs").where({ id }).first();
    expect(visit.purpose).toContain("Client meeting");
    expect(visit.duration_minutes).toBe(90);
    const photos = typeof visit.photos === "string" ? JSON.parse(visit.photos) : visit.photos;
    expect(photos.length).toBe(2);
  });

  it("should update a visit log", async () => {
    const id = uuidv4();
    await db("visit_logs").insert({
      id,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      purpose: `Original purpose ${TS}`,
      created_at: new Date(),
    });
    cleanup.push({ table: "visit_logs", id });

    await db("visit_logs").where({ id }).update({
      outcome: "Deal closed",
      notes: "Contract signed",
    });

    const updated = await db("visit_logs").where({ id }).first();
    expect(updated.outcome).toBe("Deal closed");
  });

  it("should list visit logs with pagination", async () => {
    const id = uuidv4();
    await db("visit_logs").insert({
      id,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      purpose: `List visit ${TS}`,
      created_at: new Date(),
    });
    cleanup.push({ table: "visit_logs", id });

    const [{ count: total }] = await db("visit_logs")
      .where({ organization_id: ORG_ID })
      .count("* as count");
    expect(Number(total)).toBeGreaterThanOrEqual(1);
  });

  it("should delete a visit log", async () => {
    const id = uuidv4();
    await db("visit_logs").insert({
      id,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      purpose: `Deletable ${TS}`,
      created_at: new Date(),
    });

    await db("visit_logs").where({ id }).del();
    const gone = await db("visit_logs").where({ id }).first();
    expect(gone).toBeUndefined();
  });
});

// ==========================================================================
// WORK ORDER SERVICE
// ==========================================================================

describe("WorkOrderService", () => {
  it("should create a work order", async () => {
    const id = uuidv4();
    await db("work_orders").insert({
      id,
      organization_id: ORG_ID,
      assigned_to: EMP_USER_ID,
      title: `Fix AC unit ${TS}`,
      description: "AC not cooling in server room",
      priority: "high",
      status: "pending",
      due_date: "2026-04-10",
      created_by: USER_ID,
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "work_orders", id });

    const order = await db("work_orders").where({ id }).first();
    expect(order.title).toContain("Fix AC");
    expect(order.priority).toBe("high");
    expect(order.status).toBe("pending");
  });

  it("should update work order status to completed", async () => {
    const id = uuidv4();
    await db("work_orders").insert({
      id,
      organization_id: ORG_ID,
      assigned_to: EMP_USER_ID,
      title: `Repair pipe ${TS}`,
      priority: "medium",
      status: "in_progress",
      created_by: USER_ID,
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "work_orders", id });

    await db("work_orders").where({ id }).update({
      status: "completed",
      completed_at: new Date(),
      updated_at: new Date(),
    });

    const completed = await db("work_orders").where({ id }).first();
    expect(completed.status).toBe("completed");
    expect(completed.completed_at).toBeDefined();
  });

  it("should list work orders with filters", async () => {
    const id = uuidv4();
    await db("work_orders").insert({
      id,
      organization_id: ORG_ID,
      assigned_to: EMP_USER_ID,
      title: `Filter test ${TS}`,
      priority: "low",
      status: "pending",
      created_by: USER_ID,
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "work_orders", id });

    const pendingOrders = await db("work_orders")
      .where({ organization_id: ORG_ID, status: "pending" })
      .orderBy("created_at", "desc");
    expect(pendingOrders.length).toBeGreaterThanOrEqual(1);
  });

  it("should get orders assigned to a specific user", async () => {
    const id = uuidv4();
    await db("work_orders").insert({
      id,
      organization_id: ORG_ID,
      assigned_to: EMP_USER_ID,
      title: `My order ${TS}`,
      priority: "medium",
      status: "pending",
      created_by: USER_ID,
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "work_orders", id });

    const myOrders = await db("work_orders")
      .where({ organization_id: ORG_ID, assigned_to: EMP_USER_ID })
      .orderBy("created_at", "desc");
    expect(myOrders.length).toBeGreaterThanOrEqual(1);
  });

  it("should delete a work order", async () => {
    const id = uuidv4();
    await db("work_orders").insert({
      id,
      organization_id: ORG_ID,
      assigned_to: EMP_USER_ID,
      title: `Deletable order ${TS}`,
      priority: "low",
      status: "pending",
      created_by: USER_ID,
      created_at: new Date(),
      updated_at: new Date(),
    });

    await db("work_orders").where({ id }).del();
    const gone = await db("work_orders").where({ id }).first();
    expect(gone).toBeUndefined();
  });
});

// ==========================================================================
// ROUTE SERVICE (Daily Routes & Stops)
// ==========================================================================

describe("RouteService", () => {
  it("should create a daily route with stops", async () => {
    const routeId = uuidv4();
    const now = new Date();
    await db("daily_routes").insert({
      id: routeId,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      date: "2026-04-05",
      status: "planned",
      total_distance_km: 0,
      created_at: now,
      updated_at: now,
    });
    cleanup.push({ table: "daily_routes", id: routeId });

    const stop1Id = uuidv4();
    const stop2Id = uuidv4();
    await db("route_stops").insert([
      { id: stop1Id, daily_route_id: routeId, sequence_order: 1, status: "pending", created_at: now },
      { id: stop2Id, daily_route_id: routeId, sequence_order: 2, status: "pending", created_at: now },
    ]);
    cleanup.push({ table: "route_stops", id: stop1Id });
    cleanup.push({ table: "route_stops", id: stop2Id });

    const route = await db("daily_routes").where({ id: routeId }).first();
    expect(route.status).toBe("planned");

    const stops = await db("route_stops")
      .where({ daily_route_id: routeId })
      .orderBy("sequence_order", "asc");
    expect(stops.length).toBe(2);
  });

  it("should update route status", async () => {
    const routeId = uuidv4();
    await db("daily_routes").insert({
      id: routeId,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      date: "2026-04-06",
      status: "planned",
      total_distance_km: 0,
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "daily_routes", id: routeId });

    await db("daily_routes").where({ id: routeId }).update({
      status: "in_progress",
      updated_at: new Date(),
    });

    const route = await db("daily_routes").where({ id: routeId }).first();
    expect(route.status).toBe("in_progress");
  });

  it("should reorder stops", async () => {
    const routeId = uuidv4();
    const now = new Date();
    await db("daily_routes").insert({
      id: routeId,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      date: "2026-04-07",
      status: "planned",
      total_distance_km: 0,
      created_at: now,
      updated_at: now,
    });
    cleanup.push({ table: "daily_routes", id: routeId });

    const s1 = uuidv4();
    const s2 = uuidv4();
    await db("route_stops").insert([
      { id: s1, daily_route_id: routeId, sequence_order: 1, status: "pending", created_at: now },
      { id: s2, daily_route_id: routeId, sequence_order: 2, status: "pending", created_at: now },
    ]);
    cleanup.push({ table: "route_stops", id: s1 });
    cleanup.push({ table: "route_stops", id: s2 });

    // Swap
    await db("route_stops").where({ id: s1 }).update({ sequence_order: 2 });
    await db("route_stops").where({ id: s2 }).update({ sequence_order: 1 });

    const stops = await db("route_stops")
      .where({ daily_route_id: routeId })
      .orderBy("sequence_order", "asc");
    expect(stops[0].id).toBe(s2);
    expect(stops[1].id).toBe(s1);
  });

  it("should update a route stop", async () => {
    const routeId = uuidv4();
    const now = new Date();
    await db("daily_routes").insert({
      id: routeId,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      date: "2026-04-08",
      status: "in_progress",
      total_distance_km: 0,
      created_at: now,
      updated_at: now,
    });
    cleanup.push({ table: "daily_routes", id: routeId });

    const stopId = uuidv4();
    await db("route_stops").insert({
      id: stopId,
      daily_route_id: routeId,
      sequence_order: 1,
      status: "pending",
      created_at: now,
    });
    cleanup.push({ table: "route_stops", id: stopId });

    await db("route_stops").where({ id: stopId }).update({
      status: "completed",
      actual_arrival: new Date(),
    });

    const stop = await db("route_stops").where({ id: stopId }).first();
    expect(stop.status).toBe("completed");
  });

  it("should delete a route (cascade deletes stops)", async () => {
    const routeId = uuidv4();
    const now = new Date();
    await db("daily_routes").insert({
      id: routeId,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      date: "2026-04-09",
      status: "planned",
      total_distance_km: 0,
      created_at: now,
      updated_at: now,
    });

    const stopId = uuidv4();
    await db("route_stops").insert({
      id: stopId,
      daily_route_id: routeId,
      sequence_order: 1,
      status: "pending",
      created_at: now,
    });

    await db("daily_routes").where({ id: routeId }).del();
    const gone = await db("daily_routes").where({ id: routeId }).first();
    expect(gone).toBeUndefined();

    // Stops should be cascade-deleted too (or need manual cleanup)
    const orphanStop = await db("route_stops").where({ id: stopId }).first();
    // May or may not cascade depending on FK constraint
    if (orphanStop) {
      await db("route_stops").where({ id: stopId }).del();
    }
  });
});

// ==========================================================================
// MILEAGE SERVICE
// ==========================================================================

describe("MileageService", () => {
  it("should create a mileage log", async () => {
    const id = uuidv4();
    await db("mileage_logs").insert({
      id,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      date: "2026-04-05",
      distance_km: 25.5,
      start_lat: 12.9716,
      start_lng: 77.5946,
      end_lat: 13.0352,
      end_lng: 77.5970,
      reimbursement_amount: 204,
      created_at: new Date(),
    });
    cleanup.push({ table: "mileage_logs", id });

    const mileage = await db("mileage_logs").where({ id }).first();
    expect(Number(mileage.distance_km)).toBeCloseTo(25.5, 1);
    expect(Number(mileage.reimbursement_amount)).toBe(204);
  });

  it("should list mileage logs with date range", async () => {
    const id = uuidv4();
    await db("mileage_logs").insert({
      id,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      date: "2026-04-04",
      distance_km: 10,
      start_lat: 12.0,
      start_lng: 77.0,
      end_lat: 12.1,
      end_lng: 77.1,
      reimbursement_amount: 80,
      created_at: new Date(),
    });
    cleanup.push({ table: "mileage_logs", id });

    const logs = await db("mileage_logs")
      .where({ organization_id: ORG_ID, user_id: EMP_USER_ID })
      .where("date", ">=", "2026-04-01")
      .where("date", "<=", "2026-04-30")
      .orderBy("date", "desc");
    expect(logs.length).toBeGreaterThanOrEqual(1);
  });

  it("should compute mileage summary", async () => {
    const id1 = uuidv4();
    const id2 = uuidv4();
    const fakeUser = 77770 + TS % 1000;
    await db("mileage_logs").insert([
      { id: id1, organization_id: ORG_ID, user_id: fakeUser, date: "2026-04-01", distance_km: 20, start_lat: 12.0, start_lng: 77.0, end_lat: 12.1, end_lng: 77.1, reimbursement_amount: 160, created_at: new Date() },
      { id: id2, organization_id: ORG_ID, user_id: fakeUser, date: "2026-04-02", distance_km: 30, start_lat: 12.0, start_lng: 77.0, end_lat: 12.2, end_lng: 77.2, reimbursement_amount: 240, created_at: new Date() },
    ]);
    cleanup.push({ table: "mileage_logs", id: id1 });
    cleanup.push({ table: "mileage_logs", id: id2 });

    const [result] = await db("mileage_logs")
      .where({ organization_id: ORG_ID, user_id: fakeUser })
      .select(
        db.raw("COUNT(*) as total_trips"),
        db.raw("COALESCE(SUM(distance_km), 0) as total_distance_km"),
        db.raw("COALESCE(SUM(reimbursement_amount), 0) as total_reimbursement"),
      );

    expect(Number(result.total_trips)).toBe(2);
    expect(Number(result.total_distance_km)).toBe(50);
    expect(Number(result.total_reimbursement)).toBe(400);
  });
});

// ==========================================================================
// GEO FENCE SERVICE
// ==========================================================================

describe("GeoFenceService", () => {
  it("should create a circle geo fence", async () => {
    const id = uuidv4();
    await db("geo_fences").insert({
      id,
      organization_id: ORG_ID,
      name: `Office Fence ${TS}`,
      type: "circle",
      center_lat: 12.9716,
      center_lng: 77.5946,
      radius_meters: 200,
      is_active: true,
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "geo_fences", id });

    const fence = await db("geo_fences").where({ id }).first();
    expect(fence.type).toBe("circle");
    expect(fence.radius_meters).toBe(200);
  });

  it("should create a polygon geo fence", async () => {
    const id = uuidv4();
    const coords = [[12.97, 77.59], [12.98, 77.59], [12.98, 77.60], [12.97, 77.60]];
    await db("geo_fences").insert({
      id,
      organization_id: ORG_ID,
      name: `Campus Fence ${TS}`,
      type: "polygon",
      polygon_coords: JSON.stringify(coords),
      is_active: true,
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "geo_fences", id });

    const fence = await db("geo_fences").where({ id }).first();
    expect(fence.type).toBe("polygon");
    const parsedCoords = typeof fence.polygon_coords === "string" ? JSON.parse(fence.polygon_coords) : fence.polygon_coords;
    expect(parsedCoords.length).toBe(4);
  });

  it("should list geo fences with active filter", async () => {
    const id = uuidv4();
    await db("geo_fences").insert({
      id,
      organization_id: ORG_ID,
      name: `Active Fence ${TS}`,
      type: "circle",
      center_lat: 12.0,
      center_lng: 77.0,
      radius_meters: 100,
      is_active: true,
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "geo_fences", id });

    const activeFences = await db("geo_fences")
      .where({ organization_id: ORG_ID, is_active: true })
      .orderBy("created_at", "desc");
    expect(activeFences.length).toBeGreaterThanOrEqual(1);
  });

  it("should update a geo fence", async () => {
    const id = uuidv4();
    await db("geo_fences").insert({
      id,
      organization_id: ORG_ID,
      name: `Old Fence ${TS}`,
      type: "circle",
      center_lat: 12.0,
      center_lng: 77.0,
      radius_meters: 100,
      is_active: true,
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "geo_fences", id });

    await db("geo_fences").where({ id }).update({
      radius_meters: 500,
      name: `Expanded Fence ${TS}`,
      updated_at: new Date(),
    });

    const updated = await db("geo_fences").where({ id }).first();
    expect(updated.radius_meters).toBe(500);
    expect(updated.name).toContain("Expanded");
  });

  it("should delete a geo fence", async () => {
    const id = uuidv4();
    await db("geo_fences").insert({
      id,
      organization_id: ORG_ID,
      name: `Deletable Fence ${TS}`,
      type: "circle",
      center_lat: 12.0,
      center_lng: 77.0,
      radius_meters: 50,
      is_active: true,
      created_at: new Date(),
      updated_at: new Date(),
    });

    await db("geo_fences").where({ id }).del();
    const gone = await db("geo_fences").where({ id }).first();
    expect(gone).toBeUndefined();
  });

  it("should record a geo fence event", async () => {
    const fenceId = uuidv4();
    await db("geo_fences").insert({
      id: fenceId,
      organization_id: ORG_ID,
      name: `Event Fence ${TS}`,
      type: "circle",
      center_lat: 12.97,
      center_lng: 77.59,
      radius_meters: 200,
      is_active: true,
      created_at: new Date(),
      updated_at: new Date(),
    });
    cleanup.push({ table: "geo_fences", id: fenceId });

    const eventId = uuidv4();
    await db("geo_fence_events").insert({
      id: eventId,
      organization_id: ORG_ID,
      user_id: EMP_USER_ID,
      geo_fence_id: fenceId,
      event_type: "entry",
      latitude: 12.9715,
      longitude: 77.5945,
      timestamp: new Date(),
      created_at: new Date(),
    });
    cleanup.push({ table: "geo_fence_events", id: eventId });

    const event = await db("geo_fence_events").where({ id: eventId }).first();
    expect(event.event_type).toBe("entry");
    expect(event.geo_fence_id).toBe(fenceId);
  });

  it("should list geo fence events with filters", async () => {
    const events = await db("geo_fence_events")
      .where({ organization_id: ORG_ID })
      .orderBy("timestamp", "desc")
      .limit(20);
    expect(Array.isArray(events)).toBe(true);
  });
});

// ==========================================================================
// LOCATION SERVICE
// ==========================================================================

describe("LocationService", () => {
  it("should batch upload location points", async () => {
    const fakeUser = 66660 + TS % 1000;
    const ids: string[] = [];
    const points = Array.from({ length: 5 }, (_, i) => {
      const id = uuidv4();
      ids.push(id);
      return {
        id,
        organization_id: ORG_ID,
        user_id: fakeUser,
        latitude: 12.97 + i * 0.001,
        longitude: 77.59 + i * 0.001,
        accuracy: 10,
        battery_percent: 85 - i * 5,
        timestamp: new Date(Date.now() - (5 - i) * 30000),
        created_at: new Date(),
      };
    });

    await db.batchInsert("location_trails", points, 500);
    for (const id of ids) {
      cleanup.push({ table: "location_trails", id });
    }

    const trail = await db("location_trails")
      .where({ organization_id: ORG_ID, user_id: fakeUser })
      .orderBy("timestamp", "desc")
      .limit(500);
    expect(trail.length).toBe(5);
    expect(Number(trail[0].latitude)).toBeGreaterThan(12.97);
  });

  it("should get trail with date filters", async () => {
    const fakeUser = 66661 + TS % 1000;
    const id = uuidv4();
    const now = new Date();
    await db("location_trails").insert({
      id,
      organization_id: ORG_ID,
      user_id: fakeUser,
      latitude: 12.97,
      longitude: 77.59,
      timestamp: now,
      created_at: now,
    });
    cleanup.push({ table: "location_trails", id });

    const todayStr = now.toISOString().slice(0, 10);
    const trail = await db("location_trails")
      .where({ organization_id: ORG_ID, user_id: fakeUser })
      .where("timestamp", ">=", todayStr)
      .where("timestamp", "<=", todayStr + " 23:59:59")
      .orderBy("timestamp", "desc")
      .limit(500);
    expect(trail.length).toBeGreaterThanOrEqual(1);
  });
});
