package pl.edu.resourceserver.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.resourceserver.service.BookService;

import java.net.URL;

@RestController
@RequestMapping("/api/books")
public class UserBookController {
    private final BookService bookService;

    public UserBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{isbn}/download/full")
    public URL getFullDownloadUrl(@PathVariable String isbn) {
        return bookService.getBookDownloadUrl(isbn, true);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{isbn}/download/preview")
    public URL getPreviewDownloadUrl(@PathVariable String isbn) {
        return bookService.getBookDownloadUrl(isbn, false);
    }
}
