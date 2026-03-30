// ============================================================================
// LOCATION SERVICE
// ============================================================================

import { v4 as uuidv4 } from "uuid";
import { getDB } from "../db/connection";

const TABLE = "location_trails";

export async function batchUpload(orgId: number, userId: number, points: Array<{
  latitude: number; longitude: number; accuracy?: number | null;
  battery_percent?: number | null; timestamp: string | Date;
}>) {
  const db = getDB();
  const rows = points.map((p) => ({
    id: uuidv4(),
    organization_id: orgId,
    user_id: userId,
    latitude: p.latitude,
    longitude: p.longitude,
    accuracy: p.accuracy ?? null,
    battery_percent: p.battery_percent ?? null,
    timestamp: new Date(p.timestamp),
    created_at: new Date(),
  }));

  await db.batchInsert(TABLE, rows, 500);
  return { uploaded: rows.length };
}

export async function getTrail(orgId: number, userId: number, options?: {
  startDate?: string; endDate?: string; limit?: number;
}) {
  const db = getDB();
  let query = db(TABLE).where({ organization_id: orgId, user_id: userId });
  if (options?.startDate) query = query.where("timestamp", ">=", options.startDate);
  if (options?.endDate) query = query.where("timestamp", "<=", options.endDate + " 23:59:59");
  return query.orderBy("timestamp", "desc").limit(options?.limit || 500);
}

export async function getLiveLocations(orgId: number) {
  const db = getDB();
  // Get latest location for each user in the org (last 15 minutes)
  const fifteenMinAgo = new Date(Date.now() - 15 * 60 * 1000);
  const subquery = db(TABLE)
    .where({ organization_id: orgId })
    .where("timestamp", ">=", fifteenMinAgo)
    .select("user_id")
    .max("timestamp as max_ts")
    .groupBy("user_id");

  const results = await db(TABLE)
    .where({ organization_id: orgId })
    .whereIn(["user_id", "timestamp"], function () {
      this.select("user_id", db.raw("MAX(timestamp)"))
        .from(TABLE)
        .where({ organization_id: orgId })
        .where("timestamp", ">=", fifteenMinAgo)
        .groupBy("user_id");
    });

  return results;
}
