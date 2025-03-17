package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.entity.product.Feedback;
import com.swd392.skincare_products_sales_system.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long > {
    List<Feedback> findByProduct(Product product);
    List<Feedback> findAllByProductId(String productId);
}
