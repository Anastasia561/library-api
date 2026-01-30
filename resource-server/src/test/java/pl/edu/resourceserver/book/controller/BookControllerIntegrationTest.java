package pl.edu.resourceserver.book.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pl.edu.resourceserver.AbstractControllerIntegrationTest;
import pl.edu.resourceserver.book.dto.BookUpdateDto;
import pl.edu.resourceserver.book.model.Book;

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookControllerIntegrationTest extends AbstractControllerIntegrationTest {
    @BeforeEach
    protected void setUp() {
        String folderName = "9780747532699";
        byte[] content = "Test content".getBytes();
        String fullKey = folderName + s3Properties.getBookKey();
        String coverKey = folderName + s3Properties.getCoverKey();
        String previewKey = folderName + s3Properties.getPreviewKey();

        s3Template.upload(bucketName, fullKey, new ByteArrayInputStream(content));
        s3Template.upload(bucketName, coverKey, new ByteArrayInputStream(content));
        s3Template.upload(bucketName, previewKey, new ByteArrayInputStream(content));
    }

    @AfterEach
    protected void cleanup() {
        s3Template.listObjects(bucketName, "")
                .forEach(obj -> s3Template.deleteObject(bucketName, obj.getFilename()));
    }

    @Test
    void shouldReturnBooksPage_whenBooksExist() throws Exception {
        performRequest(HttpMethod.GET, "/api/books?page=0&size=10", null, null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("Test"))
                .andExpect(jsonPath("$.data.content[0].isbn").value("9780747532699"))
                .andExpect(jsonPath("$.data.content[0].publicationYear").value(1997))
                .andExpect(jsonPath("$.data.content[0].pages").value(223))
                .andExpect(jsonPath("$.data.content[0].publisher").value("Bloomsbury Publishing"))
                .andExpect(jsonPath("$.data.content[0].genre").value("Fantasy"))
                .andExpect(jsonPath("$.data.content[0].coverImage").exists())
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    void shouldReturnBookPreviewByIsbn_whenBookExist() throws Exception {
        performRequest(HttpMethod.GET, "/api/books/{isbn}", null, null, "9780747532699")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Test"))
                .andExpect(jsonPath("$.data.isbn").value("9780747532699"))
                .andExpect(jsonPath("$.data.publicationYear").value(1997))
                .andExpect(jsonPath("$.data.pages").value(223))
                .andExpect(jsonPath("$.data.publisher").value("Bloomsbury Publishing"))
                .andExpect(jsonPath("$.data.genre").value("Fantasy"))
                .andExpect(jsonPath("$.data.coverImage").exists());
    }

    @Test
    void shouldReturn404_whenBookNotFound() throws Exception {
        performRequest(HttpMethod.GET, "/api/books/{isbn}", null, null, "9999999999999")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("Book not found"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"));
    }

    @Test
    void shouldReturn404_whenBookNotFoundForDownloadFull() throws Exception {
        performRequest(HttpMethod.GET, "/api/books/{isbn}/download/full", null, "USER",
                "9999999999999")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("Book not found"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"));
    }

    @Test
    void shouldReturn404_whenBookNotFoundForDownloadPreview() throws Exception {
        performRequest(HttpMethod.GET, "/api/books/{isbn}/download/preview", null, "USER",
                "9999999999999")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("Book not found"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"));
    }

    @Test
    void shouldReturnFullDownloadURL_whenRequestedAsUser() throws Exception {
        performRequest(HttpMethod.GET, "/api/books/{isbn}/download/full", null, "USER",
                "9780747532699")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    void shouldReturn401_whenRequestedFullDownloadWithoutAuth() throws Exception {
        performRequest(HttpMethod.GET, "/api/books/{isbn}/download/full", null, null,
                "9780747532699")
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnPreviewDownloadURL_whenRequestedAsUser() throws Exception {
        performRequest(HttpMethod.GET, "/api/books/{isbn}/download/preview", null, "USER",
                "9780747532699")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    void shouldReturn401_whenRequestedPreviewDownloadWithoutAuth() throws Exception {
        performRequest(HttpMethod.GET, "/api/books/{isbn}/download/preview", null, null,
                "9780747532699")
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnBooksInfoPage_whenBooksExist() throws Exception {
        performRequest(HttpMethod.GET, "/api/books/info?page=0&size=10", null, "LIBRARIAN")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("Test"))
                .andExpect(jsonPath("$.data.content[0].isbn").value("9780747532699"))
                .andExpect(jsonPath("$.data.content[0].publicationYear").value(1997))
                .andExpect(jsonPath("$.data.content[0].pages").value(223))
                .andExpect(jsonPath("$.data.content[0].publisher").value("Bloomsbury Publishing"))
                .andExpect(jsonPath("$.data.content[0].genre").value("Fantasy"))
                .andExpect(jsonPath("$.data.content[0].coverImage").isNotEmpty())
                .andExpect(jsonPath("$.data.content[0].previewBookDocument").isNotEmpty())
                .andExpect(jsonPath("$.data.content[0].fullBookDocument").isNotEmpty())
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    void shouldUploadBook_whenUserIsLibrarian() throws Exception {
        MockMultipartFile pdf = new MockMultipartFile("book", "book.pdf",
                MediaType.APPLICATION_PDF_VALUE, "dummy pdf content".getBytes());

        MockMultipartFile cover = new MockMultipartFile("coverImage", "cover.jpg",
                MediaType.IMAGE_JPEG_VALUE, "dummy image content".getBytes());

        mockMvc.perform(multipart("/api/books")
                        .file(pdf)
                        .file(cover)
                        .param("isbn", "9780132350884")
                        .param("title", "Test")
                        .param("publicationYear", "1900")
                        .param("pages", "30")
                        .param("publisher", "1")
                        .param("genre", "1")
                        .param("author", "1")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_LIBRARIAN"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value("Test"))
                .andExpect(jsonPath("$.data.isbn").value("9780132350884"))
                .andExpect(jsonPath("$.data.publicationYear").value(1900))
                .andExpect(jsonPath("$.data.pages").value(30))
                .andExpect(jsonPath("$.data.coverImage").exists());
    }

    @Test
    void shouldReturn401_whenRequestedBookUploadWithoutAuth() throws Exception {
        MockMultipartFile pdf = new MockMultipartFile("book", "book.pdf",
                MediaType.APPLICATION_PDF_VALUE, "dummy pdf content".getBytes());

        MockMultipartFile cover = new MockMultipartFile("coverImage", "cover.jpg",
                MediaType.IMAGE_JPEG_VALUE, "dummy image content".getBytes());

        mockMvc.perform(multipart("/api/books")
                        .file(pdf)
                        .file(cover)
                        .param("isbn", "9780132350884")
                        .param("title", "Test")
                        .param("publicationYear", "1900")
                        .param("pages", "30")
                        .param("publisher", "1")
                        .param("genre", "1")
                        .param("author", "1")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn400_whenBookUploadContainValidationErrors() throws Exception {
        MockMultipartFile pdf = new MockMultipartFile("book", "book.pdf",
                MediaType.APPLICATION_PDF_VALUE, "dummy pdf content".getBytes());

        MockMultipartFile cover = new MockMultipartFile("coverImage", "cover.jpg",
                MediaType.IMAGE_JPEG_VALUE, "dummy image content".getBytes());

        mockMvc.perform(multipart("/api/books")
                        .file(pdf)
                        .file(cover)
                        .param("isbn", "")
                        .param("title", "")
                        .param("publicationYear", "1300")
                        .param("pages", "30")
                        .param("publisher", "1")
                        .param("genre", "1")
                        .param("author", "1")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_LIBRARIAN"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error.message").value("Validation failed"))
                .andExpect(jsonPath("$.error.validationErrors").isArray())
                .andExpect(jsonPath("$.error.validationErrors.length()").value(4));
    }

    @Test
    void shouldReturn404_whenBookNotFoundForDelete() throws Exception {
        performRequest(HttpMethod.GET, "/api/books/{isbn}", null, "LIBRARIAN",
                "9999999999999")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("Book not found"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"));
    }

    @Test
    void shouldDeleteBook_whenUserIsLibrarian() throws Exception {
        String isbn = "9780747532699";

        performRequest(HttpMethod.DELETE, "/api/books/{isbn}", null, "LIBRARIAN", isbn)
                .andExpect(status().isNoContent());

        Book deleted = em.find(Book.class, isbn);
        assertThat(deleted).isNull();
    }

    @Test
    void shouldReturn404_whenBookNotFoundForUpdate() throws Exception {
        BookUpdateDto dto = new BookUpdateDto("Test", 1990, 30,
                1, 1, 1);
        performRequest(HttpMethod.PUT, "/api/books/{isbn}", dto, "LIBRARIAN",
                "9999999999999")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("Book not found"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"));
    }

    @Test
    void shouldReturn400_whenBookUpdateContainValidationErrors() throws Exception {
        BookUpdateDto dto = new BookUpdateDto(null, 0, 30,
                1, 1, 1);

        performRequest(HttpMethod.PUT, "/api/books/{isbn}", dto, "LIBRARIAN",
                "9780747532699")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error.message").value("Validation failed"))
                .andExpect(jsonPath("$.error.validationErrors").isArray())
                .andExpect(jsonPath("$.error.validationErrors.length()").value(2));
    }

    @Test
    void shouldUploadBook_whenRequestedAsLibrarian() throws Exception {
        BookUpdateDto dto = new BookUpdateDto("Test", 1990, 20,
                1, 1, 1);

        performRequest(HttpMethod.PUT, "/api/books/{isbn}", dto, "LIBRARIAN",
                "9780747532699")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Test"))
                .andExpect(jsonPath("$.data.isbn").value("9780747532699"))
                .andExpect(jsonPath("$.data.publicationYear").value(1990))
                .andExpect(jsonPath("$.data.pages").value(20))
                .andExpect(jsonPath("$.data.coverImage").exists());
    }
}
