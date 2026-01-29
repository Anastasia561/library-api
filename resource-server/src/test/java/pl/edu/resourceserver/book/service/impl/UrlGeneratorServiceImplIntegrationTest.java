package pl.edu.resourceserver.book.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.resourceserver.AbstractIntegrationTest;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UrlGeneratorServiceImplIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private UrlGeneratorServiceImpl urlGeneratorService;

    @Test
    void shouldGenerateSignedUrlForBookCover_whenInputIsValid() {
        String coverKey = FOLDER + s3Properties.getCoverKey();
        URL signedUrl = urlGeneratorService.generateBookCoverURL(FOLDER);

        assertNotNull(signedUrl);
        assertTrue(signedUrl.toString().contains(coverKey));
    }

    @Test
    void shouldGenerateSignedUrlForFullBook_whenInputIsValid() {
        String bookKey = FOLDER + s3Properties.getBookKey();
        URL signedUrl = urlGeneratorService.generateBookURL(FOLDER, true);

        assertNotNull(signedUrl);
        assertTrue(signedUrl.toString().contains(bookKey));
    }

    @Test
    void shouldGenerateSignedUrlForPreviewBook_whenInputIsValid() {
        String previewKey = FOLDER + s3Properties.getPreviewKey();
        URL signedUrl = urlGeneratorService.generateBookURL(FOLDER, false);

        assertNotNull(signedUrl);
        assertTrue(signedUrl.toString().contains(previewKey));
    }

    @Test
    void shouldGenerateSignedUrlForFullBookDownload() {
        String bookName = "My Awesome Book";
        String fullKey = FOLDER + s3Properties.getBookKey();

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
        String fullKey = FOLDER + s3Properties.getPreviewKey();

        URL fullUrl = urlGeneratorService.generateBookDownloadURL(FOLDER, bookName, false);
        String fullQuery = fullUrl.getQuery();

        assertNotNull(fullUrl);
        assertTrue(fullUrl.toString().contains(fullKey));
        assertNotNull(fullQuery);
        assertTrue(fullQuery.contains("My_Awesome_Book_preview.pdf"));
    }
}
