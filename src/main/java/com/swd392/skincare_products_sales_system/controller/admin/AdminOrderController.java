package com.swd392.skincare_products_sales_system.controller.admin;

import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.order.OrderPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.order.OrderResponse;
import com.swd392.skincare_products_sales_system.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/orders")
@Tag(name = "Order Controller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminOrderController {
    OrderService orderService;

    @GetMapping()
    @Operation(summary = "Get all orders with options: search, pagination, sort, filter (ADMIN, MANAGER)  ", description = "Retrieve all products with search, pagination, sorting, and filtering.")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<OrderPageResponse> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<OrderPageResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get orders successfully")
                .result(orderService.getOrdersByAdmin(page,size))
                .build();
    }
    @GetMapping("/{orderId}")
    @Operation(summary = "Get a Order by id(ADMIN, MANAGER)  ", description = "Retrieve order by its id")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<OrderResponse> getOrder(@PathVariable Long orderId
    ) {
        return ApiResponse.<OrderResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get order successfully")
                .result(orderService.getOrderById(orderId))
                .build();
    }

}
