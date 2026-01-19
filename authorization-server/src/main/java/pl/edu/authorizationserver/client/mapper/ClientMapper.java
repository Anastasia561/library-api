package pl.edu.authorizationserver.client.mapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Component;
import pl.edu.authorizationserver.client.model.Client;
import pl.edu.authorizationserver.client.model.RedirectUri;
import pl.edu.authorizationserver.client.model.Scope;
import pl.edu.authorizationserver.client.dto.ClientRegisterDto;
import pl.edu.authorizationserver.client.repository.AuthMethodRepository;
import pl.edu.authorizationserver.client.repository.GrantTypeRepository;
import pl.edu.authorizationserver.client.repository.ScopeRepository;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClientMapper {
    private final PasswordEncoder passwordEncoder;
    private final AuthMethodRepository authMethodRepository;
    private final GrantTypeRepository grantTypeRepository;
    private final ScopeRepository scopeRepository;

    public Client toClient(RegisteredClient registeredClient) {
        Client client = new Client();
        client.setId(registeredClient.getId());
        client.setIdClient(registeredClient.getClientId());
        client.setSecret(passwordEncoder.encode(registeredClient.getClientSecret()));

        client.setAuthMethods(registeredClient.getClientAuthenticationMethods().stream()
                .map(a -> {
                    return authMethodRepository.findByName(a.getValue())
                            .orElseThrow(() ->
                                    new EntityNotFoundException("Auth Method " + a.getValue() + " not found"));
                })
                .collect(Collectors.toSet()));

        client.setScopes(registeredClient.getScopes().stream()
                .map(s -> {
                    return scopeRepository.findByName(s)
                            .orElseGet(() -> {
                                Scope scope = new Scope();
                                scope.setName(s);
                                scopeRepository.save(scope);
                                return scope;
                            });
                })
                .collect(Collectors.toSet()));

        client.setGrantTypes(registeredClient.getAuthorizationGrantTypes().stream()
                .map(g -> {
                    return grantTypeRepository.findByName(g.getValue())
                            .orElseThrow(() ->
                                    new EntityNotFoundException("Grant type " + g.getValue() + " not found"));
                })
                .collect(Collectors.toSet()));

        Set<RedirectUri> uris = registeredClient.getRedirectUris().stream()
                .map(u -> {
                    RedirectUri redirectUri = new RedirectUri();
                    redirectUri.setClient(client);
                    redirectUri.setName(u);
                    return redirectUri;
                })
                .collect(Collectors.toSet());

        client.setRedirectUris(uris);
        return client;
    }

    public RegisteredClient toRegisteredClient(ClientRegisterDto dto) {
        return RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(dto.idClient())
                .clientSecret(dto.secret())
                .scopes(scopes -> scopes.addAll(
                        dto.scopes()))
                .authorizationGrantTypes(types -> types.addAll(
                        dto.grantTypes().stream()
                                .map(AuthorizationGrantType::new)
                                .collect(Collectors.toSet())))

                .clientAuthenticationMethods(methods -> methods.addAll(
                        dto.authMethods()
                                .stream()
                                .map(ClientAuthenticationMethod::new)
                                .collect(Collectors.toSet())
                ))

                .redirectUris(redirectUris -> redirectUris.addAll(
                        dto.redirectUris()
                ))
                .build();
    }

    public RegisteredClient toRegisteredClient(Client client) {
        return RegisteredClient.withId(String.valueOf(client.getId()))
                .clientId(client.getIdClient())
                .clientSecret(client.getSecret())
                .scopes(scopes -> scopes.addAll(
                        client.getScopes()
                                .stream()
                                .map(Scope::getName)
                                .collect(Collectors.toSet())))

                .authorizationGrantTypes(types -> types.addAll(
                        client.getGrantTypes()
                                .stream()
                                .map(t -> new AuthorizationGrantType(t.getName()))
                                .collect(Collectors.toSet())
                ))

                .clientAuthenticationMethods(methods -> methods.addAll(
                        client.getAuthMethods()
                                .stream()
                                .map(m -> new ClientAuthenticationMethod(m.getName()))
                                .collect(Collectors.toSet())
                ))

                .redirectUris(redirectUris -> redirectUris.addAll(
                        client.getRedirectUris()
                                .stream()
                                .map(RedirectUri::getName)
                                .collect(Collectors.toSet())
                ))
                .build();
    }
}
