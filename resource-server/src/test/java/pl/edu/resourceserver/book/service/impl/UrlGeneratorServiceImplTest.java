package pl.edu.resourceserver.book.service.impl;

import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.resourceserver.book.service.S3Properties;

import java.net.URI;
import java.net.URL;
import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlGeneratorServiceImplTest {
    @Mock
    private S3Template s3Template;

    @Mock
    private S3Properties s3Properties;

    @InjectMocks
    private UrlGeneratorServiceImpl urlGeneratorService;

    @Test
    void shouldGenerateBookCoverUrl_whenInputIsValid() throws Exception {
        String folderName = "books/123";
        String coverKey = "/cover";
        String bucket = "test-bucket";
        int durationMinutes = 5;

        URL expectedUrl = URI.create("https://example.com/presigned-url").toURL();

        when(s3Properties.getCoverKey()).thenReturn(coverKey);
        when(s3Properties.getBucket()).thenReturn(bucket);
        when(s3Properties.getUrlDurationMin()).thenReturn(durationMinutes);

        when(s3Template.createSignedGetURL(bucket, folderName + coverKey, Duration.ofMinutes(durationMinutes))
        ).thenReturn(expectedUrl);

        URL result = urlGeneratorService.generateBookCoverURL(folderName);

        assertThat(result).isEqualTo(expectedUrl);
        verify(s3Template).createSignedGetURL(bucket, folderName + coverKey, Duration.ofMinutes(durationMinutes));
        verifyNoMoreInteractions(s3Template);
    }
}
