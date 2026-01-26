package pl.edu.resourceserver.book.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.resourceserver.validation.annotation.Image;
import pl.edu.resourceserver.validation.annotation.PDF;
import pl.edu.resourceserver.validation.annotation.UniqueIsbn;

public record BookUploadRequestDto(
        @Image
        MultipartFile coverImage,

        @PDF
        MultipartFile book,

        @ISBN
        @UniqueIsbn
        @NotBlank(message = "ISBN is required")
        String isbn,

        @NotBlank(message = "Title is required")
        String title,

        @Min(1400)
        @NotNull(message = "Publication year is required")
        Integer publicationYear,

        @Min(0)
        Integer pages,

        @NotNull(message = "Author is required")
        Integer author,

        @NotNull(message = "Publisher is required")
        Integer publisher,

        @NotNull(message = "Genre is required")
        Integer genre
) {
}
