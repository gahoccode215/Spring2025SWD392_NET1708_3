package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.entity.product.Batch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface BatchRepository extends JpaRepository<Batch, String >{
    @Query("SELECT p FROM Batch p WHERE p.product.id = :productId ORDER BY p.expirationDate DESC")
    Page<Batch> findAllByProductIdAnd(@Param("productId") String productId, Pageable pageable);
    @Query("SELECT p FROM Batch p WHERE p.product.id = :productId ORDER BY p.expirationDate ASC ")
    List<Batch> findAllByProductId(@Param("productId") String productId);

    @Query("SELECT b FROM Batch b WHERE b.product.id = :productId AND b.quantity > 0 ORDER BY b.expirationDate ASC LIMIT 1")
    Batch findFirstBatchByProductIdAndQuantityGreaterThanZero(@Param("productId") String productId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Batch b WHERE b.id = :batchId")
    void deleteById(@Param("batchId") String batchId);

}
