/**
 * EMP Field — Infrastructure coverage tests.
 * Error classes, response helpers, geo utilities.
 */
import { describe, it, expect, vi } from "vitest";

// ---------------------------------------------------------------------------
// Error Classes
// ---------------------------------------------------------------------------
import {
  AppError,
  NotFoundError,
  ValidationError,
  UnauthorizedError,
  ForbiddenError,
  ConflictError,
} from "../utils/errors";

describe("Error classes", () => {
  describe("AppError", () => {
    it("sets all properties", () => {
      const err = new AppError(500, "INTERNAL", "Oops");
      expect(err).toBeInstanceOf(Error);
      expect(err.statusCode).toBe(500);
      expect(err.code).toBe("INTERNAL");
      expect(err.message).toBe("Oops");
      expect(err.name).toBe("AppError");
    });

    it("stores details", () => {
      expect(new AppError(400, "X", "Y", { f: ["r"] }).details).toEqual({ f: ["r"] });
    });
  });

  describe("NotFoundError", () => {
    it("with id", () => {
      const err = new NotFoundError("Visit", "123");
      expect(err.statusCode).toBe(404);
      expect(err.message).toContain("Visit");
      expect(err.message).toContain("123");
    });

    it("without id", () => {
      expect(new NotFoundError("Route").message).toBe("Route not found");
    });
  });

  describe("ValidationError", () => {
    it("creates 400", () => {
      expect(new ValidationError("Bad").statusCode).toBe(400);
      expect(new ValidationError("Bad").code).toBe("VALIDATION_ERROR");
    });
  });

  describe("UnauthorizedError", () => {
    it("creates 401", () => {
      expect(new UnauthorizedError().statusCode).toBe(401);
    });
  });

  describe("ForbiddenError", () => {
    it("creates 403", () => {
      expect(new ForbiddenError().statusCode).toBe(403);
    });
  });

  describe("ConflictError", () => {
    it("creates 409", () => {
      expect(new ConflictError("Dup").statusCode).toBe(409);
    });
  });
});

// ---------------------------------------------------------------------------
// Response Helpers
// ---------------------------------------------------------------------------
import { sendSuccess, sendError, sendPaginated } from "../utils/response";

function mockRes() {
  return { status: vi.fn().mockReturnThis(), json: vi.fn().mockReturnThis() } as any;
}

describe("Response helpers", () => {
  it("sendSuccess sends 200", () => {
    const res = mockRes();
    sendSuccess(res, { id: 1 });
    expect(res.status).toHaveBeenCalledWith(200);
    expect(res.json).toHaveBeenCalledWith({ success: true, data: { id: 1 } });
  });

  it("sendError sends error", () => {
    const res = mockRes();
    sendError(res, 400, "BAD", "Wrong");
    expect(res.json).toHaveBeenCalledWith({ success: false, error: { code: "BAD", message: "Wrong" } });
  });

  it("sendPaginated with meta", () => {
    const res = mockRes();
    sendPaginated(res, [1, 2], 20, 1, 10);
    expect(res.json.mock.calls[0][0].data.totalPages).toBe(2);
  });
});

// ---------------------------------------------------------------------------
// Geo Utilities
// ---------------------------------------------------------------------------
import {
  haversineDistance,
  haversineDistanceMeters,
  isPointInCircle,
  isPointInPolygon,
} from "../utils/geo";

describe("Geo utilities", () => {
  describe("haversineDistance", () => {
    it("returns 0 for same point", () => {
      expect(haversineDistance(0, 0, 0, 0)).toBe(0);
    });

    it("calculates known distance (approx London to Paris ~340km)", () => {
      const d = haversineDistance(51.5074, -0.1278, 48.8566, 2.3522);
      expect(d).toBeGreaterThan(330);
      expect(d).toBeLessThan(350);
    });

    it("is symmetric", () => {
      const d1 = haversineDistance(10, 20, 30, 40);
      const d2 = haversineDistance(30, 40, 10, 20);
      expect(Math.abs(d1 - d2)).toBeLessThan(0.001);
    });
  });

  describe("haversineDistanceMeters", () => {
    it("returns distance in meters (1000x km)", () => {
      const km = haversineDistance(0, 0, 0, 1);
      const m = haversineDistanceMeters(0, 0, 0, 1);
      expect(Math.abs(m - km * 1000)).toBeLessThan(0.01);
    });
  });

  describe("isPointInCircle", () => {
    it("returns true for point inside circle", () => {
      // Point 100m from center, radius 200m
      expect(isPointInCircle(28.6139, 77.2090, 28.6139, 77.2091, 200)).toBe(true);
    });

    it("returns false for point outside circle", () => {
      // Delhi to Mumbai (~1100km), radius 10km
      expect(isPointInCircle(28.6139, 77.2090, 19.0760, 72.8777, 10_000)).toBe(false);
    });

    it("center point is always inside", () => {
      expect(isPointInCircle(10, 20, 10, 20, 1)).toBe(true);
    });
  });

  describe("isPointInPolygon", () => {
    const square = [
      { lat: 0, lng: 0 },
      { lat: 0, lng: 10 },
      { lat: 10, lng: 10 },
      { lat: 10, lng: 0 },
    ];

    it("returns true for point inside polygon", () => {
      expect(isPointInPolygon(5, 5, square)).toBe(true);
    });

    it("returns false for point outside polygon", () => {
      expect(isPointInPolygon(15, 15, square)).toBe(false);
    });

    it("returns false for point clearly outside", () => {
      expect(isPointInPolygon(-5, -5, square)).toBe(false);
    });

    it("works with triangle", () => {
      const triangle = [
        { lat: 0, lng: 0 },
        { lat: 10, lng: 5 },
        { lat: 0, lng: 10 },
      ];
      expect(isPointInPolygon(3, 5, triangle)).toBe(true);
      expect(isPointInPolygon(9, 1, triangle)).toBe(false);
    });
  });
});
