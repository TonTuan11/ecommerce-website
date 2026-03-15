# E-commerce Backend API

Backend REST API for an e-commerce system built with Spring Boot.

## Live API

Base URL:

https://ecommerce-api-na0d.onrender.com

## API Documentation (Swagger)

Swagger UI:

https://ecommerce-api-na0d.onrender.com/api/swagger-ui/index.html

Note: The server is deployed on Render free tier, so the first request may take around 30–60 seconds to wake up.


---

## Tech Stack

Backend
- Java
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- Hibernate

Database
- MySQL

Tools
- Maven
- Docker
- Swagger (OpenAPI)


---

## Features
- User authentication using JWT
- Refresh token mechanism
- Product management API
- Category management API
- Shopping cart system
- Order management
- Product filtering by category and brand
- Image upload for products
- API documentation with Swagger


---


## Project Structure

src/main/java
- controller (REST APIs)
- service (business logic)
- repository (database access)
- entity (database models)
- configuration (security and application configs)
- enums (Enum definitions used in the project)
- mapper (Mapping logic between Entity and DTO)
- validator (Custom validation logic)
- dto (Data Transfer Objects)

src/main/resources
- application.yml

---


# Architecture

The project follows a layered architecture:
Controller → Service → Repository → Database
- **Controller** handles HTTP requests and responses
- **Service** contains business logic
- **Repository** interacts with the database using Spring Data JPA
- **DTO** is used to transfer data between layers

---

# Example API Endpoints
GET /api/products
GET /api/products/{id}
POST /api/auth/login
POST /api/cart/add
GET /api/orders

Full API documentation available via Swagger.

---

## Getting Started

### Clone project

git clone https://github.com/TonTuan11/ecommerce-website.git

### Run application

./mvnw spring-boot:run

Application runs at:

http://localhost:8080

## Author

Ton Tuan  
GitHub: https://github.com/TonTuan11
