package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.entity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void delete(CartItem cartItem);
}
