package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.request.booking_order.ChangeStatus;
import com.swd392.skincare_products_sales_system.dto.request.booking_order.FormCreateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.ExpertResponse;
import com.swd392.skincare_products_sales_system.dto.response.FormResponse;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.BookingOrder;
import com.swd392.skincare_products_sales_system.model.User;
import com.swd392.skincare_products_sales_system.repository.BookingRepository;
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
@RequestMapping("/booking-order")
@RequiredArgsConstructor
@Tag(name = "BookingOrder Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingOrderController {

    BookingOrderService service;
    BookingRepository bookingRepository;
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


}
