# Vehicle Monitoring System

A web application for monitoring ground-type vehicles, tracking their components, usage, and service history. This project is built with a **Spring Boot backend**, **PostgreSQL database**, and a **React frontend**. It provides detailed vehicle monitoring, alerts for upcoming service requirements, and historical insights into vehicle performance.

---

## Key Features

- **JWT Authentication & Authorization**: Secure login and access control for users.
- **Vehicle Management (CRUD)**: Add, update, delete, and view vehicles.
- **Component Tracking**: Monitor components’ conditions based on kilometers driven and fuel burnt since the last service.
- **Weekly/Monthly Data Input**: Insert kilometers driven or fuel usage; supports late entries if deadlines are missed.
- **Service Indicators**: Automatic alerts for upcoming required services and overdue components.
- **Service History Management**: Insert completed services, showing serviced components, previous and current conditions.
- **Complete Service Records**: View all services made across all vehicles.
- **Pagination & Search Filters**: Easy navigation and filtering of vehicles, components, and services.
- **Responsive Design**: Works on PC, tablet, and mobile screens.

---

## Technology Stack

- **Backend**: Spring Boot (Java 17)
- **Frontend**: React.js, Bootstrap
- **Database**: PostgreSQL
- **Authentication**: JWT (JSON Web Tokens)
- **API Documentation**: OpenAPI / Swagger
- **Hosting & Deployment**:
  - Amazon RDS (PostgreSQL)
  - Amazon EC2 (Spring Boot)
  - Nginx (Reverse Proxy)
  - DuckDNS + Let’s Encrypt (HTTPS)
  - Amazon S3 (React frontend)
  - Amazon CloudFront (React distribution & CDN)

---

## Backend Setup (Spring Boot)

1. Clone the repository:

```bash
git clone <repo_url>
cd monitoring-backend
```

2. Configure application.properties or application.yml with your PostgreSQL connection:
```bash
spring.datasource.url=jdbc:postgresql://<RDS_HOST>:5432/<DB_NAME>
spring.datasource.username=<USERNAME>
spring.datasource.password=<PASSWORD>
spring.jpa.hibernate.ddl-auto=update
server.port=8080
server.address=0.0.0.0
```

3. Build and run the backend:

```bash
mvn clean package
java -jar target/monitoring-backend-0.0.1-SNAPSHOT.jar
```

---

## Frontend Setup (React)

1. Navigate to the frontend folder:

```bash
cd monitoring-frontend
npm install
npm run build
```
---

## Deployment Overview

### Backend (Spring Boot)
- Hosted on **Amazon EC2**.
- **Nginx** reverse proxy handles HTTPS and routes traffic to `localhost:8080`.
- Secured using **Let’s Encrypt SSL certificate** via DuckDNS subdomain.

### Frontend (React)
- Build artifacts uploaded to **Amazon S3**.
- Served securely through **Amazon CloudFront** CDN.
- Supports HTTPS and caching with invalidation on updates.

### Database
- **Amazon RDS PostgreSQL** used for production database storage.
- Separate dev/local DB can use PostgreSQL on localhost.

---

## API Endpoints

### User Authentication

| Method | Endpoint            | Description                   |
|--------|--------------------|-------------------------------|
| POST   | /api/user/register  | Register a new user           |
| POST   | /api/user/login     | Login and receive JWT token   |

### Vehicle Management

| Method | Endpoint                | Description               |
|--------|------------------------|---------------------------|
| GET    | /api/vehicle/page       | List vehicles (Pageable)  |
| GET    | /api/vehicle/{id}       | Get vehicle               |
| POST   | /api/vehicle            | Create vehicle            |
| POST   | /api/vehicle/edit{id}   | Update vehicle            |
| GET    | /api/vehicle/delete/{id}| Delete vehicle            |

### Components & Services

| Method | Endpoint                          | Description                                       |
|--------|----------------------------------|---------------------------------------------------|
| GET    | /api/vehicle/{id}                 | View vehicle components and conditions            |
| POST   | /api/vehicle/insertInterval/{id}  | Insert kilometers driven / fuel usage             |
| POST   | /api/service/{id}                 | Record completed service                          |
| GET    | /api/service                      | View all services across all vehicles (Pageable)  |

---

## Features in Action

- Alerts for upcoming service: triggered automatically based on usage thresholds.
- Track component degradation and required service based on **kilometers driven and fuel burnt**.
- Historical service tracking, showing **before/after component conditions**.
- Pagination & search filters allow managing large fleets easily.
- Fully responsive UI with Bootstrap.

## Project Structure

```bash
monitoring-backend/      # Spring Boot backend
  ├─ src/main/java
  ├─ src/main/resources
  └─ pom.xml
```

```bash
monitoring-frontend/     # React frontend
  ├─ src/
  └─ package.json
```

## Environment Variables / Secrets

For production, make sure to configure:

- PostgreSQL credentials
- JWT secret
- AWS credentials (S3, CloudFront)
- EC2 SSH key for backend deployment

---

## Notes

- Backend and frontend communicate over **HTTPS**; make sure CORS is configured properly in Spring Security.
- JWT tokens are required for accessing protected endpoints.
- Ensure your EC2 security group allows **HTTP/HTTPS** traffic for frontend access and internal traffic for Nginx proxy.

## Author

Nikola Kamchev – Software Engineer
