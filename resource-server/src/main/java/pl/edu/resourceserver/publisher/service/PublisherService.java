package pl.edu.resourceserver.publisher.service;

import pl.edu.resourceserver.publisher.model.Publisher;

public interface PublisherService {
    Publisher findById(long id);
}
