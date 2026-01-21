package pl.edu.authorizationserver.client.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.authorizationserver.client.dto.ClientRegisterDto;
import pl.edu.authorizationserver.client.service.ClientService;
import pl.edu.authorizationserver.wrapper.ResponseWrapper;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public ResponseWrapper<String> registerClient(@Valid @RequestBody ClientRegisterDto dto) {
        String savedId = clientService.save(dto);
        return ResponseWrapper.withStatus(HttpStatus.CREATED, savedId);
    }
}
