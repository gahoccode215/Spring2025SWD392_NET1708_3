package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.Product;
import com.swd392.skincare_products_sales_system.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByIdAndIsDeletedFalse(String productId);
    boolean existsBySlug(String slug);
    Optional<Product> findBySlug(String slug);
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
}
