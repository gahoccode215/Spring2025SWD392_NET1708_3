package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.*;
import com.swd392.skincare_products_sales_system.dto.response.*;

import java.util.Map;

public interface AuthenticationService {

    LoginResponse login(LoginRequest request);
    RefreshTokenResponse refreshToken(RefreshTokenRequest request);
    RegisterResponse register(RegisterRequest request);
    void logout(LogoutRequest request);
    void changePassword(ChangePasswordRequest request);
}
