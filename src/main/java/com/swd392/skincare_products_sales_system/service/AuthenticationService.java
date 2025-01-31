package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.SignInRequest;
import com.swd392.skincare_products_sales_system.dto.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse getAccessToken(SignInRequest request);

    TokenResponse getRefreshToken(String request);
}
