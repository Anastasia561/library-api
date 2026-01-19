package pl.edu.authorizationserver.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.authorizationserver.client.model.Client;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, String> {
    Optional<Client> findByIdClient(String idClient);

    boolean existsByIdClient(String idClient);
}
