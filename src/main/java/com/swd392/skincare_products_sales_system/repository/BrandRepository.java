package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.entity.product.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByIdAndIsDeletedFalse(Long brandId);

    boolean existsBySlug(String slug);

    @Query("SELECT x FROM Brand x WHERE x.slug = :slug AND x.isDeleted = false")
    Optional<Brand> findBySlugAndStatusAndIsDeletedFalse(@Param("slug") String slug);


    @Query("SELECT x FROM Brand x WHERE x.isDeleted = false " +
            "AND (x.name LIKE %:keyword% OR :keyword IS NULL) "
    )
    Page<Brand> findAllByFilters(
            @Param("keyword") String keyword,
            Pageable pageable);
}
