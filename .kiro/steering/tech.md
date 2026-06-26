# Tech Stack

## Backend

### Language & Runtime
- Java 21
- Spring Boot 4.1.0

### Build System
- Gradle with Kotlin DSL (`build.gradle.kts`)
- Gradle Wrapper included — always use `./gradlew` (Linux/Mac) or `gradlew.bat` (Windows)

### Key Dependencies
- **Spring Data JPA** — ORM / repository layer
- **Spring Web MVC** — REST controllers
- **SQLite** (`org.xerial:sqlite-jdbc:3.53.2.0`) — production database
- **Hibernate Community Dialects** (`hibernate-community-dialects:8.0.0.Alpha1`) — SQLite dialect support
- **Lombok** — boilerplate reduction (`@Data`, `@Builder`, `@Getter`, etc.)
- **JJWT** (`jjwt-api:0.13.0`) — JWT creation and validation
- **H2** — in-memory database used for tests only

### Testing
- JUnit 5 (via JUnit Platform)
- Spring Boot Test slices (`@SpringBootTest`, MVC test, JPA test)
- H2 replaces SQLite during tests

### Common Commands

Run from `spring/todo/`:

```bash
# Build the project
gradlew.bat build

# Run the application
gradlew.bat bootRun

# Run tests only
gradlew.bat test

# Clean build outputs
gradlew.bat clean
```

### Database
- Production: SQLite file at `spring/todo/todo.db` (auto-created on startup)
- Schema strategy: `create-drop` — tables are dropped and recreated each run
- SQL logging is enabled in development (`show-sql=true`, `format_sql=true`)

---

## Frontend (planned — to be built after backend is complete)

### Language & Runtime
- TypeScript
- Node.js

### Framework
- Angular (latest stable)

### Build & Package Management
- npm (package manager)
- Angular CLI (`ng`) for scaffolding, building, and serving

### Common Commands

Run from `angular/`:

```bash
# Install dependencies
npm install

# Start dev server (runs on http://localhost:4200 by default)
ng serve

# Build for production
ng build --configuration production

# Run unit tests
ng test --watch=false
```

### Key Conventions
- Use Angular CLI to generate components, services, guards, and interceptors
- Communicate with the Spring backend via Angular `HttpClient` services
- Store the JWT token in `localStorage` and attach it via an HTTP interceptor
- Use TypeScript interfaces (not classes) for API response/request models

