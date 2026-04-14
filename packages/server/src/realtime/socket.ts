// ============================================================================
// REAL-TIME LOCATION TRACKING VIA SOCKET.IO
// ============================================================================
//
// Namespaces:
//   /field — for field workers pushing their GPS, and managers subscribing
//
// Rooms:
//   org:<orgId>           — all users in an org (manager dashboard)
//   org:<orgId>:user:<id> — a single user's live trail
// ============================================================================

import type { Server as HttpServer } from "http";
import { Server as IOServer, Socket } from "socket.io";
import jwt from "jsonwebtoken";
import { config } from "../config";
import { logger } from "../utils/logger";
import { locationTrailQueue, geofenceEventQueue } from "../queue/queues";
import type { AuthPayload } from "../api/middleware/auth.middleware";
import { loadPublicKey } from "../utils/jwt-keys";

function verifyToken(token: string): AuthPayload {
  const publicKey = loadPublicKey();
  if (publicKey) {
    try {
      return jwt.verify(token, publicKey, {
        algorithms: ["RS256"],
        issuer: config.jwt.issuer,
      }) as AuthPayload;
    } catch {
      // fall through to HS256
    }
  }
  return jwt.verify(token, config.jwt.secret, { algorithms: ["HS256"] }) as AuthPayload;
}

let io: IOServer | null = null;

type LocationPing = {
  latitude: number;
  longitude: number;
  accuracy?: number;
  timestamp?: string;
};

type GeofenceEventPayload = {
  geoFenceId: string;
  event: "enter" | "exit";
  latitude: number;
  longitude: number;
  occurredAt?: string;
};

export function initSocket(server: HttpServer): IOServer {
  if (io) return io;

  io = new IOServer(server, {
    cors: { origin: true, credentials: true },
    path: "/socket.io",
  });

  const fieldNs = io.of("/field");

  fieldNs.use((socket, next) => {
    const token =
      (socket.handshake.auth?.token as string | undefined) ||
      (socket.handshake.query?.token as string | undefined);
    if (!token) return next(new Error("UNAUTHORIZED"));
    try {
      const payload = verifyToken(token);
      (socket.data as any).user = payload;
      next();
    } catch {
      next(new Error("INVALID_TOKEN"));
    }
  });

  fieldNs.on("connection", (socket: Socket) => {
    const user = (socket.data as any).user as AuthPayload;
    const orgRoom = `org:${user.empcloudOrgId}`;
    const userRoom = `${orgRoom}:user:${user.empcloudUserId}`;

    socket.join(orgRoom);
    socket.join(userRoom);

    logger.info(`socket connected: org=${user.empcloudOrgId} user=${user.empcloudUserId}`);

    // Field worker streaming GPS pings
    socket.on("location:ping", async (payload: LocationPing) => {
      if (typeof payload?.latitude !== "number" || typeof payload?.longitude !== "number") return;
      // Broadcast to managers subscribed to this org
      fieldNs.to(orgRoom).emit("location:update", {
        userId: user.empcloudUserId,
        orgId: user.empcloudOrgId,
        latitude: payload.latitude,
        longitude: payload.longitude,
        accuracy: payload.accuracy,
        timestamp: payload.timestamp ?? new Date().toISOString(),
      });
      // Queue for persistence
      await locationTrailQueue.add("ping", {
        organizationId: user.empcloudOrgId,
        userId: user.empcloudUserId,
        points: [
          {
            latitude: payload.latitude,
            longitude: payload.longitude,
            accuracy: payload.accuracy,
            timestamp: payload.timestamp ?? new Date().toISOString(),
          },
        ],
      });
    });

    // Geofence enter/exit detected client-side
    socket.on("geofence:event", async (payload: GeofenceEventPayload) => {
      if (!payload?.geoFenceId || !payload?.event) return;
      await geofenceEventQueue.add("event", {
        organizationId: user.empcloudOrgId,
        userId: user.empcloudUserId,
        geoFenceId: payload.geoFenceId,
        event: payload.event,
        latitude: payload.latitude,
        longitude: payload.longitude,
        occurredAt: payload.occurredAt ?? new Date().toISOString(),
      });
      fieldNs.to(orgRoom).emit("geofence:activity", {
        userId: user.empcloudUserId,
        ...payload,
      });
    });

    socket.on("disconnect", () => {
      logger.info(`socket disconnected: user=${user.empcloudUserId}`);
    });
  });

  logger.info("Socket.io /field namespace initialized");
  return io;
}

export function getIO(): IOServer {
  if (!io) throw new Error("Socket.io not initialized");
  return io;
}

export async function closeSocket(): Promise<void> {
  if (io) {
    await io.close();
    io = null;
  }
}
