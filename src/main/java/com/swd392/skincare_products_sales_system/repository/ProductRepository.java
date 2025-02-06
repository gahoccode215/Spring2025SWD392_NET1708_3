package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.Product;
import com.swd392.skincare_products_sales_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findByIdAndIsDeletedFalse(String productId);
    boolean existsBySlug(String slug);
}
