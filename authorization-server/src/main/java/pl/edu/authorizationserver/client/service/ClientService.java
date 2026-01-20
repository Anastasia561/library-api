package pl.edu.authorizationserver.client.service;

import pl.edu.authorizationserver.client.dto.ClientRegisterDto;
import pl.edu.authorizationserver.client.model.Client;

public interface ClientService {
    String save(ClientRegisterDto dto);

    Client findById(String id);

    Client findByClientId(String clientId);
}
