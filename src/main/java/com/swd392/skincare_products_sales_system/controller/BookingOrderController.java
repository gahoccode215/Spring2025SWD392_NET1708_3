package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.request.CategoryCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.FormCreateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.CategoryResponse;
import com.swd392.skincare_products_sales_system.dto.response.FormResponse;
import com.swd392.skincare_products_sales_system.model.BookingOrder;
import com.swd392.skincare_products_sales_system.service.BookingOrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookingOrder")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingOrderController {

    BookingOrderService service;

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



}
