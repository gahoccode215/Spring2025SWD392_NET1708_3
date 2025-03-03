package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.request.quiz.SubmitQuiz;
import com.swd392.skincare_products_sales_system.dto.request.routine.RoutineCreateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.ResultResponse;
import com.swd392.skincare_products_sales_system.dto.response.RoutineResponse;
import com.swd392.skincare_products_sales_system.service.RoutineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/routine")
@RequiredArgsConstructor
@Tag(name = "Routine Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoutineController {

    RoutineService service;

    @PostMapping("/{bookingOrderId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Submit an quiz",
            description = "API to get an existing quiz by providing the quiz ID and updated attributes such as title, questions, and answers."
    )
    public ApiResponse<RoutineResponse> makeRoutine (@Valid @RequestBody RoutineCreateRequest routineCreateRequest, @PathVariable Long bookingOrderId) {
        return ApiResponse.<RoutineResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Submit Quiz successfully")
                .result(service.makeRoutine(routineCreateRequest, bookingOrderId))
                .build();
    }
}
