package iuh.fit.se.services;

import iuh.fit.se.dtos.request.RegisterRequest;
import iuh.fit.se.dtos.request.ChangePasswordRequest;
import iuh.fit.se.dtos.response.UserResponse;

public interface UserService {
    void register(RegisterRequest request);
    UserResponse getMyProfile(String email);
    void changePassword(String email, ChangePasswordRequest request);
    UserResponse updateProfile(String email, iuh.fit.se.dtos.request.UpdateProfileRequest request);
    UserResponse updateAvatar(String email, org.springframework.web.multipart.MultipartFile file);
}