# Product Overview

**Todo Management Application** — a full-stack web app that lets users register, authenticate, and manage hierarchical todo tasks with subtasks.

## Team
Team By Default: David Allen Santos, Nicholas Wilfong, Justin Cary (Revature training project)

## Core User Stories

- **Account Creation**: Users register with email + password
- **Authentication**: Users log in/out to securely access their todos
- **Task Management**: Users create, edit, and delete todo items (title, description, due date/time, completion status)
- **Subtask Organization**: Users create, edit, and delete subtasks nested under a parent task; subtasks are cascade-deleted when their parent is deleted

## Key Pages

| Page                  | Purpose                                      |
|-----------------------|----------------------------------------------|
| `AccountCreation.html`| Registration form                            |
| `Login.html`          | Login form                                   |
| `Main.html`           | Dashboard — lists all tasks and their status |
| `Tasks.html`          | Detail view for a single task + its subtasks |
| `Subtask.html`        | Detail/edit view for a single subtask        |
| `Account.html`        | Displays basic user info                     |

## Notes for AI Assistants

- Clarity, clean code, and learning-friendly patterns are priorities alongside correctness (training context).
- Leverage built-in Spring Security features for authentication where possible.
- All pages must include a logout button in the header/footer.
- Task backlog: https://github.com/users/JPCatUVA/projects/1/views/1
