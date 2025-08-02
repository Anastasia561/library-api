package pl.edu.resourceserver.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.resourceserver.entity.Genre;

import java.util.Optional;

public interface GenreRepository extends CrudRepository<Genre, Long> {
    Optional<Genre> findByName(String name);
}
