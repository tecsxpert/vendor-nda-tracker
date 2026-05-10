# Java Backend — Vendor NDA Tracker

Spring Boot REST backend that bridges the React frontend and the Flask AI microservice.

---

## Architecture

```
React Frontend  →  Java Spring Boot (port 8080)  →  Flask AI Service (port 5000)
```

---

## Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/vendor/describe` | Plain-language NDA description |
| POST | `/vendor/recommend` | Actionable recommendations |
| POST | `/vendor/generate-report` | Full NDA risk report |
| POST | `/vendor/create` | Async vendor creation (fire-and-forget) |
| GET | `/actuator/health` | Health check |

All endpoints accept:
```json
{ "input": "Your NDA text here..." }
```

---

## Setup

### 1. Prerequisites
- Java 17+
- Maven 3.8+
- Flask AI service running on `http://127.0.0.1:5000`

### 2. Run the backend
```bash
cd backend
mvn spring-boot:run
```

Server starts at: `http://localhost:8080`

### 3. Override AI service URL (for Docker)
Edit `src/main/resources/application.properties`:
```properties
ai.service.base-url=http://ai-service:5000
```

---

## Example Requests

### POST /vendor/describe
```bash
curl -X POST http://localhost:8080/vendor/describe \
  -H "Content-Type: application/json" \
  -d '{"input": "This NDA restricts the vendor from sharing confidential data for 2 years."}'
```

**Response:**
```json
{
  "result": "This agreement prevents the vendor from disclosing confidential information...",
  "is_fallback": false,
  "generated_at": "2026-05-08T00:00:00Z"
}
```

### POST /vendor/recommend
```bash
curl -X POST http://localhost:8080/vendor/recommend \
  -H "Content-Type: application/json" \
  -d '{"input": "Vendor NDA includes unlimited liability clause and no exit terms."}'
```

**Response:**
```json
{
  "recommendations": [
    { "action_type": "Review", "description": "Carefully review the liability clause." },
    { "action_type": "Negotiate", "description": "Request an exit clause." }
  ],
  "is_fallback": false,
  "generated_at": "2026-05-08T00:00:00Z"
}
```

### POST /vendor/generate-report
```bash
curl -X POST http://localhost:8080/vendor/generate-report \
  -H "Content-Type: application/json" \
  -d '{"input": "Vendor NDA with 3 year confidentiality period, unlimited liability, no exit clause."}'
```

---

## Project Structure

```
backend/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/vendor/backend/
    │   │   ├── VendorNdaBackendApplication.java  ← Main entry point
    │   │   ├── config/
    │   │   │   └── AppConfig.java                ← RestTemplate + async config
    │   │   ├── controller/
    │   │   │   └── VendorController.java          ← REST endpoints
    │   │   ├── dto/
    │   │   │   ├── NdaAnalysisRequest.java        ← Input DTO
    │   │   │   ├── DescribeResponse.java          ← /describe response DTO
    │   │   │   ├── RecommendResponse.java         ← /recommend response DTO
    │   │   │   └── ReportResponse.java            ← /generate-report response DTO
    │   │   └── service/
    │   │       ├── AiServiceClient.java           ← HTTP client for Flask AI
    │   │       └── VendorService.java             ← Business logic + fallbacks
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/com/vendor/backend/
            └── VendorServiceTest.java             ← Unit tests
```

---

## Tech Stack
- Java 17
- Spring Boot 3.2.5
- Spring Web (RestTemplate)
- Spring Validation
- Spring Actuator
- JUnit 5 + Mockito
