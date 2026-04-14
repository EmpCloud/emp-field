// ============================================================================
// REDIS CONNECTION FOR BULLMQ
// ============================================================================

import IORedis, { Redis } from "ioredis";
import { config } from "../config";
import { logger } from "../utils/logger";

let connection: Redis | null = null;

export function getRedis(): Redis {
  if (connection) return connection;

  connection = new IORedis({
    host: config.redis.host,
    port: config.redis.port,
    password: config.redis.password,
    maxRetriesPerRequest: null,
    enableReadyCheck: false,
  });

  connection.on("connect", () => {
    logger.info(`Redis connected (${config.redis.host}:${config.redis.port})`);
  });

  connection.on("error", (err) => {
    logger.error("Redis connection error:", err);
  });

  return connection;
}

export async function closeRedis(): Promise<void> {
  if (connection) {
    await connection.quit();
    connection = null;
  }
}
