package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.product.Batch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


public interface BatchRepository extends JpaRepository<Batch, Long >{
    @Query("SELECT p FROM Batch p WHERE p.product.id = :productId")
    Page<Batch> findAllByProductIdAnd(@Param("productId") String productId, Pageable pageable);
}
