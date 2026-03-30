// ============================================================================
// ROUTE SERVICE (Daily Routes & Route Stops)
// ============================================================================

import { v4 as uuidv4 } from "uuid";
import { getDB } from "../db/connection";
import { NotFoundError } from "../utils/errors";

const ROUTES_TABLE = "daily_routes";
const STOPS_TABLE = "route_stops";

export async function list(orgId: number, options?: {
  userId?: number; page?: number; perPage?: number; date?: string; status?: string;
}) {
  const db = getDB();
  const page = options?.page || 1;
  const perPage = options?.perPage || 20;
  const offset = (page - 1) * perPage;

  let query = db(ROUTES_TABLE).where({ organization_id: orgId });
  if (options?.userId) query = query.where({ user_id: options.userId });
  if (options?.date) query = query.where({ date: options.date });
  if (options?.status) query = query.where({ status: options.status });

  const [{ count: total }] = await query.clone().count("* as count");
  const data = await query.orderBy("date", "desc").limit(perPage).offset(offset);

  return { data, total: Number(total), page, perPage, totalPages: Math.ceil(Number(total) / perPage) };
}

export async function getById(orgId: number, id: string) {
  const db = getDB();
  const route = await db(ROUTES_TABLE).where({ id, organization_id: orgId }).first();
  if (!route) throw new NotFoundError("Daily route", id);

  const stops = await db(STOPS_TABLE)
    .where({ daily_route_id: id })
    .orderBy("sequence_order", "asc");

  return { ...route, stops };
}

export async function create(orgId: number, userId: number, data: {
  date: string;
  stops: Array<{ client_site_id?: string | null; sequence_order: number; planned_arrival?: string | null }>;
}) {
  const db = getDB();
  const routeId = uuidv4();
  const now = new Date();

  await db(ROUTES_TABLE).insert({
    id: routeId,
    organization_id: orgId,
    user_id: userId,
    date: data.date,
    status: "planned",
    total_distance_km: 0,
    created_at: now,
    updated_at: now,
  });

  const stopRows = data.stops.map((stop) => ({
    id: uuidv4(),
    daily_route_id: routeId,
    client_site_id: stop.client_site_id || null,
    sequence_order: stop.sequence_order,
    planned_arrival: stop.planned_arrival || null,
    status: "pending",
    created_at: now,
  }));

  if (stopRows.length > 0) {
    await db.batchInsert(STOPS_TABLE, stopRows, 100);
  }

  return getById(orgId, routeId);
}

export async function updateStatus(orgId: number, id: string, status: string) {
  const db = getDB();
  const existing = await db(ROUTES_TABLE).where({ id, organization_id: orgId }).first();
  if (!existing) throw new NotFoundError("Daily route", id);

  await db(ROUTES_TABLE).where({ id }).update({ status, updated_at: new Date() });
  return getById(orgId, id);
}

export async function reorderStops(orgId: number, routeId: string, stops: Array<{ id: string; sequence_order: number }>) {
  const db = getDB();
  const existing = await db(ROUTES_TABLE).where({ id: routeId, organization_id: orgId }).first();
  if (!existing) throw new NotFoundError("Daily route", routeId);

  for (const stop of stops) {
    await db(STOPS_TABLE)
      .where({ id: stop.id, daily_route_id: routeId })
      .update({ sequence_order: stop.sequence_order });
  }

  return getById(orgId, routeId);
}

export async function updateStop(orgId: number, routeId: string, stopId: string, data: Record<string, any>) {
  const db = getDB();
  const route = await db(ROUTES_TABLE).where({ id: routeId, organization_id: orgId }).first();
  if (!route) throw new NotFoundError("Daily route", routeId);

  const stop = await db(STOPS_TABLE).where({ id: stopId, daily_route_id: routeId }).first();
  if (!stop) throw new NotFoundError("Route stop", stopId);

  await db(STOPS_TABLE).where({ id: stopId }).update(data);
  return db(STOPS_TABLE).where({ id: stopId }).first();
}

export async function remove(orgId: number, id: string) {
  const db = getDB();
  const existing = await db(ROUTES_TABLE).where({ id, organization_id: orgId }).first();
  if (!existing) throw new NotFoundError("Daily route", id);
  await db(ROUTES_TABLE).where({ id }).del(); // CASCADE deletes stops
  return { deleted: true };
}
