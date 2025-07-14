# 🌦️ Weather App — Spring Boot REST API

A production-ready **Spring Boot application** that provides **weather information for a given Indian pincode and date**. The system fetches data from **OpenWeatherMap API** and stores it in a relational database to avoid redundant external calls. Designed with **clean architecture**, **in-memory caching**, **robust exception handling**, **Swagger docs**, and **unit tests**.

---

## ✅ Features

### 🔍 API Functionality

- Fetch **weather info** using a `pincode` and `date`
- If data exists in the database, return it directly (no API call)
- If data is not found, fetch from OpenWeatherMap API and store in DB

### 🗄️ Database

- Stores **geolocation info** (latitude, longitude, etc.) for each pincode
- Stores **weather data** for each pincode and date
- Ensures uniqueness via composite key (`pincode_id`, `date`)

### ⚡ Performance Optimization

- In-memory caching for:
  - Weather data per (pincode, date)
  - Coordinates per pincode
- Reduces API and DB hits significantly

### 🛡️ Validation & Exception Handling

- Input validation using Spring Validation annotations
- Custom error responses using `RestApiResponse` & `ErrorModel`
- Centralized error handling via `@ControllerAdvice`

### 🧪 Testing

- JUnit 5 + Mockito test coverage for:
  - Weather service logic
  - Client interactions
  - Error handling and edge cases

### 🧼 Clean Code & Structure

- DTOs vs Entities separation
- Client abstraction (`OpenWeatherClient`)
- Mappers for DTO ↔ Entity conversion
- Logger integration across services

### 📜 API Documentation

- Integrated **Swagger (OpenAPI 3)** for live testing and auto docs
- Shows input format, success, and error responses

### 🐳 Docker Support

- Multi-stage **Dockerfile** for efficient image size
- Can be run locally using Docker

---

## 🧰 Tech Stack

| Layer          | Technology                     |
|----------------|---------------------------------|
| Language       | Java 21                         |
| Framework      | Spring Boot 3.5.3               |
| Build Tool     | Gradle                          |
| Database       | PostgreSQL (using docker)                  |
| Caching        | Spring Cache (ConcurrentMap)    |
| REST Client    | Spring `RestTemplate`           |
| Docs           | Swagger (OpenAPI 3)             |
| Testing        | JUnit 5, Mockito                |
| Container      | Docker (multi-stage build)      |

---

## 📦 API Usage

### `GET /api/weather`

Query Parameters:

| Param     | Type     | Required | Description                    |
|-----------|----------|----------|--------------------------------|
| pincode   | String   | ✅        | Must be a 6-digit Indian pincode |
| date      | ISO Date | ✅        | Format: `yyyy-MM-dd` (e.g., `2025-07-14`) |

Example:

```http
GET /api/weather?pincode=123456&date=2025-07-14
