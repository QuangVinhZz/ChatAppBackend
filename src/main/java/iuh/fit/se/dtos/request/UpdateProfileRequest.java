package iuh.fit.se.dtos.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProfileRequest {
    String firstName;
    String lastName;
    String phoneNumber;
    String address;
    String bio;
    String avatarUrl;
    String dateOfBirth; // ISO 8601 format: YYYY-MM-DD
    String gender;
}
