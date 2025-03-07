package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.product.Brand;
import com.swd392.skincare_products_sales_system.model.product.Category;
import com.swd392.skincare_products_sales_system.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND p.status = :status ORDER BY p.createdAt DESC")
    Page<Product> findLatestProductsByStatus(Status status, PageRequest pageRequest);

    Optional<Product> findByIdAndIsDeletedFalse(String productId);

    @Query("SELECT x FROM Product x WHERE x.id = :productId AND x.isDeleted = false AND x.status = com.swd392.skincare_products_sales_system.enums.Status.ACTIVE")
    Optional<Product> findByIdAndIsDeletedFalseAndStatus(@Param("productId") String productId);
    boolean existsBySlug(String slug);

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false " +
            "AND (p.name LIKE %:keyword% OR :keyword IS NULL) " +
            "AND (:category IS NULL OR p.category = :category) " +
            "AND (:brand IS NULL OR p.brand = :brand) " +
            "AND (:status is null OR p.status = :status)")
    Page<Product> findAllByFilters(
            @Param("keyword") String keyword,
            @Param("status") Status status,
            @Param("category") Category category,
            @Param("brand") Brand brand,
            Pageable pageable);

    @Modifying
    @Query("UPDATE Product x SET x.status = :status WHERE x.id = :id AND x.isDeleted = false")
    void updateProductStatus(@Param("id") String id, @Param("status") Status status);

    @Query("SELECT x FROM Product x WHERE x.slug = :slug AND x.isDeleted = false AND x.status = com.swd392.skincare_products_sales_system.enums.Status.ACTIVE")
    Optional<Product> findBySlugAndIsDeletedFalseAndStatus(@Param("slug") String slug);
}
