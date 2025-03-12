package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.model.booking.ProcessBookingOrder;
import com.swd392.skincare_products_sales_system.service.ProcessBookingOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/process-booking")
@Tag(name = "Process Booking Controller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProcessBookingOrderController {

    ProcessBookingOrderService processBookingOrderService;
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Lấy tất cả Process Booking",
            description = "Lấy tất cả Process"
    )
    public ApiResponse<List<ProcessBookingOrder>> listProcessBookingOrder () {
        return ApiResponse.<List<ProcessBookingOrder>>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy tất cả Process Booking thành công")
                .result(processBookingOrderService.getProcessBookingOrderService())
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Lấy tất cả Process Booking",
            description = "Lấy tất cả Process"
    )
    public ApiResponse<ProcessBookingOrder> getProcessBookingOrderById (@PathVariable Long id) {
        return ApiResponse.<ProcessBookingOrder>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy tất cả Process Booking thành công")
                .result(processBookingOrderService.getProcessBookingOrderServiceById(id))
                .build();
    }
}
