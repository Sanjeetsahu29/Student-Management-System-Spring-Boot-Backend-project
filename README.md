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
