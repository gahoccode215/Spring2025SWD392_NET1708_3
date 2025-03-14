package com.swd392.skincare_products_sales_system.controller.admin;

import com.swd392.skincare_products_sales_system.dto.request.booking_order.AsignExpertRequest;
import com.swd392.skincare_products_sales_system.dto.request.booking_order.FormUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.FormResponse;

import com.swd392.skincare_products_sales_system.entity.booking.BookingOrder;
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

    @PostMapping("/{bookingOrderId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Asign Task Expert", description = "API retrieve Booking Order ")
    public ApiResponse<BookingOrder> updateBookingOrder(@RequestBody @Valid AsignExpertRequest request, @PathVariable Long bookingOrderId) {
        return ApiResponse.<BookingOrder>builder()
                .code(HttpStatus.OK.value())
                .message("Successfully")
                .result(service.asignBookingOrder(request, bookingOrderId))
                .build();
    }

    @GetMapping("/all-booking-order")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get All Booking Order", description = "API get all ")
    public ApiResponse<List<BookingOrder>> getAllBookingOrder() {
        return ApiResponse.<List<BookingOrder>>builder()
                .code(HttpStatus.OK.value())
                .message("Get all BookingOrder successfully")
                .result(service.listAllBookingOrder())
                .build();
    }

    @GetMapping("/booking-order-expert")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Booking Order By Expert", description = "API này là để list ra những đơn liên quan tới thằng expert được giao ")
    public ApiResponse<List<BookingOrder>> getAllBookingOrderOfExpert() {
        return ApiResponse.<List<BookingOrder>>builder()
                .code(HttpStatus.OK.value())
                .message("Get BookingOrder successfully")
                .result(service.getBookingOrderByExpertId())
                .build();
    }


}
