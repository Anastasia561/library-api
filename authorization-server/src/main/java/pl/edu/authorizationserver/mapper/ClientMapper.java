package pl.edu.authorizationserver.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Component;
import pl.edu.authorizationserver.dto.ClientRegisterDto;
import pl.edu.authorizationserver.entity.*;
import pl.edu.authorizationserver.exception.EntityNotFoundException;
import pl.edu.authorizationserver.repository.AuthMethodRepository;
import pl.edu.authorizationserver.repository.GrantTypeRepository;
import pl.edu.authorizationserver.repository.ScopeRepository;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ClientMapper {
    private final PasswordEncoder passwordEncoder;
    private final AuthMethodRepository authMethodRepository;
    private final GrantTypeRepository grantTypeRepository;
    private final ScopeRepository scopeRepository;

    public ClientMapper(PasswordEncoder passwordEncoder, AuthMethodRepository authMethodRepository,
                        GrantTypeRepository grantTypeRepository, ScopeRepository scopeRepository) {
        this.passwordEncoder = passwordEncoder;
        this.authMethodRepository = authMethodRepository;
        this.grantTypeRepository = grantTypeRepository;
        this.scopeRepository = scopeRepository;
    }

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
                            .orElseGet(() -> scopeRepository.save(new Scope(s)));
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
                .map(u -> new RedirectUri(u, client))
                .collect(Collectors.toSet());

        client.setRedirectUris(uris);
        return client;
    }

    public RegisteredClient toRegisteredClient(ClientRegisterDto dto) {
        return RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(dto.getIdClient())
                .clientSecret(dto.getSecret())
                .scopes(scopes -> scopes.addAll(
                        dto.getScopes()))
                .authorizationGrantTypes(types -> types.addAll(
                        dto.getGrantTypes().stream()
                                .map(AuthorizationGrantType::new)
                                .collect(Collectors.toSet())))

                .clientAuthenticationMethods(methods -> methods.addAll(
                        dto.getAuthMethods()
                                .stream()
                                .map(ClientAuthenticationMethod::new)
                                .collect(Collectors.toSet())
                ))

                .redirectUris(redirectUris -> redirectUris.addAll(
                        dto.getRedirectUris()
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
