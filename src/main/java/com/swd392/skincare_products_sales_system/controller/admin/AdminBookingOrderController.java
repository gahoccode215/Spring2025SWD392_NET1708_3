package com.swd392.skincare_products_sales_system.controller.admin;

import com.swd392.skincare_products_sales_system.dto.request.booking_order.ChangeStatus;
import com.swd392.skincare_products_sales_system.dto.request.booking_order.FormCreateRequest;
import com.swd392.skincare_products_sales_system.dto.request.booking_order.FormUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.FormResponse;
import com.swd392.skincare_products_sales_system.model.BookingOrder;
import com.swd392.skincare_products_sales_system.service.BookingOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/booking-order")
@RequiredArgsConstructor
@Tag(name = "BookingOrder Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminBookingOrderController {
    BookingOrderService service;

    @PutMapping("/{bookingOrderId})")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a Booking Order", description = "API retrieve Booking Order ")
    public ApiResponse<FormResponse> updateBookingOrder(@RequestBody @Valid FormUpdateRequest request, @PathVariable Long bookingOrderId) {
        return ApiResponse.<FormResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update Booking Order successfully")
                .result(service.updateBookingAdvise(request, bookingOrderId))
                .build();
    }


}
