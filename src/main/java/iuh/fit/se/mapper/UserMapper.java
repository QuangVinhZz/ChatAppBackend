package iuh.fit.se.mapper;

import iuh.fit.se.dtos.response.UserResponse;
import iuh.fit.se.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "isVerified", ignore = true)
    UserResponse toUserResponse(User user);
}