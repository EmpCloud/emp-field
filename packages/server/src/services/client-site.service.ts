// ============================================================================
// CLIENT SITE SERVICE
// ============================================================================

import { v4 as uuidv4 } from "uuid";
import { getDB } from "../db/connection";
import { NotFoundError } from "../utils/errors";

const TABLE = "client_sites";

export async function list(orgId: number, options?: { page?: number; perPage?: number; category?: string; active?: boolean }) {
  const db = getDB();
  const page = options?.page || 1;
  const perPage = options?.perPage || 20;
  const offset = (page - 1) * perPage;

  let query = db(TABLE).where({ organization_id: orgId });
  if (options?.category) query = query.where({ category: options.category });
  if (options?.active !== undefined) query = query.where({ is_active: options.active });

  const [{ count: total }] = await query.clone().count("* as count");
  const data = await query.orderBy("created_at", "desc").limit(perPage).offset(offset);

  return { data, total: Number(total), page, perPage, totalPages: Math.ceil(Number(total) / perPage) };
}

export async function getById(orgId: number, id: string) {
  const db = getDB();
  const site = await db(TABLE).where({ id, organization_id: orgId }).first();
  if (!site) throw new NotFoundError("Client site", id);
  return site;
}

export async function create(orgId: number, data: Record<string, any>) {
  const db = getDB();
  const id = uuidv4();
  const now = new Date();
  await db(TABLE).insert({ id, organization_id: orgId, ...data, created_at: now, updated_at: now });
  return db(TABLE).where({ id }).first();
}

export async function update(orgId: number, id: string, data: Record<string, any>) {
  const db = getDB();
  const existing = await db(TABLE).where({ id, organization_id: orgId }).first();
  if (!existing) throw new NotFoundError("Client site", id);
  await db(TABLE).where({ id }).update({ ...data, updated_at: new Date() });
  return db(TABLE).where({ id }).first();
}

export async function remove(orgId: number, id: string) {
  const db = getDB();
  const existing = await db(TABLE).where({ id, organization_id: orgId }).first();
  if (!existing) throw new NotFoundError("Client site", id);
  await db(TABLE).where({ id }).del();
  return { deleted: true };
}
