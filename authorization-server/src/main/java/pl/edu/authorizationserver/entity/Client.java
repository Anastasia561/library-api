package pl.edu.authorizationserver.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {
    @Id
    private String id;
    private String idClient;
    private String secret;
    @ManyToMany
    @JoinTable(name = "client_auth_method",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "auth_method_id"))
    private Set<AuthMethod> authMethods = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "client_grant_type",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "grant_type_id"))
    private Set<GrantType> grantTypes = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "client_scope",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "scope_id"))
    private Set<Scope> scopes = new HashSet<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<RedirectUri> redirectUris = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Set<AuthMethod> getAuthMethods() {
        return authMethods;
    }

    public void setAuthMethods(Set<AuthMethod> authMethods) {
        this.authMethods = authMethods;
    }

    public Set<GrantType> getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(Set<GrantType> grantTypes) {
        this.grantTypes = grantTypes;
    }

    public Set<Scope> getScopes() {
        return scopes;
    }

    public void setScopes(Set<Scope> scopes) {
        this.scopes = scopes;
    }

    public Set<RedirectUri> getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(Set<RedirectUri> redirectUris) {
        this.redirectUris = redirectUris;
    }
}
