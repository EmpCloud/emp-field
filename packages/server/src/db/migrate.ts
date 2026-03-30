// ============================================================================
// MIGRATION RUNNER
// Usage: tsx src/db/migrate.ts
// ============================================================================

import { initDB, migrateDB, closeDB } from "./connection";
import { logger } from "../utils/logger";

async function run() {
  try {
    await initDB();
    await migrateDB();
    logger.info("Migrations completed successfully");
    await closeDB();
    process.exit(0);
  } catch (error) {
    logger.error("Migration failed:", error);
    await closeDB();
    process.exit(1);
  }
}

run();
