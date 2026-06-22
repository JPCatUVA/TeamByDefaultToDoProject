package teambydefault.todo.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import teambydefault.todo.entity.ToDo;
import teambydefault.todo.entity.User;

@Repository
public interface ToDoRepo extends JpaRepository<ToDo, UUID> {
    List<ToDo> findByUser(User user);
    List<ToDo> findByUserAndIsCompleted(User user, boolean isCompleted);
    Optional<ToDo> findByTitle(String title);
}
