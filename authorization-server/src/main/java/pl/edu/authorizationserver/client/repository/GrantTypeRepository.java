package pl.edu.authorizationserver.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.authorizationserver.client.model.GrantType;

import java.util.Optional;

public interface GrantTypeRepository extends JpaRepository<GrantType, Integer> {
    Optional<GrantType> findByName(String name);
}
