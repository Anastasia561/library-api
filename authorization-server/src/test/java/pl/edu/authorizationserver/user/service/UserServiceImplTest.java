package pl.edu.authorizationserver.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.authorizationserver.user.dto.UserRegisterDto;
import pl.edu.authorizationserver.user.mapper.UserMapper;
import pl.edu.authorizationserver.user.model.User;
import pl.edu.authorizationserver.user.repository.UserRepository;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldRegisterUser_whenInputIsValid() {
        UserRegisterDto dto = new UserRegisterDto("john", "plainPassword");

        String encodedPass = "$2a$10$abcdef";
        User mappedUser = new User();
        mappedUser.setId(42);

        when(passwordEncoder.encode(dto.password())).thenReturn(encodedPass);
        when(userMapper.toUser(dto, encodedPass)).thenReturn(mappedUser);
        when(userRepository.save(mappedUser)).thenReturn(mappedUser);

        Integer userId = userService.registerUser(dto);

        assertNotNull(userId);
        assertEquals(42, userId);

        verify(passwordEncoder).encode(dto.password());
        verify(userMapper).toUser(dto, encodedPass);
        verify(userRepository).save(mappedUser);
    }
}
