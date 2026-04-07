package iuh.fit.se.mapper;

import iuh.fit.se.dtos.response.ConversationResponse;
import iuh.fit.se.entities.Conversation;
import iuh.fit.se.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ConversationMapper {

    @Mapping(target = "participants", expression = "java(mapUsersToIds(conversation.getParticipants()))")
    @Mapping(target = "groupId", source = "group.id")
    ConversationResponse toConversationResponse(Conversation conversation);

    default List<String> mapUsersToIds(Set<User> users) {
        return users.stream().map(User::getId).collect(Collectors.toList());
    }
}