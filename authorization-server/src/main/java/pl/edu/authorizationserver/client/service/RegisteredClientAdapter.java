package pl.edu.authorizationserver.client.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import pl.edu.authorizationserver.client.mapper.ClientMapper;

@Service
@RequiredArgsConstructor
public class RegisteredClientAdapter implements RegisteredClientRepository {
    private final ClientService clientService;
    private final ClientMapper clientMapper;

    @Override
    public void save(RegisteredClient registeredClient) {
        clientService.save(clientMapper.toClientRegisterDto(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        return clientMapper.toRegisteredClientFromClient(clientService.findById(id));
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return clientMapper.toRegisteredClientFromClient(clientService.findByClientId(clientId));
    }
}
