package pl.edu.authorizationserver.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.authorizationserver.client.model.AuthMethod;

import java.util.Optional;

public interface AuthMethodRepository extends JpaRepository<AuthMethod, Integer> {
    Optional<AuthMethod> findByName(String name);
}
