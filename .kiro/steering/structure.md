# Project Structure

> The project has not been scaffolded yet. This reflects the expected structure for a Spring Boot (Gradle) backend + Angular SPA frontend application. Update as the codebase takes shape.

## Expected Layout

```
Project 1/
├── backend/                          # Spring Boot application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/<package>/
│   │   │   │   ├── controller/       # REST controllers (AccountController, TaskController, SubtaskController)
│   │   │   │   ├── service/          # Business logic layer
│   │   │   │   ├── repository/       # Spring Data JPA repositories
│   │   │   │   ├── model/            # JPA entities: User, Todo, Subtask
│   │   │   │   ├── dto/              # Request/response DTOs
│   │   │   │   └── config/           # Spring Security config, app config
│   │   │   └── resources/
│   │   │       └── application.properties  # DB connection, server config
│   │   └── test/
│   │       └── java/com/<package>/   # Unit and integration tests
│   └── build.gradle                  # Gradle build config
│
├── frontend/                         # Angular SPA
│   ├── src/
│   │   ├── app/
│   │   │   ├── pages/                # Page-level components
│   │   │   │   ├── account-creation/ # Registration page
│   │   │   │   ├── login/            # Login page
│   │   │   │   ├── main/             # Dashboard — lists all tasks
│   │   │   │   ├── tasks/            # Task detail + subtasks view
│   │   │   │   ├── subtask/          # Subtask detail/edit view
│   │   │   │   └── account/          # User account info page
│   │   │   ├── services/             # HTTP client services (auth, task, subtask)
│   │   │   ├── models/               # TypeScript interfaces/models
│   │   │   └── guards/               # Route guards (auth protection)
│   │   └── environments/             # environment.ts, environment.prod.ts
│   ├── angular.json
│   └── package.json
│
├── .kiro/
│   └── steering/                     # AI assistant steering files
├── settings.gradle
└── README.md
```

## Architectural Patterns

- **Backend — Layered architecture**: Controller → Service → Repository
- Controllers handle HTTP routing and return status codes per the API spec
- Services contain business logic; never call repositories directly from controllers
- Entities map 1:1 to DB tables (`User`, `Todo`, `Subtask`)
- DTOs used for request bodies and responses (keep entities out of the API layer)
- Cascade delete: `Subtask` records are removed automatically when their parent `Todo` is deleted (use JPA `CascadeType.ALL` + `orphanRemoval = true`)
- **Frontend — Angular SPA**: communicates with the backend via REST API calls using Angular's `HttpClient`
- Angular Router handles client-side navigation between pages
- Route guards protect authenticated routes

## Naming Conventions

- Java classes: `PascalCase`
- Methods and variables: `camelCase`
- REST endpoints: lowercase kebab or single-word (e.g., `/register`, `/task`, `/subtask`)
- Angular components/files: `kebab-case` (e.g., `account-creation.component.ts`)
- TypeScript interfaces: `PascalCase` (e.g., `Todo`, `Subtask`, `User`)
- DB columns: `snake_case`
