# Railway Deployment (Recruiter Demo)

This project is pre-configured for Railway with:

- `backend/railway.json`
- `frontend/railway.json`
- `backend/Dockerfile`
- `frontend/Dockerfile`

## 1) Create Project

1. Push this repo to GitHub.
2. In Railway, click **New Project**.
3. Add a **PostgreSQL** service.
4. Add two services from GitHub repo:
   - `backend` service
   - `frontend` service

## 2) Backend Service Settings

In backend service:

1. **Source > Root Directory**: `/backend`
2. **Source > Railway Config File**: `/backend/railway.json`
3. Set env vars:
   - `DATABASE_URL=jdbc:postgresql://<PGHOST>:<PGPORT>/<PGDATABASE>`
   - `DB_USER=<PGUSER>`
   - `DB_PASSWORD=<PGPASSWORD>`
   - `APP_ADMIN_USERNAME=etladmin`
   - `APP_ADMIN_PASSWORD=<strong-password>`
   - `APP_CORS_ALLOWED_ORIGINS=https://<frontend-domain>`
   - `SPRING_PROFILES_ACTIVE=default,demo`

Notes:
- Use Railway Postgres connection values for `PGHOST/PGPORT/PGDATABASE/PGUSER/PGPASSWORD`.
- `demo` profile enables faster cron schedules for live demos.

## 3) Frontend Service Settings

In frontend service:

1. **Source > Root Directory**: `/frontend`
2. **Source > Railway Config File**: `/frontend/railway.json`
3. Set env vars:
   - `VITE_API_BASE_URL=https://<backend-domain>/api`

## 4) Domains

1. Generate domain for backend service.
2. Generate domain for frontend service.
3. Update:
   - backend `APP_CORS_ALLOWED_ORIGINS` with frontend domain
   - frontend `VITE_API_BASE_URL` with backend domain
4. Redeploy both services.

## 5) Recruiter URL

Share the frontend domain URL.

Write actions (`trigger`, create/update/delete sources) require admin credentials from:
- `APP_ADMIN_USERNAME`
- `APP_ADMIN_PASSWORD`
