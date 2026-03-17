# Employee Management System - Spring Boot + JWT

## Project Overview
A professional Spring Boot REST API for managing employee records with JWT-based authentication. The project has been simplified to use direct Entity interaction for better readability.

### Tech Stack
| Layer | Technology |
|---|---|
| Language | Java 17 |
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
├── README.md
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
    │   │   │   └── Employee.java
    │   │   └── security/
    │   │       ├── SecurityConfig.java
    │   │       ├── JwtUtil.java
    │   │       ├── JwtAuthenticationFilter.java
    │   │       └── UserDetailsServiceImpl.java
    │   └── resources/
    │       └── application.properties
    └── test/
        ├── java/com/employee/
        │   ├── service/
        │   │   └── EmployeeServiceTest.java
        │   └── repository/
        │       ├── EmployeeRepositoryTest.java
        │       └── UserRepositoryTest.java
```

---

## Prerequisites
- **Java 17+** installed (`java -version`)
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
spring.datasource.password=your_password   # ← change to your MySQL password
```

> Tables are auto-created by Hibernate (`ddl-auto=create`).

---

## How to Run

### Option A: Maven Command
```bash
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

The server starts on **http://localhost:8081**

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
**POST** `http://localhost:8081/api/auth/register`
```json
{
  "username": "saurav",
  "email": "saurav@example.com",
  "password": "password123"
}
```

---

### 2. Login
**POST** `http://localhost:8081/api/auth/login`
```json
{
  "username": "saurav",
  "password": "password123"
}
```
**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

> **Copy the token** from the response and use it in the `Authorization` header as:
> `Bearer eyJhbGciOiJIUzI1NiJ9...`

---

### 3. Create Employee
**POST** `http://localhost:8081/api/employees`

**Headers:** `Authorization: Bearer <your-token>`
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "department": "IT",
  "position": "Java Developer",
  "salary": 55000.00,
  "dateOfJoining": "2024-03-17"
}
```

---

### 4. Get All Employees (Paginated + Sorted)
**GET** `http://localhost:8081/api/employees?page=0&size=10&sortBy=name&sortDir=asc`

**Headers:** `Authorization: Bearer <token>`

---

## Postman Setup Guide

### Import Collection
1. Open Postman
2. Click **Import** → Select `Employee_Management_Postman.json`
3. The collection `Employee Management System` will appear

### Set Up Environment Variable
1. Click **Environments** → **New**
2. Name it `Employee System Local`
3. Add variable: `base_url` = `http://localhost:8081`
4. Add variable: `auth_token` = *(leave blank - auto-filled on login)*
5. Click **Save** and select this environment

---

## JWT Details

| Property | Value |
|----------|-------|
| Algorithm | HS256 |
| Token Expiry | 24 hours (86400000 ms) |
| Header | `Authorization: Bearer <token>` |
| Secret | Configurable in `application.properties` |

---

## Running Unit Tests

```bash
mvn test
```

**Test Summary:**
- `EmployeeServiceTest`: Logic validation with Mockito.
- `EmployeeRepositoryTest`: H2 Integration tests for data access.
- `UserRepositoryTest`: Authentication data layer tests.

---

## Common Issues & Fixes

| Issue | Fix |
|-------|-----|
| `Access Denied 403` | Token missing/expired. Re-login and update token |
| `Port 8081 in use` | Check if another instance is running and kill it |
| MySQL connection refused | Verify `application.properties` credentials and MySQL status |
 remote add origin https://github.com/YOUR_USERNAME/employee-management-system.git
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
