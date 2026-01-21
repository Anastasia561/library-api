package pl.edu.authorizationserver.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.authorizationserver.user.dto.UserRegisterDto;
import pl.edu.authorizationserver.user.service.UserService;
import pl.edu.authorizationserver.wrapper.ResponseWrapper;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public ResponseWrapper<Integer> register(@Valid @RequestBody UserRegisterDto dto) {
        Integer savedId = userService.registerUser(dto);
        return ResponseWrapper.withStatus(HttpStatus.CREATED, savedId);
    }
}
