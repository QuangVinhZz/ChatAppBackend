package iuh.fit.se.dtos.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupResponse {
    String id;
    String name;
    String description;
    String avatar;
    String color;
    LocalDateTime createdAt;
    String createdBy; // User ID
    List<String> members; // User IDs
    List<String> admins; // User IDs
    String conversationId;
}