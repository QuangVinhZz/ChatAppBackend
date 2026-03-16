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
public class CustomerUpdateByAdminResponse {
    String firstName;
    String lastName;
    String newPassword;
    String email;
    String phoneNumber;
    String address;
    Integer loyaltyPoints;
    Set<AccountCredentialResponse> accounts;
    Boolean isVerified;
}
