package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.response.OrderResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    String createPaymentUrl(OrderResponse order, HttpServletRequest request);
    boolean checkSignature(HttpServletRequest request);
}
