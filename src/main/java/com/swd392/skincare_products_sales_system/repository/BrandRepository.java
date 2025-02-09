package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByIdAndIsDeletedFalse(Long brandId);
}
