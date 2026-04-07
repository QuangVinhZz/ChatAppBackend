package iuh.fit.se.mapper;

import iuh.fit.se.dtos.request.CreateGroupRequest;
import iuh.fit.se.dtos.request.UpdateGroupRequest;
import iuh.fit.se.dtos.response.GroupResponse;
import iuh.fit.se.entities.Group;
import iuh.fit.se.entities.User;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(target = "createdBy", source = "createdBy.id")
    @Mapping(target = "members", expression = "java(mapUsersToIds(group.getMembers()))")
    @Mapping(target = "admins", expression = "java(mapUsersToIds(group.getAdmins()))")
    @Mapping(target = "conversationId", source = "conversation.id")
    GroupResponse toGroupResponse(Group group);

    @Mapping(target = "members", ignore = true)
    @Mapping(target = "admins", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "conversation", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Group toGroup(CreateGroupRequest request);

    @Mapping(target = "members", ignore = true)
    @Mapping(target = "admins", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "conversation", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateGroupFromRequest(UpdateGroupRequest request, @MappingTarget Group group);

    default List<String> mapUsersToIds(Set<User> users) {
        return users.stream().map(User::getId).collect(Collectors.toList());
    }
}