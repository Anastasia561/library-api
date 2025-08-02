package pl.edu.resourceserver.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.resourceserver.entity.Publisher;

import java.util.Optional;

public interface PublisherRepository extends CrudRepository<Publisher, Long> {
    Optional<Publisher> findByName(String name);
}
