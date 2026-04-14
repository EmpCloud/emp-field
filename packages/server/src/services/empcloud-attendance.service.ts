// ============================================================================
// EMPCLOUD ATTENDANCE INTEGRATION
// ============================================================================
//
// Bridges field check-ins to EmpCloud core's attendance table. Called from
// checkin.service after a successful field check-in/check-out so the central
// HRMS stays authoritative for "who was working today".
//
// Writes directly to the EmpCloud DB (read+write grant required). Failures
// are logged but never crash the field check-in flow — attendance sync is
// best-effort and can be reconciled via the webhook channel.
// ============================================================================

import { getEmpCloudDB } from "../db/empcloud";
import { logger } from "../utils/logger";

type SyncCheckInArgs = {
  orgId: number;
  userId: number;
  checkInTime: Date;
  latitude?: number | null;
  longitude?: number | null;
  source?: string;
};

type SyncCheckOutArgs = {
  orgId: number;
  userId: number;
  checkInTime: Date;
  checkOutTime: Date;
  latitude?: number | null;
  longitude?: number | null;
};

const TABLE = "attendance";

export async function syncCheckIn(args: SyncCheckInArgs): Promise<void> {
  try {
    const db = getEmpCloudDB();
    const dateKey = args.checkInTime.toISOString().slice(0, 10);

    // Idempotent upsert by (org_id, user_id, date).
    const existing = await db(TABLE)
      .where({
        organization_id: args.orgId,
        user_id: args.userId,
        attendance_date: dateKey,
      })
      .first();

    if (existing) {
      if (!existing.check_in_time) {
        await db(TABLE)
          .where({ id: existing.id })
          .update({
            check_in_time: args.checkInTime,
            check_in_latitude: args.latitude ?? null,
            check_in_longitude: args.longitude ?? null,
            source: args.source || "field",
            updated_at: new Date(),
          });
      }
      return;
    }

    await db(TABLE).insert({
      organization_id: args.orgId,
      user_id: args.userId,
      attendance_date: dateKey,
      check_in_time: args.checkInTime,
      check_in_latitude: args.latitude ?? null,
      check_in_longitude: args.longitude ?? null,
      status: "present",
      source: args.source || "field",
      created_at: new Date(),
      updated_at: new Date(),
    });
  } catch (err) {
    logger.warn(`attendance sync (check-in) failed: ${(err as Error).message}`);
  }
}

export async function syncCheckOut(args: SyncCheckOutArgs): Promise<void> {
  try {
    const db = getEmpCloudDB();
    const dateKey = args.checkInTime.toISOString().slice(0, 10);

    await db(TABLE)
      .where({
        organization_id: args.orgId,
        user_id: args.userId,
        attendance_date: dateKey,
      })
      .update({
        check_out_time: args.checkOutTime,
        check_out_latitude: args.latitude ?? null,
        check_out_longitude: args.longitude ?? null,
        updated_at: new Date(),
      });
  } catch (err) {
    logger.warn(`attendance sync (check-out) failed: ${(err as Error).message}`);
  }
}
