# Project Structure

## Repository Layout

The repository is split into two top-level directories — one for the backend, one for the frontend. They are independent projects with their own dependency and build systems.

```
TeamByDefaultToDoProject/
├── .kiro/
│   └── steering/          # AI steering rules
├── spring/
│   └── todo/              # Spring Boot backend (Java / Gradle)
│       ├── build.gradle.kts
│       ├── settings.gradle.kts
│       ├── gradlew / gradlew.bat
│       ├── gradle/wrapper/
│       └── src/
│           ├── main/
│           │   ├── java/teambydefault/todo/   # Application source code
│           │   └── resources/
│           │       └── application.properties
│           └── test/
│               └── java/teambydefault/todo/   # Test source code
├── angular/
│   └── todo/              # Angular frontend (TypeScript / Node.js)
│       ├── package.json
│       ├── angular.json
│       ├── tsconfig.json
│       └── src/
│           ├── app/       # Components, services, modules, routes
│           ├── assets/
│           └── environments/
└── README.md
```

## Java Package Structure

Base package: `teambydefault.todo`

As the application grows, code should be organized into sub-packages by layer:

```
teambydefault.todo
├── controller/     # @RestController classes — HTTP endpoints
├── service/        # @Service classes — business logic
├── repository/     # @Repository interfaces — Spring Data JPA
├── model/          # @Entity classes — JPA-mapped domain objects
├── dto/            # Request/response transfer objects
├── security/       # JWT filter, config, user details service
└── config/         # Spring @Configuration classes
```

## Angular Frontend Structure

Once created, the Angular app lives under `angular/todo/src/app/` and should follow standard Angular conventions:

```
app/
├── components/    # Reusable UI components
├── pages/         # Route-level components (one per page/view)
├── services/      # Angular services — HTTP calls to the Spring API
├── models/        # TypeScript interfaces mirroring backend DTOs
├── guards/        # Route guards (e.g. auth guard)
└── interceptors/  # HTTP interceptors (e.g. attach JWT token)
```

## Conventions

- All source lives under `spring/todo/src/main/java/teambydefault/todo/`
- Tests mirror the main source structure under `spring/todo/src/test/java/teambydefault/todo/`
- `application.properties` is the single config file — no YAML
- The SQLite database file (`todo.db`) is generated at runtime in `spring/todo/` and is gitignored
- Lombok is available — prefer it to reduce boilerplate on model and DTO classes
