package pl.edu.resourceserver.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.resourceserver.exception.FolderNotFoundException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StorageServiceTests {
    @Mock
    private S3Client s3Client;

    @InjectMocks
    private StorageService storageService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(storageService, "bucketName", "test-bucket");
    }

    @Test
    void uploadFilesShouldUploadImageTest() {
        MultipartFile file = new MockMultipartFile(
                "cover.jpg", "cover.jpg", "image/jpeg", "image data".getBytes()
        );
        List<MultipartFile> files = List.of(file);

        storageService.uploadFiles(files, "folder1");

        verify(s3Client).putObject(
                argThat((PutObjectRequest req) ->
                        req.bucket().equals("test-bucket") &&
                                req.key().equals("folder1/cover")
                ),
                any(RequestBody.class)
        );
    }

    @Test
    void uploadFilesShouldThrowIllegalArgumentExceptionTest() {
        MultipartFile file = new MockMultipartFile(
                "book.txt", "book.txt", "text/plain", "book text".getBytes()
        );
        List<MultipartFile> files = List.of(file);

        RuntimeException exception =
                assertThrows(IllegalArgumentException.class, () -> storageService.uploadFiles(files, "folder2"));
        assertEquals("Unsupported file type: text/plain", exception.getMessage());
    }

    @Test
    void uploadFilesShouldThrowRuntimeExceptionTest() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.pdf");
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getBytes()).thenThrow(new IOException("Test IO Exception"));

        List<MultipartFile> files = List.of(file);

        RuntimeException exception =
                assertThrows(RuntimeException.class, () -> storageService.uploadFiles(files, "folder2"));
        assertEquals("Failed to upload file to S3: test.pdf", exception.getMessage());
    }

    @Test
    void deleteFolderShouldThrowExceptionWhenFolderDoesNotExistTest() {
        String folderName = "missing-folder";

        ListObjectsV2Response listResponse = ListObjectsV2Response.builder()
                .contents(List.of())
                .build();

        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(listResponse);

        FolderNotFoundException exception = assertThrows(FolderNotFoundException.class, () -> {
            storageService.deleteFolder(folderName);
        });

        assertEquals("Folder with name - missing-folder not found", exception.getMessage());
        assertTrue(exception.getMessage().contains(folderName));
    }
}
