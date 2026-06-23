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
import teambydefault.todo.entity.Todo;
import teambydefault.todo.entity.User;
import teambydefault.todo.repo.UserRepo;
import teambydefault.todo.service.ToDoService;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;
    private final UserRepo userRepo;

    // GET /task?userId={userId}
    // Returns all tasks belonging to the given user
    @GetMapping
    public ResponseEntity<List<Todo>> getTasks(@RequestParam UUID userId) {
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(toDoService.getAllByUser(user));
    }

    // GET /task/{taskId}
    @GetMapping("/{taskId}")
    public ResponseEntity<Todo> getTaskById(@PathVariable UUID taskId) {
        return toDoService.getById(taskId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /task
    // Body: title, description, dueDate, userId (as a field inside the Todo body or resolved below)
    @PostMapping
    public ResponseEntity<Todo> createTask(@RequestBody Todo toDo) {
        if (toDo.getUser() == null || toDo.getUser().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        User user = userRepo.findById(toDo.getUser().getId()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        toDo.setUser(user);
        Todo created = toDoService.createToDo(toDo);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PATCH /task/{taskId}
    // Partial update — only send the fields you want to change
    @PatchMapping("/{taskId}")
    public ResponseEntity<Todo> patchTask(
            @PathVariable UUID taskId,
            @RequestBody Todo patch) {

        return toDoService.patchToDo(taskId, patch)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    // DELETE /task/{taskId}
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID taskId) {
        boolean deleted = toDoService.deleteToDo(taskId);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.noContent().build();
    }
}
