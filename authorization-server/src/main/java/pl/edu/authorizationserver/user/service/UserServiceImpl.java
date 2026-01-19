package pl.edu.authorizationserver.user.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.authorizationserver.user.dto.UserRegisterDto;
import pl.edu.authorizationserver.user.mapper.UserMapper;
import pl.edu.authorizationserver.user.model.User;
import pl.edu.authorizationserver.user.model.Role;
import pl.edu.authorizationserver.user.repository.RoleRepository;
import pl.edu.authorizationserver.user.repository.UserRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void registerUser(UserRegisterDto dto) {
        String encodedPassword = passwordEncoder.encode(dto.password());
        User user = userMapper.toUser(dto, encodedPassword);

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new EntityNotFoundException("Role USER not found"));
        user.setRoles(Set.of(userRole));

        userRepository.save(user);
    }
}
