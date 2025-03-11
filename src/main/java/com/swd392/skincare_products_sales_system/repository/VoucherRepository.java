package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Optional<Voucher> findByIdAndIsDeletedFalse(Long voucherId);
    Optional<Voucher> findByVoucherName(String voucherName);
    Optional<Voucher> findByVoucherCode(String voucherCode);

}
