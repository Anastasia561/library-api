package pl.edu.authorizationserver.user.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.edu.authorizationserver.user.dto.UserRegisterDto;
import pl.edu.authorizationserver.user.model.Role;
import pl.edu.authorizationserver.user.model.User;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperImplTest {
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void shouldMapToUserFromDto_whenInputIsValid() {
        UserRegisterDto dto = new UserRegisterDto("john_doe", "pass");
        String encodedPassword = "$2a$10$abcdef";

        User user = userMapper.toUser(dto, encodedPassword);

        assertNotNull(user);
        assertEquals("john_doe", user.getUsername());
        assertEquals(encodedPassword, user.getPassword());
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    void shouldReturnNull_whenDtoIsNull() {
        assertNull(userMapper.toUser(null, null));
    }
}
