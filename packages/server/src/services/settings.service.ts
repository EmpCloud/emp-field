// ============================================================================
// FIELD SETTINGS SERVICE
// ============================================================================

import { v4 as uuidv4 } from "uuid";
import { getDB } from "../db/connection";

const TABLE = "field_settings";

export async function get(orgId: number) {
  const db = getDB();
  let settings = await db(TABLE).where({ organization_id: orgId }).first();

  if (!settings) {
    // Create default settings
    const id = uuidv4();
    const now = new Date();
    await db(TABLE).insert({
      id,
      organization_id: orgId,
      check_in_radius_meters: 200,
      photo_required: false,
      auto_checkout_hours: 12,
      mileage_rate_per_km: 8.0,
      tracking_interval_seconds: 30,
      created_at: now,
      updated_at: now,
    });
    settings = await db(TABLE).where({ id }).first();
  }

  return settings;
}

export async function update(orgId: number, data: Record<string, any>) {
  const db = getDB();

  // Ensure settings exist
  await get(orgId);

  await db(TABLE).where({ organization_id: orgId }).update({
    ...data,
    updated_at: new Date(),
  });

  return db(TABLE).where({ organization_id: orgId }).first();
}
