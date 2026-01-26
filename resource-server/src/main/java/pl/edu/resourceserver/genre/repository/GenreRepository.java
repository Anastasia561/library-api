package pl.edu.resourceserver.genre.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.resourceserver.genre.model.Genre;

public interface GenreRepository extends CrudRepository<Genre, Long> {
}
