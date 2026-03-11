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

### Main endpoints

- **GET** `/api/properties`  
  Optional query params: `city`, `listingType` (`RENT` / `SALE`), `minPrice`, `maxPrice`

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

