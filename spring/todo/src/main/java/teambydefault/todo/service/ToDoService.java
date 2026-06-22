package teambydefault.todo.service;

import teambydefault.todo.entity.ToDo;
import teambydefault.todo.entity.User;
import teambydefault.todo.repo.ToDoRepo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ToDoService {

    private final ToDoRepo toDoRepo;

    // Get all todos for a user
    public List<ToDo> getAllByUser(User user) {
        return toDoRepo.findByUser(user);
    }

    // Get todos filtered by completion status
    public List<ToDo> getByUserAndStatus(User user, boolean isCompleted) {
        return toDoRepo.findByUserAndIsCompleted(user, isCompleted);
    }

    // Get a single todo by its ID
    public Optional<ToDo> getById(UUID taskId) {
        return toDoRepo.findById(taskId);
    }

    // Create a new todo
    public ToDo createToDo(ToDo toDo) {
        return toDoRepo.save(toDo);
    }

    // Partial update — only non-null fields in the patch are applied
    public Optional<ToDo> patchToDo(UUID taskId, ToDo patch) {
        return toDoRepo.findById(taskId).map(existing -> {
            if (patch.getTitle() != null) existing.setTitle(patch.getTitle());
            if (patch.getDescription() != null) existing.setDescription(patch.getDescription());
            if (patch.getDueDate() != null) existing.setDueDate(patch.getDueDate());
            // isCompleted is a primitive boolean — use a wrapper field pattern if needed;
            // for now, always apply it when explicitly included in the patch body
            existing.setCompleted(patch.isCompleted());
            return toDoRepo.save(existing);
        });
    }

    // Delete a todo by ID
    public boolean deleteToDo(UUID taskId) {
        if (toDoRepo.existsById(taskId)) {
            toDoRepo.deleteById(taskId);
            return true;
        }
        return false;
    }
}
