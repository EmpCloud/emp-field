// ============================================================================
// FIELD DEVICE REGISTRATION SERVICE
// ============================================================================

import { v4 as uuidv4 } from "uuid";
import { getDB } from "../db/connection";
import { NotFoundError } from "../utils/errors";

const TABLE = "field_devices";

export type RegisterDeviceArgs = {
  platform: "ios" | "android" | "web";
  push_token: string;
  device_id?: string;
  device_model?: string;
  app_version?: string;
  os_version?: string;
};

export async function register(orgId: number, userId: number, args: RegisterDeviceArgs) {
  const db = getDB();

  // Upsert by (org_id, user_id, push_token).
  const existing = await db(TABLE)
    .where({
      organization_id: orgId,
      user_id: userId,
      push_token: args.push_token,
    })
    .first();

  const now = new Date();

  if (existing) {
    await db(TABLE).where({ id: existing.id }).update({
      platform: args.platform,
      device_id: args.device_id || null,
      device_model: args.device_model || null,
      app_version: args.app_version || null,
      os_version: args.os_version || null,
      is_active: true,
      last_seen_at: now,
      updated_at: now,
    });
    return db(TABLE).where({ id: existing.id }).first();
  }

  const id = uuidv4();
  await db(TABLE).insert({
    id,
    organization_id: orgId,
    user_id: userId,
    platform: args.platform,
    push_token: args.push_token,
    device_id: args.device_id || null,
    device_model: args.device_model || null,
    app_version: args.app_version || null,
    os_version: args.os_version || null,
    is_active: true,
    last_seen_at: now,
    created_at: now,
    updated_at: now,
  });
  return db(TABLE).where({ id }).first();
}

export async function listForUser(orgId: number, userId: number) {
  const db = getDB();
  return db(TABLE)
    .where({ organization_id: orgId, user_id: userId, is_active: true })
    .orderBy("last_seen_at", "desc");
}

export async function unregister(orgId: number, userId: number, deviceId: string) {
  const db = getDB();
  const row = await db(TABLE)
    .where({ id: deviceId, organization_id: orgId, user_id: userId })
    .first();
  if (!row) throw new NotFoundError("Device", deviceId);
  await db(TABLE).where({ id: deviceId }).update({
    is_active: false,
    updated_at: new Date(),
  });
}

export async function heartbeat(orgId: number, userId: number, pushToken: string) {
  const db = getDB();
  await db(TABLE)
    .where({ organization_id: orgId, user_id: userId, push_token: pushToken })
    .update({ last_seen_at: new Date() });
}
