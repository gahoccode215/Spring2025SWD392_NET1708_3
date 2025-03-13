
package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.entity.User;
import com.swd392.skincare_products_sales_system.entity.booking.BookingOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookingOrder , Long> {
    Optional<BookingOrder> findByIdAndIsDeletedFalse(Long bookingOrderId);
    long countByUserAndOrderDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate);
    List<BookingOrder> findByUserAndIsDeletedFalse(User user);
}
