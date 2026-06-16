package teambydefault.todo.repo;

import java.util.UUID;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import teambydefault.todo.entity.Account;

@Repository
public interface AccountRepo extends JpaRepository<Account, UUID>{
    Optional<Account> findByEmail(String email);
    Account findByPassword(String password);
}
