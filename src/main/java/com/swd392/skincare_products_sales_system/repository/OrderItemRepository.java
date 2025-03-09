package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.enums.PaymentMethod;
import com.swd392.skincare_products_sales_system.enums.PaymentStatus;
import com.swd392.skincare_products_sales_system.model.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Modifying
    @Query("DELETE FROM OrderItem oi WHERE oi.order.paymentMethod = :paymentMethod AND oi.order.paymentStatus = :paymentStatus")
    void deleteByOrderPaymentMethodAndStatus(@Param("paymentMethod") PaymentMethod paymentMethod, @Param("paymentStatus") PaymentStatus paymentStatus);
}
