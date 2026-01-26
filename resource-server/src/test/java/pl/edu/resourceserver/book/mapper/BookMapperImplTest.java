package pl.edu.resourceserver.book.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.edu.resourceserver.author.model.Author;
import pl.edu.resourceserver.book.dto.BookFullViewResponseDto;
import pl.edu.resourceserver.book.dto.BookPreviewResponseDto;
import pl.edu.resourceserver.book.dto.BookUpdateDto;
import pl.edu.resourceserver.book.dto.BookUploadRequestDto;
import pl.edu.resourceserver.book.model.Book;
import pl.edu.resourceserver.genre.model.Genre;
import pl.edu.resourceserver.publisher.model.Publisher;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BookMapperImplTest {
    private final BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

    @Test
    void shouldMapToBookPreviewResponseDtoFromBook_whenInputIsValid() {
        Book book = generateBook();

        BookPreviewResponseDto dto = bookMapper.toBookPreviewResponseDto(book);

        assertNotNull(dto);
        assertEquals("title", dto.getTitle());
        assertEquals("123456789", dto.getIsbn());
        assertEquals(10, dto.getPages());
        assertEquals(2030, dto.getPublicationYear());

        assertEquals("John", dto.getAuthor().firstName());
        assertEquals("Doe", dto.getAuthor().lastName());
        assertEquals("JD", dto.getAuthor().penName());

        assertEquals("Penguin", dto.getPublisher());
        assertEquals("Fiction", dto.getGenre());
    }

    @Test
    void shouldMapToBookFullResponseDtoFromBook_whenInputIsValid() {
        Book book = generateBook();

        BookFullViewResponseDto dto = bookMapper.toBookFullViewResponseDto(book);

        assertNotNull(dto);
        assertEquals("title", dto.getTitle());
        assertEquals("123456789", dto.getIsbn());
        assertEquals(10, dto.getPages());
        assertEquals(2030, dto.getPublicationYear());

        assertEquals("John", dto.getAuthor().firstName());
        assertEquals("Doe", dto.getAuthor().lastName());
        assertEquals("JD", dto.getAuthor().penName());

        assertEquals("Penguin", dto.getPublisher());
        assertEquals("Fiction", dto.getGenre());
    }

    @Test
    void shouldReturnNull_whenBookIsNullForPreviewDto() {
        assertNull(bookMapper.toBookPreviewResponseDto(null));
    }

    @Test
    void shouldReturnNull_whenBookIsNullForFullDto() {
        assertNull(bookMapper.toBookFullViewResponseDto(null));
    }

    @Test
    void shouldReturnNull_whenUploadRequestDtoIsNull() {
        assertNull(bookMapper.toBookPreviewResponseDto(null));
    }

    @Test
    void shouldMapToBookFromUploadRequestDto_whenInputIsValid() {
        BookUploadRequestDto dto = new BookUploadRequestDto(null, null, "12345678",
                "Title", 2030, 100, 1, 1, 1);

        Book book = bookMapper.toBook(dto);

        assertNotNull(dto);
        assertEquals("Title", book.getTitle());
        assertEquals("12345678", book.getIsbn());
        assertEquals(100, book.getPages());
        assertEquals(2030, book.getPublicationYear());

        assertNull(book.getAuthor());
        assertNull(book.getPublisher());
        assertNull(book.getGenre());
    }

    @Test
    void shouldUpdateBookFromDto_whenInputIsValid() {
        Book book = generateBook();
        BookUpdateDto dto = new BookUpdateDto("NewTitle", 2030, 100,
                1, 1, 1);

        bookMapper.updateBookFromDto(dto, book);

        assertEquals("NewTitle", book.getTitle());
        assertEquals(100, book.getPages());
        assertEquals(2030, book.getPublicationYear());
    }

    @Test
    void shouldNotUpdateBook_whenFieldsAreNull() {
        Book book = generateBook();
        BookUpdateDto dto = new BookUpdateDto(null, null, null,
                1, 1, 1);

        bookMapper.updateBookFromDto(dto, book);

        assertEquals("title", book.getTitle());
        assertEquals(10, book.getPages());
        assertEquals(2030, book.getPublicationYear());
    }

    private Book generateBook() {
        Book book = new Book();
        book.setTitle("title");
        book.setIsbn("123456789");
        book.setPages(10);
        book.setPublicationYear(2030);

        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Doe");
        author.setPenName("JD");
        book.setAuthor(author);

        Publisher publisher = new Publisher();
        publisher.setName("Penguin");
        book.setPublisher(publisher);

        Genre genre = new Genre();
        genre.setName("Fiction");
        book.setGenre(genre);

        return book;
    }
}
