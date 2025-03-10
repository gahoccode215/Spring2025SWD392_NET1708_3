package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.product.FeedBack;
import com.swd392.skincare_products_sales_system.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedBackRepository extends JpaRepository<FeedBack, Long > {
    List<FeedBack> findByProduct(Product product);
    List<FeedBack> findAllByProductId(String productId);
}
