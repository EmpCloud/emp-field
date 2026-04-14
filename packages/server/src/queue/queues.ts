// ============================================================================
// BULLMQ QUEUE DEFINITIONS
// ============================================================================
//
// Queues used by emp-field for async work:
//   - location-trail: batch-process GPS pings from mobile clients
//   - notification: deliver in-app/email/push notifications
//   - geofence-event: handle geofence entry/exit side effects
//   - report: generate analytics exports
// ============================================================================

import { Queue, QueueOptions } from "bullmq";
import { getRedis } from "./connection";
import { logger } from "../utils/logger";

const defaultOpts: QueueOptions = {
  get connection() {
    return getRedis();
  },
  defaultJobOptions: {
    attempts: 5,
    backoff: { type: "exponential", delay: 2000 },
    removeOnComplete: { count: 500 },
    removeOnFail: { count: 1000 },
  },
};

export type LocationTrailJob = {
  organizationId: number;
  userId: number;
  points: Array<{
    latitude: number;
    longitude: number;
    accuracy?: number;
    timestamp: string;
  }>;
};

export type NotificationJob = {
  organizationId: number;
  userId: number;
  title: string;
  body: string;
  channel: "in_app" | "email" | "push";
  data?: Record<string, unknown>;
};

export type GeofenceEventJob = {
  organizationId: number;
  userId: number;
  geoFenceId: string;
  event: "enter" | "exit";
  latitude: number;
  longitude: number;
  occurredAt: string;
};

export type ReportJob = {
  organizationId: number;
  userId: number;
  reportType: "attendance" | "mileage" | "expense" | "productivity";
  from: string;
  to: string;
};

export const locationTrailQueue = new Queue<LocationTrailJob>("location-trail", defaultOpts);
export const notificationQueue = new Queue<NotificationJob>("notification", defaultOpts);
export const geofenceEventQueue = new Queue<GeofenceEventJob>("geofence-event", defaultOpts);
export const reportQueue = new Queue<ReportJob>("report", defaultOpts);

export async function closeQueues(): Promise<void> {
  await Promise.all([
    locationTrailQueue.close(),
    notificationQueue.close(),
    geofenceEventQueue.close(),
    reportQueue.close(),
  ]);
  logger.info("All BullMQ queues closed");
}
