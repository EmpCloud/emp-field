// ============================================================================
// ANALYTICS SERVICE
// ============================================================================

import { getDB } from "../db/connection";

export async function getDashboardKPIs(orgId: number) {
  const db = getDB();
  const today = new Date().toISOString().slice(0, 10);

  const [activeCheckins] = await db("checkins")
    .where({ organization_id: orgId, status: "active" })
    .count("* as count");

  const [todayCheckins] = await db("checkins")
    .where({ organization_id: orgId })
    .whereRaw("DATE(check_in_time) = ?", [today])
    .count("* as count");

  const [totalSites] = await db("client_sites")
    .where({ organization_id: orgId, is_active: true })
    .count("* as count");

  const [pendingOrders] = await db("work_orders")
    .where({ organization_id: orgId })
    .whereIn("status", ["pending", "in_progress"])
    .count("* as count");

  const [pendingExpenses] = await db("expenses")
    .where({ organization_id: orgId, status: "submitted" })
    .count("* as count");

  const [todayVisits] = await db("visit_logs")
    .where({ organization_id: orgId })
    .whereRaw("DATE(created_at) = ?", [today])
    .count("* as count");

  return {
    active_checkins: Number(activeCheckins.count),
    today_checkins: Number(todayCheckins.count),
    total_active_sites: Number(totalSites.count),
    pending_work_orders: Number(pendingOrders.count),
    pending_expenses: Number(pendingExpenses.count),
    today_visits: Number(todayVisits.count),
  };
}

export async function getProductivity(orgId: number, options?: {
  startDate?: string; endDate?: string;
}) {
  const db = getDB();
  let query = db("checkins")
    .where({ organization_id: orgId, status: "completed" })
    .select(
      db.raw("DATE(check_in_time) as date"),
      db.raw("COUNT(*) as total_checkins"),
      db.raw("COUNT(DISTINCT user_id) as unique_users"),
      db.raw("COALESCE(AVG(duration_minutes), 0) as avg_duration_minutes"),
    )
    .groupByRaw("DATE(check_in_time)")
    .orderByRaw("DATE(check_in_time) DESC");

  if (options?.startDate) query = query.where("check_in_time", ">=", options.startDate);
  if (options?.endDate) query = query.where("check_in_time", "<=", options.endDate + " 23:59:59");

  const data = await query.limit(30);
  return data;
}

export async function getCoverage(orgId: number) {
  const db = getDB();
  const thirtyDaysAgo = new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toISOString().slice(0, 10);

  // Sites with at least one visit in last 30 days
  const visitedSites = await db("visit_logs")
    .where({ organization_id: orgId })
    .where("created_at", ">=", thirtyDaysAgo)
    .countDistinct("client_site_id as count")
    .first();

  const [totalSites] = await db("client_sites")
    .where({ organization_id: orgId, is_active: true })
    .count("* as count");

  const total = Number(totalSites.count);
  const visited = Number(visitedSites?.count || 0);

  return {
    total_sites: total,
    visited_sites_30d: visited,
    coverage_percent: total > 0 ? Math.round((visited / total) * 100) : 0,
  };
}
