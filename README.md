# EMP Field

> Efficiently manage and track field operations for maximum efficiency

[![Part of EmpCloud](https://img.shields.io/badge/EmpCloud-Module-blue)]()
[![License: AGPL-3.0](https://img.shields.io/badge/License-AGPL--3.0-purple.svg)](LICENSE)

EMP Field is the field operations management module of the EmpCloud ecosystem. It provides GPS-based check-in/check-out, live location tracking, geo-fencing, work order management, route planning, and expense tracking for field-based workforces.

---

## Status: Awaiting Code Upload

> **This module is currently an empty skeleton.** The old team has written code that needs to be uploaded to this repo before implementation can continue.

### TODO (before development resumes)
- [ ] Old team uploads their existing code to this repo (packages/shared, packages/server, packages/client)
- [ ] Review uploaded code — check tech stack alignment (Express 5, Knex, React 19, Vite, TailwindCSS)
- [ ] Resolve port conflict — README says port 4700 but that's assigned to EMP LMS. **Use port 4800 instead.**
- [ ] Add `emp-field` to EmpCloud health-check service (`health-check.service.ts`) with port 4800
- [ ] Add `emp-field` OAuth client to EmpCloud seed data
- [ ] Add `emp-field` features to EmpCloud seed data (for marketplace plan tiers)
- [ ] Create `emp_field` MySQL database on test server
- [ ] Configure nginx for `test-field.empcloud.com` (frontend) and `test-field-api.empcloud.com` (API port 4800)
- [ ] Add SSO handler (follow emp-rewards pattern — RS256 JWT verification with EmpCloud public key)
- [ ] Add "Back to EMP Cloud" button in header (follow emp-rewards BackToDashboard component pattern)
- [ ] Add to PM2 ecosystem config (`/home/empcloud-development/ecosystem.config.js`)
- [ ] Deploy, test SSO flow end-to-end, run Playwright verification

### Once code is uploaded, pick up from here:
1. Review & fix any issues in the uploaded code
2. Run migrations to create `emp_field` DB tables
3. Deploy to test server
4. Write Playwright E2E tests
5. Continue with Phase 2/3 from the implementation plan below

---

## Features

| Feature | Description |
|---------|-------------|
| GPS Check-In/Check-Out | Field employees check in/out with GPS coordinates, photo proof, timestamp |
| Geo-Fencing | Define work zones/client sites with boundaries, alerts on entry/exit |
| Live Location Tracking | Real-time employee location on map with breadcrumb trail |
| Work Orders / Tasks | Create, assign, prioritize field tasks with location and instructions |
| Route Planning | Plan daily routes for field staff, optimize stop sequence |
| Route Optimization (Premium) | TSP-based optimal route calculation, ETA predictions |
| Visit Logs | Log client/site visits with notes, photos, duration, outcome |
| Expense Tracking | Field expense claims (travel, food, materials) with receipt upload |
| Mileage Tracking | Auto-calculate distance traveled, reimbursement computation |
| Offline Mode | Queue check-ins when offline, sync when back online |
| Field Analytics | Productivity metrics: visits/day, travel vs work time, SLA compliance |
| Client Site Management | Manage client locations, contact info, visit history |

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Runtime | Node.js 20 |
| Backend | Express 5, TypeScript |
| Frontend | React 19, Vite 6, TypeScript |
| Styling | Tailwind CSS, Radix UI |
| Database | MySQL 8 via Knex.js (`emp_field` database) |
| Cache / Queue | Redis 7, BullMQ |
| Auth | OAuth2/OIDC via EMP Cloud (RS256 JWT verification) |
| Maps | Leaflet + OpenStreetMap (Google Maps optional) |
| File Uploads | Multer (local storage, S3-ready) |

---

## Project Structure

```
emp-field/
  package.json
  pnpm-workspace.yaml
  tsconfig.json
  docker-compose.yml
  .env.example
  packages/
    shared/                     # @emp-field/shared
      src/
        types/                  # TypeScript interfaces & enums
        validators/             # Zod request validation schemas
        constants/              # Defaults, categories, permissions
    server/                     # @emp-field/server (port 4700)
      src/
        config/                 # Environment configuration
        db/
          connection.ts         # Knex connection to emp_field
          empcloud.ts           # Read-only connection to empcloud DB
          migrations/           # 5 migration files
        api/
          middleware/            # auth, RBAC, error handling, upload
          routes/               # Route handlers per domain
        services/               # Business logic per domain
        jobs/                   # BullMQ workers (auto-checkout, mileage, SLA)
        utils/
          geo.ts                # Haversine, point-in-polygon, bearing
          logger.ts
          errors.ts
          response.ts
    client/                     # @emp-field/client (port 5181)
      src/
        api/                    # API client & hooks
        components/
          layout/               # DashboardLayout
          ui/                   # Radix-based UI primitives
          maps/                 # MapView, GeoFenceEditor, RouteMap, LiveTrackerMap
        pages/                  # Route-based page components
        lib/                    # Auth store, field store, location store
```

---

## Database Tables

| Table | Purpose |
|-------|---------|
| `field_settings` | Per-org configuration (tracking interval, photo requirements, mileage rate) |
| `client_sites` | Managed client/work locations with coordinates and contact info |
| `geo_fences` | Geo-fence zone definitions (circle or polygon) |
| `checkins` | GPS check-in/check-out records with coordinates, photos, timestamps |
| `location_tracks` | Breadcrumb trail / live location data points |
| `visit_logs` | Client/site visit records with purpose, outcome, photos |
| `geo_fence_events` | Entry/exit events triggered by geo-fence boundaries |
| `work_orders` | Field tasks/assignments with location, priority, status |
| `routes` | Planned daily routes for field staff |
| `route_stops` | Ordered stops within a route |
| `expenses` | Field expense claims with receipt uploads |
| `mileage_logs` | Auto-calculated or manual distance and reimbursement |
| `notifications` | In-app notifications for task assignments, alerts, updates |
| `field_audit_logs` | Module-level audit trail |

---

## API Endpoints

All endpoints under `/api/v1/`. Server runs on port **4700**.

### Check-In / Check-Out
| Method | Path | Description |
|--------|------|-------------|
| POST | `/checkins` | Create check-in (GPS + optional photo) |
| PATCH | `/checkins/:id/checkout` | Check out from current check-in |
| GET | `/checkins` | List checkins (paginated, filterable) |
| GET | `/checkins/active` | Get current active check-in |

### Location Tracking
| Method | Path | Description |
|--------|------|-------------|
| POST | `/location/track` | Submit location point(s) (batch support) |
| GET | `/location/live` | Live locations of all field workers (admin) |
| GET | `/location/trail/:userId` | Breadcrumb trail for a user |

### Geo-Fences
| Method | Path | Description |
|--------|------|-------------|
| POST | `/geo-fences` | Create geo-fence |
| GET | `/geo-fences` | List geo-fences |
| PUT | `/geo-fences/:id` | Update geo-fence |
| POST | `/geo-fences/events` | Submit geo-fence entry/exit event |

### Client Sites
| Method | Path | Description |
|--------|------|-------------|
| POST | `/client-sites` | Create client site |
| GET | `/client-sites` | List sites (paginated, search) |
| GET | `/client-sites/:id` | Get site with visit history |
| PUT | `/client-sites/:id` | Update site |

### Work Orders
| Method | Path | Description |
|--------|------|-------------|
| POST | `/work-orders` | Create work order |
| GET | `/work-orders` | List (filterable by status, assignee, date) |
| PATCH | `/work-orders/:id/status` | Change status (start, complete, cancel) |
| GET | `/work-orders/my` | My assigned work orders |

### Routes
| Method | Path | Description |
|--------|------|-------------|
| POST | `/routes` | Create/plan route |
| GET | `/routes/:id` | Get route with stops |
| PATCH | `/routes/:id/optimize` | Trigger route optimization (premium) |
| PATCH | `/routes/:id/status` | Start/complete route |

### Expenses & Mileage
| Method | Path | Description |
|--------|------|-------------|
| POST | `/expenses` | Create expense claim |
| PATCH | `/expenses/:id/submit` | Submit for approval |
| PATCH | `/expenses/:id/review` | Approve/reject |
| GET | `/mileage` | List mileage logs |
| GET | `/mileage/summary` | Monthly mileage summary |

### Analytics (Admin)
| Method | Path | Description |
|--------|------|-------------|
| GET | `/analytics/dashboard` | Dashboard KPIs |
| GET | `/analytics/productivity` | Visits/day, travel vs work time |
| GET | `/analytics/coverage` | Coverage area metrics |
| GET | `/analytics/sla` | SLA compliance report |

### Other Endpoints
- **Visit Logs**: CRUD for client/site visit records
- **Notifications**: List, mark read, mark all read
- **Settings**: Get/update org field settings
- **Uploads**: Photo and receipt upload endpoints

---

## Frontend Pages

### Admin Dashboard (Desktop-first)
| Route | Page | Description |
|-------|------|-------------|
| `/` | Dashboard | Map with live locations, KPI cards, recent activity |
| `/workers` | Field Workers | Employee table with current status and location |
| `/checkins` | Check-ins | Paginated table with date/user filters |
| `/work-orders` | Work Orders | Kanban or table view, create/assign tasks |
| `/routes` | Routes | Route planning with drag-and-drop stops |
| `/client-sites` | Client Sites | CRUD list with map pins |
| `/geo-fences` | Geo-Fences | Map-based geo-fence creation/management |
| `/visits` | Visits | Visit log table |
| `/expenses` | Expenses | Expense claims list, approval queue |
| `/mileage` | Mileage | Mileage reports |
| `/analytics` | Analytics | Charts: visits/day, travel time, coverage heatmap |
| `/settings` | Settings | Org field settings |

### Field Worker View (Mobile-first)
| Route | Page | Description |
|-------|------|-------------|
| `/my` | My Dashboard | Current status, today's route/tasks, quick check-in |
| `/my/checkin` | Check-In | GPS capture, optional photo, site selection |
| `/my/tasks` | My Tasks | Today's assigned work orders |
| `/my/route` | My Route | Today's route with map and directions |
| `/my/visits/new` | Log Visit | Quick visit log form |
| `/my/expenses` | My Expenses | Create/view expense claims |
| `/my/mileage` | My Mileage | View mileage summary |

---

## Getting Started

### Prerequisites
- Node.js 20+
- pnpm 9+
- MySQL 8+
- Redis 7+
- EMP Cloud running (for authentication)

### Install
```bash
git clone https://github.com/anthropic/emp-field.git
cd emp-field
pnpm install
```

### Environment Setup
```bash
cp .env.example .env
# Edit .env with your database credentials and EMP Cloud URL
```

### Docker
```bash
docker-compose up -d
```

### Development
```bash
# Run all packages in development mode
pnpm dev

# Run individually
pnpm --filter @emp-field/server dev    # Server on :4700
pnpm --filter @emp-field/client dev    # Client on :5181

# Run migrations
pnpm --filter @emp-field/server migrate
```

---

## Implementation Plan

### Phase 1: MVP (Weeks 1-3) -- Basic Field Tracking
**Goal:** Field workers can check in/out with GPS, admins can view checkins.

- **Week 1:** Project scaffold, dual DB connections, Migration 001 (settings, client sites, checkins), auth middleware
- **Week 2:** Check-in service, client site CRUD, settings service, upload service, audit logging
- **Week 3:** Frontend MVP -- dashboard, check-in/checkout page (mobile-first), checkins list, client sites CRUD, settings

### Phase 2: Core Features (Weeks 4-6) -- Field Operations
**Goal:** Location tracking, geo-fencing, work orders, route planning.

- **Week 4:** Location tracking (batch ingestion, Redis live cache), geo-fence CRUD with point-in-polygon detection, live location map
- **Week 5:** Work orders (CRUD, status transitions, assignment), visit logs, notification service
- **Week 6:** Route planning (stops, distance calculation, drag-and-drop ordering), field worker "My Route" and "My Tasks" views

### Phase 3: Advanced (Weeks 7-9) -- Intelligence and Compliance
**Goal:** Expenses, mileage, analytics, route optimization, offline mode.

- **Week 7:** Expense CRUD with approval workflow, mileage auto-calculation from GPS tracks
- **Week 8:** Analytics dashboard (KPIs, productivity, coverage), route optimization (nearest-neighbor TSP + 2-opt)
- **Week 9:** Offline mode (service worker, IndexedDB queue, sync), BullMQ background jobs (auto-checkout, SLA checker), E2E tests

---

## License

This project is licensed under the [AGPL-3.0 License](LICENSE).
