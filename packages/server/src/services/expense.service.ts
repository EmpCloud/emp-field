// ============================================================================
// EXPENSE SERVICE
// ============================================================================

import { v4 as uuidv4 } from "uuid";
import { getDB } from "../db/connection";
import { NotFoundError, ValidationError } from "../utils/errors";

const TABLE = "expenses";

export async function list(orgId: number, options?: {
  page?: number; perPage?: number; userId?: number; status?: string; expenseType?: string;
}) {
  const db = getDB();
  const page = options?.page || 1;
  const perPage = options?.perPage || 20;
  const offset = (page - 1) * perPage;

  let query = db(TABLE).where({ organization_id: orgId });
  if (options?.userId) query = query.where({ user_id: options.userId });
  if (options?.status) query = query.where({ status: options.status });
  if (options?.expenseType) query = query.where({ expense_type: options.expenseType });

  const [{ count: total }] = await query.clone().count("* as count");
  const data = await query.orderBy("created_at", "desc").limit(perPage).offset(offset);

  return { data, total: Number(total), page, perPage, totalPages: Math.ceil(Number(total) / perPage) };
}

export async function getById(orgId: number, id: string) {
  const db = getDB();
  const expense = await db(TABLE).where({ id, organization_id: orgId }).first();
  if (!expense) throw new NotFoundError("Expense", id);
  return expense;
}

export async function create(orgId: number, userId: number, data: Record<string, any>) {
  const db = getDB();
  const id = uuidv4();
  const now = new Date();
  await db(TABLE).insert({
    id,
    organization_id: orgId,
    user_id: userId,
    expense_type: data.expense_type,
    amount: data.amount,
    currency: data.currency || "INR",
    description: data.description || null,
    receipt_url: data.receipt_url || null,
    status: "draft",
    created_at: now,
    updated_at: now,
  });
  return db(TABLE).where({ id }).first();
}

export async function update(orgId: number, id: string, userId: number, data: Record<string, any>) {
  const db = getDB();
  const existing = await db(TABLE).where({ id, organization_id: orgId, user_id: userId }).first();
  if (!existing) throw new NotFoundError("Expense", id);
  if (existing.status !== "draft") {
    throw new ValidationError("Can only edit expenses in draft status");
  }
  await db(TABLE).where({ id }).update({ ...data, updated_at: new Date() });
  return db(TABLE).where({ id }).first();
}

export async function submit(orgId: number, id: string, userId: number) {
  const db = getDB();
  const existing = await db(TABLE).where({ id, organization_id: orgId, user_id: userId }).first();
  if (!existing) throw new NotFoundError("Expense", id);
  if (existing.status !== "draft") {
    throw new ValidationError("Can only submit expenses in draft status");
  }
  await db(TABLE).where({ id }).update({ status: "submitted", updated_at: new Date() });
  return db(TABLE).where({ id }).first();
}

export async function approve(orgId: number, id: string, approvedBy: number) {
  const db = getDB();
  const existing = await db(TABLE).where({ id, organization_id: orgId }).first();
  if (!existing) throw new NotFoundError("Expense", id);
  if (existing.status !== "submitted") {
    throw new ValidationError("Can only approve submitted expenses");
  }
  await db(TABLE).where({ id }).update({
    status: "approved",
    approved_by: approvedBy,
    approved_at: new Date(),
    updated_at: new Date(),
  });
  return db(TABLE).where({ id }).first();
}

export async function reject(orgId: number, id: string, approvedBy: number) {
  const db = getDB();
  const existing = await db(TABLE).where({ id, organization_id: orgId }).first();
  if (!existing) throw new NotFoundError("Expense", id);
  if (existing.status !== "submitted") {
    throw new ValidationError("Can only reject submitted expenses");
  }
  await db(TABLE).where({ id }).update({
    status: "rejected",
    approved_by: approvedBy,
    approved_at: new Date(),
    updated_at: new Date(),
  });
  return db(TABLE).where({ id }).first();
}

export async function remove(orgId: number, id: string, userId: number) {
  const db = getDB();
  const existing = await db(TABLE).where({ id, organization_id: orgId, user_id: userId }).first();
  if (!existing) throw new NotFoundError("Expense", id);
  if (existing.status !== "draft") {
    throw new ValidationError("Can only delete expenses in draft status");
  }
  await db(TABLE).where({ id }).del();
  return { deleted: true };
}
