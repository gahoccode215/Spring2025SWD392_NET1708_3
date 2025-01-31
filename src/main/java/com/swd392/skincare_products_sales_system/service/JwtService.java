package com.swd392.skincare_products_sales_system.service;


import com.swd392.skincare_products_sales_system.enums.TokenType;

import java.util.List;

public interface JwtService {
    String generateAccessToken(String username, List<String> authorities);

    String generateRefreshToken(String username, List<String> authorities);

    String extractUsername(String token, TokenType type);
}
