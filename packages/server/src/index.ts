// ============================================================================
// EMP-FIELD SERVER ENTRY POINT
// ============================================================================

import http from "http";
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
import { webhookRoutes } from "./api/routes/webhook.routes";
import { deviceRoutes } from "./api/routes/device.routes";
import { mobileRoutes } from "./api/routes/mobile.routes";
import { uploadRoutes, UPLOAD_ROOT } from "./api/routes/upload.routes";
import { legacyAdminRoutes } from "./api/routes/legacy/admin.routes";
import { legacyPublicRoutes } from "./api/routes/legacy/public.routes";
import { legacyRouters } from "./api/routes/legacy";
import { errorHandler } from "./api/middleware/error.middleware";
import { apiLimiter, authLimiter } from "./api/middleware/rate-limit.middleware";
import { auditLogger } from "./api/middleware/audit.middleware";

// Queue + realtime
import { startWorkers, stopWorkers } from "./queue/workers";
import { closeQueues } from "./queue/queues";
import { closeRedis, getRedis } from "./queue/connection";
import { initSocket, closeSocket } from "./realtime/socket";

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
v1.use(auditLogger);

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
v1.use("/webhooks", webhookRoutes);
v1.use("/devices", deviceRoutes);
v1.use("/mobile", mobileRoutes);
v1.use("/uploads", uploadRoutes);

// ----- Legacy modules (commit 3409d88 parity) ----------------------------
v1.use("/admin", legacyAdminRoutes);
v1.use("/unauthorized", legacyPublicRoutes);
v1.use("/holiday", legacyRouters.holiday);
v1.use("/leave", legacyRouters.leave);
v1.use("/role", legacyRouters.role);
v1.use("/profile", legacyRouters.profile);
v1.use("/category", legacyRouters.category);
v1.use("/tags", legacyRouters.tag);
v1.use("/transport", legacyRouters.transport);
v1.use("/track", legacyRouters.tracking);
v1.use("/hrmsAdmin", legacyRouters.hrms);
v1.use("/autoEmailReport", legacyRouters.autoEmail);
v1.use("/client", legacyRouters.legacyClient);
v1.use("/user", legacyRouters.legacyUser);
v1.use("/attendance", legacyRouters.attendance);
v1.use("/task", legacyRouters.task);
v1.use("/reports", legacyRouters.reports);

app.use("/api/v1", v1);

// Static serving of uploaded files (production should front this with a CDN/S3).
app.use("/uploads", express.static(UPLOAD_ROOT));

// ---------------------------------------------------------------------------
// Error handling
// ---------------------------------------------------------------------------
app.use(errorHandler);

// ---------------------------------------------------------------------------
// Startup
// ---------------------------------------------------------------------------
const httpServer = http.createServer(app);

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

    // Initialize Redis + BullMQ workers
    getRedis();
    startWorkers();

    // Initialize socket.io on the same HTTP server
    initSocket(httpServer);

    // Start server
    httpServer.listen(config.port, config.host, () => {
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
  await closeSocket();
  await stopWorkers();
  await closeQueues();
  await closeRedis();
  await closeDB();
  await closeEmpCloudDB();
  process.exit(0);
};

process.on("SIGTERM", shutdown);
process.on("SIGINT", shutdown);

start();

export { app, httpServer };
