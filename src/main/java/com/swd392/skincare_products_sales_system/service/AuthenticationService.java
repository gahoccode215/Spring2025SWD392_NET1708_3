package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.IntrospectRequest;
import com.swd392.skincare_products_sales_system.dto.request.SignInRequest;
import com.swd392.skincare_products_sales_system.dto.response.IntrospectResponse;
import com.swd392.skincare_products_sales_system.dto.response.TokenResponse;

public interface AuthenticationService {
    IntrospectResponse introspect(IntrospectRequest request);

    
}
