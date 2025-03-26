package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.entity.booking.ProcessBookingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessBookingOrderRepository extends JpaRepository<ProcessBookingOrder, Long> {
    List<ProcessBookingOrder> findByBookingOrderId(Long bookingOrderId);
}
