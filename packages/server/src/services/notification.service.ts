// ============================================================================
// NOTIFICATION SERVICE
// ============================================================================

import { v4 as uuidv4 } from "uuid";
import { getDB } from "../db/connection";

const TABLE = "notifications";

export async function list(orgId: number, userId: number, options?: {
  page?: number; perPage?: number; unreadOnly?: boolean;
}) {
  const db = getDB();
  const page = options?.page || 1;
  const perPage = options?.perPage || 20;
  const offset = (page - 1) * perPage;

  let query = db(TABLE).where({ organization_id: orgId, user_id: userId });
  if (options?.unreadOnly) query = query.where({ is_read: false });

  const [{ count: total }] = await query.clone().count("* as count");
  const data = await query.orderBy("created_at", "desc").limit(perPage).offset(offset);

  return { data, total: Number(total), page, perPage, totalPages: Math.ceil(Number(total) / perPage) };
}

export async function markRead(orgId: number, userId: number, id: string) {
  const db = getDB();
  await db(TABLE)
    .where({ id, organization_id: orgId, user_id: userId })
    .update({ is_read: true, read_at: new Date() });
  return db(TABLE).where({ id }).first();
}

export async function markAllRead(orgId: number, userId: number) {
  const db = getDB();
  const updated = await db(TABLE)
    .where({ organization_id: orgId, user_id: userId, is_read: false })
    .update({ is_read: true, read_at: new Date() });
  return { updated };
}

export async function create(orgId: number, data: {
  user_id: number; title: string; body?: string; type?: string;
  reference_type?: string; reference_id?: string;
}) {
  const db = getDB();
  const id = uuidv4();
  await db(TABLE).insert({
    id,
    organization_id: orgId,
    user_id: data.user_id,
    title: data.title,
    body: data.body || null,
    type: data.type || null,
    reference_type: data.reference_type || null,
    reference_id: data.reference_id || null,
    is_read: false,
    created_at: new Date(),
  });
  return db(TABLE).where({ id }).first();
}

export async function getUnreadCount(orgId: number, userId: number) {
  const db = getDB();
  const [{ count }] = await db(TABLE)
    .where({ organization_id: orgId, user_id: userId, is_read: false })
    .count("* as count");
  return { count: Number(count) };
}
