package iuh.fit.se.dtos.response;

import iuh.fit.se.entities.enums.MessageStatus;
import iuh.fit.se.entities.enums.MessageType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponse {
    String id;
    String conversationId;
    String senderId;
    MessageType type;
    String content;
    LocalDateTime timestamp;
    MessageStatus status;
    String caption;
    String fileName;
    Long fileSize;
}