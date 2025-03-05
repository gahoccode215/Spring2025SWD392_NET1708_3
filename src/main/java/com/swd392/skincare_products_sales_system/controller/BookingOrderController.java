package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.request.booking_order.ChangeStatus;
import com.swd392.skincare_products_sales_system.dto.request.booking_order.FormCreateRequest;
import com.swd392.skincare_products_sales_system.dto.request.booking_order.PaymentBookingOrderRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.ExpertResponse;
import com.swd392.skincare_products_sales_system.dto.response.FormResponse;
import com.swd392.skincare_products_sales_system.dto.response.PaymentOrderResponse;
import com.swd392.skincare_products_sales_system.dto.response.order.OrderResponse;
import com.swd392.skincare_products_sales_system.enums.PaymentMethod;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.BookingOrder;
import com.swd392.skincare_products_sales_system.model.User;
import com.swd392.skincare_products_sales_system.repository.BookingRepository;
import com.swd392.skincare_products_sales_system.service.BookingOrderService;
import com.swd392.skincare_products_sales_system.service.VNPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aspectj.apache.bcel.Repository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/booking-order")
@RequiredArgsConstructor
@Tag(name = "BookingOrder Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingOrderController {

    BookingOrderService service;
    BookingRepository bookingRepository;
    VNPayService vnPayService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a Booking Order", description = "API retrieve Booking Order ")
    public ApiResponse<FormResponse> createCategory(@RequestBody @Valid FormCreateRequest request) {
        return ApiResponse.<FormResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create Booking Order successfully")
                .result(service.bookingAdvise(request))
                .build();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change a status to do Booking Order", description = "API retrieve Booking Order ")
    public ApiResponse<BookingOrder> changeStatus(@RequestBody @Valid ChangeStatus status) {
        return ApiResponse.<BookingOrder>builder()
                .code(HttpStatus.OK.value())
                .message("Change a status successfully ")
                .result(service.changeStatus(status))
                .build();
    }

    @GetMapping("/filter-user")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a Booking Order By Self", description = "API retrieve Booking Order ")
    public ApiResponse<List<BookingOrder>> getAllBookingOrderFilter() {
        return ApiResponse.<List<BookingOrder>>builder()
                .code(HttpStatus.OK.value())
                .message("Update Booking Order successfully")
                .result(service.getBookingOrder())
                .build();
    }

//    filterListExpert
    @GetMapping("/filter-expert")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a filterListExpert", description = "API get filterListExpert ")
    public ApiResponse<List<ExpertResponse>> getFilterListExpert() {
        return ApiResponse.<List<ExpertResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get filterListExpert successfully")
                .result(service.filterListExpert())
                .build();
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a filterListExpert", description = "API get filterListExpert ")
    public ApiResponse<List<BookingOrder>> getAllBookingOrder() {
        List<BookingOrder> list = bookingRepository.findAll()
                .stream()
                .filter(b -> !b.getIsDeleted() && b.getStatus().equals(Status.ACTIVE))
                .toList();
        return ApiResponse.<List<BookingOrder>>builder()
                .code(HttpStatus.OK.value())
                .message("Get all BookingOrder successfully")
                .result(list)
                .build();
    }

    @PostMapping("/payment/{bookingOrderId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Payment", description = "API get payment ")
    public ApiResponse<PaymentOrderResponse> paymentBookingOrder(@RequestBody @Valid PaymentBookingOrderRequest request,
                                                                 @PathVariable Long bookingOrderId,
                                                                 HttpServletRequest http) throws UnsupportedEncodingException {
        String clientIp = getClientIp(http);

        PaymentOrderResponse bookingOrder = service.paymentBookingOrder(request, bookingOrderId, clientIp);
        return ApiResponse.<PaymentOrderResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Payment successfully")
                .result(bookingOrder)
                .redirectUrl(vnPayService.createPaymentUrlBookingOrder(request, clientIp))
                .build();
    }

    @PostMapping("/test")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Payment", description = "API to create a payment for booking order")
    public ResponseEntity<String> test(
            @RequestBody @Valid PaymentBookingOrderRequest request,
            HttpServletRequest http) throws UnsupportedEncodingException {

        try {
            String clientIp = getClientIp(http);
            String paymentUrl = vnPayService.createPaymentUrlBookingOrder(request, clientIp);
            return ResponseEntity.ok(paymentUrl);
        } catch (UnsupportedEncodingException e) {
            // Log the error and return an appropriate response
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating payment URL: " + e.getMessage());
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
