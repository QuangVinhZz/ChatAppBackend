package iuh.fit.se.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    @NotBlank(message = "Full name is required")
    String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    String email;

    @Size(min = 6, message = "PASSWORD_INVALID")
    String password;

    @NotBlank(message = "Confirm password is required")
    String confirmPassword;

    @NotBlank(message = "OTP is required")
    String otp;
}