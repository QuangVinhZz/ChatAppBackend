package iuh.fit.se.dtos.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateGroupRequest {
    String name;
    String description;
    String color;
    List<String> memberIds;
    List<String> adminIds;
}