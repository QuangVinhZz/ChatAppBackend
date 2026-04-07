package iuh.fit.se.mapper;

import iuh.fit.se.dtos.response.MessageResponse;
import iuh.fit.se.entities.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "conversationId", source = "conversation.id")
    @Mapping(target = "senderId", source = "sender.id")
    MessageResponse toMessageResponse(Message message);
}