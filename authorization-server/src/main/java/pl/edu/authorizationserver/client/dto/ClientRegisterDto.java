package pl.edu.authorizationserver.client.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import pl.edu.authorizationserver.validation.annotation.UniqueClientId;

import java.util.Set;

public record ClientRegisterDto(
        @Size(min = 2, max = 50)
        @UniqueClientId
        @NotEmpty(message = "client id can not be empty")
        String idClient,

        @Size(min = 2, max = 50)
        @NotEmpty(message = "client secret can not be empty")
        String secret,

        @Size(min = 1, message = "must provide at leas one authentication method")
        Set<String> authMethods,

        @Size(min = 1, message = "must provide at leas one grant type")
        Set<String> grantTypes,

        @Size(min = 1, message = "must provide at least one scope")
        Set<String> scopes,

        @Size(min = 1, message = "must provide at least one redirect uri")
        Set<String> redirectUris
) {
}