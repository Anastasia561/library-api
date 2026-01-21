package pl.edu.authorizationserver.client.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import pl.edu.authorizationserver.AbstractControllerIntegrationTest;
import pl.edu.authorizationserver.client.dto.ClientRegisterDto;
import pl.edu.authorizationserver.client.model.AuthMethod;
import pl.edu.authorizationserver.client.model.Client;
import pl.edu.authorizationserver.client.model.GrantType;

import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ClientControllerTest extends AbstractControllerIntegrationTest {
    @Test
    void shouldSaveClient_whenInputIsValid() throws Exception {
        ClientRegisterDto dto = new ClientRegisterDto("new-client", "test-secret",
                Set.of(AuthMethod.CLIENT_SECRET_BASIC),
                Set.of(GrantType.AUTHORIZATION_CODE, GrantType.REFRESH_TOKEN),
                Set.of("openid"),
                Set.of("http://localhost:8080/login/oauth2/code/test"));

        String contentAsString = performRequest(HttpMethod.POST, "/client/register", dto)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.data").isString())
                .andReturn().getResponse().getContentAsString();

        String clientId = JsonPath.read(contentAsString, "$.data");

        Client client = em.find(Client.class, clientId);
        assertEquals("new-client", client.getIdClient());
        assertEquals(1, client.getAuthMethods().size());
        assertEquals(2, client.getGrantTypes().size());
        assertEquals(1, client.getScopes().size());
        assertEquals("http://localhost:8080/login/oauth2/code/test", client.getRedirectUris()
                .stream().findFirst().get().getName());
    }

    @Test
    void shouldReturn400_whenInputIsInvalid() throws Exception {
        ClientRegisterDto dto = new ClientRegisterDto(null, null, null,
                null, null, null);

        performRequest(HttpMethod.POST, "/client/register", dto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(containsString("idClient: client id can not be empty")))
                .andExpect(jsonPath("$.error")
                        .value(containsString("secret: client secret can not be empty")))
                .andExpect(jsonPath("$.error")
                        .value(containsString("authMethods: must provide at least one authentication method")))
                .andExpect(jsonPath("$.error")
                        .value(containsString("grantTypes: must provide at least one grant type")))
                .andExpect(jsonPath("$.error")
                        .value(containsString("scopes: must provide at least one scope")))
                .andExpect(jsonPath("$.error")
                        .value(containsString("redirectUris: must provide at least one redirect uri")))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }
}
