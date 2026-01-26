package pl.edu.resourceserver.book.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import pl.edu.resourceserver.book.model.Book;

import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {
    Page<Book> findAll(Pageable pageable);

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);
}
