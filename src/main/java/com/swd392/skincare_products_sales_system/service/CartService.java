package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.model.Cart;

public interface CartService {
    Cart addProductToCart(String productId, Integer quantity);
}
