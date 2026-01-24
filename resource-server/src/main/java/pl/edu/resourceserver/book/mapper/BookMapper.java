package pl.edu.resourceserver.book.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import pl.edu.resourceserver.book.dto.BookFullViewResponseDto;
import pl.edu.resourceserver.book.dto.BookUpdateDto;
import pl.edu.resourceserver.book.dto.BookUploadRequestDto;
import pl.edu.resourceserver.book.dto.BookPreviewResponseDto;
import pl.edu.resourceserver.author.model.Author;
import pl.edu.resourceserver.book.model.Book;
import pl.edu.resourceserver.genre.model.Genre;
import pl.edu.resourceserver.publisher.model.Publisher;
import pl.edu.resourceserver.author.repository.AuthorRepository;
import pl.edu.resourceserver.genre.repository.GenreRepository;
import pl.edu.resourceserver.publisher.repository.PublisherRepository;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "publisher", source = "publisher.name")
    @Mapping(target = "genre", source = "genre.name")
    BookPreviewResponseDto toBookPreviewResponseDto(Book book);

    @Mapping(target = "author", source = "author")
    @Mapping(target = "publisher", source = "publisher.name")
    @Mapping(target = "genre", source = "genre.name")
    BookFullViewResponseDto toBookFullViewResponseDto(Book book);
//
//    public Book toBook(BookUploadRequestDto dto) {
//        Book book = new Book();
//        book.setTitle(dto.getTitle());
//        book.setPublicationYear(dto.getPublicationYear());
//        book.setPages(dto.getPages());
//        book.setIsbn(dto.getIsbn());
//        Author author = authorRepository.findByFullName(dto.getAuthor().split(" ")[0], dto.getAuthor().split(" ")[1])
//                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
//        Publisher publisher = publisherRepository.findByName(dto.getPublisher())
//                .orElseThrow(() -> new EntityNotFoundException("Publisher not found"));
//        Genre genre = genreRepository.findByName(dto.getGenre())
//                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
//        book.setAuthor(author);
//        book.setPublisher(publisher);
//        book.setGenre(genre);
//        return book;
//    }

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "publisher", ignore = true)
    @Mapping(target = "genre", ignore = true)
    Book toBook(BookUploadRequestDto dto);
//
//    public void updateBook(Book book, BookUpdateDto dto) {
//        book.setTitle(dto.getTitle());
//        book.setPublicationYear(dto.getPublicationYear());
//        book.setPages(dto.getPages());
//        Author author = authorRepository.findByFullName(dto.getAuthor().split(" ")[0], dto.getAuthor().split(" ")[1])
//                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
//        Publisher publisher = publisherRepository.findByName(dto.getPublisher())
//                .orElseThrow(() -> new EntityNotFoundException("Publisher not found"));
//        Genre genre = genreRepository.findByName(dto.getGenre())
//                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
//        book.setAuthor(author);
//        book.setPublisher(publisher);
//        book.setGenre(genre);
//    }
//
//    public BookUpdateDto toBookUpdateDto(Book book) {
//        BookUpdateDto dto = new BookUpdateDto();
//        dto.setTitle(book.getTitle());
//        dto.setPublicationYear(book.getPublicationYear());
//        dto.setPages(book.getPages());
//        dto.setAuthor(book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName());
//        dto.setGenre(book.getGenre().getName());
//        dto.setPublisher(book.getPublisher().getName());
//        return dto;
//    }
}
