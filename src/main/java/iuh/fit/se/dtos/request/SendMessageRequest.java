package iuh.fit.se.dtos.request;

import iuh.fit.se.entities.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendMessageRequest {
    @NotNull(message = "Message type is required")
    MessageType type;

    @NotBlank(message = "Content is required")
    String content;

    String caption;
    String fileName;
    Long fileSize;
}