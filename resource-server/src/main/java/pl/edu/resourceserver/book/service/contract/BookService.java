package pl.edu.resourceserver.book.service.contract;

import pl.edu.resourceserver.book.dto.BookFullViewResponseDto;
import pl.edu.resourceserver.book.dto.BookUpdateDto;
import pl.edu.resourceserver.book.dto.BookUploadRequestDto;
import pl.edu.resourceserver.book.dto.BookPreviewResponseDto;

import java.net.URL;
import java.util.List;

public interface BookService {
    List<BookPreviewResponseDto> getAllPreview();

    BookPreviewResponseDto getByIsbn(String isbn);

    List<BookFullViewResponseDto> getAllFullView();

    URL getDownloadUrl(String isbn, boolean isFull);

    void delete(String isbn);

    BookPreviewResponseDto save(BookUploadRequestDto dto);

    BookPreviewResponseDto update(BookUpdateDto dto, String isbn);
}
