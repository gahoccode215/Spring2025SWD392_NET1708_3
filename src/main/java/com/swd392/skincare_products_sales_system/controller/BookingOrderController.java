package com.swd392.skincare_products_sales_system.controller;

import ch.qos.logback.classic.Logger;
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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
@CrossOrigin("*")
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
    public ApiResponse<BookingOrder> changeStatus(@RequestBody @Valid ChangeStatus status, @PathVariable Long bookingOrderId) {
        return ApiResponse.<BookingOrder>builder()
                .code(HttpStatus.OK.value())
                .message("Change a status successfully ")
                .result(service.changeStatus(status, bookingOrderId))
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

    @PostMapping("/payment")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Payment", description = "API get payment ")
    public ApiResponse<PaymentOrderResponse> paymentBookingOrder(@RequestParam @Valid Long bookingOrderId ,
                                                                 HttpServletRequest http) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String clientIp = getClientIp(http);

        PaymentOrderResponse bookingOrder = service.paymentBookingOrder(bookingOrderId,clientIp);
        return ApiResponse.<PaymentOrderResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Payment successfully")
                .result(bookingOrder)
                .redirectUrl(vnPayService.createPaymentUrlBookingOrder(bookingOrderId, Double.valueOf(bookingOrder.getPrice()), clientIp))
                .build();
    }

    @GetMapping("/payment-back")
    public ResponseEntity<ApiResponse<String>> handlePaymentBack(@RequestParam Map<String, String> params) throws UnsupportedEncodingException {
        boolean isValid = vnPayService.validateCallback(params);
        if (!isValid) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<String>builder()
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message("Invalid Signature")
                            .build()
            );
        }

        Long bookingOrderId = Long.valueOf(params.get("vnp_TxnRef"));
        String responseCode = params.get("vnp_ResponseCode");
        boolean isPaid = "00".equals(responseCode);

        service.updateBookingOrderStatus(bookingOrderId, isPaid);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .code(isPaid ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value())
                        .message(isPaid ? "Payment successful" : "Payment failed")
                        .build()
        );
    }



    private String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }

    @PatchMapping("/{bookingOrderId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Cancel Booking Order", description = "Customer want to stop your order ")
    public ApiResponse<BookingOrder> cancelBookingOrder(@PathVariable Long bookingOrderId, @RequestBody @Valid String note) {
        return ApiResponse.<BookingOrder>builder()
                .code(HttpStatus.OK.value())
                .message("Get filterListExpert successfully")
                .result(service.cancelBookingOrder(bookingOrderId, note))
                .build();
    }



}
