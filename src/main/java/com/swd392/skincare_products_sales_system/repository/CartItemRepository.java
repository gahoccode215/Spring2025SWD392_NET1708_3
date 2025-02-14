package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.Cart;
import com.swd392.skincare_products_sales_system.model.CartItem;
import com.swd392.skincare_products_sales_system.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
