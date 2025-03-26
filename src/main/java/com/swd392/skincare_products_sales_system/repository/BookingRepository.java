
package com.swd392.skincare_products_sales_system.repository;


import com.swd392.skincare_products_sales_system.entity.booking.BookingOrder;
import com.swd392.skincare_products_sales_system.entity.routine.Routine;
import com.swd392.skincare_products_sales_system.entity.user.User;
import com.swd392.skincare_products_sales_system.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookingOrder , Long> {
    Optional<BookingOrder> findByIdAndIsDeletedFalse(Long bookingOrderId);
    long countByUserAndOrderDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate);
    List<BookingOrder> findByUserAndIsDeletedFalse(User user);
    BookingOrder findByRoutine(Routine routine);
    List<BookingOrder> findAllByExpertNameAndIsDeletedFalse(String expertName);


    @Query("SELECT o.orderDate, SUM(o.price) " +
            "FROM BookingOrder o " +
            "WHERE o.paymentStatus = :status " +
            "AND o.orderDate >= :startDate " +
            "AND o.orderDate <= :endDate " +
            "GROUP BY o.orderDate ORDER BY o.orderDate")
    List<Object[]> getRevenueByDateRange(
            @Param("status") PaymentStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
