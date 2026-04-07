package iuh.fit.se.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateGroupRequest {
    @NotBlank(message = "Group name is required")
    String name;

    String description;
    String color;
    List<String> memberIds; // User IDs
}