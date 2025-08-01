package pl.edu.authorizationserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.authorizationserver.entity.Scope;

import java.util.Optional;

public interface ScopeRepository extends JpaRepository<Scope, Integer> {
    Optional<Scope> findByName(String name);
}
