package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.product.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecificationRepository extends JpaRepository<Specification, Long> {
}
