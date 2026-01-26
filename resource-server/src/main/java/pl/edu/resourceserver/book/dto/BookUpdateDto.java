package pl.edu.resourceserver.book.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookUpdateDto(
        @NotBlank(message = "Title is required")
        String title,

        @Min(value = 1400, message = "Publication year must be 1400 or later")
        @NotNull(message = "Publication year is required")
        Integer publicationYear,

        @Min(value = 0, message = "Pages cannot be negative")
        @NotNull(message = "Pages are required")
        Integer pages,

        @NotNull(message = "Author is required")
        Integer author,

        @NotNull(message = "Publisher is required")
        Integer publisher,

        @NotNull(message = "Genre is required")
        Integer genre
) {
}
