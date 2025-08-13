package pl.edu.resourceserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.resourceserver.dto.BookLibrarianResponseDto;
import pl.edu.resourceserver.dto.BookUploadRequestDto;
import pl.edu.resourceserver.dto.BookUserResponseDto;
import pl.edu.resourceserver.entity.Book;
import pl.edu.resourceserver.exception.EntityNotFoundException;
import pl.edu.resourceserver.mapper.BookMapper;
import pl.edu.resourceserver.repository.BookRepository;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private StorageService storageService;

    @Mock
    private UrlGeneratorService urlGeneratorService;

    @InjectMocks
    private BookService bookService;

    @Test
    void getAllBooksForUserTest() throws Exception {
        Book book = new Book();
        book.setIsbn("123");
        BookUserResponseDto dto = new BookUserResponseDto();
        dto.setIsbn("123");

        when(bookRepository.findAll()).thenReturn(List.of(book));
        when(bookMapper.toBookUserResponseDto(book)).thenReturn(dto);
        when(urlGeneratorService.generateBookCoverURL("123")).thenReturn(new URL("https://example.com/cover"));

        List<BookUserResponseDto> result = bookService.getAllBooksForUser();

        assertEquals(1, result.size());
        assertEquals("123", result.getFirst().getIsbn());
        assertEquals("https://example.com/cover",
                result.getFirst().getCoverImage().toString());
    }

    @Test
    void getBookByIdShouldReturnBookWithUrlTest() throws Exception {
        Book book = new Book();
        book.setIsbn("123");
        BookUserResponseDto dto = new BookUserResponseDto();
        dto.setIsbn("123");

        when(bookRepository.findByIsbn("123")).thenReturn(Optional.of(book));
        when(bookMapper.toBookUserResponseDto(book)).thenReturn(dto);
        when(urlGeneratorService.generateBookCoverURL("123")).thenReturn(new URL("https://example.com/cover"));

        BookUserResponseDto result = bookService.getBookByIsbn("123");

        assertEquals("123", result.getIsbn());
        assertEquals("https://example.com/cover", result.getCoverImage().toString());
    }

    @Test
    void getBookByIsbnShouldThrowExceptionWhenNotFoundTest() {
        when(bookRepository.findByIsbn("123")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.getBookByIsbn("123"));
    }

    @Test
    void getAllBooksForLibrarianTest() throws Exception {
        Book book = new Book();
        book.setIsbn("123");
        BookLibrarianResponseDto dto = new BookLibrarianResponseDto();
        dto.setIsbn("123");

        when(bookRepository.findAll()).thenReturn(List.of(book));
        when(bookMapper.toBookLibrarianResponseDto(book)).thenReturn(dto);
        when(urlGeneratorService.generateBookCoverURL("123"))
                .thenReturn(new URL("https://example.com/cover"));
        when(urlGeneratorService.generateBookURL("123", true))
                .thenReturn(new URL("https://example.com/book"));
        when(urlGeneratorService.generateBookURL("123", false))
                .thenReturn(new URL("https://example.com/preview"));

        List<BookLibrarianResponseDto> result = bookService.getAllBooksForLibrarian();

        assertEquals(1, result.size());
        assertEquals("123", result.getFirst().getIsbn());
        assertEquals("https://example.com/cover",
                result.getFirst().getCoverImage().toString());
        assertEquals("https://example.com/book",
                result.getFirst().getFullBookDocument().toString());
        assertEquals("https://example.com/preview",
                result.getFirst().getPreviewBookDocument().toString());
    }

    @Test
    void getFullBookDownloadUrlTest() throws Exception {
        Book book = new Book();
        book.setIsbn("123");
        book.setTitle("Test Title");

        when(bookRepository.findByIsbn("123")).thenReturn(Optional.of(book));
        when(urlGeneratorService.generateBookDownloadURL("123", "Test Title", true))
                .thenReturn(new URL("https://example.com/book"));

        URL result = bookService.getBookDownloadUrl("123", true);

        assertEquals("https://example.com/book", result.toString());
    }

    @Test
    void getPreviewBookDownloadUrlTest() throws Exception {
        Book book = new Book();
        book.setIsbn("123");
        book.setTitle("Test Title");

        when(bookRepository.findByIsbn("123")).thenReturn(Optional.of(book));
        when(urlGeneratorService.generateBookDownloadURL("123", "Test Title", false))
                .thenReturn(new URL("https://example.com/preview"));

        URL result = bookService.getBookDownloadUrl("123", false);

        assertEquals("https://example.com/preview", result.toString());
    }

    @Test
    void getBookDownloadUrlShouldTrowWhenBookNotFoundTest() {
        when(bookRepository.findByIsbn("123")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            bookService.getBookDownloadUrl("123", false);
        });
    }

    @Test
    void deleteBookByIsbnShouldDeleteBookAndFolderWhenFoundTest() throws Exception {
        Book book = new Book();
        book.setIsbn("123");

        when(bookRepository.findByIsbn("123")).thenReturn(Optional.of(book));
        doNothing().when(storageService).deleteFolder("123");

        bookService.deleteBookByIsbn("123");

        verify(bookRepository, times(1)).delete(book);
        verify(storageService, times(1)).deleteFolder("123");
    }

    @Test
    void deleteBookByIsbnShouldWrapStorageExceptionTest() {
        Book book = new Book();
        book.setIsbn("123");

        when(bookRepository.findByIsbn("123")).thenReturn(Optional.of(book));
        doThrow(new RuntimeException("S3 Error")).when(storageService).deleteFolder("123");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookService.deleteBookByIsbn("123"));
        assertTrue(ex.getMessage().contains("Failed to delete folder"));
    }

    @Test
    void deleteBookByIsbnShouldThrowEntityNotFoundExceptionWhenBookNotFoundTest() {
        when(bookRepository.findByIsbn("123")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> bookService.deleteBookByIsbn("123"));
        assertEquals("Book with isbn - 123 not found", ex.getMessage());
    }

    @Test
    void saveBook_shouldSaveBookAndUploadFiles() throws Exception {
        MultipartFile bookFile = mock(MultipartFile.class);
        MultipartFile coverFile = mock(MultipartFile.class);
        BookUploadRequestDto dto = new BookUploadRequestDto();
        dto.setIsbn("123");
        dto.setBook(bookFile);
        dto.setCoverImage(coverFile);

        Book bookEntity = new Book();

        when(bookMapper.toBook(dto)).thenReturn(bookEntity);

        bookService.saveBook(dto);

        verify(bookRepository).save(bookEntity);
        verify(storageService).uploadFiles(List.of(bookFile, coverFile), "123");
    }
}
