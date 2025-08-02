package pl.edu.resourceserver.controller;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.resourceserver.dto.BookLibrarianResponseDto;
import pl.edu.resourceserver.dto.BookUploadRequestDto;
import pl.edu.resourceserver.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class LibrarianBookController {
    private final BookService bookService;

    public LibrarianBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/info")
    public List<BookLibrarianResponseDto> getBooksInfo() {
        return bookService.getAllBooksForLibrarian().stream().toList();
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadBook(@ModelAttribute @Valid BookUploadRequestDto dto) {
        bookService.saveBook(dto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/{isbn}")
    public ResponseEntity<String> deleteBook(@PathVariable String isbn) {
        bookService.deleteBookByIsbn(isbn);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PatchMapping(path = "/{isbn}/update")
    public ResponseEntity<BookLibrarianResponseDto> mergePatchBook(@PathVariable String isbn,
                                                                   @RequestBody JsonMergePatch patch) {
        BookLibrarianResponseDto patchedBook = bookService.applyMergePatchToBook(isbn, patch);
        return ResponseEntity.ok(patchedBook);
    }
}
