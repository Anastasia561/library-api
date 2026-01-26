package pl.edu.resourceserver.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.resourceserver.book.dto.BookFullViewResponseDto;
import pl.edu.resourceserver.book.dto.BookPreviewResponseDto;
import pl.edu.resourceserver.book.dto.BookUpdateDto;
import pl.edu.resourceserver.book.dto.BookUploadRequestDto;
import pl.edu.resourceserver.book.service.contract.BookService;
import pl.edu.resourceserver.wrapper.ResponseWrapper;

import java.net.URL;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseWrapper<Page<BookPreviewResponseDto>> getBooks(Pageable pageable) {
        return ResponseWrapper.ok(bookService.getAllPreview(pageable));
    }

    @GetMapping("/{isbn}")
    public ResponseWrapper<BookPreviewResponseDto> getBookByIsbn(@PathVariable(name = "isbn") String isbn) {
        return ResponseWrapper.ok(bookService.getByIsbn(isbn));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{isbn}/download/full")
    public ResponseWrapper<URL> getFullDownloadUrl(@PathVariable(name = "isbn") String isbn) {
        return ResponseWrapper.ok(bookService.getDownloadUrl(isbn, true));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{isbn}/download/preview")
    public ResponseWrapper<URL> getPreviewDownloadUrl(@PathVariable(name = "isbn") String isbn) {
        return ResponseWrapper.ok(bookService.getDownloadUrl(isbn, false));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/info")
    public ResponseWrapper<Page<BookFullViewResponseDto>> getBooksInfo(Pageable pageable) {
        return ResponseWrapper.ok(bookService.getAllFullView(pageable));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseWrapper<BookPreviewResponseDto> upload(@ModelAttribute @Valid BookUploadRequestDto dto) {
        return ResponseWrapper.withStatus(HttpStatus.CREATED, bookService.save(dto));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{isbn}")
    public void delete(@PathVariable(name = "isbn") String isbn) {
        bookService.delete(isbn);
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping(path = "/{isbn}")
    public ResponseWrapper<BookPreviewResponseDto> update(@PathVariable(name = "isbn") String isbn,
                                                          @Valid @RequestBody BookUpdateDto dto) {
        return ResponseWrapper.ok(bookService.update(dto, isbn));
    }
}
