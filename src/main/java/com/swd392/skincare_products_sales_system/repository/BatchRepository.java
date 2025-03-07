package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.product.Batch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface BatchRepository extends JpaRepository<Batch, String >{
    @Query("SELECT p FROM Batch p WHERE p.product.id = :productId ORDER BY p.expirationDate DESC")
    Page<Batch> findAllByProductIdAnd(@Param("productId") String productId, Pageable pageable);
    @Query("SELECT p FROM Batch p WHERE p.product.id = :productId ORDER BY p.expirationDate ASC ")
    List<Batch> findAllByProductId(@Param("productId") String productId);
}
