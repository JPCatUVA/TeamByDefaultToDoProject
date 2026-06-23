package teambydefault.todo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import teambydefault.todo.entity.Subtask;
import teambydefault.todo.entity.Todo;
import teambydefault.todo.service.SubtaskService;
import teambydefault.todo.service.ToDoService;

@RestController
@RequestMapping("/subtask")
@RequiredArgsConstructor
public class SubtaskController {

    private final SubtaskService subtaskService;
    private final ToDoService toDoService;

    // GET /subtask?taskId={taskId}
    // Returns all subtasks for a given task
    @GetMapping
    public ResponseEntity<List<Subtask>> getSubtasks(@RequestParam UUID taskId) {
        Todo todo = toDoService.getById(taskId).orElse(null);
        if (todo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(subtaskService.getAllByTodo(todo));
    }

    // GET /subtask/{subtaskId}
    @GetMapping("/{subtaskId}")
    public ResponseEntity<Subtask> getSubtaskById(@PathVariable UUID subtaskId) {
        return subtaskService.getById(subtaskId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /subtask
    // Body: title, description, dueDate, todo.taskId
    @PostMapping
    public ResponseEntity<Subtask> createSubtask(@RequestBody Subtask subtask) {
        if (subtask.getTodo() == null || subtask.getTodo().getTaskId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Todo todo = toDoService.getById(subtask.getTodo().getTaskId()).orElse(null);
        if (todo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        subtask.setTodo(todo);
        Subtask created = subtaskService.createSubtask(subtask);
        return ResponseEntity.ok(created);
    }

    // PATCH /subtask/{subtaskId}
    // Partial update — only send the fields you want to change
    @PatchMapping("/{subtaskId}")
    public ResponseEntity<Subtask> patchSubtask(
            @PathVariable UUID subtaskId,
            @RequestBody Subtask patch) {

        return subtaskService.patchSubtask(subtaskId, patch)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    // DELETE /subtask/{subtaskId}
    @DeleteMapping("/{subtaskId}")
    public ResponseEntity<Void> deleteSubtask(@PathVariable UUID subtaskId) {
        boolean deleted = subtaskService.deleteSubtask(subtaskId);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.noContent().build();
    }
}
