package pl.edu.authorizationserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.authorizationserver.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
