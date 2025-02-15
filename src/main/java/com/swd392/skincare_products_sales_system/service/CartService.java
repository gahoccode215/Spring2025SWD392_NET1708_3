package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.model.Cart;

import java.util.List;

public interface CartService {
    Cart addProductToCart(String productId, Integer quantity);
    Cart getCart();
    void removeProductsFromCart(List<String> productIds);
}
