package pl.edu.resourceserver.book.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.resourceserver.author.model.Author;
import pl.edu.resourceserver.author.service.AuthorService;
import pl.edu.resourceserver.book.dto.BookFullViewResponseDto;
import pl.edu.resourceserver.book.dto.BookPreviewResponseDto;
import pl.edu.resourceserver.book.dto.BookUpdateDto;
import pl.edu.resourceserver.book.dto.BookUploadRequestDto;
import pl.edu.resourceserver.book.mapper.BookMapper;
import pl.edu.resourceserver.book.model.Book;
import pl.edu.resourceserver.book.repository.BookRepository;
import pl.edu.resourceserver.book.service.contract.StorageService;
import pl.edu.resourceserver.book.service.contract.UrlGeneratorService;
import pl.edu.resourceserver.genre.model.Genre;
import pl.edu.resourceserver.genre.service.GenreService;
import pl.edu.resourceserver.publisher.model.Publisher;
import pl.edu.resourceserver.publisher.service.PublisherService;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private StorageService storageService;
    @Mock
    private UrlGeneratorService urlGeneratorService;
    @Mock
    private PublisherService publisherService;
    @Mock
    private GenreService genreService;
    @Mock
    private AuthorService authorService;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void shouldReturnPageOfBookPreviews_whenInputIsValid() throws MalformedURLException {
        Book book = new Book();
        book.setIsbn("123456789");
        book.setTitle("Title");

        BookPreviewResponseDto dto = new BookPreviewResponseDto();
        dto.setIsbn("123456789");
        dto.setTitle("Title");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toBookPreviewResponseDto(book)).thenReturn(dto);
        when(urlGeneratorService.generateBookCoverURL("123456789"))
                .thenReturn(URI.create("http://cover.url/123456789").toURL());

        Page<BookPreviewResponseDto> result = bookService.getAllPreview(pageable);

        assertEquals(1, result.getTotalElements());
        BookPreviewResponseDto resultDto = result.getContent().getFirst();
        assertEquals("123456789", resultDto.getIsbn());
        assertEquals("http://cover.url/123456789", resultDto.getCoverImage().toString());

        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toBookPreviewResponseDto(book);
        verify(urlGeneratorService).generateBookCoverURL("123456789");
    }

    @Test
    void shouldReturnBookPreviewResponseDto_whenIsbnExists() throws MalformedURLException {
        Book book = new Book();
        book.setIsbn("123456789");
        book.setTitle("Title");

        BookPreviewResponseDto dto = new BookPreviewResponseDto();
        dto.setIsbn("123456789");
        dto.setTitle("Title");

        when(bookRepository.findByIsbn("123456789")).thenReturn(Optional.of(book));
        when(bookMapper.toBookPreviewResponseDto(book)).thenReturn(dto);
        when(urlGeneratorService.generateBookCoverURL("123456789"))
                .thenReturn(URI.create("http://cover.url/123456789").toURL());

        BookPreviewResponseDto result = bookService.getByIsbn("123456789");

        assertEquals("123456789", result.getIsbn());
        assertEquals("http://cover.url/123456789", result.getCoverImage().toString());

        verify(bookRepository).findByIsbn("123456789");
        verify(bookMapper).toBookPreviewResponseDto(book);
        verify(urlGeneratorService).generateBookCoverURL("123456789");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenIsbnNotFound() {
        when(bookRepository.findByIsbn("123456789")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                bookService.getByIsbn("123456789")
        );

        assertEquals("Book not found", exception.getMessage());
        verify(bookRepository).findByIsbn("123456789");
        verifyNoInteractions(urlGeneratorService, bookMapper);
    }

    @Test
    void shouldReturnPageOfBookFullViews_whenInputIsValid() throws MalformedURLException {
        Book book = new Book();
        book.setIsbn("123456789");
        book.setTitle("Title");

        BookFullViewResponseDto dto = new BookFullViewResponseDto();
        dto.setIsbn("123456789");
        dto.setTitle("Title");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toBookFullViewResponseDto(book)).thenReturn(dto);
        when(urlGeneratorService.generateBookCoverURL("123456789"))
                .thenReturn(URI.create("http://cover.url/123456789").toURL());
        when(urlGeneratorService.generateBookURL("123456789", true))
                .thenReturn(URI.create("http://book_full.url/123456789").toURL());
        when(urlGeneratorService.generateBookURL("123456789", false))
                .thenReturn(URI.create("http://book_preview.url/123456789").toURL());

        Page<BookFullViewResponseDto> result = bookService.getAllFullView(pageable);

        assertEquals(1, result.getTotalElements());
        BookFullViewResponseDto resultDto = result.getContent().getFirst();
        assertEquals("123456789", resultDto.getIsbn());
        assertEquals("http://cover.url/123456789", resultDto.getCoverImage().toString());
        assertEquals("http://book_full.url/123456789", resultDto.getFullBookDocument().toString());
        assertEquals("http://book_preview.url/123456789", resultDto.getPreviewBookDocument().toString());

        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toBookFullViewResponseDto(book);
        verify(urlGeneratorService).generateBookCoverURL("123456789");
        verify(urlGeneratorService).generateBookURL("123456789", true);
        verify(urlGeneratorService).generateBookURL("123456789", false);
    }

    @Test
    void shouldReturnDownloadUrlForBook_whenBookExists() throws Exception {
        String isbn = "123456789";
        boolean isFull = true;
        Book book = new Book();
        book.setTitle("My Book");

        URL expectedUrl = URI.create("http://download.url/123456789").toURL();

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
        when(urlGeneratorService.generateBookDownloadURL(isbn, "My Book", isFull))
                .thenReturn(expectedUrl);

        URL result = bookService.getDownloadUrl(isbn, isFull);

        assertEquals(expectedUrl, result);
        verify(bookRepository).findByIsbn(isbn);
        verify(urlGeneratorService).generateBookDownloadURL(isbn, "My Book", isFull);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenBookNotFoundForUrlDownload() {
        when(bookRepository.findByIsbn("123456789")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                bookService.getDownloadUrl("123456789", false)
        );

        assertEquals("Book not found", exception.getMessage());
        verify(bookRepository).findByIsbn("123456789");
        verifyNoInteractions(urlGeneratorService);
    }

    @Test
    void shouldDeleteBookAndFolder_whenBookExistsAndFolderExists() {
        String isbn = "123456789";
        Book book = new Book();
        book.setIsbn(isbn);

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));

        bookService.delete(isbn);

        verify(bookRepository).delete(book);
        verify(storageService).deleteFolder(isbn);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenBookDoesNotExistForDelete() {
        String isbn = "123456789";
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.delete(isbn));

        assertEquals("Book not found", exception.getMessage());
        verifyNoInteractions(storageService);
        verify(bookRepository).findByIsbn(isbn);
    }

    @Test
    void shouldThrowRuntimeException_whenStorageFails() {
        String isbn = "123456789";
        Book book = new Book();
        book.setIsbn(isbn);

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
        doThrow(new RuntimeException("S3 failure")).when(storageService).deleteFolder(isbn);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.delete(isbn));

        assertEquals("Failed to delete folder for isbn : " + isbn, exception.getMessage());
        verify(bookRepository).delete(book);
        verify(storageService).deleteFolder(isbn);
    }

    @Test
    void shouldSaveBookAndUploadFiles_whenDtoIsValid() throws Exception {
        BookUploadRequestDto dto = new BookUploadRequestDto(
                mock(MultipartFile.class),
                mock(MultipartFile.class),
                "123456789", "Title",
                2025, 100, 1, 1, 1
        );

        URL expectedUrl = URI.create("http://download.url/123456789").toURL();

        Book book = new Book();
        book.setIsbn(dto.isbn());

        BookPreviewResponseDto previewDto = new BookPreviewResponseDto();
        previewDto.setIsbn(dto.isbn());

        when(bookMapper.toBook(dto)).thenReturn(book);
        when(authorService.findById(dto.author())).thenReturn(new Author());
        when(genreService.findById(dto.genre())).thenReturn(new Genre());
        when(publisherService.findById(dto.publisher())).thenReturn(new Publisher());
        when(bookMapper.toBookPreviewResponseDto(book)).thenReturn(previewDto);
        when(urlGeneratorService.generateBookCoverURL(dto.isbn())).thenReturn(expectedUrl);

        BookPreviewResponseDto result = bookService.save(dto);

        assertEquals(dto.isbn(), result.getIsbn());
        assertEquals(expectedUrl, result.getCoverImage());

        verify(bookRepository).save(book);
        verify(storageService).uploadFiles(List.of(dto.book(), dto.coverImage()), dto.isbn());
        verify(urlGeneratorService).generateBookCoverURL(dto.isbn());
    }

    @Test
    void shouldThrowRuntimeException_whenUploadFails() {
        BookUploadRequestDto dto = new BookUploadRequestDto(
                mock(MultipartFile.class),
                mock(MultipartFile.class),
                "123456789", "Title",
                2025, 100, 1, 1, 1
        );

        Book book = new Book();
        book.setIsbn(dto.isbn());

        when(bookMapper.toBook(dto)).thenReturn(book);
        when(authorService.findById(dto.author())).thenReturn(new Author());
        when(genreService.findById(dto.genre())).thenReturn(new Genre());
        when(publisherService.findById(dto.publisher())).thenReturn(new Publisher());

        doThrow(new RuntimeException("S3 failure"))
                .when(storageService).uploadFiles(anyList(), anyString());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.save(dto));

        assertTrue(exception.getMessage().contains("Failed to upload book"));
        verify(bookRepository).save(book);
        verifyNoInteractions(urlGeneratorService);
        verify(storageService).uploadFiles(anyList(), anyString());
    }

    @Test
    void shouldReturnUpdatedBook_whenInputIsValid() throws Exception {
        String isbn = "123456789";
        BookUpdateDto dto = new BookUpdateDto("New Title", 2025, 200,
                1, 1, 1);
        URL coverUrl = URI.create("http://example.com/cover.jpg").toURL();

        Book book = new Book();
        book.setIsbn(isbn);

        BookPreviewResponseDto previewDto = new BookPreviewResponseDto();
        previewDto.setIsbn(isbn);

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));

        when(authorService.findById(dto.author())).thenReturn(new Author());
        when(genreService.findById(dto.genre())).thenReturn(new Genre());
        when(publisherService.findById(dto.publisher())).thenReturn(new Publisher());
        when(bookMapper.toBookPreviewResponseDto(book)).thenReturn(previewDto);
        when(urlGeneratorService.generateBookCoverURL(isbn)).thenReturn(coverUrl);

        BookPreviewResponseDto result = bookService.update(dto, isbn);

        assertEquals(isbn, result.getIsbn());
        assertEquals(coverUrl, result.getCoverImage());

        verify(bookRepository).findByIsbn(isbn);
        verify(bookMapper).updateBookFromDto(dto, book);
        verify(authorService).findById(dto.author());
        verify(genreService).findById(dto.genre());
        verify(publisherService).findById(dto.publisher());
        verify(bookMapper).toBookPreviewResponseDto(book);
        verify(urlGeneratorService).generateBookCoverURL(isbn);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenBookNotFoundForUpdate() {
        String isbn = "123456789";
        BookUpdateDto dto = new BookUpdateDto("New Title", 2025, 200,
                1, 1, 1);
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.update(dto, isbn));

        assertEquals("Book not found", exception.getMessage());
        verifyNoInteractions(storageService, bookMapper);
        verify(bookRepository).findByIsbn(isbn);
    }
}
