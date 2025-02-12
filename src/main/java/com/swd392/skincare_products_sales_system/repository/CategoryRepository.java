package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.Category;
import com.swd392.skincare_products_sales_system.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findByIdAndIsDeletedFalse(String categoryId);
    boolean existsBySlug(String slug);

    @Query("SELECT x FROM Category x WHERE x.isDeleted = false " +
            "AND x.status = com.swd392.skincare_products_sales_system.enums.Status.ACTIVE " +
            "AND x.slug = :slug")
    Optional<Category> findBySlugAndStatusAndIsDeletedFalse(@Param("slug") String slug);
}
