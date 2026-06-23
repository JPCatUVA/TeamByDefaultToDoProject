package teambydefault.todo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import teambydefault.todo.entity.Subtask;
import teambydefault.todo.entity.Todo;
import teambydefault.todo.repo.SubtaskRepo;

@Service
@RequiredArgsConstructor
public class SubtaskService {

    private final SubtaskRepo subtaskRepo;

    // Get all subtasks for a given todo
    public List<Subtask> getAllByTodo(Todo todo) {
        return subtaskRepo.findByTodo(todo);
    }

    // Get a single subtask by ID
    public Optional<Subtask> getById(UUID subtaskId) {
        return subtaskRepo.findById(subtaskId);
    }

    // Create a new subtask
    public Subtask createSubtask(Subtask subtask) {
        return subtaskRepo.save(subtask);
    }

    // Partial update — only non-null fields are applied
    public Optional<Subtask> patchSubtask(UUID subtaskId, Subtask patch) {
        return subtaskRepo.findById(subtaskId).map(existing -> {
            if (patch.getTitle() != null) existing.setTitle(patch.getTitle());
            if (patch.getDescription() != null) existing.setDescription(patch.getDescription());
            if (patch.getDueDate() != null) existing.setDueDate(patch.getDueDate());
            return subtaskRepo.save(existing);
        });
    }

    // Delete a subtask by ID
    public boolean deleteSubtask(UUID subtaskId) {
        if (subtaskRepo.existsById(subtaskId)) {
            subtaskRepo.deleteById(subtaskId);
            return true;
        }
        return false;
    }
}
