# EmpMonitor Field Tracking - Mobile App API

## Project Status & Complete Documentation

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Tech Stack](#2-tech-stack)
3. [Project Architecture](#3-project-architecture)
4. [Directory Structure](#4-directory-structure)
5. [Server Entry Point](#5-server-entry-point)
6. [Database Models & Schemas](#6-database-models--schemas)
7. [API Routes & Endpoints](#7-api-routes--endpoints)
8. [Controllers](#8-controllers)
9. [Services](#9-services)
10. [Middleware](#10-middleware)
11. [Validation Layer](#11-validation-layer)
12. [Authentication & Authorization](#12-authentication--authorization)
13. [Response Structure](#13-response-structure)
14. [External Integrations](#14-external-integrations)
15. [Cron Jobs](#15-cron-jobs)
16. [Configuration](#16-configuration)
17. [Logging](#17-logging)
18. [Dependencies](#18-dependencies)
19. [Local Development Setup Guide](#19-local-development-setup-guide)

---

## 1. Project Overview

| Field | Details |
|-------|---------|
| **Project Name** | EmpMonitor Field Tracking (Mobile App API) |
| **Version** | 1.0.0 |
| **Author** | Suresh Babu G |
| **Type** | RESTful API Backend |
| **Purpose** | Comprehensive field employee tracking and monitoring system |

### What This Project Does

EmpMonitor Field Tracking is a backend API service designed for managing and monitoring field employees. It provides a complete suite of features including:

- **Employee Field Tracking** — Real-time GPS location tracking with configurable frequency
- **Task Management** — Create, assign, and track tasks with recurring task support
- **Attendance Management** — Check-in/check-out, geofenced attendance, biometric support
- **Client Management** — Manage clients with geolocation, categories, and assigned employees
- **Leave & Holiday Management** — Leave types, leave requests, and holiday calendar
- **Reports & Analytics** — Consolidated reports, distance traveled, user stats, and automated email reports
- **User & Role Management** — Role-based access, user creation, import from EmpMonitor
- **Transport Mode Tracking** — Track employee mode of transport with configurable radius/frequency
- **Tag System** — Customizable tagging for task stages
- **Profile Management** — Employee and admin profiles with image uploads
- **Two-Factor Authentication** — OTP-based phone verification and email verification
- **Automated Email Reports** — Scheduled report generation and delivery

---

## 2. Tech Stack

| Technology | Purpose |
|------------|---------|
| **Node.js** | Runtime environment |
| **Express.js** (v4.18.2) | Web framework |
| **MongoDB** | Database (via Mongoose v7.1.0) |
| **JWT** (jsonwebtoken v9.0.0) | Authentication tokens |
| **bcrypt** (v5.1.0) | Password hashing |
| **Joi** (v17.9.2) | Request validation |
| **SendGrid** | Email delivery service |
| **Twilio** | SMS/OTP service |
| **Google Cloud Storage** | File/image storage |
| **Google Maps APIs** | Snap-to-Roads, geocoding |
| **Swagger** | API documentation |
| **Winston + Morgan** | Logging |
| **Helmet** | Security headers |
| **Multer** | File upload handling |
| **Moment.js** | Date/time handling |
| **node-cron** | Scheduled jobs |

---

## 3. Project Architecture

The project follows a **modular MVC-like architecture** with each feature domain organized into its own module under the `/core` directory.

```
Request Flow:
Client → Express Server → CORS/Helmet/Compression → Route → Middleware (JWT Verify) → Validation (Joi) → Controller → Service → Model (MongoDB) → Response
```

### Architecture Pattern

Each module follows a consistent structure:
- **Routes** (`*.routes.js`) — Define HTTP endpoints and wire up middleware + controllers
- **Controller** (`*.controller.js`) — Handle request/response, call services
- **Service** (`*.service.js`) — Business logic, database operations
- **Model** (`*.model.js`) — Mongoose schema definitions
- **Validation** (`*.validate.js`) — Joi validation schemas

---

## 4. Directory Structure

```
empmonitor-mobile-app-api/
├── server.js                          # Main entry point
├── package.json                       # Dependencies and scripts
├── .env                               # Environment variables
├── storageconfig.json                 # Google Cloud Storage credentials
│
├── config/                            # Environment-specific configurations
│   ├── development.json               # Development environment config
│   ├── localDev.json                  # Local development config
│   └── production.json                # Production environment config
│
├── core/                              # Feature modules
│   ├── admin/                         # Admin management
│   │   ├── admin.controller.js
│   │   ├── admin.model.js
│   │   ├── admin.routes.js
│   │   ├── admin.service.js
│   │   └── admin.validate.js
│   │
│   ├── user/                          # User management
│   │   ├── user.controller.js
│   │   ├── user.routes.js
│   │   ├── user.service.js
│   │   └── user.validate.js
│   │
│   ├── role/                          # Role management
│   │   ├── role.controller.js
│   │   ├── role.model.js
│   │   ├── role.routes.js
│   │   └── role.service.js
│   │
│   ├── unauthorized/                  # Public/unauthenticated routes
│   │   ├── unauthorized.controller.js
│   │   ├── unauthorized.routes.js
│   │   ├── unauthorized.service.js
│   │   └── unauthorized.validate.js
│   │
│   ├── tracking/                      # GPS location tracking
│   │   ├── track.controller.js
│   │   ├── track.model.js
│   │   ├── track.routes.js
│   │   ├── track.service.js
│   │   └── track.validate.js
│   │
│   ├── leave/                         # Leave management
│   │   ├── leave.controller.js
│   │   ├── leave.model.js
│   │   ├── leave.routes.js
│   │   ├── leave.service.js
│   │   └── leave.validate.js
│   │
│   ├── holiday/                       # Holiday management
│   │   ├── holiday.controller.js
│   │   ├── holiday.model.js
│   │   ├── holiday.routes.js
│   │   ├── holiday.service.js
│   │   └── holiday.validate.js
│   │
│   ├── client/                        # Client management
│   │   ├── client.controller.js
│   │   ├── client.model.js
│   │   ├── client.routes.js
│   │   ├── client.service.js
│   │   └── client.validate.js
│   │
│   ├── category/                      # Category management
│   │   ├── category.controller.js
│   │   ├── category.model.js
│   │   ├── category.routes.js
│   │   ├── category.service.js
│   │   └── category.validate.js
│   │
│   ├── task/                          # Task management
│   │   ├── task.controller.js
│   │   ├── task.model.js
│   │   ├── task.routes.js
│   │   ├── task.service.js
│   │   └── task.validate.js
│   │
│   ├── profile/                       # Profile management
│   │   ├── profile.controller.js
│   │   ├── profile.model.js
│   │   ├── profile.routes.js
│   │   ├── profile.service.js
│   │   └── profile.validate.js
│   │
│   ├── attendance/                    # Attendance management
│   │   ├── attendance.controller.js
│   │   ├── attendance.routes.js
│   │   ├── attendance.service.js
│   │   └── attendance.validate.js
│   │
│   ├── hrmsAdmin/                     # HRMS Admin operations
│   │   ├── hrmsAdmin.controller.js
│   │   ├── hrmsAdmin.routes.js
│   │   ├── hrmsAdmin.service.js
│   │   ├── hrmsAdmin.validate.js
│   │   ├── orgLocation.model.js
│   │   └── employeeLocation.model.js
│   │
│   ├── transport/                     # Transport mode management
│   │   ├── transport.controller.js
│   │   ├── transport.model.js
│   │   ├── transport.routes.js
│   │   ├── transport.service.js
│   │   └── transport.validate.js
│   │
│   ├── reports/                       # Reporting & analytics
│   │   ├── reports.controller.js
│   │   ├── reports.routes.js
│   │   ├── reports.service.js
│   │   ├── reports.validate.js
│   │   └── report.currency.js
│   │
│   ├── tags/                          # Tags management
│   │   ├── tag.controller.js
│   │   ├── tag.model.js
│   │   ├── tag.routes.js
│   │   └── tag.service.js
│   │
│   ├── autoEmailReport/               # Automated email reports
│   │   ├── autoEmailReport.controller.js
│   │   ├── autoEmailReport.model.js
│   │   ├── autoEmailReport.routes.js
│   │   ├── autoEmailReport.services.js
│   │   └── autoEmailReport.validation.js
│   │
│   ├── cronJobs/                      # Scheduled tasks
│   │   └── cronSchedule.js
│   │
│   └── models/                        # Shared models
│       ├── organization.model.js
│       └── userTracking.model.js
│
├── middleware/                         # Express middleware
│   └── verifyToken.js                 # JWT authentication middleware
│
├── resources/                         # Shared resources
│   ├── database/
│   │   ├── mongo.database.js          # MongoDB initialization
│   │   └── mongo.connect.js           # MongoDB connection handler
│   ├── routes/
│   │   └── public.routes.js           # Central route aggregator
│   ├── logs/
│   │   ├── logger.log.js              # Winston logger configuration
│   │   └── responselogs/              # Daily rotated log files
│   ├── utils/
│   │   └── helpers/
│   │       ├── emp.helper.js          # EmpMonitor integration helpers
│   │       └── userRoutes.helper.js   # Route access control helpers
│   └── views/
│       ├── swagger.config.js          # Swagger auto-generation config
│       └── swaggerAuth.js             # Swagger authentication setup
│
├── response/
│   └── response.js                    # Standardized API response handler
│
├── mailService/                       # Email templates and services
│
└── language/                          # Internationalization files
```

---

## 5. Server Entry Point

**File:** `server.js`

### Startup Sequence

1. **Environment Setup** — Loads `.env` file via `dotenv`, determines `NODE_ENV` (local/development/production)
2. **Express Initialization** — Creates Express app and HTTP server
3. **Security Middleware** — Applies `Helmet` (security headers) and `Compression` (gzip)
4. **CORS** — Configured to allow all origins (`*`)
5. **Body Parsers** — JSON parser (50MB limit), URL-encoded parser
6. **Logging** — Morgan HTTP logger + Winston structured logging
7. **Response Headers** — Sets `X-Powered-By`, access control headers
8. **Error Handlers** — Global `unhandledRejection` and `uncaughtException` handlers
9. **Swagger** — API documentation served at `/api-doc`
10. **MongoDB Connection** — Connects to MongoDB using config credentials
11. **Route Registration** — Loads all routes from `/resources/routes/public.routes.js`
12. **Cron Jobs** — Starts scheduled currency conversion job
13. **Server Start** — Listens on configured port (default: 3000)

### Available npm Scripts

| Script | Command | Description |
|--------|---------|-------------|
| `start` | `node server.js` | Start the server |
| `swagger-autogen` | `node ./resources/views/swagger.config.js` | Generate Swagger docs |

---

## 6. Database Models & Schemas

### 6.1 Admin Schema (`core/admin/admin.model.js`)

Stores organization administrator information.

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| `fullName` | String | Yes | — | Administrator's full name |
| `age` | Number | No | — | Age |
| `gender` | String | Yes | — | Gender |
| `email` | String | Yes | — | Email address (unique) |
| `password` | String | Yes | — | Bcrypt-hashed password |
| `profilePic` | String | No | `null` | Profile image URL |
| `role` | String | No | `null` | Admin role |
| `emp_id` | String | No | — | Employee ID |
| `orgId` | String | Yes | — | Organization ID |
| `address1` | String | No | — | Primary address |
| `address2` | String | No | — | Secondary address |
| `latitude` | String | No | `null` | GPS latitude |
| `longitude` | String | No | `null` | GPS longitude |
| `city` | String | Yes | — | City |
| `state` | String | Yes | — | State |
| `country` | String | Yes | — | Country |
| `zipCode` | String | No | — | ZIP code (max 6 chars) |
| `phoneNumber` | Number | Yes | — | Phone number |
| `timezone` | String | Yes | — | Timezone |
| `isSuspended` | Boolean | No | `false` | Suspension status |
| `orgRadius` | Number | No | `10` | Organization geofence radius |
| `snap_points_limit` | Number | No | `100` | Snap points limit |
| `snap_duration_limit` | Number | No | `60` | Snap duration limit |
| `forgotPasswordToken` | String | No | — | Password reset token |
| `forgotTokenExpire` | Date | No | — | Token expiration |
| `passwordEmailSentCount` | Number | No | `0` | Password reset email count |

---

### 6.2 User/Profile Schema (`core/profile/profile.model.js`)

Stores employee/user information. This is the primary user model.

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| `fullName` | String | Yes | — | User's full name |
| `age` | Number | No | — | Age |
| `gender` | String | No | — | Gender |
| `email` | String | Yes | — | Email address |
| `password` | String | Yes | — | Bcrypt-hashed password |
| `profilePic` | String | No | `null` | Profile image URL |
| `location` | String | No | `null` | Assigned location |
| `department` | String | No | `null` | Department name |
| `status` | Number | No | `1` | `1`=Active, `2`=Suspended, `4`=Deleted |
| `role` | String | No | — | User role |
| `emp_id` | String | No | — | Employee ID |
| `orgId` | String | Yes | — | Organization ID |
| `address1` | String | No | — | Primary address |
| `address2` | String | No | — | Secondary address |
| `latitude` | String | No | `null` | GPS latitude |
| `longitude` | String | No | `null` | GPS longitude |
| `city` | String | No | `null` | City |
| `state` | String | No | `null` | State |
| `country` | String | No | `null` | Country |
| `zipCode` | String | No | `null` | ZIP code |
| `phoneNumber` | String | No | `null` | Phone number |
| `timezone` | String | No | — | Timezone |
| `isSuspended` | Boolean | No | `false` | Suspension flag |
| `forgotPasswordToken` | String | No | — | Password reset token |
| `forgotTokenExpire` | Date | No | — | Token expiry |
| `passwordEmailSentCount` | Number | No | `0` | Reset email count |
| `isGeoFencingOn` | Number | No | `0` | Geofencing enabled flag |
| `isMobileDeviceEnabled` | Number | No | `1` | Mobile device access |
| `isBioMetricEnabled` | Number | No | `0` | Biometric auth enabled |
| `isWebEnabled` | Number | No | `0` | Web access enabled |
| `frequency` | Number | No | `20` | Location tracking frequency (seconds) |
| `geoLogsStatus` | Boolean | No | `true` | Geolocation logging active |
| `snap_points_limit` | Number | No | `100` | Snap points limit |
| `snap_duration_limit` | Number | No | `60` | Snap duration limit |
| `isBiometricUser` | Boolean | No | `false` | Biometric user flag |

---

### 6.3 Role Schema (`core/role/role.model.js`)

Defines custom roles within an organization.

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| `role` | String | Yes | — | Role name |
| `orgId` | String | Yes | — | Organization ID |

---

### 6.4 Client Schema (`core/client/client.model.js`)

Stores client/customer information with geolocation.

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| `clientName` | String | Yes | — | Client name |
| `orgId` | String | No | — | Organization ID |
| `emp_id` | String | No | — | Associated employee ID |
| `emailId` | String | No | — | Client email |
| `contactNumber` | String | No | `null` | Contact number |
| `clientProfilePic` | String | No | `null` | Profile image URL |
| `countryCode` | String | No | `null` | Phone country code |
| `address1` | String | Yes | — | Primary address |
| `address2` | String | No | `null` | Secondary address |
| `city` | String | No | — | City |
| `state` | String | No | — | State |
| `country` | String | Yes | — | Country |
| `zipCode` | String | No | `null` | ZIP code (max 6) |
| `latitude` | String | Yes | — | GPS latitude |
| `longitude` | String | Yes | — | GPS longitude |
| `clientType` | Number | No | `0` | `0`=Active, `1`=Suspended, `2`=Expired |
| `category` | String | Yes | — | Client category |
| `status` | Number | No | `0` | `0`=Active, `1`=Suspended, `2`=Deleted |
| `assignedEmployees` | ObjectId | No | — | Reference to User schema |
| `createdBy` | String | No | — | Creator identifier |
| `updatedBy` | String | No | — | Last updater identifier |

---

### 6.5 Category Schema (`core/category/category.model.js`)

Defines client categories for organizational classification.

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| `categoryName` | String | No | — | Category name |
| `orgId` | String | No | — | Organization ID |

---

### 6.6 Task Schema (`core/task/task.model.js`)

Comprehensive task management with recurrence support.

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| `clientId` | String | No | — | Associated client ID |
| `orgId` | String | Yes | — | Organization ID |
| `taskName` | String | Yes | — | Task title |
| `emp_id` | String | Yes | — | Assigned employee ID |
| `date` | String | No | — | Task date |
| `start_time` | String | No | — | Scheduled start time |
| `end_time` | String | No | — | Scheduled end time |
| `taskDescription` | String | No | — | Detailed task description |
| `taskApproveStatus` | Number | No | `0` | Approval status |
| `empStartTime` | String | No | `null` | Employee's actual start time |
| `empEndTime` | String | No | `null` | Employee's actual end time |
| `tagLogs` | Array | No | `[]` | `[{tagName, time}]` — Tag stage logs |
| `createdBy` | String | No | — | Creator |
| `updatedBy` | String | No | — | Last updater |
| `files` | Array | No | `[]` | `[{url}]` — Attached file URLs |
| `images` | Array | No | `[]` | `[{url, description}]` — Attached images |
| `value` | Object | No | — | `{currency, amount, convertedAmountInUSD}` |
| `taskVolume` | Number | No | — | Task volume metric |
| `recurrenceId` | String | No | — | Recurrence group ID |
| `recurrenceDetails` | Object | No | — | See below |

**Recurrence Details:**
| Field | Type | Description |
|-------|------|-------------|
| `TaskCycle` | Number | `0`=None, `1`=Repeating, `2`=Daily |
| `startDate` | String | Recurrence start date |
| `endDate` | String | Recurrence end date |
| `daysOfWeek` | [Number] | Days of the week (0=Sun, 6=Sat) |

---

### 6.7 Track Schema (`core/tracking/track.model.js`)

Stores daily location tracking data for employees.

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| `orgId` | String | Yes | — | Organization ID |
| `emp_id` | String | Yes | — | Employee ID |
| `distTravelled` | Number | No | `0` | Total distance traveled |
| `date` | String | No | — | Tracking date |
| `checkIn` | String | No | `null` | Check-in time |
| `checkOut` | String | No | `null` | Check-out time |
| `currentFrequency` | Number | No | `25` | Current tracking frequency |
| `currentMode` | String | No | `"bike"` | Current transport mode |
| `geologs` | Array | No | `[]` | Array of geo-log entries |

**GeoLog Entry:**
| Field | Type | Default | Description |
|-------|------|---------|-------------|
| `time` | String | — | Timestamp of log |
| `latitude` | Number | — | GPS latitude |
| `longitude` | Number | — | GPS longitude |
| `status` | Number | `0` | Log status |
| `viewed` | Number | `0` | Viewed flag |
| `taskId` | ObjectId | — | Reference to Task |

---

### 6.8 Leave Schema (`core/leave/leave.model.js`)

Defines leave types for an organization.

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| `name` | String | Yes | — | Leave type name |
| `duration` | Number | Yes | — | `1`=Yearly, `2`=Half-yearly, `3`=Quarterly, `4`=Monthly |
| `no_of_days` | Number | Yes | — | Number of allowed days |
| `carry_forward` | Number | Yes | — | `0`=No carry forward, `1`=Carry forward allowed |
| `orgId` | String | Yes | — | Organization ID |

---

### 6.9 Holiday Schema (`core/holiday/holiday.model.js`)

Stores organization holidays.

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| `name` | String | Yes | — | Holiday name |
| `date` | Date | Yes | — | Holiday date |
| `orgId` | String | Yes | — | Organization ID |

---

### 6.10 Transport Schema (`core/transport/transport.model.js`)

Stores employee transport mode configuration.

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| `orgId` | String | Yes | — | Organization ID |
| `emp_id` | String | Yes | — | Employee ID |
| `currentRadius` | Number | No | `10` | Current geofence radius |
| `currentFrequency` | Number | No | `25` | Location ping frequency (seconds) |
| `currentMode` | String | No | `"bike"` | Transport mode |
| `defaultConfig` | Array | No | `[]` | Default transport configurations |

---

### 6.11 Tags Schema (`core/tags/tag.model.js`)

Custom tags for task stages and categorization.

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| `orgId` | String | Yes | — | Organization ID |
| `tagName` | String | Yes | — | Tag name (unique, trimmed) |
| `tagDescription` | String | No | — | Tag description |
| `color` | String | No | — | Tag color (hex/name) |
| `createdBy` | ObjectId | Yes | — | Reference to creator |
| `updatedBy` | ObjectId | No | — | Reference to last updater |
| `isActive` | Boolean | Yes | `true` | Active status |
| `order` | Number | Yes | — | Display order (1-10) |

---

### 6.12 Auto Email Report Schema (`core/autoEmailReport/autoEmailReport.model.js`)

Configuration for automated report generation and email delivery.

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| `reportsTitle` | String | Yes | — | Report title |
| `orgId` | String | Yes | — | Organization ID |
| `frequency` | Array | No | — | Scheduling: `[{Daily, Weekly, Monthly, Time, Date: {startDate, endDate}}]` |
| `Recipients` | [String] | No | — | Email recipients list |
| `Content` | Array | No | — | `[{consolidatedReport, task, clients, leaves, tags, role}]` |
| `ReportsType` | Array | No | — | `[{pdf, csv}]` |
| `filter` | Object | No | — | `{wholeOrganization, specificEmployees: [{id}]}` |
| `sendTestMail` | Boolean | No | — | Send test mail flag |

---

### 6.13 Organization Schema (`core/models/organization.model.js`)

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| `orgName` | String | No | — | Organization name |

---

### 6.14 User Tracking Schema (`core/models/userTracking.model.js`)

Tracks user login/logout/task activity events.

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| `userId` | String | Yes | — | User ID |
| `orgId` | String | Yes | — | Organization ID |
| `trackingType` | String | Yes | — | `"login"`, `"logOut"`, `"InTask"` |
| `taskId` | String | No | — | Associated task ID |
| `date` | Date | No | `Date.now()` | Event timestamp |
| `latitude` | String | Yes | — | GPS latitude |
| `longitude` | String | Yes | — | GPS longitude |

---

### 6.15 Organization Location Schema (`core/hrmsAdmin/orgLocation.model.js`)

Defines office/organization locations with geofencing.

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| `orgId` | String | Yes | — | Organization ID |
| `locationName` | String | No | — | Location name |
| `address` | String | No | `null` | Full address |
| `latitude` | String | No | `null` | GPS latitude |
| `longitude` | String | No | `null` | GPS longitude |
| `range` | Number | No | `10` | Geofence radius (meters) |
| `geo_fencing` | Boolean | No | `false` | Geofencing enabled |
| `isMobEnabled` | Boolean | No | `true` | Mobile access enabled |
| `global` | Array | No | `[]` | Global configuration |
| `createdBy` | String | No | — | Creator |
| `updatedBy` | String | No | — | Last updater |

---

### 6.16 Employee Location Schema (`core/hrmsAdmin/employeeLocation.model.js`)

Per-employee location configuration with individual geofencing.

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| `orgId` | String | Yes | — | Organization ID |
| `employeeId` | ObjectId | Yes | — | Reference to User schema |
| `address` | String | No | `null` | Employee address |
| `latitude` | Number | No | `null` | GPS latitude |
| `longitude` | Number | No | `null` | GPS longitude |
| `range` | Number | No | `10` | Geofence radius |
| `geo_fencing` | Boolean | No | `false` | Geofencing enabled |
| `isMobEnabled` | Boolean | No | `true` | Mobile device enabled |
| `createdBy` | String | No | — | Creator |
| `updatedBy` | String | No | — | Last updater |

---

## 7. API Routes & Endpoints

All routes are prefixed with the API version: `/v1`

### 7.1 Admin Routes — `/v1/admin`

**File:** `core/admin/admin.routes.js`

#### Authentication & Account

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/sign-up` | No | Register a new organization and admin account. Creates the org, then the admin user with hashed password and JWT token. |
| POST | `/sign-in` | No | Admin login with email/password. Returns JWT token on success. |
| POST | `/forgot-password-mail` | No | Sends a password reset email with a unique token to the admin's registered email. |
| POST | `/reset-password` | No | Resets admin password using the token received via email. |
| POST | `/email-verification-token-generate` | No | Generates and sends an email verification token. |
| POST | `/phone-verification-otp-generate` | No | Generates and sends an OTP to the admin's phone via Twilio. |
| POST | `/update-phone` | Yes | Add or update admin phone number. |
| POST | `/verify-phone` | Yes | Verify phone number using OTP. |
| PUT | `/update` | Yes | Update admin account details (name, address, etc.). |
| PUT | `/update-password` | Yes | Change admin password (requires current password). |
| PUT | `/two-factor-auth` | Yes | Enable or disable two-factor authentication. |
| GET | `/admin-fetch` | Yes | Fetch current admin's profile data. |
| POST | `/adminUploadProfileImage` | Yes | Upload admin profile picture (via Multer + GCS). |

#### Dashboard & Analytics

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/get-dashboard-stats` | Yes | Get dashboard statistics — total employees, active users, task counts, attendance summary. |
| GET | `/allTaskStats` | Yes | Get aggregated task statistics across the organization. |
| GET | `/average-working-hours` | Yes | Calculate and return average working hours across employees. |
| POST | `/getUserTimeLine` | Yes | Get detailed timeline for a specific user — includes check-in/out, tasks, and location history. |
| POST | `/getEmployeeDetails` | Yes | Get detailed information about a specific employee. |

#### Employee Management

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/getAllAdminFieldTrackingUsers` | Yes | Get all field tracking users for the admin's organization with pagination and filters. |
| POST | `/allOrgEmployee` | Yes | Get all employees in the organization. |
| DELETE | `/permanent-delete-user` | Yes | Permanently delete a user and all associated data. |
| DELETE | `/soft-delete-user` | Yes | Soft delete (set status to deleted) without removing data. |
| POST | `/restore-softDelete-Users` | Yes | Restore previously soft-deleted users. |

#### Location & Tracking

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/getUserCoordinates` | Yes | Get GPS coordinates for a specific user on a given date. |
| PUT | `/update-Coordinates-Status` | Yes | Update the status of a user's geolocation log entry (mark as viewed, etc.). |
| GET | `/getLocation` | Yes | Get list of configured locations for the organization. |
| GET | `/getDepartment` | Yes | Get list of departments. |
| POST | `/allUsers-Tracking-Data` | Yes | Get tracking data for all users — includes distance, check-in/out, geo-logs. |
| POST | `/users-LocationDetails` | Yes | Get detailed location info for specific users. |

#### Admin Tasks

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/admin-task` | Yes | Get admin-specific tasks. |
| POST | `/createTask` | Yes | Create a new task from admin panel. |
| GET | `/fetchTask` | Yes | Fetch all admin tasks. |
| PUT | `/updateTask` | Yes | Update an existing admin task. |
| DELETE | `/deleteTask` | Yes | Delete an admin task. |

#### Admin Client Management

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/createClient` | Yes | Create a new client from admin panel. |
| GET | `/fetchClient` | Yes | Fetch all clients. |
| PUT | `/updateClient` | Yes | Update client information. |
| DELETE | `/deleteClient` | Yes | Delete a client. |

#### Attendance

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/allEmployeesAttendance` | Yes | Get attendance records for all employees with date filters. |

#### Miscellaneous

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/get-languages` | Yes | Get list of supported languages for the application. |

---

### 7.2 User Routes — `/v1/user`

**File:** `core/user/user.routes.js`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/create` | Yes | Create a new user/employee. Sends welcome email with verification link. |
| GET | `/fetch` | Yes | Fetch users with pagination, search, and filter support. |
| PUT | `/update` | Yes | Update user details (name, role, department, etc.). |
| DELETE | `/delete` | Yes | Delete a user. |
| GET | `/fetch-emp-users` | Yes | Fetch users from the main EmpMonitor system. |
| POST | `/add-emp-users` | Yes | Import users from EmpMonitor into the field tracking system. |
| GET | `/user-frequency-geolocation` | Yes | Get user's current frequency and geolocation settings. |
| PUT | `/update_Biometric_config` | Yes | Update biometric authentication configuration for a user. |

---

### 7.3 Role Routes — `/v1/roles`

**File:** `core/role/role.routes.js`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/create` | Yes | Create a new custom role for the organization. |
| GET | `/get` | Yes | Get all roles in the organization. |
| PUT | `/update` | Yes | Update a role name. |
| DELETE | `/delete` | Yes | Delete a role. |

---

### 7.4 Open/Public User Routes — `/v1/open-user`

**File:** `core/unauthorized/unauthorized.routes.js`

These routes do **not** require authentication (public endpoints).

#### Authentication

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/verify-email` | No | Verify user email address using verification token. |
| POST | `/verify-phone` | No | Verify user phone number using OTP. |
| POST | `/set-password` | No | Set password for a newly verified user. |
| POST | `/user-login` | No | User login with email/password. Returns JWT token. |
| POST | `/forgot-password` | No | Initiate password reset flow — sends reset email. |
| GET | `/verifyOTP` | No | Verify OTP code. |
| PUT | `/reset-password` | No | Reset password using reset token. |
| POST | `/generate-token` | No | Generate a new email verification token. |
| POST | `/phone-verification-otp-generate` | No | Generate phone OTP via Twilio. |

#### Profile & Attendance

| Method | Endpoint | Auth | No | Description |
|--------|----------|------|-----|-------------|
| PUT | `/update-profile` | Token | — | Update user profile information. |
| POST | `/check-in` | Token | — | Mark attendance check-in with GPS location. |
| GET | `/attendance-history` | Token | — | Get attendance history for the authenticated user. |

#### User Import

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/import-users` | No | Bulk import users from CSV/Excel. |
| POST | `/import-admin` | No | Admin-level user import. |
| POST | `/empmonitor-employee-import` | No | Import employees from EmpMonitor HRMS. |
| POST | `/delete-user` | No | Permanently delete a user. |

#### Location

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/get-location-list` | No | Get list of organization locations. |
| POST | `/get-geo-location-details` | No | Get geolocation details for a user/location. |
| POST | `/update-geo-location-details` | No | Update geolocation configuration. |

---

### 7.5 Tracking Routes — `/v1/track`

**File:** `core/tracking/track.routes.js`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/get-location` | Yes | Submit and track user location. Receives GPS coordinates from mobile device, calculates distance traveled, and stores geo-log entries. |

---

### 7.6 Leave Routes — `/v1/leaves`

**File:** `core/leave/leave.routes.js`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/create-leave-type` | Yes | Create a new leave type (e.g., Sick Leave, Casual Leave) with duration and carry-forward settings. |
| GET | `/get-leave-type` | Yes | Get all configured leave types for the organization. |
| PUT | `/update-leave-type` | Yes | Update an existing leave type's configuration. |
| DELETE | `/delete-leave-type` | Yes | Delete a leave type. |
| POST | `/get-leaves` | Yes | Get employee leave records with date range filters. |
| POST | `/create-leave` | Yes | Create/apply for a new leave request. |
| POST | `/fetch-leave-type` | Yes | Get leave type options for dropdown/selection. |
| POST | `/update-leaves` | Yes | Update a leave request (approve, reject, modify). |
| POST | `/delete-leaves` | Yes | Delete a leave request. |

---

### 7.7 Holiday Routes — `/v1/holiday`

**File:** `core/holiday/holiday.routes.js`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/create-holiday` | Yes | Create a new holiday entry for the organization calendar. |
| GET | `/get-holiday` | Yes | Get all holidays for the organization. |
| PUT | `/update-holiday` | Yes | Update a holiday (name, date). |
| DELETE | `/delete-holiday` | Yes | Delete a holiday. |

---

### 7.8 Client Routes — `/v1/client`

**File:** `core/client/client.routes.js`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/create` | Yes | Create a new client with address, GPS coordinates, category, and assigned employees. |
| GET | `/fetch` | Yes | Fetch all clients for the organization with search and pagination. |
| PUT | `/update` | Yes | Update client details. |
| DELETE | `/delete` | Yes | Delete a client. |
| POST | `/clientUploadProfileImage` | Yes | Upload a client profile picture via Multer + Google Cloud Storage. |

---

### 7.9 Category Routes — `/v1/category`

**File:** `core/category/category.routes.js`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/create` | Yes | Create a new client category (e.g., "Retail", "Enterprise"). |
| GET | `/fetch` | Yes | Get all categories for the organization. |
| PUT | `/update` | Yes | Update a category name. |
| DELETE | `/delete` | Yes | Delete a category. |

---

### 7.10 Task Routes — `/v1/task`

**File:** `core/task/task.routes.js`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/create` | Yes | Create a new task. Supports recurring tasks (daily, weekly, custom days). Assigns to employee with client, time, and description. |
| GET | `/fetch` | Yes | Fetch tasks with filters (date, employee, client, status). |
| PUT | `/update` | Yes | Update task details. |
| DELETE | `/delete` | Yes | Delete a task. |
| PUT | `/update-approve` | Yes | Approve or reject a task submission. |
| POST | `/update-taskStatus` | Yes | Update task status (started, in-progress, completed). Records employee timestamps and tag stage logs. |
| POST | `/uploadTask-files` | Yes | Upload files/images to a task via Multer + Google Cloud Storage. |
| DELETE | `/deleteDocument` | Yes | Delete attached documents from a task. |
| POST | `/filterTask` | Yes | Advanced task filtering with multiple criteria. |
| GET | `/getNotification` | Yes | Get task-related notifications for the user. |

---

### 7.11 Profile Routes — `/v1/profile`

**File:** `core/profile/profile.routes.js`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/fetchProfile` | Yes | Get the authenticated user's complete profile. |
| POST | `/updateProfile` | Yes | Update profile information. |
| POST | `/uploadProfileImage` | Yes | Upload user profile picture. |
| POST | `/update-snap-details` | Yes | Update snap (location point) configuration details. |

---

### 7.12 Attendance Routes — `/v1/attendance`

**File:** `core/attendance/attendance.routes.js`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/fetch-attendance` | Yes | Fetch attendance records with date range and employee filters. |
| POST | `/mark-attendance` | Yes | Mark attendance (check-in/check-out) with GPS coordinates. Validates against geofence if enabled. |
| GET | `/attendance` | Yes | Get attendance summary for the authenticated user. |
| POST | `/attendance-request` | Yes | Submit an attendance correction/adjustment request. |

---

### 7.13 HRMS Admin Routes — `/v1/hrmsAdmin`

**File:** `core/hrmsAdmin/hrmsAdmin.routes.js`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/location-update` | Yes | Create or update an organization office location with geofencing configuration. |
| POST | `/get-location-details` | Yes | Get detailed information about organization locations. |
| POST | `/get-employee-conf` | Yes | Get employee configuration settings (geofencing, mobile access, etc.). |
| POST | `/update-employee-conf` | Yes | Update employee configuration (enable/disable geofencing, mobile, biometric). |
| GET | `/org-location-list` | Yes | Get all configured locations for the organization. |
| POST | `/updateEmployeeLocation` | Yes | Set or update individual employee's geofenced location. |
| GET | `/getEmployeeLocation` | Yes | Get employee's assigned location and geofencing settings. |

---

### 7.14 Transport Routes — `/v1/profile`

**File:** `core/transport/transport.routes.js`

> Note: These routes are mounted under the `/v1/profile` prefix.

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/Update-Emp-mode-of-transport` | Yes | Update employee's mode of transport (bike, car, walk, etc.). Adjusts tracking frequency based on mode. |
| POST | `/Update-Emp-Mot-Frequency-Radius` | Yes | Update transport-specific frequency and geofence radius settings. |

---

### 7.15 Reports Routes — `/v1/reports`

**File:** `core/reports/reports.routes.js`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/get-Consolidated-Reports` | Yes | Generate consolidated reports combining attendance, tasks, distance, and client data for date ranges. |
| POST | `/updateGeoFencing` | Yes | Update geofencing configuration from reports panel. |
| POST | `/getUserDetails` | Yes | Get detailed user report data — includes all metrics for a specific employee. |
| POST | `/taskListDetails` | Yes | Get detailed task list with all task data and statuses. |
| POST | `/userStats` | Yes | Get user statistics — attendance percentage, task completion rate, distance. |
| POST | `/taskStatus` | Yes | Get task status breakdown (pending, in-progress, completed, approved). |
| POST | `/taskStages` | Yes | Get task stage progression data based on tags. |
| POST | `/clientDetails` | Yes | Get client-specific report data — tasks per client, visit frequency. |
| POST | `/distanceTraveled` | Yes | Get distance traveled report with daily/weekly/monthly breakdown. |
| POST | `/getIndividualAttendanceData` | Yes | Get individual employee attendance report with detailed check-in/out times. |

---

### 7.16 Tags Routes — `/v1/tags`

**File:** `core/tags/tag.routes.js`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/createTags` | Yes | Create a new tag with name, description, color, and display order. |
| POST | `/updateTags` | Yes | Update tag details. |
| POST | `/updateTagsOrder` | Yes | Reorder tags (change display order). |
| GET | `/getTags` | Yes | Get all active tags for the organization. |
| DELETE | `/deleteTags` | Yes | Delete a tag (soft delete — sets `isActive` to `false`). |
| GET | `/getAdminTags` | Yes | Get all tags including inactive ones (admin view). |

---

### 7.17 Auto Email Report Routes — `/v1/auto-Generate-Report`

**File:** `core/autoEmailReport/autoEmailReport.routes.js`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/createAutoEmailReport` | Yes | Configure a new automated email report with frequency, recipients, content selection, and filters. |
| POST | `/get` | Yes | Get all configured auto email reports. |
| POST | `/Update` | Yes | Update an existing auto email report configuration. |
| POST | `/delete` | Yes | Delete an auto email report. |

---

## 8. Controllers

### 8.1 Admin Controller (`core/admin/admin.controller.js`)

**Total Methods:** 46+

The admin controller is the largest controller and handles all administrative operations:

- **Authentication Methods:** `addAdmin`, `fetchAdmin`, `updatePhone`, `verifyPhone`, `forgotPassword`, `resetPassword`, `generateToken`, `generateOtp`, `enableTwoFactor`, `updatePassword`
- **Profile Methods:** `uploadProfileImage`, `updateAdmin`, `adminDataFetch`
- **Dashboard Methods:** `dashboardStats`, `allTaskStats`, `averageWorkingHours`
- **Employee Management:** `getAllAdminFieldTrackingUsers`, `allOrgEmployee`, `deleteAdminUsers`, `softDeleteAdminUsers`, `restoreDeletedUsers`, `getEmployeeDetails`
- **Location Methods:** `getUserCoordinates`, `updateCoordinatesStatus`, `getLocation`, `getDepartment`, `getUserTimeLine`, `adminUsersTrackingData`, `adminUsersLocationDetails`
- **Task Methods:** `getAdminTask`, `createTask`, `fetchTask`, `updateTask`, `deleteTask`
- **Client Methods:** `createClient`, `fetchClientInfo`, `updateClient`, `deleteClient`
- **Attendance Methods:** `allEmployeesAttendance`
- **Utility Methods:** `getLanguages`

### 8.2 User Controller (`core/user/user.controller.js`)

**Methods:** `createUser`, `fetchUsers`, `updateUser`, `deleteUser`, `fetchEmpUsers`, `addEmpUsers`, `setFrequencyAndGeoLoc`, `updateBiometricConfig`

### 8.3 Role Controller (`core/role/role.controller.js`)

**Methods:** `create`, `get`, `update`, `delete`

### 8.4 Unauthorized Controller (`core/unauthorized/unauthorized.controller.js`)

**Methods:** `verifyUser`, `verifyPhone`, `setPassword`, `UserLogin`, `forgotPassword`, `verifyOTP`, `resetPassword`, `generateToken`, `generateOtp`, `updateProfile`, `attendance`, `getAttendance`, `importUsers`, `adminImportUsers`, `deleteUserPerm`, `getLocationList`, `getGeoLocationDetails`, `updateGeoLocationDetails`, `empEmployeeImport`

### 8.5 Track Controller (`core/tracking/track.controller.js`)

**Methods:** `trackUser` — Receives location data from mobile app and stores geo-logs with distance calculations.

### 8.6 Leave Controller (`core/leave/leave.controller.js`)

**Methods:** `createLeaveType`, `getLeaveType`, `updateLeaveType`, `deleteLeaveType`, `getLeaves`, `createLeave`, `leaveTypeOption`, `updateLeaves`, `deleteLeaves`

### 8.7 Holiday Controller (`core/holiday/holiday.controller.js`)

**Methods:** `createHoliday`, `getHoliday`, `updateHoliday`, `deleteHoliday`

### 8.8 Client Controller (`core/client/client.controller.js`)

**Methods:** `createClient`, `fetchClientInfo`, `updateClient`, `deleteClient`, `uploadProfileImage`

### 8.9 Category Controller (`core/category/category.controller.js`)

**Methods:** `addCategory`, `fetchCategory`, `updateCategory`, `deleteCategory`

### 8.10 Task Controller (`core/task/task.controller.js`)

**Methods:** `createTask`, `fetchTask`, `updateTask`, `deleteTask`, `approveTask`, `updateStatus`, `uploadTaskData`, `deleteDocs`, `filterTask`, `getNotification`

### 8.11 Profile Controller (`core/profile/profile.controller.js`)

**Methods:** `fetchProfile`, `updateProfile`, `uploadProfileImage`, `updateSnapDetails`

### 8.12 Attendance Controller (`core/attendance/attendance.controller.js`)

**Methods:** `fetchAttendance`, `markAttendance`, `getAttendance`, `attendanceRequest`

### 8.13 HRMS Admin Controller (`core/hrmsAdmin/hrmsAdmin.controller.js`)

**Methods:** `updateLocation`, `getLocationDetails`, `getEmployeeConf`, `updateEmployeeConf`, `orgLocationList`, `updateEmployeeLocation`, `getEmployeeLocation`

### 8.14 Transport Controller (`core/transport/transport.controller.js`)

**Methods:** `empModeOfTransport`, `empUpdFreqRad`

### 8.15 Reports Controller (`core/reports/reports.controller.js`)

**Methods:** `fetchConsolidatedReport`, `updateGeoFencing`, `getUserDetails`, `taskListDetails`, `userStats`, `taskStatus`, `taskStages`, `clientDetails`, `distanceTraveled`, `getIndividualAttendanceData`

### 8.16 Tags Controller (`core/tags/tag.controller.js`)

**Methods:** `createTags`, `updateTags`, `updateTagsOrder`, `getTags`, `deleteTags`, `getAdminTags`

### 8.17 Auto Email Report Controller (`core/autoEmailReport/autoEmailReport.controller.js`)

**Methods:** `createAutoEmailReport`, `fetchReportDetails`, `updateReport`, `deleteReport`

---

## 9. Services

Each controller has a corresponding service layer that contains the business logic and database operations.

| Service File | Module | Key Operations |
|-------------|--------|----------------|
| `admin.service.js` | Admin | Org creation, admin CRUD, dashboard queries, employee aggregation, coordinate processing |
| `user.service.js` | User | User CRUD, EmpMonitor integration, frequency/geolocation config |
| `role.service.js` | Role | Role CRUD operations |
| `unauthorized.service.js` | Public Auth | Email/phone verification, password management, login, user import |
| `track.service.js` | Tracking | Location storage, distance calculation, geo-log management |
| `leave.service.js` | Leave | Leave type CRUD, leave request management |
| `holiday.service.js` | Holiday | Holiday CRUD operations |
| `client.service.js` | Client | Client CRUD, profile image upload to GCS |
| `category.service.js` | Category | Category CRUD operations |
| `task.service.js` | Task | Task CRUD, recurrence generation, file uploads, status updates, notifications |
| `profile.service.js` | Profile | Profile fetch/update, image upload |
| `attendance.service.js` | Attendance | Attendance marking, geofence validation, history retrieval |
| `hrmsAdmin.service.js` | HRMS | Location management, employee configuration, geofencing setup |
| `transport.service.js` | Transport | Transport mode updates, frequency/radius configuration |
| `reports.service.js` | Reports | Consolidated reports, user stats, task analytics, distance calculations |
| `tag.service.js` | Tags | Tag CRUD, ordering, activation |
| `autoEmailReport.services.js` | Auto Reports | Report configuration CRUD, scheduling |
| `report.currency.js` | Currency | Currency conversion rate fetching and updates |

---

## 10. Middleware

### JWT Token Verification (`middleware/verifyToken.js`)

The primary middleware for protecting authenticated routes.

**How it works:**

1. Extracts JWT token from the `x-access-token` HTTP header
2. Verifies the token using the secret key from configuration
3. Checks user `memberType`:
   - If `memberType === 0` (restricted role), only allows access to whitelisted routes
   - All other member types get full access to authenticated endpoints
4. Attaches decoded user data to `req.verified` for use in controllers
5. Returns appropriate error responses:
   - `401 Unauthorized` — Missing or invalid token
   - `403 Forbidden` — Access denied for restricted users

**Whitelisted Routes for MemberType 0:**
- `/v1/app/submit` (defined in `resources/utils/helpers/userRoutes.helper.js`)

---

## 11. Validation Layer

Request validation is handled using **Joi** schemas. Each module has a `*.validate.js` file that defines validation rules.

### Validation Files

| File | Module | Key Validations |
|------|--------|-----------------|
| `admin.validate.js` | Admin | fullName (min 3), email (valid format), password, phoneNumber (10 digits), zipCode (4-6 digits), age (positive), city/state/country (trimmed) |
| `user.validate.js` | User | User creation/update fields |
| `unauthorized.validate.js` | Public Auth | Login, password set/reset, email/phone verification |
| `track.validate.js` | Tracking | GPS coordinates, emp_id, orgId |
| `leave.validate.js` | Leave | Leave type fields, leave request fields |
| `holiday.validate.js` | Holiday | Holiday name, date |
| `client.validate.js` | Client | Client name, address, coordinates, category |
| `category.validate.js` | Category | Category name |
| `task.validate.js` | Task | Task name, emp_id, dates, times, recurrence |
| `profile.validate.js` | Profile | Profile update fields |
| `attendance.validate.js` | Attendance | Check-in coordinates, date/time |
| `hrmsAdmin.validate.js` | HRMS | Location fields, employee config |
| `transport.validate.js` | Transport | Transport mode, frequency, radius |
| `reports.validate.js` | Reports | Date ranges, filters |
| `autoEmailReport.validation.js` | Auto Reports | Report config, frequency, recipients |

---

## 12. Authentication & Authorization

### Authentication Flow

```
┌─────────────┐     ┌──────────────┐     ┌───────────────┐
│  Client App  │────>│  Login API   │────>│  Validate     │
│  (Mobile)    │     │  (POST)      │     │  Credentials  │
└─────────────┘     └──────────────┘     └───────────────┘
                                                │
                                                ▼
                                         ┌───────────────┐
                                         │  Generate JWT  │
                                         │  Token         │
                                         └───────────────┘
                                                │
                                                ▼
┌─────────────┐     ┌──────────────┐     ┌───────────────┐
│  Protected   │────>│  verifyToken │────>│  Decode &     │
│  API Call    │     │  Middleware   │     │  Validate JWT │
│  (+ Header)  │     └──────────────┘     └───────────────┘
└─────────────┘                                 │
      x-access-token                            ▼
                                         ┌───────────────┐
                                         │  req.verified  │
                                         │  (user data)   │
                                         └───────────────┘
```

### Authentication Methods

| Method | Endpoint | Description |
|--------|----------|-------------|
| **Admin Signup** | `POST /v1/admin/sign-up` | Creates org + admin, returns JWT |
| **Admin Login** | `POST /v1/admin/sign-in` | Email/password auth, returns JWT |
| **User Login** | `POST /v1/open-user/user-login` | Email/password auth, returns JWT |
| **Email Verify** | `POST /v1/open-user/verify-email` | Token-based email verification |
| **Phone Verify** | `POST /v1/admin/verify-phone` | OTP-based phone verification |
| **2FA** | `PUT /v1/admin/two-factor-auth` | Enable/disable two-factor auth |
| **Password Reset** | `POST /v1/admin/forgot-password-mail` | Email-based reset token |

### Password Security

- Passwords are hashed using **bcrypt** before storage
- Password reset uses unique tokens with expiration
- OTP generation for phone verification via **Twilio**

### Token Details

- **Algorithm:** HS256 (default JWT)
- **Header:** `x-access-token`
- **Payload:** Contains `userData` object with user details and `memberType`

---

## 13. Response Structure

**File:** `response/response.js`

All API responses follow a standardized format:

### Success Response (200)
```json
{
  "statusCode": 200,
  "body": {
    "status": "success",
    "message": "Operation successful",
    "data": { ... }
  }
}
```

### Partial Success Response (206)
```json
{
  "statusCode": 206,
  "body": {
    "status": "partial_success",
    "message": "Partial operation completed",
    "data": { ... }
  }
}
```

### Failure Response (400)
```json
{
  "statusCode": 400,
  "body": {
    "status": "fail",
    "message": "Error description",
    "error": { ... }
  }
}
```

### Access Denied Response (403)
```json
{
  "statusCode": 403,
  "body": {
    "status": "access_denied",
    "message": "Access denied",
    "error": { ... }
  }
}
```

### Available Response Methods

| Method | Status Code | Use Case |
|--------|-------------|----------|
| `SuccessResp(message, data)` | 200 | Successful operations |
| `PartialSuccessResp(message, data)` | 206 | Partial success scenarios |
| `FailResp(message, error)` | 400 | General failures |
| `validationFailResp(message, error)` | 400 | Validation errors |
| `tokenFailResp(message, error)` | 400 | Token-related errors |
| `accessDeniedResp(message, error)` | 403 | Authorization failures |
| `taskFailResp(message, error)` | 400 | Task-specific errors |

---

## 14. External Integrations

### 14.1 SendGrid (Email Service)

- **Purpose:** Transactional emails — welcome emails, password reset, verification, automated reports
- **Sender:** `admin@empmonitor.com` (EmpMonitor)
- **Features Used:** Single send, template-based emails

### 14.2 Twilio (SMS/OTP Service)

- **Purpose:** Phone number verification via OTP
- **Features Used:** SMS sending, OTP generation and verification

### 14.3 Google Cloud Storage

- **Bucket:** `emp-field-tracking`
- **Project:** `empmonitor-production-487213`
- **Purpose:** File storage for profile images, task documents, biometric data
- **Config:** `storageconfig.json` (service account credentials)

### 14.4 Google Maps APIs

- **Snap to Roads API** — Snaps GPS coordinates to nearest road for accurate path tracking
- **Geocoding** — Reverse geocoding via OpenStreetMap (Nominatim) for address resolution

### 14.5 EmpMonitor HRMS Integration

- **Purpose:** Sync employees and attendance with the main EmpMonitor HRMS platform
- **Features:** Employee import, attendance submission, user synchronization
- **Helper:** `resources/utils/helpers/emp.helper.js`

---

## 15. Cron Jobs

### Currency Conversion Update (`core/cronJobs/cronSchedule.js`)

- **Purpose:** Periodically fetches and updates currency conversion rates
- **Service:** Uses `report.currency.js` for fetching rates
- **Trigger:** Started automatically on server initialization
- **Use Case:** Task values can have different currencies; this job keeps conversion rates up-to-date for USD normalization

---

## 16. Configuration

### Environment-Specific Config Files

Configuration is managed via JSON files in the `/config` directory, selected based on `NODE_ENV`.

| Environment | File | Port | Description |
|------------|------|------|-------------|
| Local | `localDev.json` | Varies | Local development |
| Development | `development.json` | 6000 | Staging/development server |
| Production | `production.json` | 6000 | Production server |

### Configuration Structure

```json
{
  "EmpmonitorFieldTracking": {
    "port": 6000,
    "host_url": "https://..."
  },
  "app_version": "1.0",
  "swagger_host_url": "...",
  "mongo": {
    "username": "...",
    "password": "...",
    "host": "...",
    "db_name": "Empmonitor-FieldTracking"
  },
  "token_secret": "...",
  "sendgrid": {
    "key": "...",
    "name": "EmpMonitor",
    "email": "admin@empmonitor.com"
  },
  "TWILIO_ACCOUNT_SID": "...",
  "TWILIO_AUTH_TOKEN": "...",
  "googleSnapRoadApi": "https://roads.googleapis.com/v1/snapToRoads",
  "googleSnapRoadSecretKey": "..."
}
```

### Environment Variables (`.env`)

| Variable | Values | Description |
|----------|--------|-------------|
| `NODE_ENV` | `local`, `development`, `production` | Active environment |
| `PORT` | `3000` (default) | Server port override |

---

## 17. Logging

### Winston Logger (`resources/logs/logger.log.js`)

- **Transports:** Console + Daily Rotated File
- **Log Directory:** `resources/logs/responselogs/`
- **Format:** Structured JSON with timestamps
- **Rotation:** Daily log rotation

### Morgan (HTTP Request Logger)

- Integrated with Express for HTTP request/response logging
- Outputs to both console and Winston

### Log Levels

| Level | Usage |
|-------|-------|
| `error` | Application errors, unhandled exceptions |
| `warn` | Potential issues, deprecations |
| `info` | General operational messages |
| `debug` | Detailed debugging information |

---

## 18. Dependencies

### Production Dependencies

| Package | Version | Purpose |
|---------|---------|---------|
| `express` | ^4.18.2 | Web framework |
| `mongoose` | ^7.1.0 | MongoDB ODM |
| `jsonwebtoken` | ^9.0.0 | JWT authentication |
| `bcrypt` | ^5.1.0 | Password hashing |
| `joi` | ^17.9.2 | Request validation |
| `multer` | ^1.4.5-lts.1 | File upload handling |
| `@sendgrid/mail` | ^7.7.0 | Email service |
| `twilio` | ^4.18.0 | SMS/OTP service |
| `@google-cloud/storage` | ^6.12.0 | Cloud file storage |
| `axios` | ^1.6.8 | HTTP client for external APIs |
| `moment` | ^2.29.4 | Date/time manipulation |
| `moment-timezone` | ^0.5.43 | Timezone handling |
| `cron` | ^3.1.7 | Scheduled jobs |
| `helmet` | ^6.1.5 | Security headers |
| `compression` | ^1.7.4 | Response compression |
| `cors` | ^2.8.5 | Cross-origin resource sharing |
| `morgan` | ^1.10.0 | HTTP request logger |
| `winston` | ^3.8.2 | Application logger |
| `winston-daily-rotate-file` | ^4.7.1 | Log rotation |
| `swagger-ui-express` | ^4.6.2 | Swagger UI |
| `swagger-autogen` | ^2.23.1 | Auto-generate Swagger docs |
| `config` | ^3.3.9 | Configuration management |
| `dotenv` | ^16.0.3 | Environment variable loading |
| `uuid` | ^9.0.0 | UUID generation |
| `http-errors` | ^2.0.0 | HTTP error creation |

---

## Appendix: API Endpoint Summary

### Total Endpoints: ~130+

| Module | Count | Base Path |
|--------|-------|-----------|
| Admin | ~35 | `/v1/admin` |
| User | 8 | `/v1/user` |
| Role | 4 | `/v1/roles` |
| Open/Public User | ~18 | `/v1/open-user` |
| Tracking | 1 | `/v1/track` |
| Leave | 9 | `/v1/leaves` |
| Holiday | 4 | `/v1/holiday` |
| Client | 5 | `/v1/client` |
| Category | 4 | `/v1/category` |
| Task | 10 | `/v1/task` |
| Profile | 4 | `/v1/profile` |
| Attendance | 4 | `/v1/attendance` |
| HRMS Admin | 7 | `/v1/hrmsAdmin` |
| Transport | 2 | `/v1/profile` |
| Reports | 10 | `/v1/reports` |
| Tags | 6 | `/v1/tags` |
| Auto Email Report | 4 | `/v1/auto-Generate-Report` |

---

> **Swagger Documentation:** Available at `/api-doc` when the server is running.

---

## 19. Local Development Setup Guide

### Prerequisites

| Requirement | Version | Purpose |
|-------------|---------|---------|
| **Node.js** | v16+ (recommended) | JavaScript runtime |
| **MongoDB** | v5+ | Database — must be running locally |
| **npm** | Comes with Node.js | Package manager |
| **Git** | Any recent version | Version control |

### Step 1: Clone the Repository

```bash
git clone <repository-url>
cd empmonitor-mobile-app-api
```

### Step 2: Install Dependencies

```bash
npm install
```

This installs all production and dev dependencies including `nodemon` for auto-restart during development.

### Step 3: Create the `.env` File

Create a `.env` file in the **project root** directory:

```env
NODE_ENV=localDev
PORT=3000
```

**Environment Options:**

| `NODE_ENV` Value | Config File Used | MongoDB Host | Port |
|-----------------|------------------|--------------|------|
| `local` | `config/localDev.json` | `127.0.0.1` (no auth) | 3000 |
| `development` | `config/development.json` | `140.245.27.185` (with auth) | 6000 |
| `production` | `config/production.json` | Production server (with auth) | 6000 |

> **For local development, always use `NODE_ENV=local`** — it points to localhost MongoDB with no authentication required.

### Step 4: Start MongoDB Locally

Make sure MongoDB is running on your machine at `127.0.0.1:27017` (default port).

**Windows (if installed as service):**
```bash
# Usually starts automatically. Check via:
net start MongoDB
```

**Windows (manual start):**
```bash
mongod --dbpath "C:\data\db"
```

**macOS/Linux:**
```bash
mongod
# or
sudo systemctl start mongod
```

The local config (`localDev.json`) expects:
- **Host:** `127.0.0.1`
- **Username:** _(empty — no auth)_
- **Password:** _(empty — no auth)_
- **Database Name:** `FieldTracking`

> The database and collections will be created automatically by Mongoose when data is first inserted.

### Step 5: Create Logs Directory (if not exists)

```bash
mkdir -p resources/logs/responselogs
```

On Windows (Command Prompt):
```cmd
mkdir resources\logs\responselogs
```

### Step 6: Start the Development Server

```bash
npm run dev
```

This command does two things:
1. Runs `swagger-autogen` to generate Swagger API documentation
2. Starts the server with `nodemon` (auto-restarts on file changes)

**Expected Output:**
```
Swagger auto-generation completed...
Server is running on port 3000
MongoDB connected successfully
```

### Step 7: Verify the Server is Running

| Check | URL | Expected Result |
|-------|-----|-----------------|
| **API Root** | `http://localhost:3000` | Server responds |
| **Swagger Docs** | `http://localhost:3000/api-doc` | Swagger UI loads |
| **Health Check** | Any valid endpoint | JSON response |

**Swagger UI Credentials:**
- **Username:** `admin`
- **Password:** `admin`

### Step 8: Test an API Endpoint

**Register a new admin (creates organization + admin account):**

```bash
curl -X POST http://localhost:3000/v1/admin/sign-up \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Test Admin",
    "email": "admin@test.com",
    "password": "TestPass123",
    "gender": "male",
    "phoneNumber": 9876543210,
    "city": "Bangalore",
    "state": "Karnataka",
    "country": "India",
    "timezone": "Asia/Kolkata"
  }'
```

**Login as admin:**

```bash
curl -X POST http://localhost:3000/v1/admin/sign-in \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@test.com",
    "password": "TestPass123"
  }'
```

> Copy the `token` from the login response and use it as the `x-access-token` header for all authenticated requests.

**Example authenticated request:**

```bash
curl -X GET http://localhost:3000/v1/admin/admin-fetch \
  -H "x-access-token: <your-jwt-token>"
```

### Available npm Scripts

| Script | Command | Description |
|--------|---------|-------------|
| `npm run dev` | `swagger-autogen && nodemon server.js` | Start dev server with auto-restart |
| `npm run swagger` | `node ./resources/views/swagger.config.js` | Regenerate Swagger docs only |

### External Services — Local Limitations

Some features depend on external services that may not work locally without valid credentials:

| Service | Local Behavior | How to Fix |
|---------|---------------|------------|
| **SendGrid (Emails)** | Emails won't send without a valid API key | Add your own SendGrid key in `localDev.json` |
| **Twilio (OTP/SMS)** | OTP verification won't work | Add your own Twilio credentials |
| **Google Cloud Storage** | File uploads will fail | Configure `storageconfig.json` with valid service account |
| **Google Maps APIs** | Snap-to-roads and geocoding may fail | Add valid Google API key |
| **EmpMonitor HRMS** | Employee import/sync won't work | Requires access to EmpMonitor staging |

> **Core functionality** (CRUD operations, authentication, attendance, tasks, tracking) works fully with just MongoDB running locally.

### Troubleshooting

| Issue | Solution |
|-------|----------|
| `ECONNREFUSED 127.0.0.1:27017` | MongoDB is not running — start it with `mongod` |
| `PORT 3000 already in use` | Change `PORT` in `.env` or kill the process using port 3000 |
| `Cannot find module` errors | Run `npm install` again |
| `swagger-api-view.json` not found | Run `npm run swagger` to generate it |
| `nodemon` not found | Run `npm install` — it's a dev dependency |
| `bcrypt` build errors | Install Python and C++ build tools: `npm install -g node-gyp` |

---

> **Last Updated:** 2026-03-23
