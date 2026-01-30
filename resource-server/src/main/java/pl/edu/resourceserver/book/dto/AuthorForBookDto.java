package pl.edu.resourceserver.book.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO for providing author information for a book")
public record AuthorForBookDto(
        String firstName,
        String lastName,
        String penName
) {
}
