package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.OrderPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.OrderResponse;
import com.swd392.skincare_products_sales_system.enums.PaymentMethod;
import com.swd392.skincare_products_sales_system.service.OrderService;
import com.swd392.skincare_products_sales_system.service.impl.VNPayService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order Controller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderController {
    OrderService orderService;
    VNPayService vnPayService;


    @GetMapping("/history-order")
    public ApiResponse<OrderPageResponse> getHistoryOrder(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ApiResponse.<OrderPageResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get order history")
                .result(orderService.getOrdersByCustomer(page, size))
                .build();
    }

    @PostMapping("/checkout")
    public ApiResponse<OrderResponse> checkout(@RequestParam("addressId") Long addressId, @RequestParam("cartId") Long cartId,
                                               @RequestParam("paymentMethod") PaymentMethod paymentMethod, HttpServletRequest request) throws UnsupportedEncodingException {
        String clientIp = getClientIp(request);
        OrderResponse orderResponse = orderService.createOrder(cartId, addressId, paymentMethod);
        if (paymentMethod == PaymentMethod.VNPAY) {
            return ApiResponse.<OrderResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Redirecting to VNPay")
                    .result(orderResponse)
                    .redirectUrl(vnPayService.createPaymentUrl(orderResponse.getOrderId(), orderResponse.getTotalAmount(), clientIp))
                    .build();
        }
        return ApiResponse.<OrderResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Order created successfully")
                .result(orderResponse)
                .build();
    }

    @GetMapping("/payment-callback")
    public ApiResponse<String> handlePaymentCallback(@RequestParam Map<String, String> params) throws UnsupportedEncodingException {

        boolean isValid = vnPayService.validateCallback(params);
        if (!isValid) {
            log.error("Invalid signature in VNPay callback.");
            return ApiResponse.<String>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Invalid Signature")
                    .build();
        }

        String orderId = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");
        boolean isPaid = "00".equals(responseCode);

        // Cập nhật trạng thái đơn hàng
        orderService.updateOrderStatus(Long.parseLong(orderId), isPaid);

        if (isPaid) {
            return ApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("Payment successful, order confirmed.")
                    .build();
        } else {
            return ApiResponse.<String>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Payment verification failed.")
                    .build();
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }

}
