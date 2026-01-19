package pl.edu.authorizationserver.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.edu.authorizationserver.user.dto.UserRegisterDto;
import pl.edu.authorizationserver.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", source = "passwordEncoded")
    @Mapping(target = "role", constant = "USER")
    User toUser(UserRegisterDto dto, String passwordEncoded);
}
