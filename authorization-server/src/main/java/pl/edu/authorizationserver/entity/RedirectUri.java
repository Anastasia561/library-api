package pl.edu.authorizationserver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "redirect_uri")
public class RedirectUri {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public RedirectUri() {
    }

    public RedirectUri(String name, Client client) {
        this.name = name;
        this.client = client;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
