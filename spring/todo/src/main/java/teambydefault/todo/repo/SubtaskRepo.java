package teambydefault.todo.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import teambydefault.todo.entity.Subtask;
import teambydefault.todo.entity.ToDo;

@Repository
public interface SubtaskRepo extends JpaRepository<Subtask, UUID> {
    List<Subtask> findByTodo(ToDo todo);
}
