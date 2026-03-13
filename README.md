# 🔥 Firewall Rule Management API

A production-ready REST API for managing firewall rules — built with **Java 17**, **Spring Boot 3**, and **Spring Security**. Features JWT authentication, role-based access control, and a clean layered architecture.

---

## What It Does

The API simulates the backend of a network security management system. It allows authenticated users to manage firewall rules — defining what network traffic should be allowed or denied based on IP addresses, ports, and protocols.

- **ADMINs** can create, update, delete, and toggle rules
- **VIEWERs** have read-only access to all rules
- All endpoints are protected by **JWT tokens**
- Passwords are hashed with **BCrypt** — never stored in plain text

---

## Features

- 🔐 **JWT Authentication** — stateless, token-based auth on every request
- 👥 **Role-Based Access Control** — ADMIN and VIEWER roles with different permissions
- ✅ **Input Validation** — request validation with meaningful error messages
- 🧪 **Unit Tested** — service layer tested with JUnit 5 and Mockito
- 🐳 **Dockerised** — runs in a container with a single command
- 🗄️ **H2 In-Memory Database** — zero setup required, pre-seeded with demo data

---

## How to Run

### Prerequisites

- Java 17
- Maven 3.9+
- Docker (optional)

---

### Option 1: Run Locally with Maven

**1. Clone the repo**
```bash
git clone https://github.com/your-username/firewall-rule-api.git
cd firewall-rule-api
```

**2. Set Java 17 as active (if you have multiple JDK versions)**
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

**3. Build the project**
```bash
mvn clean install -DskipTests
```

**4. Run the app**
```bash
mvn spring-boot:run
```

The API will start at `http://localhost:8080`

---

### Option 2: Run with Docker

**1. Build and start the container**
```bash
docker-compose up --build
```

**2. Stop the container**
```bash
docker-compose down
```

The API will be available at `http://localhost:8080` — no Java or Maven installation required.

---

## Demo Credentials

Two users are pre-seeded on startup:

| Username | Password | Role |
|----------|----------|------|
| `admin` | `admin123` | ADMIN |
| `viewer` | `viewer123` | VIEWER |

---

## API Usage

### 1. Login and get a token

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "admin",
  "role": "ADMIN"
}
```

### 2. Use the token in requests

```bash
curl -X GET http://localhost:8080/api/firewall-rules \
  -H "Authorization: Bearer <your-token-here>"
```

### 3. Create a firewall rule (ADMIN only)

```bash
curl -X POST http://localhost:8080/api/firewall-rules \
  -H "Authorization: Bearer <your-token-here>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Block FTP",
    "sourceIp": "0.0.0.0/0",
    "destinationIp": "0.0.0.0/0",
    "port": 21,
    "protocol": "TCP",
    "action": "DENY",
    "description": "Block insecure FTP protocol",
    "enabled": true
  }'
```

---

## Endpoints

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| `POST` | `/api/auth/login` | Public | Login and receive JWT |
| `POST` | `/api/auth/register/admin` | Public | Register an admin user |
| `POST` | `/api/auth/register/viewer` | Public | Register a viewer user |
| `GET` | `/api/firewall-rules` | ADMIN, VIEWER | Get all rules |
| `GET` | `/api/firewall-rules/{id}` | ADMIN, VIEWER | Get rule by ID |
| `GET` | `/api/firewall-rules/enabled` | ADMIN, VIEWER | Get enabled rules only |
| `GET` | `/api/firewall-rules/action/{action}` | ADMIN, VIEWER | Filter by ALLOW or DENY |
| `POST` | `/api/firewall-rules` | ADMIN | Create a rule |
| `PUT` | `/api/firewall-rules/{id}` | ADMIN | Update a rule |
| `DELETE` | `/api/firewall-rules/{id}` | ADMIN | Delete a rule |
| `PATCH` | `/api/firewall-rules/{id}/toggle` | ADMIN | Enable/disable a rule |

---

## Running Tests

```bash
mvn test
```

Tests cover the service layer using Mockito to mock the repository — no database required to run them.

---

## H2 Database Console

While the app is running, you can inspect the database at:

```
http://localhost:8080/h2-console
```

| Field | Value |
|-------|-------|
| JDBC URL | `jdbc:h2:mem:firewalldb` |
| Username | `sa` |
| Password | *(leave blank)* |

---

## Project Structure

```
src/
├── main/java/com/barracuda/firewallapi/
│   ├── auth/           # JWT service, filter, login/register
│   ├── config/         # Spring Security config, data seeder
│   ├── exception/      # Global exception handling
│   ├── firewall/       # Core CRUD — entity, service, controller
│   └── user/           # User entity, repository, roles
└── test/java/com/barracuda/firewallapi/
    └── firewall/       # Unit tests
```