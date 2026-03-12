## Housing Backend (Spring Boot + MySQL)

This is a backend-only housing app where people can list their house for **rent** or **sale**.

### Tech stack
- **Backend**: Spring Boot (Web, Data JPA, Validation)
- **Database**: MySQL
- **Build**: Maven

### Configure MySQL
Create a database (or let Hibernate create it) and update credentials in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/housing_app?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```

### Run the app

From the project root:

```bash
mvn spring-boot:run
```

Server will start by default on `http://localhost:8080`.

### CORS

Global CORS is configured for all `/api/**` endpoints in:
- `com.example.Project.config.CorsConfig`

It currently allows any origin (`*`) and common HTTP methods, which is convenient for local frontend development.

### Main endpoints (properties)

- **GET** `/api/properties`  
  Optional query params:  
  - `city`  
  - `listingType` (`RENT` / `SALE`)  
  - `minPrice`, `maxPrice`  
  - `minBedrooms`  
  - `status` (`AVAILABLE`, `PENDING`, `SOLD`, `RENTED`)  
  - `sortBy` (`createdAt` (default), `price`, `bedrooms`, `city`)  
  - `sortDir` (`asc` / `desc`, default `desc`)  
  - `page` (0-based, default `0`), `size` (default `20`)

- **GET** `/api/properties/{id}`

- **POST** `/api/properties`  
  JSON body (required fields marked with `*`):
  - `title`*, `description`
  - `addressLine1`*, `addressLine2`, `city`*, `state`, `country`, `zipCode`
  - `price`* (number), `bedrooms`, `bathrooms`, `areaSqFt`, `furnished`
  - `listingType`* (`RENT` / `SALE`)
  - `ownerName`*, `ownerEmail`, `ownerPhone`

- **PUT** `/api/properties/{id}`  
  Same body as POST, replaces the resource.

- **PATCH** `/api/properties/{id}/status?status=AVAILABLE|PENDING|SOLD|RENTED`

- **DELETE** `/api/properties/{id}`

### Extra property endpoints

- **GET** `/api/properties/latest`  
  Query param: `limit` (default `10`) – returns the most recently created listings.

- **GET** `/api/properties/stats/by-city`  
  Returns a JSON map of `city -> count`.

- **GET** `/api/properties/stats/by-status`  
  Returns a JSON map of `status -> count`.

### Inquiry endpoints

These let interested buyers/renters contact owners about a specific property.

- **POST** `/api/properties/{propertyId}/inquiries`  
  Body:
  - `name` (string, required)
  - `email` (string, required, email)
  - `phone` (string, optional)
  - `message` (string, required)
  - `preferredVisitTime` (ISO date-time, optional)

- **GET** `/api/properties/{propertyId}/inquiries`  
  Get all inquiries for a given property.

- **GET** `/api/inquiries`  
  Optional: `status` (`NEW`, `VIEWED`, `RESPONDED`, `CLOSED`) – otherwise returns all.

- **PATCH** `/api/inquiries/{id}/status?status=NEW|VIEWED|RESPONDED|CLOSED`  
  Update the status of a specific inquiry.

