package pl.edu.authorizationserver.client.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
public class Client {
    @Id
    @Column(name = "id", length = 200, nullable = false)
    private String id;
    @Column(name = "id_client", length = 50, nullable = false)
    private String idClient;
    @Column(name = "secret", length = 200)
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
