// ============================================================================
// GEO FENCE SERVICE
// ============================================================================

import { v4 as uuidv4 } from "uuid";
import { getDB } from "../db/connection";
import { NotFoundError } from "../utils/errors";
import { isPointInCircle, isPointInPolygon } from "../utils/geo";

const TABLE = "geo_fences";
const EVENTS_TABLE = "geo_fence_events";

export async function list(orgId: number, options?: { page?: number; perPage?: number; active?: boolean }) {
  const db = getDB();
  const page = options?.page || 1;
  const perPage = options?.perPage || 20;
  const offset = (page - 1) * perPage;

  let query = db(TABLE).where({ organization_id: orgId });
  if (options?.active !== undefined) query = query.where({ is_active: options.active });

  const [{ count: total }] = await query.clone().count("* as count");
  const data = await query.orderBy("created_at", "desc").limit(perPage).offset(offset);

  // Parse polygon_coords JSON
  const parsed = data.map((row: any) => ({
    ...row,
    polygon_coords: row.polygon_coords ? JSON.parse(row.polygon_coords) : null,
  }));

  return { data: parsed, total: Number(total), page, perPage, totalPages: Math.ceil(Number(total) / perPage) };
}

export async function getById(orgId: number, id: string) {
  const db = getDB();
  const fence = await db(TABLE).where({ id, organization_id: orgId }).first();
  if (!fence) throw new NotFoundError("Geo fence", id);
  return {
    ...fence,
    polygon_coords: fence.polygon_coords ? JSON.parse(fence.polygon_coords) : null,
  };
}

export async function create(orgId: number, data: Record<string, any>) {
  const db = getDB();
  const id = uuidv4();
  const now = new Date();
  await db(TABLE).insert({
    id,
    organization_id: orgId,
    client_site_id: data.client_site_id || null,
    name: data.name,
    type: data.type,
    center_lat: data.center_lat || null,
    center_lng: data.center_lng || null,
    radius_meters: data.radius_meters || null,
    polygon_coords: data.polygon_coords ? JSON.stringify(data.polygon_coords) : null,
    is_active: data.is_active ?? true,
    created_at: now,
    updated_at: now,
  });
  const row = await db(TABLE).where({ id }).first();
  return { ...row, polygon_coords: row.polygon_coords ? JSON.parse(row.polygon_coords) : null };
}

export async function update(orgId: number, id: string, data: Record<string, any>) {
  const db = getDB();
  const existing = await db(TABLE).where({ id, organization_id: orgId }).first();
  if (!existing) throw new NotFoundError("Geo fence", id);

  const updateData: Record<string, any> = { ...data, updated_at: new Date() };
  if (data.polygon_coords) {
    updateData.polygon_coords = JSON.stringify(data.polygon_coords);
  }

  await db(TABLE).where({ id }).update(updateData);
  const row = await db(TABLE).where({ id }).first();
  return { ...row, polygon_coords: row.polygon_coords ? JSON.parse(row.polygon_coords) : null };
}

export async function remove(orgId: number, id: string) {
  const db = getDB();
  const existing = await db(TABLE).where({ id, organization_id: orgId }).first();
  if (!existing) throw new NotFoundError("Geo fence", id);
  await db(TABLE).where({ id }).del();
  return { deleted: true };
}

export async function checkPointInFence(orgId: number, geoFenceId: string, lat: number, lng: number) {
  const fence = await getById(orgId, geoFenceId);
  let inside = false;

  if (fence.type === "circle" && fence.center_lat && fence.center_lng && fence.radius_meters) {
    inside = isPointInCircle(lat, lng, Number(fence.center_lat), Number(fence.center_lng), Number(fence.radius_meters));
  } else if (fence.type === "polygon" && fence.polygon_coords) {
    inside = isPointInPolygon(lat, lng, fence.polygon_coords);
  }

  return { geo_fence_id: geoFenceId, latitude: lat, longitude: lng, inside };
}

export async function recordEvent(orgId: number, userId: number, data: {
  geo_fence_id: string; event_type: "entry" | "exit"; latitude: number; longitude: number;
}) {
  const db = getDB();
  const id = uuidv4();
  await db(EVENTS_TABLE).insert({
    id,
    organization_id: orgId,
    user_id: userId,
    geo_fence_id: data.geo_fence_id,
    event_type: data.event_type,
    latitude: data.latitude,
    longitude: data.longitude,
    timestamp: new Date(),
    created_at: new Date(),
  });
  return db(EVENTS_TABLE).where({ id }).first();
}

export async function getEvents(orgId: number, options?: {
  userId?: number; geoFenceId?: string; page?: number; perPage?: number;
}) {
  const db = getDB();
  const page = options?.page || 1;
  const perPage = options?.perPage || 20;
  const offset = (page - 1) * perPage;

  let query = db(EVENTS_TABLE).where({ organization_id: orgId });
  if (options?.userId) query = query.where({ user_id: options.userId });
  if (options?.geoFenceId) query = query.where({ geo_fence_id: options.geoFenceId });

  const [{ count: total }] = await query.clone().count("* as count");
  const data = await query.orderBy("timestamp", "desc").limit(perPage).offset(offset);

  return { data, total: Number(total), page, perPage, totalPages: Math.ceil(Number(total) / perPage) };
}
