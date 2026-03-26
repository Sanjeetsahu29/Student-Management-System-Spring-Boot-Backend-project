# Student Management System
A production-ready RESTful backend service built with Spring Boot, designed around a clean layered MVC architecture. This project demonstrates industry-standard backend engineering practices including separation of concerns, DTO-based API contracts, centralized exception handling, and object mapping via ModelMapper — all patterns you'll encounter in real MNC codebases.
<hr>

## Project Overview

The Student Management Service exposes a set of RESTful HTTP endpoints for managing student records — create, read, update, and delete (CRUD). While the domain is intentionally simple, the engineering structure mirrors what you'd find in enterprise-grade Spring Boot applications:
- The Controller layer handles HTTP concerns only (routing, status codes, request parsing).
- The Service layer owns all business logic and orchestrates operations.
- The Repository layer is the single point of contact with the database.
- DTOs (Data Transfer Objects) act as a firewall between the API surface and internal domain models.
- A global exception handler ensures all errors return consistent, structured JSON responses.

  
## Architecture
```
Client (Postman / Frontend / Another Service)
        │
        ▼
┌──────────────────┐
│  Controller Layer │  ← Receives HTTP requests, delegates to Service
│  (@RestController)│    Returns HTTP responses with appropriate status codes
└────────┬─────────┘
         │ uses DTOs (no Entity objects cross this boundary)
         ▼
┌──────────────────┐
│  Service Layer    │  ← Owns business logic, validation rules,
│  (@Service)       │    orchestrates Repository calls, maps Entity ↔ DTO
└────────┬─────────┘
         │ uses Entity objects internally
         ▼
┌──────────────────┐
│ Repository Layer  │  ← Talks to the database via Spring Data JPA
│ (JpaRepository)   │    No SQL written manually for standard operations
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│    Database       │  ← H2 (in-memory for dev) / MySQL (production)
└──────────────────┘
```
**The key architectural rule**<br>
Entity objects never leave the Service layer. The Controller always receives and returns DTOs. This protects your database schema from being exposed in API responses and gives you freedom to evolve both independently.

## Project Structure
```
com.example.studentmanagement
│
├── controller
│   └── StudentController.java
│
├── dto
│   ├── StudentRequestDTO.java
│   ├── StudentResponseDTO.java
│   └── ApiError.java
│
├── entity
│   └── Student.java
│
├── exception
│   ├── StudentNotFoundException.java
│   ├── DuplicateStudentException.java
│   └── GlobalExceptionHandler.java
│
├── repository
│   └── StudentRepository.java
│
├── service
│   └── StudentService.java
│
├── service/impl
│   └── StudentServiceImpl.java
│
└── config
    └── ModelMapperConfig.java
```
## Entity Layer — Student.java
An Entity is a Java class that mirrors a database table. JPA reads the annotations and maps the class fields to table columns. The Entity should only hold data — no business logic, no API-specific fields.

**Why @Entity and @Table?**<br>
@Entity tells JPA "this class is a DB table". @Table(name="students") names the table explicitly. Without it, JPA uses the class name — fine, but explicit naming is a best practice.
```
package com.example.studentmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String department;

    private Boolean active;
}
```
**Why NOT use int for id?**<br>
Use Long (not int) for IDs. A Java int has a max of ~2 billion. A Long holds ~9.2 quintillion. Also, Long (object type) can be null, which helps distinguish "no ID yet" vs "ID is 0".

## DTO Layer — Request & Response
**Why TWO DTOs?**<br>
Request DTO = shape of data the client sends (POST/PUT). It includes validation rules.
Response DTO = shape of data you return. It may include computed fields or exclude sensitive ones.
They are intentionally different because input requirements ≠ output requirements.

StudentRequestDTO.java<br>
This DTO validates incoming data before it even touches our service layer. If validation fails, Spring throws a MethodArgumentNotValidException — no business code runs.

