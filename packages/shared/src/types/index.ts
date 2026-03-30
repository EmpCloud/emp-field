// ============================================================================
// EMP-FIELD SHARED TYPES
// ============================================================================

// ---------------------------------------------------------------------------
// API Response envelope
// ---------------------------------------------------------------------------

export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  error?: {
    code: string;
    message: string;
    details?: Record<string, string[]>;
  };
}

// ---------------------------------------------------------------------------
// Field Settings
// ---------------------------------------------------------------------------

export interface FieldSettings {
  id: string;
  organization_id: number;
  check_in_radius_meters: number;
  photo_required: boolean;
  auto_checkout_hours: number;
  mileage_rate_per_km: number;
  tracking_interval_seconds: number;
  created_at: Date;
  updated_at: Date;
}

// ---------------------------------------------------------------------------
// Client Sites
// ---------------------------------------------------------------------------

export interface ClientSite {
  id: string;
  organization_id: number;
  name: string;
  address: string | null;
  city: string | null;
  state: string | null;
  country: string | null;
  latitude: number;
  longitude: number;
  radius_meters: number;
  contact_name: string | null;
  contact_phone: string | null;
  contact_email: string | null;
  category: string | null;
  is_active: boolean;
  created_at: Date;
  updated_at: Date;
}

// ---------------------------------------------------------------------------
// Check-ins
// ---------------------------------------------------------------------------

export type CheckInStatus = "active" | "completed" | "auto_closed";

export interface CheckIn {
  id: string;
  organization_id: number;
  user_id: number;
  client_site_id: string | null;
  check_in_time: Date;
  check_out_time: Date | null;
  check_in_lat: number;
  check_in_lng: number;
  check_out_lat: number | null;
  check_out_lng: number | null;
  photo_url: string | null;
  notes: string | null;
  status: CheckInStatus;
  duration_minutes: number | null;
  created_at: Date;
  updated_at: Date;
}

// ---------------------------------------------------------------------------
// Location Trails
// ---------------------------------------------------------------------------

export interface LocationTrail {
  id: string;
  organization_id: number;
  user_id: number;
  latitude: number;
  longitude: number;
  accuracy: number | null;
  battery_percent: number | null;
  timestamp: Date;
  created_at: Date;
}

// ---------------------------------------------------------------------------
// Geo Fences
// ---------------------------------------------------------------------------

export type GeoFenceType = "circle" | "polygon";

export interface GeoFence {
  id: string;
  organization_id: number;
  client_site_id: string | null;
  name: string;
  type: GeoFenceType;
  center_lat: number | null;
  center_lng: number | null;
  radius_meters: number | null;
  polygon_coords: Array<{ lat: number; lng: number }> | null;
  is_active: boolean;
  created_at: Date;
  updated_at: Date;
}

// ---------------------------------------------------------------------------
// Geo Fence Events
// ---------------------------------------------------------------------------

export type GeoFenceEventType = "entry" | "exit";

export interface GeoFenceEvent {
  id: string;
  organization_id: number;
  user_id: number;
  geo_fence_id: string;
  event_type: GeoFenceEventType;
  latitude: number;
  longitude: number;
  timestamp: Date;
  created_at: Date;
}

// ---------------------------------------------------------------------------
// Work Orders
// ---------------------------------------------------------------------------

export type WorkOrderPriority = "low" | "medium" | "high" | "urgent";
export type WorkOrderStatus = "pending" | "in_progress" | "completed" | "cancelled";

export interface WorkOrder {
  id: string;
  organization_id: number;
  assigned_to: number;
  client_site_id: string | null;
  title: string;
  description: string | null;
  priority: WorkOrderPriority;
  status: WorkOrderStatus;
  due_date: Date | null;
  completed_at: Date | null;
  created_by: number;
  created_at: Date;
  updated_at: Date;
}

// ---------------------------------------------------------------------------
// Expenses
// ---------------------------------------------------------------------------

export type ExpenseType = "travel" | "food" | "lodging" | "materials" | "other";
export type ExpenseStatus = "draft" | "submitted" | "approved" | "rejected";

export interface Expense {
  id: string;
  organization_id: number;
  user_id: number;
  expense_type: ExpenseType;
  amount: number;
  currency: string;
  description: string | null;
  receipt_url: string | null;
  status: ExpenseStatus;
  approved_by: number | null;
  approved_at: Date | null;
  created_at: Date;
  updated_at: Date;
}

// ---------------------------------------------------------------------------
// Mileage Logs
// ---------------------------------------------------------------------------

export interface MileageLog {
  id: string;
  organization_id: number;
  user_id: number;
  date: string;
  distance_km: number;
  start_lat: number;
  start_lng: number;
  end_lat: number;
  end_lng: number;
  reimbursement_amount: number;
  created_at: Date;
}

// ---------------------------------------------------------------------------
// Daily Routes
// ---------------------------------------------------------------------------

export type RouteStatus = "planned" | "in_progress" | "completed";

export interface DailyRoute {
  id: string;
  organization_id: number;
  user_id: number;
  date: string;
  status: RouteStatus;
  total_distance_km: number;
  created_at: Date;
  updated_at: Date;
}

// ---------------------------------------------------------------------------
// Route Stops
// ---------------------------------------------------------------------------

export type RouteStopStatus = "pending" | "visited" | "skipped";

export interface RouteStop {
  id: string;
  daily_route_id: string;
  client_site_id: string | null;
  sequence_order: number;
  planned_arrival: Date | null;
  actual_arrival: Date | null;
  status: RouteStopStatus;
  notes: string | null;
  created_at: Date;
}

// ---------------------------------------------------------------------------
// Visit Logs
// ---------------------------------------------------------------------------

export interface VisitLog {
  id: string;
  organization_id: number;
  user_id: number;
  client_site_id: string | null;
  checkin_id: string | null;
  purpose: string | null;
  outcome: string | null;
  notes: string | null;
  photos: string[] | null;
  duration_minutes: number | null;
  created_at: Date;
}

// ---------------------------------------------------------------------------
// Notifications
// ---------------------------------------------------------------------------

export interface Notification {
  id: string;
  organization_id: number;
  user_id: number;
  title: string;
  body: string | null;
  type: string | null;
  reference_type: string | null;
  reference_id: string | null;
  is_read: boolean;
  created_at: Date;
  read_at: Date | null;
}
