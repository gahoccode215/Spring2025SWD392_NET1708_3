package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.model.routine.Step;
import com.swd392.skincare_products_sales_system.service.StepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/step")
@RequiredArgsConstructor
@Tag(name = "Step Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StepController {

    StepService service;

    @PutMapping("/{stepId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Routine cua thằng customer", description = "")
    public ApiResponse<Step> updateStatusStep(@PathVariable Long stepId) {
        return ApiResponse.<Step>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy routine thành công")
                .result(service.updateStep(stepId))
                .build();
    }
}
