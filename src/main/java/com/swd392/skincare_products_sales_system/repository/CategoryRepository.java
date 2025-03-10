package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.product.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findByIdAndIsDeletedFalse(String id);

    @Query("SELECT c FROM Category c WHERE c.isDeleted = false " +
            "AND c.slug = :slug")
    Optional<Category> findBySlugAndStatusAndIsDeletedFalse(@Param("slug") String slug);

    boolean existsBySlug(String slug);

    @Query("SELECT c FROM Category c WHERE c.isDeleted = false " +
            "AND (c.name LIKE %:keyword% OR :keyword IS NULL) "
    )
    Page<Category> findAllByFilters(
            @Param("keyword") String keyword,
            Pageable pageable);

}
