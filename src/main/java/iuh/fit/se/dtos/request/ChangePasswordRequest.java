package iuh.fit.se.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @NotBlank(message = "Old password is required")
    String oldPassword;

    @Size(min = 6, message = "PASSWORD_INVALID")
    String newPassword;

    @NotBlank(message = "Confirm new password is required")
    String confirmNewPassword;
}