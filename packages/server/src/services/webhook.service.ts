// ============================================================================
// EMPCLOUD WEBHOOK EVENT HANDLERS
// ============================================================================
//
// Handles cross-module events fired by EmpCloud core. Each event type is
// routed to a dedicated handler that updates local state (or invalidates
// caches) so emp-field stays consistent with the source of truth.
// ============================================================================

import { logger } from "../utils/logger";
import { getDB } from "../db/connection";

export type EmpCloudEvent =
  | { type: "user.created"; payload: UserPayload }
  | { type: "user.updated"; payload: UserPayload }
  | { type: "user.deactivated"; payload: { id: number; organization_id: number } }
  | { type: "org.updated"; payload: OrgPayload }
  | { type: "subscription.updated"; payload: SubscriptionPayload };

interface UserPayload {
  id: number;
  organization_id: number;
  email: string;
  first_name: string;
  last_name: string;
  role: string;
  status: number;
}

interface OrgPayload {
  id: number;
  name: string;
  is_active: boolean;
}

interface SubscriptionPayload {
  org_id: number;
  module_slug: string;
  status: "active" | "suspended" | "cancelled";
}

export async function handleEvent(event: EmpCloudEvent): Promise<void> {
  logger.info(`webhook received: ${event.type}`);
  switch (event.type) {
    case "user.created":
    case "user.updated":
      await onUserUpsert(event.payload);
      break;
    case "user.deactivated":
      await onUserDeactivated(event.payload);
      break;
    case "org.updated":
      await onOrgUpdated(event.payload);
      break;
    case "subscription.updated":
      await onSubscriptionUpdated(event.payload);
      break;
    default:
      logger.warn(`Unhandled event type: ${(event as any).type}`);
  }
}

async function onUserUpsert(user: UserPayload): Promise<void> {
  // emp-field relies on EmpCloud for the user table; nothing to persist here.
  // Hook left for cache invalidation / notification prefs in the future.
  logger.info(`user upserted: ${user.email} (org ${user.organization_id})`);
}

async function onUserDeactivated(payload: { id: number; organization_id: number }): Promise<void> {
  const db = getDB();
  // Close any open check-ins for the deactivated user.
  await db("checkins")
    .where({ organization_id: payload.organization_id, user_id: payload.id, status: "active" })
    .update({ status: "force_closed", updated_at: new Date() });
  logger.info(`deactivated user ${payload.id}: closed open check-ins`);
}

async function onOrgUpdated(org: OrgPayload): Promise<void> {
  logger.info(`org updated: ${org.name} (active=${org.is_active})`);
}

async function onSubscriptionUpdated(sub: SubscriptionPayload): Promise<void> {
  if (sub.module_slug !== "field") return;
  logger.info(`field subscription for org ${sub.org_id}: ${sub.status}`);
  // Subscription cache in rbac.middleware.ts auto-refreshes after TTL (60s).
}
