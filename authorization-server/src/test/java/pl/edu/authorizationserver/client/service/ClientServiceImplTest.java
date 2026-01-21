package pl.edu.authorizationserver.client.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.authorizationserver.client.dto.ClientRegisterDto;
import pl.edu.authorizationserver.client.mapper.ClientMapper;
import pl.edu.authorizationserver.client.model.AuthMethod;
import pl.edu.authorizationserver.client.model.Client;
import pl.edu.authorizationserver.client.model.GrantType;
import pl.edu.authorizationserver.client.model.Scope;
import pl.edu.authorizationserver.client.repository.ClientRepository;
import pl.edu.authorizationserver.client.repository.ScopeRepository;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ScopeRepository scopeRepository;
    @Mock
    private ClientMapper clientMapper;
    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void shouldFindClientById_whenClientExists() {
        String clientId = "123";
        Client client = new Client();
        client.setId(clientId);
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        Client result = clientService.findById(clientId);

        assertNotNull(result);
        assertEquals(clientId, result.getId());
        verify(clientRepository).findById(clientId);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenClientDoesNotExist() {
        String clientId = "456";
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> clientService.findById(clientId));

        assertEquals("Client not found", exception.getMessage());
        verify(clientRepository).findById(clientId);
    }

    @Test
    void shouldFindClientByClientId_whenClientExists() {
        String clientId = "123";
        Client client = new Client();
        client.setIdClient(clientId);
        when(clientRepository.findByIdClient(clientId)).thenReturn(Optional.of(client));

        Client result = clientService.findByClientId(clientId);

        assertNotNull(result);
        assertEquals(clientId, result.getIdClient());
        verify(clientRepository).findByIdClient(clientId);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenClientDoesNotExistForIdClient() {
        String clientId = "456";
        when(clientRepository.findByIdClient(clientId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> clientService.findByClientId(clientId));

        assertEquals("Client not found", exception.getMessage());
        verify(clientRepository).findByIdClient(clientId);
    }

    @Test
    void shouldSaveClient_whenInputIsValid() {
        ClientRegisterDto dto = new ClientRegisterDto(
                "id-client", "secret", Set.of(AuthMethod.CLIENT_SECRET_BASIC),
                Set.of(GrantType.REFRESH_TOKEN), Set.of("read", "write"), Set.of("uri"));

        Client client = new Client();
        client.setId("id-client");

        Scope readScope = new Scope();
        Scope writeScope = new Scope();
        readScope.setName("read");
        writeScope.setName("write");

        when(clientMapper.toClient(dto)).thenReturn(client);
        when(scopeRepository.findByName("read")).thenReturn(Optional.of(readScope));
        when(scopeRepository.findByName("write")).thenReturn(Optional.of(writeScope));
        when(clientRepository.save(client)).thenReturn(client);

        String savedClientId = clientService.save(dto);

        assertEquals("id-client", savedClientId);
        assertEquals(2, client.getScopes().size());
        assertTrue(client.getScopes().contains(readScope));
        assertTrue(client.getScopes().contains(writeScope));

        verify(clientMapper).toClient(dto);
        verify(scopeRepository).findByName("read");
        verify(scopeRepository).findByName("write");
        verify(clientRepository).save(client);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenScopeNotFound() {
        ClientRegisterDto dto = new ClientRegisterDto(
                "id-client", "secret", Set.of(AuthMethod.CLIENT_SECRET_BASIC),
                Set.of(GrantType.REFRESH_TOKEN), Set.of("write"), Set.of("uri"));

        Client client = new Client();
        when(clientMapper.toClient(dto)).thenReturn(client);
        when(scopeRepository.findByName("write")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> clientService.save(dto));

        assertEquals("Scope not found", exception.getMessage());

        verify(clientMapper).toClient(dto);
        verify(scopeRepository).findByName("write");
        verifyNoInteractions(clientRepository);
    }
}
