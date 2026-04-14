// ============================================================================
// CHECK-IN SERVICE
// ============================================================================

import { v4 as uuidv4 } from "uuid";
import { getDB } from "../db/connection";
import { NotFoundError, ValidationError } from "../utils/errors";
import * as attendanceSync from "./empcloud-attendance.service";

const TABLE = "checkins";

export async function checkIn(orgId: number, userId: number, data: Record<string, any>) {
  const db = getDB();

  // Check for existing active check-in
  const active = await db(TABLE)
    .where({ organization_id: orgId, user_id: userId, status: "active" })
    .first();
  if (active) {
    throw new ValidationError("You already have an active check-in. Please check out first.");
  }

  const id = uuidv4();
  const now = new Date();
  await db(TABLE).insert({
    id,
    organization_id: orgId,
    user_id: userId,
    client_site_id: data.client_site_id || null,
    check_in_time: now,
    check_in_lat: data.check_in_lat,
    check_in_lng: data.check_in_lng,
    photo_url: data.photo_url || null,
    notes: data.notes || null,
    status: "active",
    created_at: now,
    updated_at: now,
  });

  // Best-effort sync to EmpCloud attendance.
  await attendanceSync.syncCheckIn({
    orgId,
    userId,
    checkInTime: now,
    latitude: data.check_in_lat,
    longitude: data.check_in_lng,
    source: "field",
  });

  return db(TABLE).where({ id }).first();
}

export async function checkOut(orgId: number, userId: number, checkinId: string, data: Record<string, any>) {
  const db = getDB();

  const checkin = await db(TABLE)
    .where({ id: checkinId, organization_id: orgId, user_id: userId, status: "active" })
    .first();
  if (!checkin) {
    throw new NotFoundError("Active check-in", checkinId);
  }

  const now = new Date();
  const durationMs = now.getTime() - new Date(checkin.check_in_time).getTime();
  const durationMinutes = Math.round(durationMs / 60000);

  await db(TABLE).where({ id: checkinId }).update({
    check_out_time: now,
    check_out_lat: data.check_out_lat,
    check_out_lng: data.check_out_lng,
    notes: data.notes || checkin.notes,
    status: "completed",
    duration_minutes: durationMinutes,
    updated_at: now,
  });

  await attendanceSync.syncCheckOut({
    orgId,
    userId,
    checkInTime: new Date(checkin.check_in_time),
    checkOutTime: now,
    latitude: data.check_out_lat,
    longitude: data.check_out_lng,
  });

  return db(TABLE).where({ id: checkinId }).first();
}

export async function getActive(orgId: number, userId: number) {
  const db = getDB();
  return db(TABLE)
    .where({ organization_id: orgId, user_id: userId, status: "active" })
    .first() || null;
}

export async function getToday(orgId: number, userId: number) {
  const db = getDB();
  const today = new Date().toISOString().slice(0, 10);
  return db(TABLE)
    .where({ organization_id: orgId, user_id: userId })
    .whereRaw("DATE(check_in_time) = ?", [today])
    .orderBy("check_in_time", "desc");
}

export async function list(orgId: number, options?: {
  userId?: number; page?: number; perPage?: number; status?: string;
  startDate?: string; endDate?: string;
}) {
  const db = getDB();
  const page = options?.page || 1;
  const perPage = options?.perPage || 20;
  const offset = (page - 1) * perPage;

  let query = db(TABLE).where({ organization_id: orgId });
  if (options?.userId) query = query.where({ user_id: options.userId });
  if (options?.status) query = query.where({ status: options.status });
  if (options?.startDate) query = query.where("check_in_time", ">=", options.startDate);
  if (options?.endDate) query = query.where("check_in_time", "<=", options.endDate + " 23:59:59");

  const [{ count: total }] = await query.clone().count("* as count");
  const data = await query.orderBy("check_in_time", "desc").limit(perPage).offset(offset);

  return { data, total: Number(total), page, perPage, totalPages: Math.ceil(Number(total) / perPage) };
}

export async function getById(orgId: number, id: string) {
  const db = getDB();
  const checkin = await db(TABLE).where({ id, organization_id: orgId }).first();
  if (!checkin) throw new NotFoundError("Check-in", id);
  return checkin;
}
