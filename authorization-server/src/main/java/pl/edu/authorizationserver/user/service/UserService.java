package pl.edu.authorizationserver.user.service;

import pl.edu.authorizationserver.user.dto.UserRegisterDto;

public interface UserService {
    Integer registerUser(UserRegisterDto userRegisterDto);
}
