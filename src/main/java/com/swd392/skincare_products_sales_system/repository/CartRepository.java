package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.cart.Cart;
import com.swd392.skincare_products_sales_system.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(String userId);
//    Optional<Cart> findByUsername(String user);
    Optional<Cart> findByUser(User user);
}
