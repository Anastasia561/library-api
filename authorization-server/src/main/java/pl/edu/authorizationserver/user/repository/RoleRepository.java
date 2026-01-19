package pl.edu.authorizationserver.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.authorizationserver.user.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
