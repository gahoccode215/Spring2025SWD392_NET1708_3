package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.*;
import com.swd392.skincare_products_sales_system.dto.response.AuthenticationResponse;
import com.swd392.skincare_products_sales_system.dto.response.IntrospectResponse;
import com.swd392.skincare_products_sales_system.dto.response.TokenResponse;

public interface AuthenticationService {
    IntrospectResponse introspect(IntrospectRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
    void logout(LogoutRequest request);
    AuthenticationResponse refreshToken(RefreshTokenRequest request);
}
