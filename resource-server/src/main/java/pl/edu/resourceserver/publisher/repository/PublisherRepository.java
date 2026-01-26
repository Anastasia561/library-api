package pl.edu.resourceserver.publisher.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.resourceserver.publisher.model.Publisher;

public interface PublisherRepository extends CrudRepository<Publisher, Long> {
}
