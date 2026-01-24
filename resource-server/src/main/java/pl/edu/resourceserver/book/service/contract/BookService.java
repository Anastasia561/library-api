package pl.edu.resourceserver.book.service.contract;

//import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import pl.edu.resourceserver.book.dto.BookFullViewResponseDto;
import pl.edu.resourceserver.book.dto.BookUploadRequestDto;
import pl.edu.resourceserver.book.dto.BookPreviewResponseDto;

import java.net.URL;
import java.util.List;

public interface BookService {
    List<BookPreviewResponseDto> getAllBooksPreview();

    BookPreviewResponseDto getBookByIsbn(String isbn);

    List<BookFullViewResponseDto> getAllBooksFullView();

    URL getBookDownloadUrl(String isbn, boolean isFull);

//    void deleteBookByIsbn(String isbn);
//
//    void saveBook(BookUploadRequestDto dto);
//
//    BookFullViewResponseDto applyMergePatchToBook(String isbn, JsonMergePatch patch);
}
