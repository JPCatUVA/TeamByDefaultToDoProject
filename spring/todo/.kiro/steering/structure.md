# Project Structure

## Repository Layout

```
TeamByDefaultToDoProject/
├── .kiro/
│   └── steering/          # AI steering rules
├── spring/
│   └── todo/              # Spring Boot application root
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

## Conventions

- All source lives under `spring/todo/src/main/java/teambydefault/todo/`
- Tests mirror the main source structure under `spring/todo/src/test/java/teambydefault/todo/`
- `application.properties` is the single config file — no YAML
- The SQLite database file (`todo.db`) is generated at runtime in `spring/todo/` and is gitignored
- Lombok is available — prefer it to reduce boilerplate on model and DTO classes
