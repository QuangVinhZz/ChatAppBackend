package iuh.fit.se.mapper;

import iuh.fit.se.dtos.response.CustomerUpdateByAdminResponse;
import iuh.fit.se.dtos.response.UserResponse;
import iuh.fit.se.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
//    @Mapping(target = "id", ignore = true)
//    User toUser(CustomerRegistrationRequest request);
//
//    @Mapping(target = "id", ignore = true)
//    User toUser(EmployeeRegistrationRequest request);
//
    UserResponse toUserResponse(User user);
//    @AfterMapping
//    default void mappingUsername(@MappingTarget UserResponse userResponse, User user){
//        userResponse.setUsername(userResponse.get);
//    }
    CustomerUpdateByAdminResponse toUserUpdateResponse(User user);
}
