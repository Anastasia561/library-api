package pl.edu.authorizationserver.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.authorizationserver.client.model.Scope;

import java.util.Optional;

public interface ScopeRepository extends JpaRepository<Scope, Integer> {
    Optional<Scope> findByName(String name);
}
