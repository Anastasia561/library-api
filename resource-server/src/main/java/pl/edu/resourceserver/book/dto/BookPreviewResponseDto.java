package pl.edu.resourceserver.book.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@Schema(description = "DTO for providing preview book information")
@Getter
@Setter
public class BookPreviewResponseDto {
    protected String isbn;
    protected String title;
    protected int publicationYear;
    protected int pages;
    protected AuthorForBookDto author;
    protected String publisher;
    protected String genre;
    protected URL coverImage;
}
