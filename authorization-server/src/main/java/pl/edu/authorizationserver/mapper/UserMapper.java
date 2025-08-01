package pl.edu.authorizationserver.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.edu.authorizationserver.dto.UserRegisterDto;
import pl.edu.authorizationserver.entity.User;
import pl.edu.authorizationserver.repository.RoleRepository;

import java.util.Set;

@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserMapper(PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User toUser(UserRegisterDto dto) {
        User user = new User();
        user.setRoles(Set.of(roleRepository.findByName("USER").orElseThrow()));
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return user;
    }
}
