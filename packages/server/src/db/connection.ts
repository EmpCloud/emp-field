// ============================================================================
// FIELD MODULE DATABASE CONNECTION
// ============================================================================

import knex from "knex";
import type { Knex } from "knex";
import { config } from "../config";
import { logger } from "../utils/logger";

let db: Knex | null = null;

export async function initDB(): Promise<Knex> {
  if (db) return db;

  db = knex({
    client: "mysql2",
    connection: {
      host: config.db.host,
      port: config.db.port,
      user: config.db.user,
      password: config.db.password,
      database: config.db.name,
    },
    pool: { min: config.db.poolMin, max: config.db.poolMax },
    migrations: {
      directory: "./src/db/migrations/sql",
      extension: "ts",
    },
  });

  await db.raw("SELECT 1");
  logger.info(`Field database connected (${config.db.host}:${config.db.port}/${config.db.name})`);

  return db;
}

export function getDB(): Knex {
  if (!db) {
    throw new Error("Field database not initialized. Call initDB() first.");
  }
  return db;
}

export async function migrateDB(): Promise<void> {
  const conn = getDB();
  await conn.migrate.latest();
  logger.info("Field database migrations applied");
}

export async function closeDB(): Promise<void> {
  if (db) {
    await db.destroy();
    db = null;
  }
}
