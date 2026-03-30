// ============================================================================
// EMP-FIELD ZOD VALIDATORS
// ============================================================================

import { z } from "zod";

// ---------------------------------------------------------------------------
// Pagination
// ---------------------------------------------------------------------------

export const paginationSchema = z.object({
  page: z.coerce.number().int().min(1).default(1),
  perPage: z.coerce.number().int().min(1).max(100).default(20),
});

// ---------------------------------------------------------------------------
// Client Site
// ---------------------------------------------------------------------------

export const createClientSiteSchema = z.object({
  name: z.string().min(1, "Name is required").max(255),
  address: z.string().max(500).nullable().optional(),
  city: z.string().max(100).nullable().optional(),
  state: z.string().max(100).nullable().optional(),
  country: z.string().max(100).nullable().optional(),
  latitude: z.number().min(-90).max(90),
  longitude: z.number().min(-180).max(180),
  radius_meters: z.number().int().min(10).max(50000).default(200),
  contact_name: z.string().max(255).nullable().optional(),
  contact_phone: z.string().max(50).nullable().optional(),
  contact_email: z.string().email().nullable().optional(),
  category: z.string().max(100).nullable().optional(),
});

export const updateClientSiteSchema = createClientSiteSchema.partial();

// ---------------------------------------------------------------------------
// Check-in
// ---------------------------------------------------------------------------

export const checkInSchema = z.object({
  client_site_id: z.string().uuid().nullable().optional(),
  check_in_lat: z.number().min(-90).max(90),
  check_in_lng: z.number().min(-180).max(180),
  photo_url: z.string().url().nullable().optional(),
  notes: z.string().max(1000).nullable().optional(),
});

export const checkOutSchema = z.object({
  check_out_lat: z.number().min(-90).max(90),
  check_out_lng: z.number().min(-180).max(180),
  notes: z.string().max(1000).nullable().optional(),
});

// ---------------------------------------------------------------------------
// Work Order
// ---------------------------------------------------------------------------

export const createWorkOrderSchema = z.object({
  assigned_to: z.number().int().positive(),
  client_site_id: z.string().uuid().nullable().optional(),
  title: z.string().min(1, "Title is required").max(255),
  description: z.string().max(2000).nullable().optional(),
  priority: z.enum(["low", "medium", "high", "urgent"]).default("medium"),
  status: z.enum(["pending", "in_progress", "completed", "cancelled"]).default("pending"),
  due_date: z.string().nullable().optional(),
});

export const updateWorkOrderSchema = createWorkOrderSchema.partial();

// ---------------------------------------------------------------------------
// Location Trail (batch upload)
// ---------------------------------------------------------------------------

export const locationPointSchema = z.object({
  latitude: z.number().min(-90).max(90),
  longitude: z.number().min(-180).max(180),
  accuracy: z.number().nullable().optional(),
  battery_percent: z.number().int().min(0).max(100).nullable().optional(),
  timestamp: z.string().or(z.date()),
});

export const batchLocationSchema = z.object({
  points: z.array(locationPointSchema).min(1).max(500),
});

// ---------------------------------------------------------------------------
// Geo Fence
// ---------------------------------------------------------------------------

export const createGeoFenceSchema = z.object({
  client_site_id: z.string().uuid().nullable().optional(),
  name: z.string().min(1, "Name is required").max(255),
  type: z.enum(["circle", "polygon"]),
  center_lat: z.number().min(-90).max(90).nullable().optional(),
  center_lng: z.number().min(-180).max(180).nullable().optional(),
  radius_meters: z.number().int().min(10).max(50000).nullable().optional(),
  polygon_coords: z.array(z.object({
    lat: z.number().min(-90).max(90),
    lng: z.number().min(-180).max(180),
  })).min(3).nullable().optional(),
  is_active: z.boolean().default(true),
});

export const updateGeoFenceSchema = createGeoFenceSchema.partial();

export const checkPointInFenceSchema = z.object({
  latitude: z.number().min(-90).max(90),
  longitude: z.number().min(-180).max(180),
  geo_fence_id: z.string().uuid(),
});

// ---------------------------------------------------------------------------
// Expense
// ---------------------------------------------------------------------------

export const createExpenseSchema = z.object({
  expense_type: z.enum(["travel", "food", "lodging", "materials", "other"]),
  amount: z.number().positive(),
  currency: z.string().length(3).default("INR"),
  description: z.string().max(1000).nullable().optional(),
  receipt_url: z.string().url().nullable().optional(),
});

export const updateExpenseSchema = createExpenseSchema.partial();

// ---------------------------------------------------------------------------
// Mileage
// ---------------------------------------------------------------------------

export const createMileageSchema = z.object({
  date: z.string().regex(/^\d{4}-\d{2}-\d{2}$/),
  distance_km: z.number().positive(),
  start_lat: z.number().min(-90).max(90),
  start_lng: z.number().min(-180).max(180),
  end_lat: z.number().min(-90).max(90),
  end_lng: z.number().min(-180).max(180),
});

// ---------------------------------------------------------------------------
// Daily Route
// ---------------------------------------------------------------------------

export const createDailyRouteSchema = z.object({
  date: z.string().regex(/^\d{4}-\d{2}-\d{2}$/),
  stops: z.array(z.object({
    client_site_id: z.string().uuid().nullable().optional(),
    sequence_order: z.number().int().positive(),
    planned_arrival: z.string().nullable().optional(),
  })).min(1),
});

export const updateRouteStatusSchema = z.object({
  status: z.enum(["planned", "in_progress", "completed"]),
});

export const reorderStopsSchema = z.object({
  stops: z.array(z.object({
    id: z.string().uuid(),
    sequence_order: z.number().int().positive(),
  })).min(1),
});

export const updateRouteStopSchema = z.object({
  status: z.enum(["pending", "visited", "skipped"]).optional(),
  actual_arrival: z.string().nullable().optional(),
  notes: z.string().max(1000).nullable().optional(),
});

// ---------------------------------------------------------------------------
// Visit Log
// ---------------------------------------------------------------------------

export const createVisitLogSchema = z.object({
  client_site_id: z.string().uuid().nullable().optional(),
  checkin_id: z.string().uuid().nullable().optional(),
  purpose: z.string().max(500).nullable().optional(),
  outcome: z.string().max(500).nullable().optional(),
  notes: z.string().max(2000).nullable().optional(),
  photos: z.array(z.string().url()).nullable().optional(),
  duration_minutes: z.number().int().positive().nullable().optional(),
});

export const updateVisitLogSchema = createVisitLogSchema.partial();

// ---------------------------------------------------------------------------
// Field Settings
// ---------------------------------------------------------------------------

export const updateSettingsSchema = z.object({
  check_in_radius_meters: z.number().int().min(10).max(50000).optional(),
  photo_required: z.boolean().optional(),
  auto_checkout_hours: z.number().min(1).max(48).optional(),
  mileage_rate_per_km: z.number().min(0).optional(),
  tracking_interval_seconds: z.number().int().min(5).max(3600).optional(),
});

// ---------------------------------------------------------------------------
// SSO
// ---------------------------------------------------------------------------

export const ssoSchema = z.object({
  token: z.string().min(1, "SSO token is required"),
});
