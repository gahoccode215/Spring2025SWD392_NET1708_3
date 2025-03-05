package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.Otp;
import com.swd392.skincare_products_sales_system.model.User;
import com.swd392.skincare_products_sales_system.repository.OtpRepository;
import com.swd392.skincare_products_sales_system.repository.UserRepository;
import com.swd392.skincare_products_sales_system.service.OtpService;
import com.swd392.skincare_products_sales_system.service.PostmarkService;
import com.swd392.skincare_products_sales_system.util.OtpUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpServiceImpl implements OtpService {

    OtpRepository otpRepository;
    UserRepository userRepository;
    PostmarkService postmarkService;

    @Override
    @Transactional
    public Otp generateAndSaveOtp(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tạo OTP ngẫu nhiên
        String otp = OtpUtil.generateOTP();

        // Xác định thời gian hết hạn (ví dụ 10 phút sau khi tạo)
        Date expirationTime = new Date(System.currentTimeMillis() + (10 * 60 * 1000)); // 10 phút

        // Tạo và lưu OTP
        Otp otpRecord = Otp.builder()
                .user(user)
                .otp(otp)
                .expirationTime(expirationTime)
                .createdAt(new Date())
                .build();

        return otpRepository.save(otpRecord);
    }

    @Override
    public boolean validateOtp(String userId, String otp) {
        Date currentTime = new Date();
        Optional<Otp> otpRecord = otpRepository.findByUserIdAndOtpAndExpirationTimeAfter(userId, otp, currentTime);

        return otpRecord.isPresent();
    }

    @Override
    public void deleteExpiredOtps() {
        Date currentTime = new Date();
        otpRepository.deleteByExpirationTimeBefore(currentTime);
    }

    @Override
    @Transactional
    public void resendOtp(String userId) {
        // Lấy người dùng từ ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Vô hiệu hóa OTP cũ (nếu có)
        Optional<Otp> existingOtp = otpRepository.findByUser(user);
        existingOtp.ifPresent(otp -> {
            otp.setExpirationTime(new Date()); // Thay đổi thời gian hết hạn để đánh dấu OTP cũ là không hợp lệ
            otpRepository.save(otp); // Lưu lại OTP đã vô hiệu hóa
        });

        // Tạo OTP mới
        Otp newOtp = generateOtpForUser(user);

        // Gửi OTP mới qua email
        try {
            postmarkService.sendVerificationEmailWithOTP(user.getEmail(), user.getUsername(), newOtp.getOtp());
        } catch (Exception e) {
            throw new AppException(ErrorCode.EMAIL_SEND_FAILED);
        }

        // Lưu OTP mới vào cơ sở dữ liệu
        otpRepository.save(newOtp);
    }

    @Override
    public void verifyOtp(String userId, String otpCode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra OTP hợp lệ
        Optional<Otp> otpOptional = otpRepository.findByUserAndOtp(user, otpCode);
        if (otpOptional.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_OTP); // Nếu không tìm thấy OTP trùng khớp
        }

        Otp otp = otpOptional.get();

        // Kiểm tra thời gian hết hạn của OTP
        if (otp.getExpirationTime().before(new Date())) {
            throw new AppException(ErrorCode.OTP_EXPIRED); // OTP đã hết hạn
        }

        // Nếu OTP hợp lệ và chưa hết hạn, có thể tiến hành xác minh tài khoản
        user.setStatus(Status.ACTIVE); // Ví dụ: chuyển trạng thái tài khoản thành "ACTIVE"
        userRepository.save(user);

        // Xóa OTP đã xác thực khỏi cơ sở dữ liệu để không sử dụng lại
        otpRepository.delete(otp);
    }

    private Otp generateOtpForUser(User user) {
        // Tạo mã OTP ngẫu nhiên (hoặc dùng một phương thức sinh mã OTP phù hợp)
        String otpCode = OtpUtil.generateOTP();

        // Thiết lập thời gian hết hạn cho OTP (10 phút từ thời điểm tạo)
        Date expirationTime = new Date(System.currentTimeMillis() + 10 * 60 * 1000); // 10 phút sau

        // Tạo đối tượng OTP mới
        Otp otp = new Otp();
        otp.setOtp(otpCode);
        otp.setUser(user);
        otp.setExpirationTime(expirationTime);
        otp.setCreatedAt(new Date()); // Thời gian tạo OTP mới

        return otp;
    }
}
