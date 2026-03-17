# Employee Management System - Spring Boot + JWT

## Project Overview
A full-stack Spring Boot REST API for managing employee records with JWT-based authentication.

### Tech Stack
| Layer | Technology |
|---|---|
| Language | Java 11 |
| Framework | Spring Boot 2.7.x |
| Security | Spring Security + JWT (JJWT 0.11.5) |
| Database | MySQL 8 (H2 for tests) |
| ORM | Spring Data JPA / Hibernate |
| Build | Maven |
| Utilities | Lombok |

---

## Project Structure
```
employee-management-system/
├── pom.xml
├── PROJECT_INFO.md
├── Employee_Management_Postman.json          ← Postman collection
└── src/
    ├── main/
    │   ├── java/com/employee/
    │   │   ├── EmployeeManagementApplication.java
    │   │   ├── controller/
    │   │   │   ├── AuthController.java
    │   │   │   └── EmployeeController.java
    │   │   ├── service/
    │   │   │   ├── AuthService.java
    │   │   │   └── EmployeeService.java
    │   │   ├── repository/
    │   │   │   ├── UserRepository.java
    │   │   │   └── EmployeeRepository.java
    │   │   ├── model/
    │   │   │   ├── User.java
    │   │   │   ├── Employee.java
    │   │   │   └── Role.java
    │   │   ├── dto/
    │   │   │   ├── RegisterRequest.java
    │   │   │   ├── LoginRequest.java
    │   │   │   ├── AuthResponse.java
    │   │   │   ├── EmployeeRequest.java
    │   │   │   ├── EmployeeResponse.java
    │   │   │   └── PagedResponse.java
    │   │   ├── security/
    │   │   │   ├── SecurityConfig.java
    │   │   │   ├── JwtUtil.java
    │   │   │   ├── JwtAuthenticationFilter.java
    │   │   │   └── UserDetailsServiceImpl.java
    │   │   └── exception/
    │   │       ├── ResourceNotFoundException.java
    │   │       ├── DuplicateResourceException.java
    │   │       ├── ErrorResponse.java
    │   │       └── GlobalExceptionHandler.java
    │   └── resources/
    │       └── application.properties
    └── test/
        ├── java/com/employee/
        │   ├── service/
        │   │   ├── EmployeeServiceTest.java  (7 unit tests)
        │   │   └── AuthServiceTest.java      (5 unit tests)
        │   └── repository/
        │       └── EmployeeRepositoryTest.java (6 integration tests)
        └── resources/
            └── application.properties        (H2 config)
```

---

## Prerequisites
- **Java 11+** installed (`java -version`)
- **Maven 3.6+** installed (`mvn -version`)
- **MySQL 8** running locally
- **Postman** for API testing

---

## Database Setup

### Step 1: Create MySQL Database
```sql
CREATE DATABASE employee_db;
```
The application uses `createDatabaseIfNotExist=true` so it can also auto-create.

### Step 2: Configure Credentials
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=root   # ← change to your MySQL password
```

> Tables are auto-created by Hibernate (`ddl-auto=update`).

---

## How to Run

### Option A: Maven Command
```bash
cd D:\Antigravity_Project\Employee_System
mvn spring-boot:run
```

### Option B: Build JAR and Run
```bash
mvn clean package -DskipTests
java -jar target/employee-management-system-1.0.0.jar
```

### Option C: Run Tests Only
```bash
mvn test
```

The server starts on **http://localhost:8080**

---

## API Endpoints

### Authentication (No token required)

| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login & get JWT token |

### Employee Management (JWT token required)

| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/employees` | Create employee |
| GET | `/api/employees` | List all (paginated/sorted) |
| GET | `/api/employees/{id}` | Get by ID |
| PUT | `/api/employees/{id}` | Update employee |
| DELETE | `/api/employees/{id}` | Delete employee |

---

## Request / Response Examples

### 1. Register User
**POST** `http://localhost:8080/api/auth/register`
```json
{
  "username": "admin",
  "email": "admin@company.com",
  "password": "admin123"
}
```
**Response (201):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "username": "admin",
  "email": "admin@company.com",
  "role": "ROLE_USER",
  "message": "User registered successfully!"
}
```

---

### 2. Login
**POST** `http://localhost:8080/api/auth/login`
```json
{
  "username": "admin",
  "password": "admin123"
}
```
**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "username": "admin",
  "email": "admin@company.com",
  "role": "ROLE_USER",
  "message": "Login successful!"
}
```

> **Copy the token** from the response and use it in the `Authorization` header as:
> `Bearer eyJhbGciOiJIUzI1NiJ9...`

---

### 3. Create Employee
**POST** `http://localhost:8080/api/employees`

**Headers:** `Authorization: Bearer <your-token>`
```json
{
  "name": "John Doe",
  "email": "john.doe@company.com",
  "department": "Engineering",
  "position": "Software Engineer",
  "salary": 75000.00,
  "dateOfJoining": "2023-01-15"
}
```
**Response (201):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@company.com",
  "department": "Engineering",
  "position": "Software Engineer",
  "salary": 75000.00,
  "dateOfJoining": "2023-01-15"
}
```

---

### 4. Get All Employees (Paginated + Sorted)
**GET** `http://localhost:8080/api/employees?page=0&size=5&sortBy=name&sortDir=asc`

**Headers:** `Authorization: Bearer <token>`

**Optional query parameters:**
| Parameter | Default | Description |
|-----------|---------|-------------|
| `page` | `0` | Page number (0-indexed) |
| `size` | `10` | Records per page |
| `sortBy` | `id` | Field to sort (id, name, salary, department) |
| `sortDir` | `asc` | Sort direction: `asc` or `desc` |
| `department` | - | Filter by department |
| `position` | - | Filter by position |
| `search` | - | Search by name or email |

**Response (200):**
```json
{
  "content": [
    {
      "id": 1,
      "name": "John Doe",
      "email": "john.doe@company.com",
      "department": "Engineering",
      "position": "Software Engineer",
      "salary": 75000.00,
      "dateOfJoining": "2023-01-15"
    }
  ],
  "pageNumber": 0,
  "pageSize": 5,
  "totalElements": 1,
  "totalPages": 1,
  "last": true
}
```

---

### 5. Get Employee by ID
**GET** `http://localhost:8080/api/employees/1`

**Headers:** `Authorization: Bearer <token>`

---

### 6. Update Employee
**PUT** `http://localhost:8080/api/employees/1`

**Headers:** `Authorization: Bearer <token>`
```json
{
  "name": "John Doe",
  "email": "john.doe@company.com",
  "department": "Engineering",
  "position": "Senior Software Engineer",
  "salary": 90000.00,
  "dateOfJoining": "2023-01-15"
}
```

---

### 7. Delete Employee
**DELETE** `http://localhost:8080/api/employees/1`

**Headers:** `Authorization: Bearer <token>`

**Response:** `204 No Content`

---

## Error Responses

All errors follow a consistent format:
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id : '99'",
  "path": "/api/employees/99",
  "timestamp": "2024-01-15T10:30:00"
}
```

### Validation Error Example (400)
```json
{
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed. Please check the errors.",
  "path": "/api/employees",
  "timestamp": "2024-01-15T10:30:00",
  "validationErrors": {
    "email": "Email must be valid",
    "salary": "Salary must be greater than 0"
  }
}
```

---

## Postman Setup Guide

### Import Collection
1. Open Postman
2. Click **Import** → Select `Employee_Management_Postman.json`
3. The collection `Employee Management System` will appear

### Set Up Environment Variable
1. Click **Environments** → **New**
2. Name it `Employee System Local`
3. Add variable: `base_url` = `http://localhost:8080`
4. Add variable: `auth_token` = *(leave blank - auto-filled on login)*
5. Click **Save** and select this environment

### Testing Flow in Postman
1. **Register** → sends POST to `/api/auth/register`
2. **Login** → sends POST to `/api/auth/login` (copy the token from response)
3. Set `auth_token` variable to the copied token
4. **Create Employee** → POST with Bearer token
5. **Get All Employees** → GET with pagination params
6. **Update Employee** → PUT with ID
7. **Delete Employee** → DELETE with ID

> All employee endpoints have `Authorization: Bearer {{auth_token}}` pre-configured.

---

## JWT Details

| Property | Value |
|----------|-------|
| Algorithm | HS256 |
| Token Expiry | 24 hours (86400000 ms) |
| Header | `Authorization: Bearer <token>` |
| Secret (configurable) | Set in `application.properties` |

---

## Running Unit Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=EmployeeServiceTest
mvn test -Dtest=AuthServiceTest
mvn test -Dtest=EmployeeRepositoryTest
```

**Test Summary:**
| Test Class | Tests | Type |
|---|---|---|
| `EmployeeServiceTest` | 7 | Unit (Mockito) |
| `AuthServiceTest` | 5 | Unit (Mockito) |
| `EmployeeRepositoryTest` | 6 | Integration (H2) |
| **Total** | **18** | |

---

## GitHub Setup

```bash
cd D:\Antigravity_Project\Employee_System
git init
git add .
git commit -m "feat: complete Employee Management System with JWT auth"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/employee-management-system.git
git push -u origin main
```

---

## Common Issues & Fixes

| Issue | Fix |
|-------|-----|
| `Access Denied 403` | Token missing/expired. Re-login and update token |
| `Port 8080 in use` | Add `server.port=8081` to `application.properties` |
| MySQL connection refused | Ensure MySQL is running: `net start MySQL80` |
| `Could not autowire JwtUtil` | Ensure `@SpringBootApplication` scans `com.employee` |
