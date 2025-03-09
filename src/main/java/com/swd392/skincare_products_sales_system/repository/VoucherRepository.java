package com.swd392.skincare_products_sales_system.repository;


import com.swd392.skincare_products_sales_system.model.user.Voucher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Optional<Voucher> findByCode(String code);
    @Query("SELECT v FROM Voucher v JOIN v.users u WHERE u.id = :userId")
    Page<Voucher> findAllByUserId(@Param("userId") String userId, Pageable pageable);

    @Query("SELECT v FROM Voucher v")
    Page<Voucher> findAllMyVoucher(Pageable pageable);
    @Query("SELECT p FROM Voucher p ")
    Page<Voucher> findAllByFilterAdmin(Pageable pageable);
}
