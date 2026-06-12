# Project Structure

> The project has not been scaffolded yet. This reflects the expected structure for a Spring Boot + HTML frontend application based on the spec. Update as the codebase takes shape.

## Expected Layout

```
Project 1/
├── src/
│   ├── main/
│   │   ├── java/com/<package>/
│   │   │   ├── controller/       # REST controllers (AccountController, TaskController, SubtaskController)
│   │   │   ├── service/          # Business logic layer
│   │   │   ├── repository/       # Spring Data JPA repositories
│   │   │   ├── model/            # JPA entities: User, Todo, Subtask
│   │   │   ├── dto/              # Request/response DTOs
│   │   │   └── config/           # Spring Security config, app config
│   │   └── resources/
│   │       ├── static/           # HTML pages, CSS, JS
│   │       │   ├── AccountCreation.html
│   │       │   ├── Login.html
│   │       │   ├── Main.html
│   │       │   ├── Tasks.html
│   │       │   ├── Subtask.html
│   │       │   └── Account.html
│   │       └── application.properties  # DB connection, server config
│   └── test/
│       └── java/com/<package>/   # Unit and integration tests
├── .kiro/
│   └── steering/                 # AI assistant steering files
├── pom.xml                       # Maven build config
└── README.md
```

## Architectural Patterns

- **Layered architecture**: Controller → Service → Repository
- Controllers handle HTTP routing and return status codes per the API spec
- Services contain business logic; never call repositories directly from controllers
- Entities map 1:1 to DB tables (`User`, `Todo`, `Subtask`)
- DTOs used for request bodies and responses (keep entities out of the API layer)
- Cascade delete: `Subtask` records are removed automatically when their parent `Todo` is deleted (use JPA `CascadeType.ALL` + `orphanRemoval = true`)

## Naming Conventions

- Java classes: `PascalCase`
- Methods and variables: `camelCase`
- REST endpoints: lowercase kebab or single-word (e.g., `/register`, `/task`, `/subtask`)
- HTML pages: `PascalCase.html` (matching the spec: `Main.html`, `Tasks.html`, etc.)
- DB columns: `snake_case`
