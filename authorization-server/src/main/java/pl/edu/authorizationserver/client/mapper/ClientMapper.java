package pl.edu.authorizationserver.client.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Component;
import pl.edu.authorizationserver.client.model.AuthMethod;
import pl.edu.authorizationserver.client.model.Client;
import pl.edu.authorizationserver.client.dto.ClientRegisterDto;
import pl.edu.authorizationserver.client.model.GrantType;
import pl.edu.authorizationserver.client.model.RedirectUri;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClientMapper {
    private static final Map<GrantType, AuthorizationGrantType> grantTypeMap = Map.of(
            GrantType.AUTHORIZATION_CODE, AuthorizationGrantType.AUTHORIZATION_CODE,
            GrantType.REFRESH_TOKEN, AuthorizationGrantType.REFRESH_TOKEN,
            GrantType.CLIENT_CREDENTIALS, AuthorizationGrantType.CLIENT_CREDENTIALS
    );

    private static final Map<AuthMethod, ClientAuthenticationMethod> authMethodMap = Map.of(
            AuthMethod.CLIENT_SECRET_BASIC, ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
            AuthMethod.CLIENT_SECRET_POST, ClientAuthenticationMethod.CLIENT_SECRET_POST,
            AuthMethod.NONE, ClientAuthenticationMethod.NONE
    );

    private final PasswordEncoder passwordEncoder;

    public ClientRegisterDto toClientRegisterDto(RegisteredClient registeredClient) {
        if (registeredClient == null) return null;

        Set<AuthMethod> authMethods = registeredClient.getClientAuthenticationMethods().stream()
                .map(a -> AuthMethod.valueOf(a.getValue().toUpperCase()))
                .collect(Collectors.toSet());


        Set<GrantType> grantTypes = registeredClient.getAuthorizationGrantTypes().stream()
                .map(g -> GrantType.valueOf(g.getValue().toUpperCase()))
                .collect(Collectors.toSet());


        Set<String> scopes = registeredClient.getScopes();
        Set<String> redirectUris = registeredClient.getRedirectUris();

        return new ClientRegisterDto(
                registeredClient.getClientId(),
                registeredClient.getClientSecret(),
                authMethods,
                grantTypes,
                scopes,
                redirectUris
        );
    }

    public Client toClient(ClientRegisterDto dto) {
        if (dto == null) return null;
        Client client = new Client();
        client.setId(UUID.randomUUID().toString());
        client.setAuthMethods(dto.authMethods());
        client.setGrantTypes(dto.grantTypes());
        client.setIdClient(dto.idClient());
        if (dto.secret() != null && !dto.secret().isBlank()) {
            client.setSecret(passwordEncoder.encode(dto.secret()));
        } else {
            client.setSecret(null);
        }

        client.setRedirectUris(dto.redirectUris().stream().map(u ->
                {
                    RedirectUri redirectUri = new RedirectUri();
                    redirectUri.setName(u);
                    redirectUri.setClient(client);
                    return redirectUri;
                }
        ).collect(Collectors.toSet()));
        return client;
    }

    public RegisteredClient toRegisteredClientFromClient(Client client) {
        if (client == null) return null;

        RegisteredClient.Builder builder =
                RegisteredClient.withId(client.getId())
                        .clientId(client.getIdClient())

                        .scopes(scopes ->
                                client.getScopes()
                                        .forEach(s -> scopes.add(s.getName())))

                        .authorizationGrantTypes(grants ->
                                client.getGrantTypes()
                                        .forEach(gt -> grants.add(grantTypeMap.get(gt))))

                        .clientAuthenticationMethods(methods ->
                                client.getAuthMethods()
                                        .forEach(am -> methods.add(authMethodMap.get(am))))

                        .redirectUris(uris ->
                                client.getRedirectUris()
                                        .forEach(r -> uris.add(r.getName())));

        if (client.getSecret() != null) {
            builder.clientSecret(client.getSecret());
        }

        return builder.build();
    }
}
