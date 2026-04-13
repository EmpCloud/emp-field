// ============================================================================
// BULLMQ WORKER REGISTRATION
// ============================================================================

import { Worker, WorkerOptions } from "bullmq";
import { getRedis } from "./connection";
import { logger } from "../utils/logger";
import type {
  LocationTrailJob,
  NotificationJob,
  GeofenceEventJob,
  ReportJob,
} from "./queues";
import { getDB } from "../db/connection";
import { v4 as uuidv4 } from "uuid";

let workers: Worker[] = [];

function workerOpts(): WorkerOptions {
  return {
    connection: getRedis(),
    concurrency: 5,
  };
}

export function startWorkers(): void {
  if (workers.length) return;

  // Location trail worker — persists GPS points in bulk
  workers.push(
    new Worker<LocationTrailJob>(
      "location-trail",
      async (job) => {
        const { organizationId, userId, points } = job.data;
        if (!points?.length) return;
        const db = getDB();
        const rows = points.map((p) => ({
          id: uuidv4(),
          organization_id: organizationId,
          user_id: userId,
          latitude: p.latitude,
          longitude: p.longitude,
          accuracy: p.accuracy ?? null,
          recorded_at: new Date(p.timestamp),
        }));
        await db("location_trails").insert(rows);
      },
      workerOpts(),
    ),
  );

  // Notification worker — persist and dispatch
  workers.push(
    new Worker<NotificationJob>(
      "notification",
      async (job) => {
        const { organizationId, userId, title, body, channel, data } = job.data;
        const db = getDB();
        await db("notifications").insert({
          id: uuidv4(),
          organization_id: organizationId,
          user_id: userId,
          title,
          body,
          channel,
          data: data ? JSON.stringify(data) : null,
          status: "pending",
        });
      },
      workerOpts(),
    ),
  );

  // Geofence event worker — side-effects on enter/exit
  workers.push(
    new Worker<GeofenceEventJob>(
      "geofence-event",
      async (job) => {
        const db = getDB();
        await db("geo_fence_events").insert({
          id: uuidv4(),
          organization_id: job.data.organizationId,
          user_id: job.data.userId,
          geo_fence_id: job.data.geoFenceId,
          event_type: job.data.event,
          latitude: job.data.latitude,
          longitude: job.data.longitude,
          occurred_at: new Date(job.data.occurredAt),
        });
      },
      workerOpts(),
    ),
  );

  // Report worker — placeholder for async export generation
  workers.push(
    new Worker<ReportJob>(
      "report",
      async (job) => {
        logger.info(`Generating ${job.data.reportType} report for org ${job.data.organizationId}`);
      },
      workerOpts(),
    ),
  );

  for (const w of workers) {
    w.on("failed", (job, err) => {
      logger.error(`Queue job failed [${w.name}] ${job?.id}:`, err);
    });
  }

  logger.info(`BullMQ workers started (${workers.length} queues)`);
}

export async function stopWorkers(): Promise<void> {
  await Promise.all(workers.map((w) => w.close()));
  workers = [];
}
