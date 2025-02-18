package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.Brand;
import com.swd392.skincare_products_sales_system.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query("UPDATE Brand x SET x.status = :status WHERE x.id = :id AND x.isDeleted = false")
    void updateBrandStatus(@Param("id") Long id, @Param("status") Status status);

    @Query("SELECT x FROM Brand x WHERE x.isDeleted = false " +
            "AND (x.name LIKE %:keyword% OR :keyword IS NULL) "
            + "AND (:status is null OR x.status = :status)"
    )
    Page<Brand> findAllByFilters(
            @Param("keyword") String keyword,
            @Param("status") Status status,
            Pageable pageable);
}
