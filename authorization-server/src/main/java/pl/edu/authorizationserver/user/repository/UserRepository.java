package pl.edu.authorizationserver.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.authorizationserver.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
