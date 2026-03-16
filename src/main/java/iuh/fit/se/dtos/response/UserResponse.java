package iuh.fit.se.dtos.response;

import iuh.fit.se.entities.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String firstName;
    String lastName;
    String username;
    String email;
    String phoneNumber;
    String address;
    Set<AccountCredentialResponse> accounts;
    Set<Role> roles;
    Boolean isVerified;
}
