package pl.edu.authorizationserver.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import pl.edu.authorizationserver.validation.annotation.UniqueClientId;

import java.util.Set;

public class ClientRegisterDto {
    @Size(min = 2, max = 50)
    @UniqueClientId
    @NotEmpty(message = "client id can not be empty")
    private String idClient;
    @Size(min = 2, max = 50)
    @NotEmpty(message = "client secret can not be empty")
    private String secret;
    @Size(min = 1, message = "must provide at leas one authentication method")
    private Set<String> authMethods;
    @Size(min = 1, message = "must provide at leas one grant type")
    private Set<String> grantTypes;
    @Size(min = 1, message = "must provide at least one scope")
    private Set<String> scopes;
    @Size(min = 1, message = "must provide at least one redirect uri")
    private Set<String> redirectUris;

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Set<String> getAuthMethods() {
        return authMethods;
    }

    public void setAuthMethods(Set<String> authMethods) {
        this.authMethods = authMethods;
    }

    public Set<String> getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(Set<String> grantTypes) {
        this.grantTypes = grantTypes;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public Set<String> getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(Set<String> redirectUris) {
        this.redirectUris = redirectUris;
    }
}
