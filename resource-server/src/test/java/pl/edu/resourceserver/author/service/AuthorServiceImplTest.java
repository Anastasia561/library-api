package pl.edu.resourceserver.author.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.resourceserver.author.model.Author;
import pl.edu.resourceserver.author.repository.AuthorRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {
    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    void shouldFindAuthorById_whenInputIsValid() {
        Author author = new Author();
        author.setId(1L);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        assertEquals(author, authorService.findById(1L));
        verify(authorRepository).findById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenAuthorNotFound() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(
                EntityNotFoundException.class, () -> authorService.findById(1L));

        assertEquals("Author not found", entityNotFoundException.getMessage());
        verify(authorRepository).findById(1L);
    }
}
