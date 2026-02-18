# Railway Deployment (Recruiter Demo)

This project is pre-configured for Railway with:

- `backend/railway.json`
- `frontend/railway.json`
- `backend/Dockerfile`
- `frontend/Dockerfile`

## 1) Create Project

1. Push this repo to GitHub.
2. In Railway, create a new project.
3. Add a PostgreSQL service.
4. Add two services from this repo:
   - backend service
   - frontend service

## 2) Backend Service Settings

In backend service:

1. Source -> Root Directory: `/backend`
2. Source -> Railway Config File: `/backend/railway.json`
3. Set variables:

```env
DATABASE_URL=jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}
DB_USER=${{Postgres.PGUSER}}
DB_PASSWORD=${{Postgres.PGPASSWORD}}
APP_ADMIN_USERNAME=etladmin
APP_ADMIN_PASSWORD=<strong-password>
APP_CORS_ALLOWED_ORIGINS=https://<frontend-domain>
SPRING_PROFILES_ACTIVE=default,demo
```

Important:
- `DATABASE_URL` must start with `jdbc:postgresql://`.
- Keep only one `APP_CORS_ALLOWED_ORIGINS` variable (no typos/duplicates).

## 3) Frontend Service Settings

In frontend service:

1. Source -> Root Directory: `/frontend`
2. Source -> Railway Config File: `/frontend/railway.json`
3. Set variables:

```env
VITE_API_BASE_URL=https://<backend-domain>/api
```

## 4) Domains and Redeploy

1. Generate backend domain.
2. Generate frontend domain.
3. Update backend `APP_CORS_ALLOWED_ORIGINS` with frontend domain.
4. Update frontend `VITE_API_BASE_URL` with backend domain.
5. Redeploy backend, then frontend.

## 5) Verification

- Backend health: `https://<backend-domain>/actuator/health` -> `{"status":"UP"}`
- Frontend URL loads and shows dashboard.
- Triggering jobs prompts for admin credentials and starts a run.

## 6) Recruiter Link

Share the frontend domain URL.

Write actions require admin credentials from:
- `APP_ADMIN_USERNAME`
- `APP_ADMIN_PASSWORD`
