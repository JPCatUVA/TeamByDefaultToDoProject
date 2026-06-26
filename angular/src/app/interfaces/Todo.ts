/*
  This interface mirrors the `Todo` @Entity class in the Spring backend
  (src/main/java/teambydefault/todo/entity/Todo.java).

  When the backend serializes a Todo to JSON and sends it to Angular, the field
  names and types below are what we expect to receive. Keeping this interface in
  sync with the Java class ensures TypeScript can type-check our HTTP responses.

  Field mapping from Java → TypeScript:
    UUID taskId           → string   (UUIDs are serialized as strings in JSON)
    String title          → string
    String description    → string
    LocalDateTime dueDate → string   (dates are serialized as ISO-8601 strings in JSON)
    boolean isCompleted   → boolean
    User user             → { userId: string }  (only the foreign key ID is needed on the frontend)
*/
export interface Todo {
  taskId: string;
  title: string;
  description: string;
  dueDate: string;
  isCompleted: boolean;
  user: { userId: string };
}
