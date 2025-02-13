package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.Category;
import com.swd392.skincare_products_sales_system.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findByIdAndIsDeletedFalse(String id);

    Optional<Category> findBySlugAndIsDeletedFalse(String slug);

    @Query("SELECT c FROM Category c WHERE c.isDeleted = false " +
            "AND c.status = com.swd392.skincare_products_sales_system.enums.Status.ACTIVE " +
            "AND c.slug = :slug")
    Optional<Category> findBySlugAndStatusAndIsDeletedFalse(@Param("slug") String slug);

    boolean existsBySlug(String slug);

    @Query("SELECT c FROM Category c WHERE c.isDeleted = false " +
            "AND (c.name LIKE %:keyword% OR :keyword IS NULL) "
            + "AND (:status is null OR c.status = :status)"
    )
    Page<Category> findAllByFilters(
            @Param("keyword") String keyword,
            @Param("status") Status status,
            Pageable pageable);


    @Modifying
    @Query("UPDATE Category c SET c.status = :status WHERE c.id = :id AND c.isDeleted = false")
    void updateCategoryStatus(@Param("id") String id, @Param("status") Status status);
}
