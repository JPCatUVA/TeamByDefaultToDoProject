package teambydefault.todo.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import teambydefault.todo.entity.Todo;
import teambydefault.todo.entity.User;

@Repository
public interface ToDoRepo extends JpaRepository<Todo, UUID> {
    List<Todo> findByUser(User user);
    List<Todo> findByUserAndIsCompleted(User user, boolean isCompleted);
    Optional<Todo> findByTitle(String title);
}
