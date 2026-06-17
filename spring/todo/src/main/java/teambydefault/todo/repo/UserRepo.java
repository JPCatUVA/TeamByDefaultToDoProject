package teambydefault.todo.repo;

import java.util.UUID;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import teambydefault.todo.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, UUID>{
    Optional<User> findByEmail(String email);
    Optional<User> findByPassword(String password);
}
