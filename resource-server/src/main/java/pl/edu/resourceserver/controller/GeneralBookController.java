package pl.edu.resourceserver.controller;

import org.springframework.web.bind.annotation.*;
import pl.edu.resourceserver.dto.BookUserResponseDto;
import pl.edu.resourceserver.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/books/public")
public class GeneralBookController {
    private final BookService bookService;

    public GeneralBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookUserResponseDto> getBooks() {
        return bookService.getAllBooksForUser().stream().toList();
    }

    @GetMapping("/{isbn}")
    public BookUserResponseDto getBookByIsbn(@PathVariable String isbn) {
        return bookService.getBookByIsbn(isbn);
    }
}
