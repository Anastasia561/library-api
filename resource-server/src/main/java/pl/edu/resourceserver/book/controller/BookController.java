package pl.edu.resourceserver.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.resourceserver.book.dto.BookPreviewResponseDto;
import pl.edu.resourceserver.book.service.contract.BookService;

import java.net.URL;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<BookPreviewResponseDto> getBooks() {
        return bookService.getAllBooksPreview();
    }

    @GetMapping("/{isbn}")
    public BookPreviewResponseDto getBookByIsbn(@PathVariable(name = "isbn") String isbn) {
        return bookService.getBookByIsbn(isbn);
    }

    //user
    @GetMapping("/{isbn}/download/full")
    public URL getFullDownloadUrl(@PathVariable(name = "isbn") String isbn) {
        return bookService.getBookDownloadUrl(isbn, true);
    }

    //user
    @GetMapping("/{isbn}/download/preview")
    public URL getPreviewDownloadUrl(@PathVariable(name = "isbn") String isbn) {
        return bookService.getBookDownloadUrl(isbn, false);
    }

    //    @PreAuthorize("hasRole('LIBRARIAN')")
//    @GetMapping("/info")
//    public List<BookFullViewResponseDto> getBooksInfo() {
//        return bookService.getAllBooksForLibrarian().stream().toList();
//    }
//
//    @PreAuthorize("hasRole('LIBRARIAN')")
//    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<String> uploadBook(@ModelAttribute @Valid BookUploadRequestDto dto) {
//        bookService.saveBook(dto);
//        return ResponseEntity.ok().build();
//    }
//
//    @PreAuthorize("hasRole('LIBRARIAN')")
//    @DeleteMapping("/{isbn}")
//    public ResponseEntity<String> deleteBook(@PathVariable String isbn) {
//        bookService.deleteBookByIsbn(isbn);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PreAuthorize("hasRole('LIBRARIAN')")
//    @PatchMapping(path = "/{isbn}/update")
//    public ResponseEntity<BookFullViewResponseDto> mergePatchBook(@PathVariable String isbn,
//                                                                  @RequestBody JsonMergePatch patch) {
//        BookFullViewResponseDto patchedBook = bookService.applyMergePatchToBook(isbn, patch);
//        return ResponseEntity.ok(patchedBook);
//    }
}
