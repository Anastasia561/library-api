package pl.edu.resourceserver.publisher.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.resourceserver.publisher.model.Publisher;

import java.util.Optional;

public interface PublisherRepository extends CrudRepository<Publisher, Long> {
    Optional<Publisher> findByName(String name);
}
