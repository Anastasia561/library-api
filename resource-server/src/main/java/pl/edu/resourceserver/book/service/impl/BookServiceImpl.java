package pl.edu.resourceserver.book.service.impl;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.github.fge.jsonpatch.JsonPatchException;
//import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.resourceserver.book.service.contract.BookService;
import pl.edu.resourceserver.book.dto.BookFullViewResponseDto;
import pl.edu.resourceserver.book.dto.BookUpdateDto;
import pl.edu.resourceserver.book.dto.BookUploadRequestDto;
import pl.edu.resourceserver.book.dto.BookPreviewResponseDto;
import pl.edu.resourceserver.book.model.Book;
import pl.edu.resourceserver.book.mapper.BookMapper;
import pl.edu.resourceserver.book.repository.BookRepository;
import pl.edu.resourceserver.book.service.contract.StorageService;
import pl.edu.resourceserver.book.service.contract.UrlGeneratorService;
import tools.jackson.databind.ObjectMapper;

import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final StorageService storageService;
    private final UrlGeneratorService urlGeneratorService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Override
    public List<BookPreviewResponseDto> getAllBooksPreview() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookPreviewResponseDto)
                .peek(b -> b.setCoverImage(urlGeneratorService.generateBookCoverURL(b.getIsbn())))
                .collect(Collectors.toList());
    }

    @Override
    public BookPreviewResponseDto getBookByIsbn(String isbn) {
        BookPreviewResponseDto book = bookRepository.findByIsbn(isbn)
                .map(bookMapper::toBookPreviewResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        book.setCoverImage(urlGeneratorService.generateBookCoverURL(book.getIsbn()));
        return book;
    }

    @Override
    public List<BookFullViewResponseDto> getAllBooksFullView() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookFullViewResponseDto)
                .peek(b -> {
                    b.setCoverImage(urlGeneratorService.generateBookCoverURL(b.getIsbn()));
                    b.setFullBookDocument(urlGeneratorService.generateBookURL(b.getIsbn(), true));
                    b.setPreviewBookDocument(urlGeneratorService.generateBookURL(b.getIsbn(), false));
                })
                .collect(Collectors.toList());
    }

    @Override
    public URL getBookDownloadUrl(String isbn, boolean isFull) {
        String title = bookRepository.findByIsbn(isbn)
                .map(Book::getTitle)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        return urlGeneratorService.generateBookDownloadURL(isbn, title, isFull);
    }
//
//    @Override
//    @Transactional
//    public void deleteBookByIsbn(String isbn) {
//        bookRepository.findByIsbn(isbn)
//                .ifPresentOrElse(
//                        book -> {
//                            bookRepository.delete(book);
//                            try {
//                                storageService.deleteFolder(isbn);
//                            } catch (Exception e) {
//                                throw new RuntimeException("Failed to delete folder for isbn : " + isbn);
//                            }
//                        },
//                        () -> {
//                            throw new EntityNotFoundException("Book with isbn - " + isbn + " not found");
//                        }
//                );
//    }
//
//@Override
//@Transactional
//public void saveBook(BookUploadRequestDto dto) {
//    bookRepository.save(bookMapper.toBook(dto));
//    try {
//        storageService.uploadFiles(List.of(dto.getBook(), dto.getCoverImage()), dto.getIsbn());
//    } catch (Exception e) {
//        throw new RuntimeException("Failed to delete folder for isbn : " + dto.getIsbn());
//    }
//}
////
//    @Override
//    public BookFullViewResponseDto applyMergePatchToBook(String isbn, JsonMergePatch patch) {
//        BookUpdateDto bookUpdateDto = bookRepository.findByIsbn(isbn).map(bookMapper::toBookUpdateDto)
//                .orElseThrow(() -> new EntityNotFoundException("Book with isbn - " + isbn + " not found"));
//
//        JsonNode bookNode = objectMapper.valueToTree(bookUpdateDto);
//
//        try {
//            JsonNode patched = patch.apply(bookNode);
//            BookUpdateDto patchedBook = objectMapper.treeToValue(patched, BookUpdateDto.class);
//
//            Set<ConstraintViolation<BookUpdateDto>> violations = validator.validate(patchedBook);
//            if (!violations.isEmpty()) {
//                throw new ConstraintViolationException(violations);
//            }
//            updateBook(isbn, patchedBook);
//            BookFullViewResponseDto dto = bookRepository.findByIsbn(isbn).map(bookMapper::toBookLibrarianResponseDto)
//                    .orElseThrow(() -> new EntityNotFoundException("Book with isbn - " + isbn + " not found"));
//            dto.setCoverImage(urlGeneratorService.generateBookCoverURL(isbn));
//            dto.setFullBookDocument(urlGeneratorService.generateBookURL(isbn, true));
//            dto.setPreviewBookDocument(urlGeneratorService.generateBookURL(isbn, false));
//            return dto;
//        } catch (JsonProcessingException | JsonPatchException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private void updateBook(String isbn, BookUpdateDto dto) {
//        Book book = bookRepository.findByIsbn(isbn)
//                .orElseThrow(() -> new EntityNotFoundException("Book with isbn - " + isbn + " not found"));
//        bookMapper.updateBook(book, dto);
//        bookRepository.save(book);
//    }

}
