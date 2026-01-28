package pl.edu.resourceserver.book.service.impl;

import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import pl.edu.resourceserver.AbstractIntegrationTest;
import pl.edu.resourceserver.book.service.S3Properties;

import java.io.ByteArrayInputStream;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UrlGeneratorServiceImplIntegrationTest extends AbstractIntegrationTest {
    private static final String FOLDER = "test-folder";

    @Autowired
    private UrlGeneratorServiceImpl urlGeneratorService;

    @Autowired
    private S3Template s3Template;

    @Autowired
    private S3Properties s3Properties;

    @Value("${app.aws.s3.bucket}")
    private String bucketName;

    @Test
    void shouldGenerateSignedUrlForBookCover_whenInputIsValid() {
        String coverKey = FOLDER + s3Properties.getCoverKey();
        byte[] content = "dummy cover content".getBytes();
        s3Template.upload(bucketName, coverKey, new ByteArrayInputStream(content));

        URL signedUrl = urlGeneratorService.generateBookCoverURL(FOLDER);

        assertNotNull(signedUrl);
        assertTrue(signedUrl.toString().contains(coverKey));
    }

    @Test
    void shouldGenerateSignedUrlForFullBook_whenInputIsValid() {
        String bookKey = FOLDER + s3Properties.getBookKey();
        byte[] content = "dummy cover content".getBytes();
        s3Template.upload(bucketName, bookKey, new ByteArrayInputStream(content));

        URL signedUrl = urlGeneratorService.generateBookURL(FOLDER, true);

        assertNotNull(signedUrl);
        assertTrue(signedUrl.toString().contains(bookKey));
    }

    @Test
    void shouldGenerateSignedUrlForPreviewBook_whenInputIsValid() {
        String previewKey = FOLDER + s3Properties.getPreviewKey();
        byte[] content = "dummy cover content".getBytes();
        s3Template.upload(bucketName, previewKey, new ByteArrayInputStream(content));

        URL signedUrl = urlGeneratorService.generateBookURL(FOLDER, false);

        assertNotNull(signedUrl);
        assertTrue(signedUrl.toString().contains(previewKey));
    }

    @Test
    void shouldGenerateSignedUrlForFullBookDownload() {
        String bookName = "My Awesome Book";
        byte[] fullContent = "full book content".getBytes();
        String fullKey = FOLDER + s3Properties.getBookKey();

        s3Template.upload(bucketName, fullKey, new ByteArrayInputStream(fullContent));

        URL fullUrl = urlGeneratorService.generateBookDownloadURL(FOLDER, bookName, true);
        String fullQuery = fullUrl.getQuery();

        assertNotNull(fullUrl);
        assertTrue(fullUrl.toString().contains(fullKey));
        assertNotNull(fullQuery);
        assertTrue(fullQuery.contains("My_Awesome_Book_full.pdf"));
    }

    @Test
    void shouldGenerateSignedUrlForPreviewBookDownload() {
        String bookName = "My Awesome Book";
        byte[] previewContent = "preview book content".getBytes();
        String fullKey = FOLDER + s3Properties.getPreviewKey();

        s3Template.upload(bucketName, fullKey, new ByteArrayInputStream(previewContent));

        URL fullUrl = urlGeneratorService.generateBookDownloadURL(FOLDER, bookName, false);
        String fullQuery = fullUrl.getQuery();

        assertNotNull(fullUrl);
        assertTrue(fullUrl.toString().contains(fullKey));
        assertNotNull(fullQuery);
        assertTrue(fullQuery.contains("My_Awesome_Book_preview.pdf"));
    }
}
