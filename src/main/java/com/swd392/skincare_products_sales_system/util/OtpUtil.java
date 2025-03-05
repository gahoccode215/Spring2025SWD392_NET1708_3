package com.swd392.skincare_products_sales_system.util;

import java.security.SecureRandom;

public class OtpUtil {
    private static final String CHARACTERS = "0123456789";
    private static final int OTP_LENGTH = 6; // Độ dài OTP, ví dụ: 6 chữ số

    public static String generateOTP() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return otp.toString();
    }
}
