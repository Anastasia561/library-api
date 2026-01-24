package pl.edu.resourceserver.book.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.resourceserver.book.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findAll();

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);
}
