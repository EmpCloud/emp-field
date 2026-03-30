// ============================================================================
// MILEAGE SERVICE
// ============================================================================

import { v4 as uuidv4 } from "uuid";
import { getDB } from "../db/connection";
import { haversineDistance } from "../utils/geo";

const TABLE = "mileage_logs";

export async function list(orgId: number, userId: number, options?: {
  page?: number; perPage?: number; startDate?: string; endDate?: string;
}) {
  const db = getDB();
  const page = options?.page || 1;
  const perPage = options?.perPage || 20;
  const offset = (page - 1) * perPage;

  let query = db(TABLE).where({ organization_id: orgId, user_id: userId });
  if (options?.startDate) query = query.where("date", ">=", options.startDate);
  if (options?.endDate) query = query.where("date", "<=", options.endDate);

  const [{ count: total }] = await query.clone().count("* as count");
  const data = await query.orderBy("date", "desc").limit(perPage).offset(offset);

  return { data, total: Number(total), page, perPage, totalPages: Math.ceil(Number(total) / perPage) };
}

export async function create(orgId: number, userId: number, data: Record<string, any>) {
  const db = getDB();

  // Get mileage rate from settings
  const settings = await db("field_settings").where({ organization_id: orgId }).first();
  const ratePerKm = settings?.mileage_rate_per_km || 8;

  const distance = data.distance_km || haversineDistance(
    data.start_lat, data.start_lng, data.end_lat, data.end_lng
  );
  const reimbursement = distance * Number(ratePerKm);

  const id = uuidv4();
  await db(TABLE).insert({
    id,
    organization_id: orgId,
    user_id: userId,
    date: data.date,
    distance_km: distance,
    start_lat: data.start_lat,
    start_lng: data.start_lng,
    end_lat: data.end_lat,
    end_lng: data.end_lng,
    reimbursement_amount: reimbursement,
    created_at: new Date(),
  });

  return db(TABLE).where({ id }).first();
}

export async function getSummary(orgId: number, userId: number, options?: {
  startDate?: string; endDate?: string;
}) {
  const db = getDB();
  let query = db(TABLE).where({ organization_id: orgId, user_id: userId });
  if (options?.startDate) query = query.where("date", ">=", options.startDate);
  if (options?.endDate) query = query.where("date", "<=", options.endDate);

  const [result] = await query
    .select(
      db.raw("COUNT(*) as total_trips"),
      db.raw("COALESCE(SUM(distance_km), 0) as total_distance_km"),
      db.raw("COALESCE(SUM(reimbursement_amount), 0) as total_reimbursement"),
    );

  return {
    total_trips: Number(result.total_trips),
    total_distance_km: Number(result.total_distance_km),
    total_reimbursement: Number(result.total_reimbursement),
  };
}
