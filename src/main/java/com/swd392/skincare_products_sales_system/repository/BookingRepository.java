package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.entity.BookingOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookingOrder , Long> {
    Optional<BookingOrder> findByIdAndIsDeletedFalse(Long bookingOrderId);
}
