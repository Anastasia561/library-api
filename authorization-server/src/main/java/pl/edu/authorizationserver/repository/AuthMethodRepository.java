package pl.edu.authorizationserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.authorizationserver.entity.AuthMethod;

import java.util.Optional;

public interface AuthMethodRepository extends JpaRepository<AuthMethod, Integer> {
    Optional<AuthMethod> findByName(String name);
}
