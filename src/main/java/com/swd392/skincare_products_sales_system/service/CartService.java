package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.response.CartResponse;

public interface CartService {
    void addProductToCart(String productId, Integer quantity);
    void removeProductFromCart(String productId, String username);
    CartResponse getCart();
}
