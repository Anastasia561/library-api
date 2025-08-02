package pl.edu.resourceserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import pl.edu.resourceserver.dto.BookLibrarianResponseDto;
import pl.edu.resourceserver.dto.BookUpdateDto;
import pl.edu.resourceserver.dto.BookUploadRequestDto;
import pl.edu.resourceserver.dto.BookUserResponseDto;
import pl.edu.resourceserver.entity.Book;
import pl.edu.resourceserver.exception.EntityNotFoundException;
import pl.edu.resourceserver.mapper.BookMapper;
import pl.edu.resourceserver.repository.BookRepository;

import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final StorageService storageService;
    private final UrlGeneratorService urlGeneratorService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public BookService(BookRepository bookRepository, BookMapper bookMapper,
                       StorageService storageService, ObjectMapper objectMapper, Validator validator,
                       UrlGeneratorService urlGeneratorService) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.storageService = storageService;
        this.objectMapper = objectMapper;
        this.validator = validator;
        this.urlGeneratorService = urlGeneratorService;
    }

    public List<BookUserResponseDto> getAllBooksForUser() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookUserResponseDto)
                .peek(b -> b.setCoverImage(urlGeneratorService.generateBookCoverURL(b.getIsbn())))
                .collect(Collectors.toList());
    }

    public BookUserResponseDto getBookByIsbn(String isbn) {
        BookUserResponseDto book = bookRepository.findByIsbn(isbn).map(bookMapper::toBookUserResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("Book with isbn - " + isbn + " not found"));
        book.setCoverImage(urlGeneratorService.generateBookCoverURL(book.getIsbn()));
        return book;
    }

    public List<BookLibrarianResponseDto> getAllBooksForLibrarian() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookLibrarianResponseDto)
                .peek(b -> {
                    b.setCoverImage(urlGeneratorService.generateBookCoverURL(b.getIsbn()));
                    b.setFullBookDocument(urlGeneratorService.generateBookURL(b.getIsbn(), true));
                    b.setPreviewBookDocument(urlGeneratorService.generateBookURL(b.getIsbn(), false));
                })
                .collect(Collectors.toList());
    }

    public URL getBookDownloadUrl(String isbn, boolean isFull) {
        String title = bookRepository.findByIsbn(isbn)
                .map(Book::getTitle)
                .orElseThrow(() -> new EntityNotFoundException("Book with isbn - " + isbn + " not found"));


        return urlGeneratorService.generateBookDownloadURL(isbn, title, isFull);
    }

    @Transactional
    public void deleteBookByIsbn(String isbn) {
        bookRepository.findByIsbn(isbn)
                .ifPresentOrElse(
                        book -> {
                            bookRepository.delete(book);
                            try {
                                storageService.deleteFolder(isbn);
                            } catch (Exception e) {
                                throw new RuntimeException("Failed to delete folder for isbn : " + isbn);
                            }
                        },
                        () -> {
                            throw new EntityNotFoundException("Book with isbn - " + isbn + " not found");
                        }
                );
    }

    @Transactional
    public void saveBook(BookUploadRequestDto dto) {
        bookRepository.save(bookMapper.toBook(dto));
        try {
            storageService.uploadFiles(List.of(dto.getBook(), dto.getCoverImage()), dto.getIsbn());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete folder for isbn : " + dto.getIsbn());
        }
    }

    public BookLibrarianResponseDto applyMergePatchToBook(String isbn, JsonMergePatch patch) {
        BookUpdateDto bookUpdateDto = bookRepository.findByIsbn(isbn).map(bookMapper::toBookUpdateDto)
                .orElseThrow(() -> new EntityNotFoundException("Book with isbn - " + isbn + " not found"));

        JsonNode bookNode = objectMapper.valueToTree(bookUpdateDto);

        try {
            JsonNode patched = patch.apply(bookNode);
            BookUpdateDto patchedBook = objectMapper.treeToValue(patched, BookUpdateDto.class);

            Set<ConstraintViolation<BookUpdateDto>> violations = validator.validate(patchedBook);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
            updateBook(isbn, patchedBook);
            BookLibrarianResponseDto dto = bookRepository.findByIsbn(isbn).map(bookMapper::toBookLibrarianResponseDto)
                    .orElseThrow(() -> new EntityNotFoundException("Book with isbn - " + isbn + " not found"));
            dto.setCoverImage(urlGeneratorService.generateBookCoverURL(isbn));
            dto.setFullBookDocument(urlGeneratorService.generateBookURL(isbn, true));
            dto.setPreviewBookDocument(urlGeneratorService.generateBookURL(isbn, false));
            return dto;
        } catch (JsonProcessingException | JsonPatchException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateBook(String isbn, BookUpdateDto dto) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new EntityNotFoundException("Book with isbn - " + isbn + " not found"));
        bookMapper.updateBook(book, dto);
        bookRepository.save(book);
    }
}
