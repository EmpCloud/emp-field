// ============================================================================
// EMP-FIELD SERVER ENTRY POINT
// ============================================================================

import express from "express";
import cors from "cors";
import helmet from "helmet";
import morgan from "morgan";
import compression from "compression";
import { config } from "./config";
import { initDB, migrateDB, closeDB } from "./db/connection";
import { initEmpCloudDB, closeEmpCloudDB } from "./db/empcloud";
import { logger } from "./utils/logger";

// Route imports
import { healthRoutes } from "./api/routes/health.routes";
import { authRoutes } from "./api/routes/auth.routes";
import { clientSiteRoutes } from "./api/routes/client-site.routes";
import { checkinRoutes } from "./api/routes/checkin.routes";
import { workOrderRoutes } from "./api/routes/work-order.routes";
import { locationRoutes } from "./api/routes/location.routes";
import { geoFenceRoutes } from "./api/routes/geo-fence.routes";
import { expenseRoutes } from "./api/routes/expense.routes";
import { mileageRoutes } from "./api/routes/mileage.routes";
import { routeRoutes } from "./api/routes/route.routes";
import { visitRoutes } from "./api/routes/visit.routes";
import { analyticsRoutes } from "./api/routes/analytics.routes";
import { settingsRoutes } from "./api/routes/settings.routes";
import { notificationRoutes } from "./api/routes/notification.routes";
import { errorHandler } from "./api/middleware/error.middleware";
import { apiLimiter, authLimiter } from "./api/middleware/rate-limit.middleware";

const app = express();

// ---------------------------------------------------------------------------
// Middleware
// ---------------------------------------------------------------------------
app.use(helmet());
app.use(
  cors({
    origin: (origin, callback) => {
      if (!origin) return callback(null, true);
      if (config.cors.origin === "*") return callback(null, true);
      // Allow empcloud.com subdomains
      if (origin.endsWith(".empcloud.com") && origin.startsWith("https://")) {
        return callback(null, true);
      }
      if (
        config.env === "development" &&
        (origin.startsWith("http://localhost") ||
          origin.startsWith("http://127.0.0.1") ||
          origin.endsWith(".ngrok-free.dev"))
      ) {
        return callback(null, true);
      }
      const allowed = config.cors.origin.split(",").map((s) => s.trim());
      if (allowed.includes(origin)) return callback(null, true);
      callback(new Error("Not allowed by CORS"));
    },
    credentials: true,
  }),
);
app.use(compression());
app.use(express.json({ limit: "10mb" }));
app.use(express.urlencoded({ extended: true }));
app.use(morgan("combined", { stream: { write: (msg) => logger.info(msg.trim()) } }));

// ---------------------------------------------------------------------------
// Health check
// ---------------------------------------------------------------------------
app.use("/health", healthRoutes);

// ---------------------------------------------------------------------------
// API Routes (v1)
// ---------------------------------------------------------------------------
const v1 = express.Router();
v1.use(apiLimiter);

v1.use("/auth", authLimiter, authRoutes);
v1.use("/client-sites", clientSiteRoutes);
v1.use("/checkins", checkinRoutes);
v1.use("/work-orders", workOrderRoutes);
v1.use("/locations", locationRoutes);
v1.use("/geo-fences", geoFenceRoutes);
v1.use("/expenses", expenseRoutes);
v1.use("/mileage", mileageRoutes);
v1.use("/routes", routeRoutes);
v1.use("/visits", visitRoutes);
v1.use("/analytics", analyticsRoutes);
v1.use("/settings", settingsRoutes);
v1.use("/notifications", notificationRoutes);

app.use("/api/v1", v1);

// ---------------------------------------------------------------------------
// Error handling
// ---------------------------------------------------------------------------
app.use(errorHandler);

// ---------------------------------------------------------------------------
// Startup
// ---------------------------------------------------------------------------
async function start() {
  try {
    // Validate configuration
    const { validateConfig } = await import("./config/validate");
    validateConfig();

    // Initialize EmpCloud master database
    await initEmpCloudDB();

    // Initialize field module database
    await initDB();
    await migrateDB();

    // Start server
    app.listen(config.port, config.host, () => {
      logger.info(`emp-field server running at http://${config.host}:${config.port}`);
      logger.info(`   Environment: ${config.env}`);
    });
  } catch (error) {
    logger.error("Failed to start server:", error);
    process.exit(1);
  }
}

// Graceful shutdown
const shutdown = async () => {
  logger.info("Shutting down...");
  await closeDB();
  await closeEmpCloudDB();
  process.exit(0);
};

process.on("SIGTERM", shutdown);
process.on("SIGINT", shutdown);

start();

export { app };
