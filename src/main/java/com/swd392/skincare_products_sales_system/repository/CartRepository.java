package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.entity.cart.Cart;
import com.swd392.skincare_products_sales_system.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(String userId);
//    Optional<Cart> findByUsername(String user);
    Optional<Cart> findByUser(User user);
}
