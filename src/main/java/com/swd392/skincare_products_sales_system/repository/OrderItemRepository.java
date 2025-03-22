package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.dto.response.TopSellingProductResponse;
import com.swd392.skincare_products_sales_system.enums.PaymentMethod;
import com.swd392.skincare_products_sales_system.enums.PaymentStatus;
import com.swd392.skincare_products_sales_system.entity.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Modifying
    @Query("DELETE FROM OrderItem oi WHERE oi.order.paymentMethod = :paymentMethod AND oi.order.paymentStatus = :paymentStatus")
    void deleteByOrderPaymentMethodAndStatus(@Param("paymentMethod") PaymentMethod paymentMethod, @Param("paymentStatus") PaymentStatus paymentStatus);
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi")
    Long getTotalQuantitySold();

    @Query("SELECT oi.product.name AS product, SUM(oi.quantity) AS quantitySold " +
            "FROM OrderItem oi JOIN oi.order o " +
            "WHERE o.status = 'DONE' " +
            "GROUP BY oi.product.name " +
            "ORDER BY quantitySold DESC")
    List<Object[]> getTopSellingProducts();

    // Hàm lấy các sản phẩm bán chạy nhất theo số lượng
    default List<Map<String, Object>> getTopSellingProducts(int topCount) {
        List<Object[]> topSellingProducts = getTopSellingProducts();
        List<Map<String, Object>> result = new ArrayList<>();

        // Chỉ lấy số lượng sản phẩm theo topCount
        for (int i = 0; i < Math.min(topCount, topSellingProducts.size()); i++) {
            Object[] row = topSellingProducts.get(i);
            Map<String, Object> productData = new HashMap<>();
            productData.put("product", row[0]);
            productData.put("quantitySold", row[1]);
            result.add(productData);
        }
        return result;
    }
}
