package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void delete(CartItem cartItem);
}
