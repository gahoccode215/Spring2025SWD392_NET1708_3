package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.entity.routine.DailyRoutine;
import com.swd392.skincare_products_sales_system.entity.routine.Routine;
import com.swd392.skincare_products_sales_system.entity.routine.Step;
import com.swd392.skincare_products_sales_system.service.DailyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/daily-routine")
@Tag(name = "Daily Routine Controller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DailyRoutineController {

    DailyService service;
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Routine cua thằng customer", description = "")
    public ApiResponse<DailyRoutine> updateStatusStep(@PathVariable Long id) {
        return ApiResponse.<DailyRoutine>builder()
                .code(HttpStatus.OK.value())
                .message("Cap nhat thành công")
                .result(service.updateDailyRoutine(id))
                .build();
    }
}
