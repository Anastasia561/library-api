package pl.edu.authorizationserver.client.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
public class Client {
    @Id
    private String id;
    private String idClient;
    private String secret;

    @ElementCollection(targetClass = AuthMethod.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "client_auth_methods", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "auth_method")
    private Set<AuthMethod> authMethods;

    @ElementCollection(targetClass = GrantType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "client_grant_types", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "grant_type")
    private Set<GrantType> grantTypes;

    @ManyToMany
    @JoinTable(name = "client_scope",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "scope_id"))
    private Set<Scope> scopes;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<RedirectUri> redirectUris;

}
