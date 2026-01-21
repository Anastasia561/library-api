package pl.edu.authorizationserver.client.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import pl.edu.authorizationserver.client.dto.ClientRegisterDto;
import pl.edu.authorizationserver.client.model.AuthMethod;
import pl.edu.authorizationserver.client.model.Client;
import pl.edu.authorizationserver.client.model.GrantType;
import pl.edu.authorizationserver.client.model.RedirectUri;
import pl.edu.authorizationserver.client.model.Scope;

import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientMapperImplTest {
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientMapper clientMapper;

    @Test
    void shouldReturnNull_whenRegisteredClientIsNull() {
        assertNull(clientMapper.toClientRegisterDto(null));
    }

    @Test
    void shouldMapRegisteredClientToClientRegisterDto_whenInputIsValid() {
        RegisteredClient registeredClient = RegisteredClient.withId("id-123")
                .clientId("test-client")
                .clientSecret("secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://localhost:8080/login/oauth2/code/test")
                .scope("openid")
                .scope("read")
                .build();

        ClientRegisterDto dto = clientMapper.toClientRegisterDto(registeredClient);

        assertNotNull(dto);
        assertEquals("test-client", dto.idClient());
        assertEquals("secret", dto.secret());
        assertEquals(Set.of(AuthMethod.CLIENT_SECRET_BASIC), dto.authMethods());
        assertEquals(Set.of(GrantType.AUTHORIZATION_CODE, GrantType.REFRESH_TOKEN), dto.grantTypes());
        assertEquals(Set.of("openid", "read"), dto.scopes());
        assertEquals(Set.of("http://localhost:8080/login/oauth2/code/test"), dto.redirectUris());
    }

    @Test
    void shouldReturnNull_whenClientRegisterDtoIsNull() {
        assertNull(clientMapper.toClient(null));
    }

    @Test
    void shouldMapClientRegisterDtoToClient_whenInputIsValid() {
        ClientRegisterDto dto = new ClientRegisterDto("test-client", "test-secret", Set.of(AuthMethod.CLIENT_SECRET_BASIC),
                Set.of(GrantType.AUTHORIZATION_CODE, GrantType.REFRESH_TOKEN), Set.of("openid", "read"),
                Set.of("http://localhost:8080/login/oauth2/code/test"));
        when(passwordEncoder.encode("test-secret")).thenReturn("encoded-secret");

        Client client = clientMapper.toClient(dto);

        assertNotNull(client);
        assertEquals("test-client", client.getIdClient());
        assertEquals("encoded-secret", client.getSecret());
        assertEquals(Set.of(AuthMethod.CLIENT_SECRET_BASIC), client.getAuthMethods());
        assertEquals(Set.of(GrantType.AUTHORIZATION_CODE, GrantType.REFRESH_TOKEN), client.getGrantTypes());
        assertEquals("http://localhost:8080/login/oauth2/code/test", client.getRedirectUris()
                .stream().findFirst().get().getName());
    }

    @Test
    void shouldMapToRegisteredClientFromClient_whenInputIsValid() {
        Client client = new Client();
        client.setId("test-id");
        client.setIdClient("test-client");
        client.setSecret("secret123");
        Scope scope1 = new Scope();
        Scope scope2 = new Scope();
        scope1.setName("read");
        scope2.setName("write");
        client.setScopes(Set.of(scope1, scope2));

        GrantType grant1 = GrantType.AUTHORIZATION_CODE;
        GrantType grant2 = GrantType.REFRESH_TOKEN;
        client.setGrantTypes(Set.of(grant1, grant2));

        AuthMethod method1 = AuthMethod.CLIENT_SECRET_BASIC;
        client.setAuthMethods(Set.of(method1));

        RedirectUri uri1 = new RedirectUri();
        uri1.setName("http://localhost/callback");
        client.setRedirectUris(Set.of(uri1));


        RegisteredClient registeredClient = clientMapper.toRegisteredClientFromClient(client);

        assertNotNull(registeredClient);
        assertEquals("test-id", registeredClient.getId());
        assertEquals("test-client", registeredClient.getClientId());
        assertEquals("secret123", registeredClient.getClientSecret());

        assertTrue(registeredClient.getScopes().contains("read"));
        assertTrue(registeredClient.getScopes().contains("write"));

        assertTrue(registeredClient.getAuthorizationGrantTypes()
                .contains(new AuthorizationGrantType("AUTHORIZATION_CODE")));
        assertTrue(registeredClient.getAuthorizationGrantTypes()
                .contains(new AuthorizationGrantType("REFRESH_TOKEN")));

        assertTrue(registeredClient.getClientAuthenticationMethods()
                .contains(new ClientAuthenticationMethod("CLIENT_SECRET_BASIC")));

        assertTrue(registeredClient.getRedirectUris().contains("http://localhost/callback"));
    }

    @Test
    void shouldReturnNull_whenClientIsNull() {
        assertNull(clientMapper.toRegisteredClientFromClient(null));
    }
}
