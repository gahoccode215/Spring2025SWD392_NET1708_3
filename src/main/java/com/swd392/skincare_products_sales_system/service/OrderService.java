package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.response.OrderPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.OrderResponse;
import com.swd392.skincare_products_sales_system.enums.PaymentMethod;
import com.swd392.skincare_products_sales_system.model.Order;

import java.util.Map;

public interface OrderService {
    OrderResponse createOrder(Long cartId, Long addressId, PaymentMethod paymentMethod);
    void updateOrderStatus(Long orderId, boolean isPaid);
    OrderPageResponse getOrdersByCustomer(int page, int size);
    OrderPageResponse getOrdersByAdmin(int page, int size);
    OrderResponse getOrderById(Long id);
}
