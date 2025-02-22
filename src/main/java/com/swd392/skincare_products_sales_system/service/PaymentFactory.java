package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.enums.PaymentMethod;
import com.swd392.skincare_products_sales_system.service.impl.VNPayPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentFactory {
    private final VNPayPaymentService vnPayPaymentService;

    public PaymentService getPaymentService(PaymentMethod method) {
        return switch (method) {
            case VNPAY -> vnPayPaymentService;
//            case MOMO -> moMoPaymentService;
            default -> throw new IllegalArgumentException("Không hỗ trợ phương thức thanh toán này!");
        };
    }
}
