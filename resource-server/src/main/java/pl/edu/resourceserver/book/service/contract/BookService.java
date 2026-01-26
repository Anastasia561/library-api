package pl.edu.resourceserver.book.service.contract;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.edu.resourceserver.book.dto.BookFullViewResponseDto;
import pl.edu.resourceserver.book.dto.BookUpdateDto;
import pl.edu.resourceserver.book.dto.BookUploadRequestDto;
import pl.edu.resourceserver.book.dto.BookPreviewResponseDto;

import java.net.URL;

public interface BookService {
    Page<BookPreviewResponseDto> getAllPreview(Pageable pageable);

    BookPreviewResponseDto getByIsbn(String isbn);

    Page<BookFullViewResponseDto> getAllFullView(Pageable pageable);

    URL getDownloadUrl(String isbn, boolean isFull);

    void delete(String isbn);

    BookPreviewResponseDto save(BookUploadRequestDto dto);

    BookPreviewResponseDto update(BookUpdateDto dto, String isbn);
}
