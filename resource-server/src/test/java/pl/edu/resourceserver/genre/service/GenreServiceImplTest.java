package pl.edu.resourceserver.genre.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.resourceserver.genre.model.Genre;
import pl.edu.resourceserver.genre.repository.GenreRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {
    @Mock
    private GenreRepository genreRepository;
    @InjectMocks
    private GenreServiceImpl genreService;

    @Test
    void shouldFindGenreById_whenInputIsValid() {
        Genre genre = new Genre();
        genre.setId(1L);

        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        assertEquals(genre, genreService.findById(1L));
        verify(genreRepository).findById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenGenreNotFound() {
        when(genreRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(
                EntityNotFoundException.class, () -> genreService.findById(1L));

        assertEquals("Genre not found", entityNotFoundException.getMessage());
        verify(genreRepository).findById(1L);
    }
}
