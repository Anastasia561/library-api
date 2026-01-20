package pl.edu.authorizationserver.client.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.authorizationserver.client.dto.ClientRegisterDto;
import pl.edu.authorizationserver.client.mapper.ClientMapper;
import pl.edu.authorizationserver.client.model.Client;
import pl.edu.authorizationserver.client.repository.ClientRepository;
import pl.edu.authorizationserver.client.repository.ScopeRepository;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ScopeRepository scopeRepository;
    private final ClientMapper clientMapper;

    @Override
    @Transactional
    public String save(ClientRegisterDto dto) {
        Client client = clientMapper.toClient(dto);

        client.setScopes(dto.scopes().stream().map(
                m -> scopeRepository.findByName(m)
                        .orElseThrow(() -> new EntityNotFoundException("Scope not found"))
        ).collect(Collectors.toSet()));

        return clientRepository.save(client).getId();
    }

    @Override
    public Client findById(String id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));
    }

    @Override
    public Client findByClientId(String clientId) {
        return clientRepository.findByIdClient(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));
    }
}
