package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.OriginPageResponse;
import com.swd392.skincare_products_sales_system.service.OriginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/origins")
@Tag(name = "Origin Controller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OriginController {

    OriginService originService;
    @GetMapping
    @Operation(summary = "Get all origins (ADMIN, MANAGER)", description = "Retrieve all origins with pagination, sorting, and filtering.")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<OriginPageResponse> getAllBrands(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "100") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order) {
        return ApiResponse.<OriginPageResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get brands successfully")
                .result(originService.getAll(false, keyword, page, size, sortBy, order))
                .build();
    }
}
