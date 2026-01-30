package pl.edu.resourceserver.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
@Tag(name = "Books", description = "Endpoints for managing books")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Get a page of books preview information")
    @GetMapping
    public ResponseWrapper<Page<BookPreviewResponseDto>> getBooks(@ParameterObject Pageable pageable) {
        return ResponseWrapper.ok(bookService.getAllPreview(pageable));
    }

    @Operation(summary = "Get book preview information by ISBN")
    @GetMapping("/{isbn}")
    public ResponseWrapper<BookPreviewResponseDto> getBookByIsbn(@PathVariable(name = "isbn") String isbn) {
        return ResponseWrapper.ok(bookService.getByIsbn(isbn));
    }

    @Operation(summary = "Get book download url for full book version")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{isbn}/download/full")
    public ResponseWrapper<URL> getFullDownloadUrl(@PathVariable(name = "isbn") String isbn) {
        return ResponseWrapper.ok(bookService.getDownloadUrl(isbn, true));
    }

    @Operation(summary = "Get book download url for preview book version")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{isbn}/download/preview")
    public ResponseWrapper<URL> getPreviewDownloadUrl(@PathVariable(name = "isbn") String isbn) {
        return ResponseWrapper.ok(bookService.getDownloadUrl(isbn, false));
    }

    @Operation(summary = "Get a page of books full information")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/info")
    public ResponseWrapper<Page<BookFullViewResponseDto>> getBooksInfo(@ParameterObject Pageable pageable) {
        return ResponseWrapper.ok(bookService.getAllFullView(pageable));
    }

    @Operation(summary = "Upload a new book")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseWrapper<BookPreviewResponseDto> upload(@ModelAttribute @Valid BookUploadRequestDto dto) {
        return ResponseWrapper.withStatus(HttpStatus.CREATED, bookService.save(dto));
    }

    @Operation(summary = "Delete book by ISBN")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{isbn}")
    public void delete(@PathVariable(name = "isbn") String isbn) {
        bookService.delete(isbn);
    }

    @Operation(summary = "Update book by ISBN")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping(path = "/{isbn}")
    public ResponseWrapper<BookPreviewResponseDto> update(@PathVariable(name = "isbn") String isbn,
                                                          @Valid @RequestBody BookUpdateDto dto) {
        return ResponseWrapper.ok(bookService.update(dto, isbn));
    }
}
