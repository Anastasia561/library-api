package pl.edu.resourceserver.author.service;

import pl.edu.resourceserver.author.model.Author;

public interface AuthorService {
    Author findById(long id);
}
