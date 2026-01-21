package pl.edu.authorizationserver.user.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import pl.edu.authorizationserver.AbstractControllerIntegrationTest;
import pl.edu.authorizationserver.user.dto.UserRegisterDto;
import pl.edu.authorizationserver.user.model.User;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class UserControllerTest extends AbstractControllerIntegrationTest {
    @Test
    void shouldSaveUser_whenInputIsValid() throws Exception {
        UserRegisterDto dto = new UserRegisterDto("john_doe", "ValidPass123!");

        String contentAsString = performRequest(HttpMethod.POST, "/user/register", dto)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.data").isNumber())
                .andReturn().getResponse().getContentAsString();


        Integer userId = JsonPath.read(contentAsString, "$.data");

        User user = em.find(User.class, userId);
        assertEquals("john_doe", user.getUsername());
    }

    @Test
    void shouldReturn400_whenInputIsInvalid() throws Exception {
        UserRegisterDto dto = new UserRegisterDto(null, null);

        performRequest(HttpMethod.POST, "/user/register", dto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(containsString("username: Username is required")))
                .andExpect(jsonPath("$.error")
                        .value(containsString("password: Password is required")))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }
}
