package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.OrderResponse;
import com.swd392.skincare_products_sales_system.enums.PaymentMethod;
import com.swd392.skincare_products_sales_system.service.OrderService;
import com.swd392.skincare_products_sales_system.service.PaymentFactory;
import com.swd392.skincare_products_sales_system.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order Controller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;
    PaymentFactory paymentFactory;

    @PostMapping("/checkout")
    public ApiResponse<OrderResponse> checkout(@RequestParam("addressId") Long addressId, @RequestParam("cartId") Long cartId,
                                               @RequestParam("paymentMethod") PaymentMethod paymentMethod) {
        OrderResponse orderResponse = orderService.createOrder(cartId, addressId, paymentMethod);
        if(paymentMethod != PaymentMethod.COD){
            String paymentUrl = paymentFactory.getPaymentService(paymentMethod).createPaymentUrl(orderResponse);
            return ApiResponse.<OrderResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Redirecting to VNPay")
                    .result(orderResponse)
                    .redirectUrl(paymentUrl)
                    .build();
        }
        return ApiResponse.<OrderResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Order created successfully")
                .result(orderResponse)
                .build();
    }

}
