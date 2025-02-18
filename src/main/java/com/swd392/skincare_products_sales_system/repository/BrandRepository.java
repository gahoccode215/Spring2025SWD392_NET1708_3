package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByIdAndIsDeletedFalse(Long brandId);

    boolean existsBySlug(String slug);

    @Query("SELECT x FROM Brand x WHERE x.slug = :slug AND x.isDeleted = false AND x.status = com.swd392.skincare_products_sales_system.enums.Status.ACTIVE")
    Optional<Brand> findBySlugAndStatusAndIsDeletedFalse(@Param("slug") String slug);
}
