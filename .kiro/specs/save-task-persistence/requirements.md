# Requirements Document

## Introduction

The home page of the Todo application presents an "Add Task" form with a "Save Task" button. Pressing that button currently has no effect — the task is not persisted to the database. This feature makes the "Save Task" button functional by wiring the Angular frontend form submission through the existing `TaskService` to the Spring Boot `POST /task` endpoint, persisting the new task in the SQLite database and associating it with the authenticated user whose identity is carried in the JWT stored in `localStorage`.

## Glossary

- **Task_Form**: The Angular reactive form rendered on the home page (`home-view`) that collects a task's `title`, `description`, and `dueDate` fields.
- **Save_Task_Button**: The submit button labeled "Save Task" inside the Task_Form.
- **TaskService**: The Angular injectable service (`task-service.ts`) that wraps all HTTP calls to the Spring Boot `/task` endpoint.
- **API**: The Spring Boot REST backend running at `http://localhost:8080`.
- **Todo_Endpoint**: The `POST /task` endpoint on the API that creates and persists a new task record.
- **Auth_Interceptor**: The Angular HTTP interceptor (`auth-interceptor.ts`) that attaches the `Authorization: Bearer <token>` header to every outgoing HTTP request.
- **JWT**: The JSON Web Token stored in `localStorage` under the key `token`, issued by the API upon login and containing the authenticated user's `userId` as the token subject.
- **AuthService**: The Angular injectable service (`auth-service.ts`) that reads the JWT and `userId` from `localStorage`.
- **Authenticated_User**: The user whose `userId` is stored in `localStorage` under the key `userId` and whose JWT is currently valid.
- **Task**: A persistent record in the `ToDos` database table consisting of a server-generated UUID, a title, an optional description, an optional due date, a completion status, and a foreign key reference to the owning `User`.

---

## Requirements

### Requirement 1: Task Form Submission Calls the API

**User Story:** As a logged-in user, I want clicking "Save Task" to send my new task to the backend, so that the task is stored in the database.

#### Acceptance Criteria

1. WHEN the Authenticated_User submits the Task_Form with a valid title and due date, THE TaskService SHALL send a `POST` request to the Todo_Endpoint with the task's `title`, `description`, `dueDate`, and the `userId` of the Authenticated_User in the request body.
2. WHEN the TaskService sends the `POST` request, THE Auth_Interceptor SHALL attach the `Authorization: Bearer <token>` header using the JWT retrieved from `localStorage`.
3. WHEN the Todo_Endpoint receives the `POST` request, THE API SHALL validate that the `user.id` field in the request body references an existing user record in the database.
4. WHEN validation passes, THE API SHALL persist the new Task record in the database and return the created Task with HTTP status `201 Created`.

---

### Requirement 2: User Ownership Association

**User Story:** As a logged-in user, I want my saved tasks to be linked to my account, so that only my tasks are visible to me.

#### Acceptance Criteria

1. WHEN a Task is created via the Todo_Endpoint, THE API SHALL associate the Task with the User identified by the `user.id` field in the request body.
2. WHEN the Task_Form is submitted, THE TaskService SHALL read the `userId` from `localStorage` via the AuthService and include it as the `user.id` field in the request body.
3. WHEN the Authenticated_User retrieves their task list via `GET /task?userId={userId}`, THE API SHALL return only the Tasks associated with that user's account.

---

### Requirement 3: Form Validation Before Submission

**User Story:** As a logged-in user, I want the form to prevent submission of incomplete data, so that invalid tasks are never sent to the backend.

#### Acceptance Criteria

1. WHILE the Task_Form's `title` field is empty, THE Save_Task_Button SHALL remain disabled and the form SHALL NOT submit.
2. WHILE the Task_Form's `dueDate` field is empty, THE Save_Task_Button SHALL remain disabled and the form SHALL NOT submit.
3. WHEN the Authenticated_User touches the `title` field and leaves it empty, THE Task_Form SHALL display the validation error message "Title is required."
4. WHEN the Authenticated_User touches the `dueDate` field and leaves it empty, THE Task_Form SHALL display the validation error message "Due date is required."

---

### Requirement 4: Post-Save UI Feedback

**User Story:** As a logged-in user, I want the task list to reflect my new task immediately after saving, so that I can confirm the save was successful without manually refreshing the page.

#### Acceptance Criteria

1. WHEN the Todo_Endpoint responds with `201 Created`, THE home page SHALL reset the Task_Form to its empty state.
2. WHEN the Todo_Endpoint responds with `201 Created`, THE home page SHALL hide the Task_Form and reload the task list so the newly created Task appears in the list.
3. WHEN the Todo_Endpoint responds with `201 Created`, THE home page SHALL display the new Task sorted in ascending due date order alongside any existing tasks.

---

### Requirement 5: Error Handling for Failed Saves

**User Story:** As a logged-in user, I want to see a clear error message when saving a task fails, so that I know the task was not persisted and can try again.

#### Acceptance Criteria

1. IF the Todo_Endpoint returns an HTTP error response, THEN THE home page SHALL display the error message "Failed to create task. Please try again."
2. IF the Authenticated_User's `userId` is absent from `localStorage`, THEN THE home page SHALL display the error message "No logged-in user found." and SHALL NOT submit the request to the API.
3. IF the Auth_Interceptor finds no JWT in `localStorage` when the Save_Task_Button is pressed, THEN THE home page SHALL NOT submit the request to the API.

---

### Requirement 6: Backend Request Validation

**User Story:** As a system operator, I want the API to reject malformed task creation requests, so that the database is not corrupted with incomplete or unauthorized records.

#### Acceptance Criteria

1. IF the `POST /task` request body is missing the `user` field or the `user.id` field, THEN THE API SHALL return HTTP status `400 Bad Request`.
2. IF the `user.id` in the `POST /task` request body does not correspond to any existing user in the database, THEN THE API SHALL return HTTP status `400 Bad Request`.
3. IF the `POST /task` request is missing the `Authorization` header or the header contains an invalid or expired JWT, THEN THE API SHALL return HTTP status `401 Unauthorized`.
4. IF the `POST /task` request body is missing the `title` field, THEN THE API SHALL return HTTP status `400 Bad Request`.
