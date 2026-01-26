package pl.edu.resourceserver.publisher.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.resourceserver.publisher.model.Publisher;
import pl.edu.resourceserver.publisher.repository.PublisherRepository;

@Service
@RequiredArgsConstructor
class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;

    @Override
    public Publisher findById(long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found"));
    }
}
