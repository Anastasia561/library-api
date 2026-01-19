package pl.edu.authorizationserver.client.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.authorizationserver.client.dto.ClientRegisterDto;
import pl.edu.authorizationserver.client.mapper.ClientMapper;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final RegisteredClientRepository jpaRegisteredClientRepository;
    private final ClientMapper clientMapper;

    @PostMapping("/register")
    public ResponseEntity<String> registerClient(@Valid @RequestBody ClientRegisterDto dto) {
        jpaRegisteredClientRepository.save(clientMapper.toRegisteredClient(dto));
        return ResponseEntity.ok("Client registered successfully");
    }
}
