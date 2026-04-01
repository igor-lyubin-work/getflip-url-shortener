# Redis URL Shortener

A high-performance URL shortening service built with **Java 21**, **Spring Boot 3**, and **Redis**.
It provides endpoints to shorten long URLs and instantly redirect shortened aliases back to their original destinations.

## Tech Stack
- **Languages / Frameworks**: Java 21, Spring Boot 3.2.5, Maven, Lombok
- **Persistence**: Redis
- **Infrastructure**: Docker & Docker Compose

---

## How to Run Locally

1. **Start the Redis Database:**
   ```bash
   docker-compose up -d
   ```
   *(Ensure Docker Desktop is running. This will spin up Redis on port 6379).*

2. **Run the Spring Boot Application:**
   ```bash
   mvn spring-boot:run
   ```
   *(The application will be available at `http://localhost:8080`).*

---

## API Endpoints

### 1. Create a Short URL
- **URL**: `POST /api/v1/urls`
- **Content-Type**: `application/json`
- **Body**:
  ```json
  {
    "url": "https://www.getflip.com/about"
  }
  ```
- **Response** `201 Created`:
  ```json
  {
    "shortUrl": "http://localhost:8080/api/v1/urls/abc12345",
    "longUrl": "https://www.getflip.com/about"
  }
  ```

### 2. Redirect
- **URL**: `GET /api/v1/urls?alias={alias}` (e.g., `GET /api/v1/urls?alias=abc12345`)
- **Response**: HTTP `302 Found` with `Location` header redirecting to the original URL.
- **Response**: HTTP `404 Not Found` if alias does not exist.
