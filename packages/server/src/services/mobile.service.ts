// ============================================================================
// MOBILE AGGREGATE SERVICE
// ============================================================================
//
// Endpoints optimized for mobile clients:
//   - today:  one-call digest for the field worker's home screen
//   - sync:   offline batch upload (check-ins, locations, expenses, mileage)
// ============================================================================

import { v4 as uuidv4 } from "uuid";
import { getDB } from "../db/connection";
import * as checkinService from "./checkin.service";

type SyncPayload = {
  checkins?: any[];
  locationPoints?: any[];
  expenses?: any[];
  mileage?: any[];
};

type SyncReport = {
  checkins: { accepted: number; failed: number };
  locationPoints: { accepted: number; failed: number };
  expenses: { accepted: number; failed: number };
  mileage: { accepted: number; failed: number };
};

export async function getToday(orgId: number, userId: number) {
  const db = getDB();
  const todayKey = new Date().toISOString().slice(0, 10);

  const [
    activeCheckin,
    todaysCheckins,
    assignedWorkOrders,
    todaysRoutes,
    unreadNotifications,
    pendingExpenses,
  ] = await Promise.all([
    db("checkins")
      .where({ organization_id: orgId, user_id: userId, status: "active" })
      .first(),
    db("checkins")
      .where({ organization_id: orgId, user_id: userId })
      .whereRaw("DATE(check_in_time) = ?", [todayKey])
      .orderBy("check_in_time", "desc"),
    db("work_orders")
      .where({ organization_id: orgId, assigned_to: userId })
      .whereIn("status", ["pending", "in_progress"])
      .orderBy("due_date", "asc"),
    db("daily_routes")
      .where({ organization_id: orgId, user_id: userId, route_date: todayKey }),
    db("notifications")
      .where({ organization_id: orgId, user_id: userId, status: "pending" })
      .count<{ count: number }[]>("* as count")
      .first(),
    db("expenses")
      .where({ organization_id: orgId, user_id: userId, status: "draft" })
      .count<{ count: number }[]>("* as count")
      .first(),
  ]);

  return {
    date: todayKey,
    activeCheckin,
    todaysCheckins,
    assignedWorkOrders,
    todaysRoutes,
    unreadNotifications: Number(unreadNotifications?.count ?? 0),
    pendingExpenses: Number(pendingExpenses?.count ?? 0),
  };
}

export async function sync(
  orgId: number,
  userId: number,
  payload: SyncPayload,
): Promise<SyncReport> {
  const db = getDB();
  const report: SyncReport = {
    checkins: { accepted: 0, failed: 0 },
    locationPoints: { accepted: 0, failed: 0 },
    expenses: { accepted: 0, failed: 0 },
    mileage: { accepted: 0, failed: 0 },
  };

  // Check-ins (create + optional close in the same payload)
  for (const c of payload.checkins ?? []) {
    try {
      const created = await checkinService.checkIn(orgId, userId, {
        client_site_id: c.client_site_id,
        check_in_lat: c.check_in_lat,
        check_in_lng: c.check_in_lng,
        photo_url: c.photo_url,
        notes: c.notes,
      });
      if (c.check_out_time && created?.id) {
        await checkinService.checkOut(orgId, userId, created.id, {
          check_out_lat: c.check_out_lat,
          check_out_lng: c.check_out_lng,
        });
      }
      report.checkins.accepted++;
    } catch {
      report.checkins.failed++;
    }
  }

  // Location points — bulk insert into location_trails
  if (payload.locationPoints?.length) {
    try {
      const rows = payload.locationPoints.map((p) => ({
        id: uuidv4(),
        organization_id: orgId,
        user_id: userId,
        latitude: p.latitude,
        longitude: p.longitude,
        accuracy: p.accuracy ?? null,
        recorded_at: new Date(p.timestamp),
      }));
      await db("location_trails").insert(rows);
      report.locationPoints.accepted = rows.length;
    } catch {
      report.locationPoints.failed = payload.locationPoints.length;
    }
  }

  // Expenses
  for (const e of payload.expenses ?? []) {
    try {
      await db("expenses").insert({
        id: uuidv4(),
        organization_id: orgId,
        user_id: userId,
        amount: e.amount,
        category: e.category,
        description: e.description ?? null,
        expense_date: e.expense_date,
        receipt_url: e.receipt_url ?? null,
        status: "draft",
        created_at: new Date(),
        updated_at: new Date(),
      });
      report.expenses.accepted++;
    } catch {
      report.expenses.failed++;
    }
  }

  // Mileage
  for (const m of payload.mileage ?? []) {
    try {
      await db("mileage_logs").insert({
        id: uuidv4(),
        organization_id: orgId,
        user_id: userId,
        distance_km: m.distance_km,
        start_location: m.start_location,
        end_location: m.end_location,
        trip_date: m.trip_date,
        purpose: m.purpose ?? null,
        created_at: new Date(),
        updated_at: new Date(),
      });
      report.mileage.accepted++;
    } catch {
      report.mileage.failed++;
    }
  }

  return report;
}
