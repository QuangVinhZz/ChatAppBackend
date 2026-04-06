package iuh.fit.se.services.impl;

import iuh.fit.se.entities.Otp;
import iuh.fit.se.entities.enums.HttpCode;
import iuh.fit.se.exceptions.AppException;
import iuh.fit.se.repositories.OtpRepository;
import iuh.fit.se.services.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {

    JavaMailSender mailSender;
    OtpRepository otpRepository;

    @Override
    public void sendOtpRegister(String email) {
        // 1. Sinh mã OTP 6 số ngẫu nhiên
        String otpCode = String.format("%06d", new Random().nextInt(999999));

        // 2. Lưu thông tin OTP vào Database với thời hạn 5 phút
        Otp otp = Otp.builder()
                .email(email)
                .otpCode(otpCode)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .build();
        otpRepository.save(otp);

        // 3. Chuẩn bị nội dung Email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[ChatApp] Mã xác nhận đăng ký tài khoản");

        String mailContent = "Chào bạn,\n\n"
                + "Bạn đang thực hiện đăng ký tài khoản trên ChatApp.\n"
                + "Mã xác nhận (OTP) của bạn là: " + otpCode + "\n\n"
                + "Mã này có hiệu lực trong vòng 5 phút.\n"
                + "Vui lòng không chia sẻ mã này cho bất kỳ ai.\n\n"
                + "Trân trọng,\n"
                + "Đội ngũ ChatApp Backend.";
        message.setText(mailContent);

        // 4. Thực hiện gửi mail
        try {
            mailSender.send(message);
        } catch (Exception e) {
            // In ra log để dễ debug nếu gửi mail thất bại (sai pass, sai cấu hình port,...)
            System.err.println("Lỗi gửi email đến " + email + ": " + e.getMessage());

            // Ném lỗi để báo về cho Client biết
            // Lưu ý: Tốt nhất bạn nên vào file HttpCode.java định nghĩa thêm 1 mã lỗi là EMAIL_SEND_FAILED
            throw new AppException(HttpCode.BAD_REQUEST);
        }
    }
}