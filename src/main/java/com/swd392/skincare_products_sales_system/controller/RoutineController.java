package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.request.routine.RoutineCreateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.RoutineResponse;
import com.swd392.skincare_products_sales_system.entity.product.Product;
import com.swd392.skincare_products_sales_system.entity.routine.DailyRoutine;
import com.swd392.skincare_products_sales_system.entity.routine.Routine;
import com.swd392.skincare_products_sales_system.repository.ProductRepository;
import com.swd392.skincare_products_sales_system.service.RoutineService;
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
@RequestMapping("/routine")
@RequiredArgsConstructor
@Tag(name = "Routine Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoutineController {

    RoutineService service;
    ProductRepository productRepository;

    @PostMapping("/{bookingOrderId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a routine skincare for customer",
            description = "A Booking Order have only one routine skincare of customer"
    )
    public ApiResponse<RoutineResponse> makeRoutine (@Valid @RequestBody RoutineCreateRequest routineCreateRequest,@PathVariable Long bookingOrderId) {
        return ApiResponse.<RoutineResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Submit Quiz successfully")
                .result(service.makeRoutine(routineCreateRequest,bookingOrderId))
                .build();
    }

    @GetMapping("/get-product")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Create a routine skincare for customer",
            description = "A Booking Order have only one routine skincare of customer"
    )
    public ApiResponse<List<Product>> listProduct () {
        List<Product> list = productRepository.findAll();
        return ApiResponse.<List<Product>>builder()
                .code(HttpStatus.OK.value())
                .message("Submit Quiz successfully")
                .result(list)
                .build();
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Lấy tất cả routine", description = "Customer want to stop your order ")
    public ApiResponse<List<RoutineResponse>> getAllRoutine() {
        return ApiResponse.<List<RoutineResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy tất cả routine thành công")
                .result(service.getAllRoutines())
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Lấy tất cả routine", description = "Customer want to stop your order ")
    public ApiResponse<RoutineResponse> getRoutineById(@PathVariable Long id) {
        return ApiResponse.<RoutineResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy routine thành công")
                .result(service.getRoutineById(id))
                .build();
    }

    @GetMapping("/customer")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Routine cua thằng customer", description = "")
    public ApiResponse<List<RoutineResponse>> getRoutineOfCustomer() {
        return ApiResponse.<List<RoutineResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy routine thành công")
                .result(service.getRoutineOfCustomer())
                .build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Cap nhat trang thai routine", description = "")
    public ApiResponse<Routine> updateStatusStep(@PathVariable Long id, @RequestBody Long bookingOrderId ) {
        return ApiResponse.<Routine>builder()
                .code(HttpStatus.OK.value())
                .message("Cap nhat thành công")
                .result(service.updateStatusRoutine(id,bookingOrderId))
                .build();
    }



}
