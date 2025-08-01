package pl.edu.authorizationserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.authorizationserver.entity.GrantType;

import java.util.Optional;

public interface GrantTypeRepository extends JpaRepository<GrantType, Integer> {
    Optional<GrantType> findByName(String name);
}
