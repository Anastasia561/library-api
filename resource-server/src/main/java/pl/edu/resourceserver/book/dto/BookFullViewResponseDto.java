package pl.edu.resourceserver.book.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@Schema(description = "DTO for providing full book information")
@Getter
@Setter
public class BookFullViewResponseDto extends BookPreviewResponseDto {
    private URL fullBookDocument;
    private URL previewBookDocument;
}
