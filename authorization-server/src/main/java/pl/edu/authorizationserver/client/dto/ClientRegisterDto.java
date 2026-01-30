package pl.edu.authorizationserver.client.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import pl.edu.authorizationserver.client.model.AuthMethod;
import pl.edu.authorizationserver.client.model.GrantType;
import pl.edu.authorizationserver.validation.annotation.UniqueClientId;

import java.util.Set;

public record ClientRegisterDto(
        @Size(min = 2, max = 50)
        @UniqueClientId
        @NotEmpty(message = "client id can not be empty")
        String idClient,

        String secret,

        @NotEmpty(message = "must provide at least one authentication method")
        @Size(min = 1)
        Set<AuthMethod> authMethods,

        @NotEmpty(message = "must provide at least one grant type")
        @Size(min = 1)
        Set<GrantType> grantTypes,

        @NotEmpty(message = "must provide at least one scope")
        @Size(min = 1)
        Set<String> scopes,

        @NotEmpty(message = "must provide at least one redirect uri")
        @Size(min = 1)
        Set<String> redirectUris
) {
}