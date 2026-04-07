package iuh.fit.se.controllers;

import iuh.fit.se.dtos.request.ChangePasswordRequest;
import iuh.fit.se.dtos.request.RegisterRequest;
import iuh.fit.se.dtos.request.UpdateProfileRequest;
import iuh.fit.se.dtos.response.ApiResponse;
import iuh.fit.se.dtos.response.UserResponse;
import iuh.fit.se.entities.enums.HttpCode;
import iuh.fit.se.services.EmailService;
import iuh.fit.se.services.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;
    EmailService emailService;

    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody @Valid RegisterRequest request) {
        userService.register(request);
        return ApiResponse.<Void>builder()
                .code(HttpCode.CREATED.getCODE())
                .message("User registered successfully!")
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getMyProfile() {
        // Lấy identifier (email) từ JWT thông qua SecurityContext
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ApiResponse.<UserResponse>builder()
                .code(HttpCode.OK.getCODE())
                .message(HttpCode.OK.getMESSAGE())
                .data(userService.getMyProfile(email))
                .build();
    }

    @PutMapping("/password")
    public ApiResponse<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.changePassword(email, request);
        return ApiResponse.<Void>builder()
                .code(HttpCode.OK.getCODE())
                .message("Password updated successfully!")
                .build();
    }

    @PutMapping("/me")
    public ApiResponse<UserResponse> updateProfile(@RequestBody UpdateProfileRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ApiResponse.<UserResponse>builder()
                .code(HttpCode.OK.getCODE())
                .message("Profile updated successfully")
                .data(userService.updateProfile(email, request))
                .build();
    }

    @PostMapping("/me/avatar")
    public ApiResponse<UserResponse> updateAvatar(@RequestParam("file") MultipartFile file) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ApiResponse.<UserResponse>builder()
                .code(HttpCode.OK.getCODE())
                .message("Avatar updated successfully")
                .data(userService.updateAvatar(email, file))
                .build();
    }

    @PostMapping("/send-otp")
    public ApiResponse<Void> sendOtp(@RequestParam String email) {
        emailService.sendOtpRegister(email);
        return ApiResponse.<Void>builder()
                .code(HttpCode.OK.getCODE())
                .message("OTP sent to your email")
                .build();
    }
}