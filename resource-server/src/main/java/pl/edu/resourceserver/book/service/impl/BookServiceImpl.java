package pl.edu.resourceserver.book.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.resourceserver.author.service.AuthorService;
import pl.edu.resourceserver.book.dto.BookUpdateDto;
import pl.edu.resourceserver.book.service.contract.BookService;
import pl.edu.resourceserver.book.dto.BookFullViewResponseDto;
import pl.edu.resourceserver.book.dto.BookUploadRequestDto;
import pl.edu.resourceserver.book.dto.BookPreviewResponseDto;
import pl.edu.resourceserver.book.model.Book;
import pl.edu.resourceserver.book.mapper.BookMapper;
import pl.edu.resourceserver.book.repository.BookRepository;
import pl.edu.resourceserver.book.service.contract.StorageService;
import pl.edu.resourceserver.book.service.contract.UrlGeneratorService;
import pl.edu.resourceserver.genre.service.GenreService;
import pl.edu.resourceserver.publisher.service.PublisherService;
import tools.jackson.databind.ObjectMapper;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final StorageService storageService;
    private final UrlGeneratorService urlGeneratorService;
    private final PublisherService publisherService;
    private final GenreService genreService;
    private final AuthorService authorService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Override
    public List<BookPreviewResponseDto> getAllPreview() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookPreviewResponseDto)
                .peek(b -> b.setCoverImage(urlGeneratorService.generateBookCoverURL(b.getIsbn())))
                .collect(Collectors.toList());
    }

    @Override
    public BookPreviewResponseDto getByIsbn(String isbn) {
        BookPreviewResponseDto book = bookRepository.findByIsbn(isbn)
                .map(bookMapper::toBookPreviewResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        book.setCoverImage(urlGeneratorService.generateBookCoverURL(book.getIsbn()));
        return book;
    }

    @Override
    public List<BookFullViewResponseDto> getAllFullView() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookFullViewResponseDto)
                .peek(b -> {
                    b.setCoverImage(urlGeneratorService.generateBookCoverURL(b.getIsbn()));
                    b.setFullBookDocument(urlGeneratorService.generateBookURL(b.getIsbn(), true));
                    b.setPreviewBookDocument(urlGeneratorService.generateBookURL(b.getIsbn(), false));
                })
                .collect(Collectors.toList());
    }

    @Override
    public URL getDownloadUrl(String isbn, boolean isFull) {
        String title = bookRepository.findByIsbn(isbn)
                .map(Book::getTitle)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        return urlGeneratorService.generateBookDownloadURL(isbn, title, isFull);
    }

    @Override
    @Transactional
    public void delete(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        bookRepository.delete(book);
        try {
            storageService.deleteFolder(isbn);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete folder for isbn : " + isbn);
        }
    }

    @Override
    @Transactional
    public BookPreviewResponseDto save(BookUploadRequestDto dto) {
        Book book = bookMapper.toBook(dto);
        book.setAuthor(authorService.findById(dto.author()));
        book.setGenre(genreService.findById(dto.genre()));
        book.setPublisher(publisherService.findById(dto.publisher()));

        bookRepository.save(book);

        try {
            storageService.uploadFiles(List.of(dto.book(), dto.coverImage()), dto.isbn());
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload book");
        }
        BookPreviewResponseDto saved = bookMapper.toBookPreviewResponseDto(book);
        saved.setCoverImage(urlGeneratorService.generateBookCoverURL(book.getIsbn()));
        return saved;
    }

    @Override
    @Transactional
    public BookPreviewResponseDto update(BookUpdateDto dto, String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        bookMapper.updateBookFromDto(dto, book);
        book.setAuthor(authorService.findById(dto.author()));
        book.setGenre(genreService.findById(dto.genre()));
        book.setPublisher(publisherService.findById(dto.publisher()));

        BookPreviewResponseDto saved = bookMapper.toBookPreviewResponseDto(book);
        saved.setCoverImage(urlGeneratorService.generateBookCoverURL(book.getIsbn()));
        return saved;
    }
}
