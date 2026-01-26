package pl.edu.resourceserver.publisher.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.resourceserver.publisher.model.Publisher;
import pl.edu.resourceserver.publisher.repository.PublisherRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublisherServiceImplTest {
    @Mock
    private PublisherRepository publisherRepository;
    @InjectMocks
    private PublisherServiceImpl publisherService;

    @Test
    void shouldFindPublisherById_whenInputIsValid() {
        Publisher publisher = new Publisher();
        publisher.setId(1L);

        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
        assertEquals(publisher, publisherService.findById(1L));
        verify(publisherRepository).findById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenPublisherNotFound() {
        when(publisherRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(
                EntityNotFoundException.class, () -> publisherService.findById(1L));

        assertEquals("Publisher not found", entityNotFoundException.getMessage());
        verify(publisherRepository).findById(1L);
    }
}
