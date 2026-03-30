// ============================================================================
// WORK ORDER SERVICE
// ============================================================================

import { v4 as uuidv4 } from "uuid";
import { getDB } from "../db/connection";
import { NotFoundError } from "../utils/errors";

const TABLE = "work_orders";

export async function list(orgId: number, options?: {
  page?: number; perPage?: number; status?: string; priority?: string;
  assignedTo?: number;
}) {
  const db = getDB();
  const page = options?.page || 1;
  const perPage = options?.perPage || 20;
  const offset = (page - 1) * perPage;

  let query = db(TABLE).where({ organization_id: orgId });
  if (options?.status) query = query.where({ status: options.status });
  if (options?.priority) query = query.where({ priority: options.priority });
  if (options?.assignedTo) query = query.where({ assigned_to: options.assignedTo });

  const [{ count: total }] = await query.clone().count("* as count");
  const data = await query.orderBy("created_at", "desc").limit(perPage).offset(offset);

  return { data, total: Number(total), page, perPage, totalPages: Math.ceil(Number(total) / perPage) };
}

export async function getById(orgId: number, id: string) {
  const db = getDB();
  const order = await db(TABLE).where({ id, organization_id: orgId }).first();
  if (!order) throw new NotFoundError("Work order", id);
  return order;
}

export async function create(orgId: number, createdBy: number, data: Record<string, any>) {
  const db = getDB();
  const id = uuidv4();
  const now = new Date();
  await db(TABLE).insert({
    id,
    organization_id: orgId,
    assigned_to: data.assigned_to,
    client_site_id: data.client_site_id || null,
    title: data.title,
    description: data.description || null,
    priority: data.priority || "medium",
    status: data.status || "pending",
    due_date: data.due_date || null,
    created_by: createdBy,
    created_at: now,
    updated_at: now,
  });
  return db(TABLE).where({ id }).first();
}

export async function update(orgId: number, id: string, data: Record<string, any>) {
  const db = getDB();
  const existing = await db(TABLE).where({ id, organization_id: orgId }).first();
  if (!existing) throw new NotFoundError("Work order", id);

  const updateData: Record<string, any> = { ...data, updated_at: new Date() };
  if (data.status === "completed" && existing.status !== "completed") {
    updateData.completed_at = new Date();
  }

  await db(TABLE).where({ id }).update(updateData);
  return db(TABLE).where({ id }).first();
}

export async function remove(orgId: number, id: string) {
  const db = getDB();
  const existing = await db(TABLE).where({ id, organization_id: orgId }).first();
  if (!existing) throw new NotFoundError("Work order", id);
  await db(TABLE).where({ id }).del();
  return { deleted: true };
}

export async function getMyOrders(orgId: number, userId: number, options?: {
  page?: number; perPage?: number; status?: string;
}) {
  const db = getDB();
  const page = options?.page || 1;
  const perPage = options?.perPage || 20;
  const offset = (page - 1) * perPage;

  let query = db(TABLE).where({ organization_id: orgId, assigned_to: userId });
  if (options?.status) query = query.where({ status: options.status });

  const [{ count: total }] = await query.clone().count("* as count");
  const data = await query.orderBy("created_at", "desc").limit(perPage).offset(offset);

  return { data, total: Number(total), page, perPage, totalPages: Math.ceil(Number(total) / perPage) };
}
