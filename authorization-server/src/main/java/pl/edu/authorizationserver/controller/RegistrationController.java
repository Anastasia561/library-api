package pl.edu.authorizationserver.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.authorizationserver.dto.ClientRegisterDto;
import pl.edu.authorizationserver.dto.UserRegisterDto;
import pl.edu.authorizationserver.mapper.ClientMapper;
import pl.edu.authorizationserver.service.JpaRegisteredClientRepository;
import pl.edu.authorizationserver.service.UserService;

@RestController
public class RegistrationController {

    private final UserService userService;
    private final JpaRegisteredClientRepository jpaRegisteredClientRepository;
    private final ClientMapper clientMapper;

    public RegistrationController(UserService userService,
                                  JpaRegisteredClientRepository jpaRegisteredClientRepository,
                                  ClientMapper clientMapper) {
        this.userService = userService;
        this.jpaRegisteredClientRepository = jpaRegisteredClientRepository;
        this.clientMapper = clientMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterDto dto) {
        userService.registerUser(dto);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/register-client")
    public ResponseEntity<String> registerClient(@Valid @RequestBody ClientRegisterDto dto) {
        jpaRegisteredClientRepository.save(clientMapper.toRegisteredClient(dto));
        return ResponseEntity.ok("Client registered successfully");
    }
}
