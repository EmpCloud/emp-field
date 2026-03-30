// ============================================================================
// EMP-FIELD CONSTANTS
// ============================================================================

// ---------------------------------------------------------------------------
// Check-in Status
// ---------------------------------------------------------------------------
export const CHECK_IN_STATUSES = ["active", "completed", "auto_closed"] as const;

// ---------------------------------------------------------------------------
// Geo Fence Types
// ---------------------------------------------------------------------------
export const GEO_FENCE_TYPES = ["circle", "polygon"] as const;

// ---------------------------------------------------------------------------
// Geo Fence Event Types
// ---------------------------------------------------------------------------
export const GEO_FENCE_EVENT_TYPES = ["entry", "exit"] as const;

// ---------------------------------------------------------------------------
// Work Order Priority
// ---------------------------------------------------------------------------
export const WORK_ORDER_PRIORITIES = ["low", "medium", "high", "urgent"] as const;

// ---------------------------------------------------------------------------
// Work Order Status
// ---------------------------------------------------------------------------
export const WORK_ORDER_STATUSES = ["pending", "in_progress", "completed", "cancelled"] as const;

// ---------------------------------------------------------------------------
// Expense Types
// ---------------------------------------------------------------------------
export const EXPENSE_TYPES = ["travel", "food", "lodging", "materials", "other"] as const;

// ---------------------------------------------------------------------------
// Expense Statuses
// ---------------------------------------------------------------------------
export const EXPENSE_STATUSES = ["draft", "submitted", "approved", "rejected"] as const;

// ---------------------------------------------------------------------------
// Route Statuses
// ---------------------------------------------------------------------------
export const ROUTE_STATUSES = ["planned", "in_progress", "completed"] as const;

// ---------------------------------------------------------------------------
// Route Stop Statuses
// ---------------------------------------------------------------------------
export const ROUTE_STOP_STATUSES = ["pending", "visited", "skipped"] as const;

// ---------------------------------------------------------------------------
// Permissions / Roles
// ---------------------------------------------------------------------------
export const ADMIN_ROLES = ["super_admin", "org_admin", "hr_admin", "hr_manager"] as const;
export const ALL_ROLES = [...ADMIN_ROLES, "employee"] as const;

// ---------------------------------------------------------------------------
// Defaults
// ---------------------------------------------------------------------------
export const DEFAULT_CHECK_IN_RADIUS_METERS = 200;
export const DEFAULT_AUTO_CHECKOUT_HOURS = 12;
export const DEFAULT_MILEAGE_RATE_PER_KM = 8; // INR
export const DEFAULT_TRACKING_INTERVAL_SECONDS = 30;
export const DEFAULT_PAGE_SIZE = 20;
export const MAX_PAGE_SIZE = 100;
