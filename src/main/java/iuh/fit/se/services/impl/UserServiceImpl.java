package iuh.fit.se.services.impl;

import iuh.fit.se.dtos.request.ChangePasswordRequest;
import iuh.fit.se.dtos.request.RegisterRequest;
import iuh.fit.se.dtos.request.UpdateProfileRequest;
import iuh.fit.se.dtos.response.UserResponse;
import iuh.fit.se.entities.AccountCredential;
import iuh.fit.se.entities.Otp;
import iuh.fit.se.entities.Role;
import iuh.fit.se.entities.User;
import iuh.fit.se.entities.enums.AccountType;
import iuh.fit.se.entities.enums.HttpCode;
import iuh.fit.se.exceptions.AppException;
import iuh.fit.se.mapper.UserMapper;
import iuh.fit.se.repositories.AccountCredentialRepository;
import iuh.fit.se.repositories.OtpRepository;
import iuh.fit.se.repositories.RoleRepository;
import iuh.fit.se.repositories.UserRepository;
import iuh.fit.se.services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    AccountCredentialRepository accountCredentialRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;

    private final OtpRepository otpRepository;

    @Override
    public void register(RegisterRequest request) {
        // 1. Kiểm tra OTP
        Otp otpEntity = otpRepository.findTopByEmailOrderByExpiryTimeDesc(request.getEmail())
                .orElseThrow(() -> new AppException(HttpCode.BAD_REQUEST)); // Nên tự tạo mã OTP_NOT_FOUND

        if (!otpEntity.getOtpCode().equals(request.getOtp())) {
            throw new AppException(HttpCode.BAD_REQUEST); // Nên tự tạo mã OTP_INCORRECT
        }

        if (otpEntity.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new AppException(HttpCode.BAD_REQUEST); // Nên tự tạo mã OTP_EXPIRED
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new AppException(HttpCode.PASSWORD_NOMATCH);
        }

        if (userRepository.findUserByEmail(request.getEmail()) != null ||
                accountCredentialRepository.findByCredential(request.getEmail()) != null) {
            throw new AppException(HttpCode.EMAIL_EXISTED);
        }

        String fullName = request.getFullName().trim();
        String firstName = fullName;
        String lastName = "";
        int firstSpaceIndex = fullName.indexOf(" ");
        if (firstSpaceIndex > 0) {
            lastName = fullName.substring(0, firstSpaceIndex).trim();
            firstName = fullName.substring(firstSpaceIndex + 1).trim();
        }

        Role userRole = roleRepository.findById("CUSTOMER")
                .orElseThrow(() -> new AppException(HttpCode.ROLE_NOT_FOUND));

        User newUser = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(request.getEmail())
                .roles(Set.of(userRole))
                .build();
        userRepository.save(newUser);

        AccountCredential account = AccountCredential.builder()
                .credential(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .type(AccountType.EMAIL)
                .user(newUser)
                .isVerified(true)
                .build();
        accountCredentialRepository.save(account);
        // 3. Xóa OTP sau khi dùng thành công
        otpRepository.delete(otpEntity);
    }

    @Override
    public UserResponse getMyProfile(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new AppException(HttpCode.USER_NOT_FOUND);
        }
        return userMapper.toUserResponse(user);
    }

    @Override
    public void changePassword(String email, ChangePasswordRequest request) {
        AccountCredential account = accountCredentialRepository.findByCredential(email);
        if (account == null) {
            throw new AppException(HttpCode.ACCOUNT_NOT_FOUND);
        }

        // Kiểm tra pass cũ
        if (!passwordEncoder.matches(request.getOldPassword(), account.getPassword())) {
            throw new AppException(HttpCode.PASSWORD_INCORRECT);
        }

        // Kiểm tra xác nhận pass mới
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new AppException(HttpCode.PASSWORD_NOMATCH);
        }

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountCredentialRepository.save(account);
    }

    @Override
    public UserResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) throw new AppException(HttpCode.USER_NOT_FOUND);

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getBio() != null) user.setBio(request.getBio());

        // Thêm 3 trường mới
        if (request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());
        if (request.getGender() != null) user.setGender(request.getGender());

        if (request.getDateOfBirth() != null) {
            try {
                // Chuyển đổi chuỗi "YYYY-MM-DD" thành LocalDate
                user.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));
            } catch (DateTimeParseException e) {
                throw new AppException(HttpCode.BAD_REQUEST); // Hoặc bạn có thể tự định nghĩa thêm mã DATE_FORMAT_INVALID
            }
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateAvatar(String email, MultipartFile file) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) throw new AppException(HttpCode.USER_NOT_FOUND);

        try {
            // 1. Tạo thư mục nếu chưa tồn tại
            String uploadDir = "uploads/avatars/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 2. Tạo tên file duy nhất để tránh bị ghi đè (dùng UUID)
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // 3. Copy file vào thư mục local
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 4. Sinh ra URL để client có thể truy cập ảnh (dựa vào cấu hình WebConfig ở bước 2)
            // Lưu ý: Nếu deploy lên server thật, chuỗi "localhost:8080" sẽ phải đổi thành Domain của server
            String fileUrl = "http://localhost:8080/avatars/" + fileName;
            user.setAvatarUrl(fileUrl);

            return userMapper.toUserResponse(userRepository.save(user));

        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!", e);
        }
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        // 1. Verify OTP
        Otp otpEntity = otpRepository.findByEmailAndOtpCode(email, otp);
        if (otpEntity == null) {
            throw new AppException(HttpCode.BAD_REQUEST);
        }

        // 2. Check if OTP is expired
        if (otpEntity.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpRepository.delete(otpEntity);
            throw new AppException(HttpCode.BAD_REQUEST);
        }

        // 3. Find user by email
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new AppException(HttpCode.USER_NOT_FOUND);
        }

        // 4. Update password
        AccountCredential credential = accountCredentialRepository.findByUser(user);
        if (credential == null) {
            throw new AppException(HttpCode.USER_NOT_FOUND);
        }

        credential.setPassword(passwordEncoder.encode(newPassword));
        accountCredentialRepository.save(credential);

        // 5. Delete used OTP
        otpRepository.delete(otpEntity);
    }
}