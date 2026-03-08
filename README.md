# I'm Booked — Backend

A REST API for small business owners to manage their services and appointments.

## Tech Stack

- **Framework:** Spring Boot 3 (Java 21)
- **Database:** MySQL
- **Migrations:** Flyway
- **Security:** Spring Security + JWT (access & refresh tokens)
- **Documentation:** SpringDoc OpenAPI (Swagger UI)

## Features

- JWT authentication with short-lived access tokens and long-lived refresh tokens stored in HttpOnly cookies
- Business management — create and manage multiple businesses per user
- Service management — define services with pricing and duration per business
- Appointment management — book appointments with conflict detection, and status tracking
- Business reports — stats per business including revenue, appointment counts by status, and monthly breakdowns

## Project Structure

```
src/main/java/com/imbooked/
├── auth/               # JWT auth, login, refresh, logout
├── business/           # Business entity, service, repository, DTOs
├── service/            # Service entity, service, repository, DTOs
├── appointment/        # Appointment entity, service, repository, DTOs
├── report/             # Reporting service and controller
├── user/               # User entity, registration, roles
└── shared/             # Exception handling, security utils, enums
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/auth/login` | Login and receive tokens |
| POST | `/api/auth/refresh` | Refresh access token via cookie |
| POST | `/api/auth/logout` | Invalidate session |
| GET | `/api/auth/me` | Get current authenticated user |
| POST | `/api/users` | Register a new user |
| GET | `/api/businesses` | Get all businesses for current user |
| GET | `/api/businesses/:id` | Get a business by ID |
| POST | `/api/users/businesses` | Create a new business |
| POST | `/api/services/:businessId` | Add a service to a business |
| PATCH | `/api/services/:serviceId` | Update a service |
| DELETE | `/api/services/:serviceId` | Delete a service |
| GET | `/api/appointments/:businessId` | Get paginated appointments |
| POST | `/api/appointments/:businessId` | Book an appointment |
| PATCH | `/api/appointments/:businessId/:appointmentId` | Update an appointment |
| PATCH | `/api/appointments/:appointmentId/status` | Update appointment status |
| DELETE | `/api/appointments/:appointmentId` | Delete an appointment |
| GET | `/api/reports/:businessId` | Get current month report |
| GET | `/api/reports/:businessId/previous` | Get all previous month reports |

## Setup

1. Clone the repo
2. Create a MySQL database:
   ```sql
   CREATE DATABASE im_booked;
   ```
3. Copy `env.properties.example` to `env.properties` and fill in the values:
   ```properties
   DB_USER=
   DB_PASSWORD=
   DB_URL=
   JWT_SECRET=
   JWT_ACCESS_TOKEN_EXPIRATION=
   JWT_REFRESH_TOKEN_EXPIRATION=
   FRONTEND_URL=
   ```
4. Copy `flyway.conf.example` to `flyway.conf` and fill in the values
5. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

Swagger UI is available at `http://localhost:8080/swagger-ui.html` when the app is running.

## Authentication Flow

- On login, the server issues a short-lived access token (returned in the response body) and a long-lived refresh token (set as an HttpOnly cookie)
- The access token is stored in memory on the frontend and attached to each request as a Bearer token
- When the access token expires, the frontend automatically calls `/api/auth/refresh` — the browser sends the HttpOnly cookie automatically
- On logout, the session is cleared and the cookie is removed

## Frontend

- [I'm Booked Frontend](https://github.com/enelrith/im-booked-frontend)