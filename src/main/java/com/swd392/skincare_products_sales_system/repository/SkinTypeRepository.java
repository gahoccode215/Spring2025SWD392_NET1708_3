package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.SkinType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkinTypeRepository extends JpaRepository<SkinType, Long> {
    Optional<SkinType> findByIdAndIsDeletedFalse(Long skinTypeId);
}
