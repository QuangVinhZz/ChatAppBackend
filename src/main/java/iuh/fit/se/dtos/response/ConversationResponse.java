package iuh.fit.se.dtos.response;

import iuh.fit.se.entities.enums.ConversationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationResponse {
    String id;
    ConversationType type;
    String name;
    String avatar;
    String lastMessage;
    LocalDateTime lastMessageTime;
    Integer unreadCount;
    List<String> participants; // User IDs
    String groupId;
}