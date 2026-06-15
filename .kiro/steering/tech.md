# Tech Stack

## Backend
- **Language**: Java
- **Framework**: Spring Boot (Web, Data JPA)
- **Auth**: Spring Security (leverage built-in features)
- **Database**: SQLite
- **ORM**: Spring Data JPA / Hibernate
- **Build Tool**: Gradle

## Frontend
- **Framework**: Angular (SPA)
- **Pages/Views**: Account Creation, Login, Main Dashboard, Tasks, Subtask, Account

## Version Control
- **Tools**: Git, GitHub

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

```bash
# Build
./gradlew build

# Run
./gradlew bootRun

# Test
./gradlew test
```
