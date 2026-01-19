package pl.edu.authorizationserver.client.service;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.authorizationserver.client.mapper.ClientMapper;
import pl.edu.authorizationserver.client.repository.ClientRepository;

@Service
@Transactional
public class JpaRegisteredClientRepository implements RegisteredClientRepository {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public JpaRegisteredClientRepository(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        clientRepository.save(clientMapper.toClient(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        return clientRepository.findById(id)
                .map(clientMapper::toRegisteredClient)
                .orElseThrow();
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return clientRepository.findByIdClient(clientId)
                .map(clientMapper::toRegisteredClient)
                .orElseThrow();
    }
}
