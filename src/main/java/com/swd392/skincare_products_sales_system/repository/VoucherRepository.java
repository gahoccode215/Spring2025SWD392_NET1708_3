package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.Question;
import com.swd392.skincare_products_sales_system.model.Voucher;
import com.swd392.skincare_products_sales_system.model.product.Brand;
import com.swd392.skincare_products_sales_system.model.product.Category;
import com.swd392.skincare_products_sales_system.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Optional<Voucher> findByCode(String code);
    @Query("SELECT p FROM Voucher p WHERE (:status is null OR p.status = :status)")
    Page<Voucher> findAllByFilter(
            @Param("status") Status status,
            Pageable pageable);
    @Query("SELECT p FROM Voucher p ")
    Page<Voucher> findAllByFilterAdmin(
            Pageable pageable);
}
