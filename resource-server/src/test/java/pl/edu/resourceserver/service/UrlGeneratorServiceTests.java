package pl.edu.resourceserver.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlGeneratorServiceTests {
    @Mock
    private S3Presigner presigner;

    @InjectMocks
    private UrlGeneratorService urlGeneratorService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(urlGeneratorService, "bucketName", "test-bucket");
    }

    @Test
    void generateBookCoverURLShouldReturnPresignedURLTest() throws Exception {
        PresignedGetObjectRequest presignedRequest = mock(PresignedGetObjectRequest.class);
        URL url = new URL("https://example.com/cover");
        when(presignedRequest.url()).thenReturn(url);
        when(presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(presignedRequest);

        URL result = urlGeneratorService.generateBookCoverURL("folder1");

        assertEquals(url, result);
    }

    @Test
    void generateBookDownloadURLShouldReturnPresignedURLWithFullBookTest() throws Exception {
        PresignedGetObjectRequest presignedRequest = mock(PresignedGetObjectRequest.class);
        URL url = new URL("https://example.com/book.pdf");
        when(presignedRequest.url()).thenReturn(url);
        when(presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(presignedRequest);

        URL result = urlGeneratorService.generateBookDownloadURL("folder1", "My Book", true);

        assertEquals(url, result);
    }

    @Test
    void generateBookURLShouldReturnPresignedURLForPreviewTest() throws Exception {
        PresignedGetObjectRequest presignedRequest = mock(PresignedGetObjectRequest.class);
        URL url = new URL("https://example.com/preview.pdf");
        when(presignedRequest.url()).thenReturn(url);
        when(presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(presignedRequest);

        URL result = urlGeneratorService.generateBookURL("folder1", false);

        assertEquals(url, result);
    }
}
