# Employee Management System API

A professional Spring Boot REST API for managing employee records, featuring secure JWT authentication, advanced filtering, and pagination.

## 🚀 Features

- **Secure Authentication**: JWT-based login and registration.
- **Employee CRUD**: Fully functional Create, Read, Update, and Delete operations.
- **Advanced Search**: Filter employees by Name, Email, Department, or Position.
- **Pagination & Sorting**: Efficiently handles large datasets with customizable sorting.
- **Unit Testing**: 100% logic coverage for Service and Repository layers using JUnit 5 and Mockito.
- **Database Support**: MySQL for production and H2 for testing.

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 2.7.18**
- **Spring Security** (JWT)
- **Spring Data JPA**
- **MySQL**
- **Maven**
- **Lombok**

## 📋 Prerequisites

- **Java JDK 17+**
- **Maven 3.6+**
- **MySQL Server**

## ⚙️ Setup & Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Saurav-4912/employee_management_api.git
   cd employee_management_api
   ```

2. **Configure Database**:
   Open `src/main/resources/application.properties` and update your MySQL credentials:
   ```properties
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Run the Application**:
   ```bash
   mvn clean spring-boot:run
   ```
   The server will start on `http://localhost:8081`.

## 🧪 Running Tests

To execute the unit test suite:
```bash
mvn test
```

## 📍 API Endpoints

### Authentication
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and get JWT token |

### Employees (Requires Authorization Header: `Bearer <token>`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| GET | `/api/employees` | Get all employees (with filters/paging) |
| GET | `/api/employees/{id}` | Get employee by ID |
| POST | `/api/employees` | Create a new employee |
| PUT | `/api/employees/{id}` | Update existing employee |
| DELETE | `/api/employees/{id}` | Delete employee |

## 📂 Documentation
- A Postman collection is included in the root folder: `Employee_Management_Postman.json`.
- [Walkthrough & Fixes](.gemini/antigravity/brain/0f158ce8-88ac-4689-9be0-a02e11e1b0e7/walkthrough.md) (Internal Documentation)
