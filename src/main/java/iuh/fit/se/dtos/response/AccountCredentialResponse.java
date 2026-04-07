package iuh.fit.se.dtos.response;

import iuh.fit.se.entities.enums.AccountType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountCredentialResponse {
    String id;
    AccountType type;
    String credential;
    Boolean isVerified;
    LocalDateTime createdAt;
    LocalDateTime lastLogin;
    String userId;
}
