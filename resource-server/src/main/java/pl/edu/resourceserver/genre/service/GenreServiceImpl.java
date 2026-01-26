package pl.edu.resourceserver.genre.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.resourceserver.genre.model.Genre;
import pl.edu.resourceserver.genre.repository.GenreRepository;

@Service
@RequiredArgsConstructor
class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public Genre findById(long id) {
        return genreRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Genre not found"));
    }
}
