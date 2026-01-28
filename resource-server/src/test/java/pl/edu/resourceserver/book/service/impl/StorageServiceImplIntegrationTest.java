package pl.edu.resourceserver.book.service.impl;

import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.resourceserver.AbstractIntegrationTest;
import pl.edu.resourceserver.exception.FolderNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageServiceImplIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private S3Template s3Template;

    @Autowired
    private StorageServiceImpl storageService;

    @Value("${app.aws.s3.bucket}")
    private String bucketName;

    @AfterEach
    protected void cleanup() {
        s3Template.listObjects(bucketName, "")
                .forEach(obj -> s3Template.deleteObject(bucketName, obj.getFilename()));
    }

    @Test
    void shouldUploadFilesToS3_whenInputIsValid() {
        String folderName = "test-folder";
        List<MultipartFile> files = List.of(
                new MockMultipartFile("file1.txt", "file1.txt",
                        "application/pdf", "Hello".getBytes()),
                new MockMultipartFile("file2.txt", "file2.txt",
                        "image/jpg", "World".getBytes())
        );

        storageService.uploadFiles(files, folderName);

        String expectedBookFileName = "test-folder/book.pdf";
        String expectedImageFileName = "test-folder/cover.png";

        boolean bookExists = s3Template.listObjects(bucketName, folderName + "/")
                .stream()
                .anyMatch(res -> res.getFilename().equals(expectedBookFileName));

        boolean coverExists = s3Template.listObjects(bucketName, folderName + "/")
                .stream()
                .anyMatch(res -> res.getFilename().equals(expectedImageFileName));

        assertTrue(bookExists);
        assertTrue(coverExists);
    }

    @Test
    void shouldDeleteFolderFromS3_whenFolderExists() {
        String folderName = "test-folder";
        List<MultipartFile> files = List.of(
                new MockMultipartFile("book.pdf", "book.pdf",
                        "application/pdf", "PDF Content".getBytes()));

        storageService.uploadFiles(files, folderName);

        storageService.deleteFolder(folderName);

        assertTrue(s3Template.listObjects(bucketName, folderName).isEmpty());
    }

    @Test
    void shouldThrowFolderNotFoundException_whenFolderNotFound() {
        String nonExistentFolder = "non-existent-folder";

        FolderNotFoundException exception = assertThrows(
                FolderNotFoundException.class,
                () -> storageService.deleteFolder(nonExistentFolder)
        );

        assertEquals("Folder with name - " + nonExistentFolder + " not found", exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentException_whenUnsupportedFileType() {
        List<MultipartFile> files = List.of(
                new MockMultipartFile("text.txt", "text.txt",
                        "text/plain", "Some text".getBytes())
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storageService.uploadFiles(files, "test-folder")
        );

        assertEquals("Unsupported file type: text/plain", exception.getMessage());
    }
}
