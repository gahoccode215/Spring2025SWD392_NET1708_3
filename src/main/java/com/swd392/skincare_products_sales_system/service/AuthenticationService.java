package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.*;
import com.swd392.skincare_products_sales_system.dto.response.AuthenticationResponse;
import com.swd392.skincare_products_sales_system.dto.response.IntrospectResponse;
import com.swd392.skincare_products_sales_system.dto.response.TokenResponse;

import java.util.Map;

public interface AuthenticationService {

    Map<String, String> register(RegisterRequest request);
    Map<String, String> login(LoginRequest request);
}
