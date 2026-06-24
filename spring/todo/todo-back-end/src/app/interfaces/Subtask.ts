/*
  This interface mirrors the `Subtask` @Entity class in the Spring backend
  (src/main/java/teambydefault/todo/entity/Subtask.java).

  Field mapping from Java → TypeScript:
    UUID id               → string   (UUIDs are serialized as strings in JSON)
    String title          → string
    String description    → string
    LocalDateTime dueDate → string   (dates are serialized as ISO-8601 strings in JSON)
    boolean isCompleted   → boolean
    Todo todo             → { taskId: string }  (only the foreign key ID is needed on the frontend)
*/
export interface Subtask {
  id: string;
  title: string;
  description: string;
  dueDate: string;
  isCompleted: boolean;
  todo: { taskId: string };
}
