# EmpCloud Field Tracking - Web Dashboard

A comprehensive React + Vite web dashboard for real-time employee field tracking, geolocation monitoring, task management, and workforce analytics. Built with a modular architecture using modern React patterns, TanStack Query for server state, and Google Maps for live location visualization.

## Features

### Dashboard & Analytics
- Real-time employee insights (active, online, idle, busy statuses)
- Live employee location tracking on Google Maps
- Task completion statistics and bar charts
- Employee status overview with visual indicators

### Employee Management
- Searchable employee data table with pagination (TanStack Table)
- Column sorting, visibility toggles, and advanced filtering
- Add/edit employee profiles via modal dialogs
- Employee task assignment and workload tracking

### Live Tracking (Org-Restricted)
- Real-time employee geolocation on Google Maps
- Geofencing with configurable radius per employee
- Activity timeline with markers (Check-In, Check-Out, Task Status)
- Distance traveled and transportation mode detection
- Date range filtering with calendar picker

### Attendance & Leave
- Attendance records by date with status filters
- Leave management, requests, and approvals
- Leave type configuration and holiday calendar

### Task Management (Org-Restricted)
- Task creation, assignment, and staging workflows
- Status tracking (Pending, Started, Paused, Resumed, Finished)
- Stage visualization and employee workload analytics

### Client Management (Org-Restricted)
- Client directory with search and filtering
- Full CRUD operations for client records

### Reports & Export
- Employee performance and attendance reports
- Distance traveled and client distribution charts
- PDF export (jsPDF) and Excel/CSV export (xlsx)

### Authentication & Authorization
- JWT-based authentication stored in HTTP-only cookies
- Role-based route protection with `PrivateRoute`
- Organization-based feature flags (`RestrictedRouteByOrg`)
- Password recovery flow (forgot/reset)

## Tech Stack

| Category | Technologies |
|---|---|
| **Framework** | React 18, Vite 5 |
| **Styling** | Tailwind CSS 3, Radix UI / shadcn/ui, Material-UI, Emotion |
| **State Management** | TanStack React Query (server state), React Context (UI state) |
| **Routing** | React Router DOM v6 |
| **Data Tables** | TanStack React Table |
| **Forms** | Formik + Yup / validate.js |
| **HTTP Client** | Axios with JWT auth headers |
| **Maps** | @react-google-maps/api |
| **Charts** | ApexCharts, AMCharts 5 |
| **Date Utilities** | moment / moment-timezone, date-fns |
| **Export** | jsPDF + jspdf-autotable, xlsx, html2canvas |
| **Notifications** | Sonner (toast) |
| **Code Quality** | ESLint, Prettier, lint-staged, simple-git-hooks |

## Prerequisites

- Node.js (v16 or higher)
- npm or yarn
- Google Maps API Key (for location features)

## Setup Instructions

### 1. Clone the repository

```bash
git clone <repository-url>
cd emp-field/packages/client
```

### 2. Install dependencies

```bash
npm install
```

### 3. Configure Environment Variables

```bash
cp .example.env .env
```

Edit `.env` with your values:

| Variable | Description | Required |
|---|---|---|
| `VITE_USER_API` | Backend API endpoint (e.g., `http://localhost:3000/api`) | Yes |
| `VITE_SOME_KEY` | Google Maps API key ([Cloud Console](https://console.cloud.google.com/)) | Yes |
| `VITE_DICE_BEAR` | Avatar API endpoint (default: DiceBear Avataaars) | No |
| `VITE_LIVE_TRACK_DISABLE_ORG_ID` | Org IDs to disable live tracking (comma-separated) | No |
| `VITE_HIDE_GOOGLE_MAPS_FEATURE_ORG` | Org IDs to hide maps feature (comma-separated) | No |

### 4. Start Development Server

```bash
npm run dev
```

The application will be available at `http://localhost:5173`

### 5. Build for Production

```bash
npm run build
```

### 6. Preview Production Build

```bash
npm run preview
```

## Architecture

### Project Structure

```
src/
├── @/                          # shadcn/ui components & utilities
│   ├── components/ui/          # 26+ Radix + Tailwind primitives (button, dialog, input, etc.)
│   └── lib/utils.js            # clsx + tailwind-merge helper
├── assets/                     # Static assets
│   ├── images/                 # App icons, map markers, module-specific images
│   └── font/                   # Quicksand font files
├── auth/                       # Authentication layer
│   └── PrivateRoute.jsx        # Token-based route guard
├── components/                 # Feature modules
│   ├── AttendanceModule/       # Leave, attendance, holidays, requests
│   ├── ClientModule/           # Client search, table, CRUD
│   ├── DashboardModule/        # Dashboard insights, live location
│   ├── EmployeeModule/         # Employee data table, filtering
│   ├── FilterModule/           # Live tracking filters (date, employee search)
│   ├── GeoLocationModule/      # Google Maps geofencing & tracking visualization
│   ├── GeoLocationSetting/     # Geofence configuration & API
│   ├── ReportModule/           # Reports, analytics, charts
│   ├── StageTaskModule/        # Task staging workflow
│   ├── TaskModule/             # Task management
│   ├── TimelineModule/         # Activity timeline visualization
│   ├── ChartContext/           # Shared chart data context
│   ├── Helpers/                # Utility functions (PDF export)
│   ├── UIElements/             # Custom UI components & modals
│   ├── Sidebar.jsx             # Navigation sidebar
│   ├── TopHeader.jsx           # Top bar with profile
│   └── Footer.jsx              # Footer
├── context/                    # Global state providers
│   ├── context.jsx             # UIController (sidebar toggle via useReducer)
│   └── Filters/
│       ├── FilterContext.jsx   # Report filters & filter popup contexts
│       └── util.token.jsx      # JWT decoding utility
├── hooks/                      # Custom React hooks
│   ├── UsePasswordVisibility.jsx
│   ├── UsePathString.jsx
│   └── UserTitles.jsx          # Page title management
├── layout/
│   └── Layout.jsx              # Main layout (Sidebar + TopHeader + Outlet + Footer)
├── page/                       # Route-level page components
│   ├── authentication/         # Login, Forgot Password, Reset Password
│   │   └── Api/post/           # Auth API calls
│   └── user/                   # Protected pages
│       ├── Dashboard/
│       ├── Employee/
│       ├── Attendance/
│       ├── LiveTracking/
│       ├── Clients/
│       ├── Reports/
│       ├── Task/ & Tasks/
│       ├── StageTasks/
│       ├── MobileSetting/      # Geofence settings
│       ├── Profile/
│       └── Status/
├── routes/
│   ├── routes.jsx              # Route definitions & lazy loading
│   └── PrivateRoute.jsx        # Auth guard + org-based restrictions
├── schema/                     # Form validation schemas (Yup / validate.js)
├── styles/
│   └── global.css              # Global styles + Tailwind directives
├── App.jsx                     # Root component with providers
├── main.jsx                    # ReactDOM entry point
└── index.css                   # Tailwind base + CSS custom properties
```

### Application Flow

```
main.jsx
  └── UIControllerProvider (sidebar state)
        └── App.jsx
              ├── QueryClientProvider (React Query - server state cache)
              ├── EmployeeReportFiltersProvider
              ├── FilterPopupProvider
              ├── ChartProvider
              └── RouterProvider
                    ├── /admin/login, /forgot-password, /reset-password  (public)
                    └── /admin/* (PrivateRoute → Layout)
                          ├── /dashboard
                          ├── /employee, /employee/attendance
                          ├── /live-tracking        (org-restricted)
                          ├── /clients              (org-restricted)
                          ├── /task, /task/stagetasks (org-restricted)
                          ├── /report, /employee-report (org-restricted)
                          ├── /mobile-setting
                          ├── /stats
                          └── /profile
```

### API Layer Pattern

Each feature module contains its own API layer under an `Api/` folder organized by HTTP method:

```javascript
// components/{Module}/Api/get/index.jsx
import axios from 'axios';
import Cookies from 'js-cookie';

const HOST = import.meta.env.VITE_USER_API;

export const fetchData = async (params) => {
  return await axios.get(HOST + '/endpoint', {
    headers: { 'x-access-token': Cookies.get('token') }
  });
};
```

API calls are consumed via **TanStack React Query** hooks (`useQuery`, `useInfiniteQuery`) for automatic caching, background refetching, and pagination.

### Authentication Flow

1. User submits credentials on `/admin/login`
2. POST to `{VITE_USER_API}/admin/sign-in` returns JWT
3. Token stored in browser cookie via `js-cookie`
4. `PrivateRoute` checks cookie on each navigation; redirects to login if absent
5. `RestrictedRouteByOrg` decodes JWT to extract `orgId` and blocks disabled features
6. Logout removes cookie and redirects to login

### Styling Architecture

- **Primary:** Tailwind CSS utility classes with CSS custom properties (HSL color system)
- **Component library:** shadcn/ui pattern (Radix UI primitives + Tailwind + CVA)
- **Supplementary:** Material-UI for select components, Emotion for dynamic styles
- **Theming:** Dark mode support via class strategy, extended color palette

## Available Scripts

| Script | Description |
|---|---|
| `npm run dev` | Start Vite development server with HMR |
| `npm run build` | Production build |
| `npm run preview` | Preview production build locally |
| `npm run lint` | Run ESLint checks |

## Path Aliases

Configured in `jsconfig.json`:

| Alias | Path |
|---|---|
| `*` | `src/*` |
| `@assets/*` | `src/assets/*` |
| `@/*` | `src/@/*` (shadcn/ui) |

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

Pre-commit hooks automatically run Prettier formatting and ESLint checks via `simple-git-hooks` and `lint-staged`.

## Security Note

**Never commit `.env` files to version control.** Use `.example.env` as a template and ensure `.env` is in your `.gitignore`.

## License

This project is open source and available under the [MIT License](LICENSE).
