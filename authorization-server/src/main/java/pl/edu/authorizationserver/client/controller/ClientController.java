package pl.edu.authorizationserver.client.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.authorizationserver.client.dto.ClientRegisterDto;
import pl.edu.authorizationserver.client.service.ClientService;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping("/register")
    public ResponseEntity<String> registerClient(@Valid @RequestBody ClientRegisterDto dto) {
        clientService.save(dto);
        return ResponseEntity.ok("Client registered successfully");
    }
}
