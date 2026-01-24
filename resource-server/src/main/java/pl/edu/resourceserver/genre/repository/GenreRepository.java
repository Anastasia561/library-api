package pl.edu.resourceserver.genre.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.resourceserver.genre.model.Genre;

import java.util.Optional;

public interface GenreRepository extends CrudRepository<Genre, Long> {
    Optional<Genre> findByName(String name);
}
