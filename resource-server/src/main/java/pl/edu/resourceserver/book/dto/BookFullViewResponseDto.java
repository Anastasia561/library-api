package pl.edu.resourceserver.book.dto;

import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@Getter
@Setter
public class BookFullViewResponseDto extends BookPreviewResponseDto {
    private URL fullBookDocument;
    private URL previewBookDocument;
}
