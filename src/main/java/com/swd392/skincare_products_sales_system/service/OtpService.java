package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.entity.authentication.Otp;

public interface OtpService {
    Otp generateAndSaveOtp(String userId);
    boolean validateOtp(String userId, String otp);
    void resendOtp(String userId);
    void verifyOtp(String userId, String otpCode);
    void resendOtpByEmail(String email);
}
