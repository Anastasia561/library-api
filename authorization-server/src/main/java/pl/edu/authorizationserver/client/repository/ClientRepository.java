package pl.edu.authorizationserver.client.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.authorizationserver.client.model.Client;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, String> {
    @EntityGraph(attributePaths = {
            "scopes",
            "grantTypes",
            "redirectUris",
            "authMethods"
    })
    Optional<Client> findByIdClient(String idClient);

    boolean existsByIdClient(String idClient);
}
