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
import pl.edu.authorizationserver.client.model.Scope;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClientMapper {
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
        client.setSecret(passwordEncoder.encode(dto.secret()));
        client.setIdClient(dto.idClient());

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

        return RegisteredClient.withId(String.valueOf(client.getId()))
                .clientId(client.getIdClient())
                .clientSecret(client.getSecret())
                .scopes(scopes -> scopes.addAll(client.getScopes()
                        .stream()
                        .map(Scope::getName)
                        .collect(Collectors.toSet())))
                .authorizationGrantTypes(types -> types.addAll(client.getGrantTypes()
                        .stream()
                        .map(t -> new AuthorizationGrantType(t.name()))
                        .collect(Collectors.toSet())))
                .clientAuthenticationMethods(methods -> methods.addAll(client.getAuthMethods()
                        .stream()
                        .map(m -> new ClientAuthenticationMethod(m.name()))
                        .collect(Collectors.toSet())))
                .redirectUris(redirectUris -> redirectUris.addAll(client.getRedirectUris()
                        .stream()
                        .map(RedirectUri::getName)
                        .collect(Collectors.toSet())))
                .build();
    }
}

