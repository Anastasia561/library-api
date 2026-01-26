package pl.edu.resourceserver.author.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.resourceserver.author.model.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {
}
