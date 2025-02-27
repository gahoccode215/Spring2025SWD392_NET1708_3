package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.response.cart.CartResponse;

import java.util.List;

public interface CartService {
    void addProductToCart(String productId, Integer quantity);
    void removeProductFromCart(List<String> productIds);
    CartResponse getCart();
    void updateProductQuantityInCart(String productId, Integer quantity);
}
