# Tech Stack

## Backend
- **Language**: Java
- **Framework**: Spring Boot
- **Auth**: Spring Security (leverage built-in features)
- **Database**: Relational DB (SQLite)
- **ORM**: Spring Data JPA / Hibernate (assumed; confirm when set up)

## Frontend
- **Templating**: Plain HTML pages (no SPA framework indicated)
- **Pages served**: `AccountCreation.html`, `Login.html`, `Main.html`, `Tasks.html`, `Subtask.html`, `Account.html`

## Data Model (from ERD)

**users**: `id (PK)`, `email`, `password`

**todos**: `id (PK)`, `user_id (FK → users)`, `title`, `description`, `due_date`, `is_completed`

**subtasks**: `id (PK)`, `todo_id (FK → todos)`, `title`, `description`, `due_date`, `is_completed`

> Subtasks are cascade-deleted when their parent todo is deleted.

## API Conventions

- Use standard HTTP methods: `POST`, `PATCH`, `DELETE`
- Return appropriate status codes:
  - `201` — resource created
  - `200` — success / updated
  - `204` — deleted (no content)
  - `400` — bad request / validation failure
  - `401` — unauthorized
  - `303` — redirect after action
- Failure responses must include a user-facing error message

## API Endpoints Summary

| Resource | Method | URL        | Notes                        |
|----------|--------|------------|------------------------------|
| Register | POST   | /register  | Body: email, password        |
| Login    | POST   | /login.html| Body: email, password        |
| Task     | POST   | /task      | Create task                  |
| Task     | PATCH  | /task      | Edit task (body: id + fields)|
| Task     | DELETE | /task      | Delete task (body: task id)  |
| Subtask  | POST   | /subtask   | Create subtask               |
| Subtask  | PATCH  | /subtask   | Edit subtask                 |
| Subtask  | DELETE | /subtask   | Delete subtask               |

## Common Commands

> Update this section once the build system is finalized.

```bash
# Build
./mvnw clean package

# Run
./mvnw spring-boot:run

# Test
./mvnw test
```
