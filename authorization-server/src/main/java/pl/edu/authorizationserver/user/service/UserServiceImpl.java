package pl.edu.authorizationserver.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.authorizationserver.user.dto.UserRegisterDto;
import pl.edu.authorizationserver.user.mapper.UserMapper;
import pl.edu.authorizationserver.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Integer registerUser(UserRegisterDto dto) {
        String encodedPass = passwordEncoder.encode(dto.password());
        return userRepository.save(userMapper.toUser(dto, encodedPass)).getId();
    }
}
