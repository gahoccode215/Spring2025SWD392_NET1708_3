package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.response.order.OrderPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.order.OrderResponse;
import com.swd392.skincare_products_sales_system.enums.OrderStatus;
import com.swd392.skincare_products_sales_system.enums.PaymentMethod;

public interface OrderService {
    OrderResponse createOrder(Long cartId, Long addressId, PaymentMethod paymentMethod);
    void updateOrderStatus(Long orderId, boolean isPaid);
    OrderPageResponse getOrdersByCustomer(int page, int size);
    OrderPageResponse getOrdersByAdmin(int page, int size);
    OrderResponse getOrderById(Long id);
    void changeOrderStatus(Long id, OrderStatus orderStatus);
}
