package pl.edu.authorizationserver.service;

import org.springframework.stereotype.Service;
import pl.edu.authorizationserver.dto.UserRegisterDto;
import pl.edu.authorizationserver.mapper.UserMapper;
import pl.edu.authorizationserver.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void registerUser(UserRegisterDto userRegisterDto) {
        userRepository.save(userMapper.toUser(userRegisterDto));
    }
}
