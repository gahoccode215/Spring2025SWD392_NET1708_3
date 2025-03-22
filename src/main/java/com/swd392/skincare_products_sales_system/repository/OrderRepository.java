package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.entity.order.Order;
import com.swd392.skincare_products_sales_system.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT x FROM Order x WHERE x.username = :username")
    Page<Order> findAllByFilters(
            @Param("username") String username,
            Pageable pageable);

    Page<Order> findAll(Pageable pageable);

    @Query("SELECT x FROM Order x WHERE x.status = com.swd392.skincare_products_sales_system.enums.OrderStatus.PROCESSING OR x.status = com.swd392.skincare_products_sales_system.enums.OrderStatus.DELIVERING OR x.status = com.swd392.skincare_products_sales_system.enums.OrderStatus.DONE ")
    Page<Order> findAllByFiltersDelivery(Pageable pageable);


    @Modifying
    @Query("DELETE FROM Order o WHERE o.paymentMethod = com.swd392.skincare_products_sales_system.enums.PaymentMethod.VNPAY AND o.paymentStatus = com.swd392.skincare_products_sales_system.enums.PaymentStatus.NOT_PAID")
    void deleteUnpaidVnpayOrders();

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = com.swd392.skincare_products_sales_system.enums.OrderStatus.DONE")
    Double sumTotalAmount();

    @Query("SELECT COUNT(x) FROM Order x WHERE x.status = :status")
    Long countOrdersByStatus(@Param("status") OrderStatus status);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = com.swd392.skincare_products_sales_system.enums.OrderStatus.DONE")
    Long countByStatusDone();

    @Query("SELECT MIN(o.orderDate) FROM Order o WHERE o.status = :status")
    LocalDate getMinOrderDate(OrderStatus status);

    @Query("SELECT MAX(o.orderDate) FROM Order o WHERE o.status = :status")
    LocalDate getMaxOrderDate(OrderStatus status);

    @Query("SELECT o.orderDate, SUM(o.totalAmount) " +
            "FROM Order o " +
            "WHERE o.status = :status " +
            "AND o.orderDate >= :startDate " +
            "AND o.orderDate <= :endDate " +
            "GROUP BY o.orderDate ORDER BY o.orderDate")
    List<Object[]> getRevenueByDateRange(
            @Param("status") OrderStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
