package pl.edu.resourceserver.author.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.edu.resourceserver.author.model.Author;

import java.util.Optional;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    @Query("select a from Author a where a.firstName=:firstName and a.lastName=:lastName")
    Optional<Author> findByFullName(String firstName, String lastName);
}
