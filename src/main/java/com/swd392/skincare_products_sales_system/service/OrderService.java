package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.response.OrderResponse;
import com.swd392.skincare_products_sales_system.enums.PaymentMethod;
import com.swd392.skincare_products_sales_system.model.Order;

public interface OrderService {
    OrderResponse createOrder(Long cartId, Long addressId, PaymentMethod paymentMethod);
    Order getOrderById(Long orderId);
    void confirmOrder(Long orderId);
}
