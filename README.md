# EmpCloud Field Force System

EmpCloud field force system for GPS tracking, check-ins, attendance, task workflows, and workforce analytics.

- GitHub: [https://github.com/EmpCloud/emp-field](https://github.com/EmpCloud/emp-field)
- Monorepo structure:
  - `packages/server` - Backend API (Node.js + Express + MongoDB)
  - `packages/client` - Frontend dashboard (React + Vite)

## Open Source

This project is open source and maintained in the public repository:
[https://github.com/EmpCloud/emp-field](https://github.com/EmpCloud/emp-field)

## Project Overview

This project helps organizations manage and monitor field employees with:

- Real-time location tracking and geofence-aware activity
- Attendance and leave management
- Task assignment, stage progression, and reporting
- Client management and operational dashboards
- Exportable reports and analytics

## Core Modules

### Frontend (`packages/client`)

- Authentication and protected route handling
- Dashboard with charts, status cards, and activity widgets
- Employee directory, filters, and data tables
- Live tracking and timeline visualization
- Attendance, leave, holiday, and request screens
- Client and report management

### Backend (`packages/server`)

- Modular domain APIs under `core/*`
- JWT-based access control via middleware
- CRUD and reporting endpoints for major business modules
- MongoDB persistence via Mongoose
- Swagger generation and API docs endpoint
- Logging, security middleware, and scheduled jobs

## Tech Stack

- Frontend: React 18, Vite, React Router, React Query, Tailwind, Radix UI, MUI
- Backend: Node.js, Express, Mongoose, Joi, JWT, Helmet, Morgan/Winston
- Database: MongoDB
- Docs: Swagger (server-generated)

## Repository Structure

```text
emp-field/
  package.json                # Root scripts to run full stack
  README.md                   # This file
  packages/
    client/                   # Frontend app
    server/                   # Backend API
```

## Quick Start (Run Frontend + Backend Together)

### 1) Prerequisites

- Node.js 16+ (recommended latest LTS)
- npm
- MongoDB running for backend local development

### 2) Install dependencies

From project root:

```bash
npm install
npm run install:all
```

### 3) Configure environment files

- Frontend: create `packages/client/.env` (check `packages/client/README.md`)
- Backend: create `packages/server/.env` (check `packages/server/Readme.md`)

### 4) Start both apps

```bash
npm run dev
```

Default local URLs (typical):

- Frontend: `http://localhost:5173`
- Backend: `http://localhost:3000` (or as configured)

## Root Scripts

- `npm run dev` - Start backend and frontend together
- `npm run dev:server` - Start only backend (`packages/server`)
- `npm run dev:client` - Start only frontend (`packages/client`)
- `npm run install:all` - Install dependencies for both packages

## API and Documentation

- Backend base path typically uses `/v1/*` routes.
- Swagger docs are available when server is running (usually `/api-doc`).
- For detailed endpoint and domain documentation, see `packages/server/Readme.md`.

## Development Notes

- Keep frontend API base URL aligned with backend host/port.
- Ensure MongoDB is running before starting backend.
- The client and server are independently runnable from their own package directories.
- Root scripts are provided for convenience in local full-stack development.

## Troubleshooting

- `npm run dev` fails at root:
  - Run `npm install` at root and `npm run install:all`.
- Backend fails to start:
  - Verify MongoDB and backend `.env` values.
- Frontend cannot call APIs:
  - Verify client `.env` API URL points to backend.
- Swagger file errors:
  - Run backend scripts from `packages/server` to regenerate docs if needed.

## Contribution Workflow

1. Create a feature branch
2. Make changes with clear commits
3. Test frontend and backend locally
4. Open a pull request with summary and test steps

## Package-Level Documentation

- Client docs: `packages/client/README.md`
- Server docs: `packages/server/Readme.md`
