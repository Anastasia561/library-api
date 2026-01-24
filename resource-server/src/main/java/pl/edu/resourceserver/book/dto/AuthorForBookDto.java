package pl.edu.resourceserver.book.dto;

public record AuthorForBookDto(
        String firstName,
        String lastName,
        String penName
) {
}
