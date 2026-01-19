package pl.edu.authorizationserver.client.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
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

}
