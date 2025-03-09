package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.order.OrderPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.order.OrderResponse;
import com.swd392.skincare_products_sales_system.enums.PaymentMethod;
import com.swd392.skincare_products_sales_system.service.OrderService;
import com.swd392.skincare_products_sales_system.service.VNPayService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ApiResponse<OrderPageResponse> getHistoryOrder(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        orderService.removeUnpaidVnpayOrders();
        return ApiResponse.<OrderPageResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách mua hàng thành công")
                .result(orderService.getOrdersByCustomer(page, size))
                .build();
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ApiResponse<Void> checkout(@RequestParam("addressId") Long addressId, @RequestParam("cartId") Long cartId, @RequestParam(required = false) String voucherCode,
                                      @RequestParam("paymentMethod") PaymentMethod paymentMethod, HttpServletRequest request) throws UnsupportedEncodingException {
        String clientIp = getClientIp(request);
        if (paymentMethod == PaymentMethod.VNPAY) {
            OrderResponse orderResponse = orderService.createOrder(cartId, addressId, paymentMethod, voucherCode);
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("Chuyển hướng sang VNPay")
                    .redirectUrl(vnPayService.createPaymentUrl(orderResponse.getOrderId(), orderResponse.getTotalAmount(), clientIp))
                    .build();
        } else {
            orderService.createOrder(cartId, addressId, paymentMethod, voucherCode);
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("Đặt hàng thành công")
                    .build();
        }
    }

    @GetMapping("/payment-callback")
    public ApiResponse<String> handlePaymentCallback(@RequestParam Map<String, String> params) throws UnsupportedEncodingException {
        boolean isValid = vnPayService.validateCallback(params);
        if (!isValid) {
            return ApiResponse.<String>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Invalid Signature")
                    .build();
        }

        String orderId = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");
        boolean isPaid = "00" .equals(responseCode);

        orderService.updateOrderStatus(Long.parseLong(orderId), isPaid);

        if (isPaid) {
            return ApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("Thanh toán thành công")
                    .build();
        } else {
            orderService.deleteOrder(Long.parseLong(orderId));
            return ApiResponse.<String>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Thanh toán thất bại")
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
