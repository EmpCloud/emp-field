// ============================================================================
// VISIT LOG SERVICE
// ============================================================================

import { v4 as uuidv4 } from "uuid";
import { getDB } from "../db/connection";
import { NotFoundError } from "../utils/errors";

const TABLE = "visit_logs";

export async function list(orgId: number, options?: {
  userId?: number; clientSiteId?: string; page?: number; perPage?: number;
}) {
  const db = getDB();
  const page = options?.page || 1;
  const perPage = options?.perPage || 20;
  const offset = (page - 1) * perPage;

  let query = db(TABLE).where({ organization_id: orgId });
  if (options?.userId) query = query.where({ user_id: options.userId });
  if (options?.clientSiteId) query = query.where({ client_site_id: options.clientSiteId });

  const [{ count: total }] = await query.clone().count("* as count");
  const data = await query.orderBy("created_at", "desc").limit(perPage).offset(offset);

  const parsed = data.map((row: any) => ({
    ...row,
    photos: row.photos ? (typeof row.photos === "string" ? (typeof row.photos === "string" ? JSON.parse(row.photos) : row.photos) : row.photos) : null,
  }));

  return { data: parsed, total: Number(total), page, perPage, totalPages: Math.ceil(Number(total) / perPage) };
}

export async function getById(orgId: number, id: string) {
  const db = getDB();
  const visit = await db(TABLE).where({ id, organization_id: orgId }).first();
  if (!visit) throw new NotFoundError("Visit log", id);
  return { ...visit, photos: visit.photos ? (typeof visit.photos === "string" ? (typeof visit.photos === "string" ? JSON.parse(visit.photos) : visit.photos) : visit.photos) : null };
}

export async function create(orgId: number, userId: number, data: Record<string, any>) {
  const db = getDB();
  const id = uuidv4();
  await db(TABLE).insert({
    id,
    organization_id: orgId,
    user_id: userId,
    client_site_id: data.client_site_id || null,
    checkin_id: data.checkin_id || null,
    purpose: data.purpose || null,
    outcome: data.outcome || null,
    notes: data.notes || null,
    photos: data.photos ? JSON.stringify(data.photos) : null,
    duration_minutes: data.duration_minutes || null,
    created_at: new Date(),
  });
  const row = await db(TABLE).where({ id }).first();
  return { ...row, photos: row.photos ? (typeof row.photos === "string" ? JSON.parse(row.photos) : row.photos) : null };
}

export async function update(orgId: number, id: string, userId: number, data: Record<string, any>) {
  const db = getDB();
  const existing = await db(TABLE).where({ id, organization_id: orgId, user_id: userId }).first();
  if (!existing) throw new NotFoundError("Visit log", id);

  const updateData: Record<string, any> = { ...data };
  if (data.photos) {
    updateData.photos = JSON.stringify(data.photos);
  }

  await db(TABLE).where({ id }).update(updateData);
  const row = await db(TABLE).where({ id }).first();
  return { ...row, photos: row.photos ? (typeof row.photos === "string" ? JSON.parse(row.photos) : row.photos) : null };
}

export async function remove(orgId: number, id: string, userId: number) {
  const db = getDB();
  const existing = await db(TABLE).where({ id, organization_id: orgId, user_id: userId }).first();
  if (!existing) throw new NotFoundError("Visit log", id);
  await db(TABLE).where({ id }).del();
  return { deleted: true };
}
