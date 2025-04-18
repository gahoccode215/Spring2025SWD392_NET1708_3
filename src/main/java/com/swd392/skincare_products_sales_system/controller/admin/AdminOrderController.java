package com.swd392.skincare_products_sales_system.controller.admin;

import com.swd392.skincare_products_sales_system.dto.request.DeliveryRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.order.OrderPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.order.OrderResponse;
import com.swd392.skincare_products_sales_system.enums.OrderStatus;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/orders")
@Tag(name = "Order Controller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminOrderController {
    OrderService orderService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF', 'DELIVERY')")
    public ApiResponse<OrderPageResponse> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        orderService.removeUnpaidVnpayOrders();
        return ApiResponse.<OrderPageResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Xem danh sách đơn hàng thành công")
                .result(orderService.getOrdersByAdmin(page, size))
                .build();
    }

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF', 'DELIVERY')")
    public ApiResponse<OrderResponse> getOrder(@PathVariable Long orderId
    ) {
        orderService.removeUnpaidVnpayOrders();
        return ApiResponse.<OrderResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Xem chi tiết đơn hàng thành công")
                .result(orderService.getOrderById(orderId))
                .build();
    }

    @PatchMapping("/confirm-order/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ApiResponse<Void> confirmOrderByStaff(@PathVariable Long orderId, @RequestParam OrderStatus orderStatus) {
        orderService.removeUnpaidVnpayOrders();
        orderService.confirmOrder(orderId, orderStatus);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Đổi trạng thái sang " + orderStatus + " Thành công")
                .build();
    }

    @PatchMapping("/change-to-delivery/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'DELIVERY', 'STAFF')")
    public ApiResponse<Void> deliveringByDelivery(@PathVariable Long orderId, @RequestParam OrderStatus orderStatus) {
        orderService.removeUnpaidVnpayOrders();
        orderService.deliveringOrder(orderId, orderStatus);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Đổi trạng thái sang " + orderStatus + " Thành công")
                .build();
    }

    @PatchMapping("/delivery/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'DELIVERY')")
    public ApiResponse<Void> deliveryOrder(@PathVariable Long orderId, @RequestParam OrderStatus
            orderStatus, @RequestBody(required = false) DeliveryRequest request) {
        orderService.removeUnpaidVnpayOrders();
        orderService.deliveryOrder(orderId, orderStatus, request);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Cập nhật giao hàng thành công")
                .build();
    }

}