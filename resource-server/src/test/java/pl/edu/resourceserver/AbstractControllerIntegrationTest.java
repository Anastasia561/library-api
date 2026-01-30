package pl.edu.resourceserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public abstract class AbstractControllerIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected EntityManager em;

    protected ResultActions performRequest(HttpMethod method, String url, Object body, String role,
                                           Object... uriVars) throws Exception {
        MockHttpServletRequestBuilder requestBuilder;

        if (method == GET) {
            requestBuilder = get(url, uriVars);
        } else if (method == POST) {
            requestBuilder = post(url, uriVars);
        } else if (method == PUT) {
            requestBuilder = put(url, uriVars);
        } else if (method == DELETE) {
            requestBuilder = delete(url, uriVars);
        } else {
            throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        if (body != null) {
            requestBuilder
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body));
        }

        if (role != null) {
            requestBuilder.with(jwt()
                    .authorities(new SimpleGrantedAuthority("ROLE_" + role))
            );
        }

        return mockMvc.perform(requestBuilder);
    }
}
