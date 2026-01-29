package pl.edu.resourceserver.book.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import pl.edu.resourceserver.AbstractControllerIntegrationTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookControllerIntegrationTest extends AbstractControllerIntegrationTest {
    @Test
    void shouldReturnBooksPage_whenBooksExist() throws Exception {
        performRequest(HttpMethod.GET, "/api/books?page=0&size=10", null)
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
    void shouldReturnBookPreviewByIsbn_whenBooksExist() throws Exception {
        performRequest(HttpMethod.GET, "/api/books/{isbn}", null, "9780747532699")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Test"))
                .andExpect(jsonPath("$.data.isbn").value("9780747532699"))
                .andExpect(jsonPath("$.data.publicationYear").value(1997))
                .andExpect(jsonPath("$.data.pages").value(223))
                .andExpect(jsonPath("$.data.publisher").value("Bloomsbury Publishing"))
                .andExpect(jsonPath("$.data.genre").value("Fantasy"))
                .andExpect(jsonPath("$.data.coverImage").exists());
    }
}
