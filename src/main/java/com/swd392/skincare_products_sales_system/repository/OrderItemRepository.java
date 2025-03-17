package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.dto.response.TopSellingProductResponse;
import com.swd392.skincare_products_sales_system.enums.PaymentMethod;
import com.swd392.skincare_products_sales_system.enums.PaymentStatus;
import com.swd392.skincare_products_sales_system.entity.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Modifying
    @Query("DELETE FROM OrderItem oi WHERE oi.order.paymentMethod = :paymentMethod AND oi.order.paymentStatus = :paymentStatus")
    void deleteByOrderPaymentMethodAndStatus(@Param("paymentMethod") PaymentMethod paymentMethod, @Param("paymentStatus") PaymentStatus paymentStatus);
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi")
    Long getTotalQuantitySold();

    @Query(value = "SELECT oi.product.name AS productName, SUM(oi.quantity) AS sellingQuantity " +
            "FROM tbl_order_item oi " +
            "GROUP BY oi.product_id " +
            "ORDER BY SUM(oi.quantity) DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<TopSellingProductResponse> findTopSellingProducts(int limit);
}
