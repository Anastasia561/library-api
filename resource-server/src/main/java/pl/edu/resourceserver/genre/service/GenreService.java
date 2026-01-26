package pl.edu.resourceserver.genre.service;

import pl.edu.resourceserver.genre.model.Genre;

public interface GenreService {
    Genre findById(long id);
}
