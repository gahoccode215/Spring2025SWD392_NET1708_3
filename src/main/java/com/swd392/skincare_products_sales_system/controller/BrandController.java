package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.BrandPageResponse;
import com.swd392.skincare_products_sales_system.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/brands")
@Tag(name = "Brand Controller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandController {
    BrandService brandService;

    @GetMapping
    @Operation(summary = "Get all brands  ", description = "Retrieve all active brands with pagination, sorting, and filtering.")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<BrandPageResponse> getAllCategories(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "100") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order) {
        return ApiResponse.<BrandPageResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get brands successfully")
                .result(brandService.getBrands(false, keyword, page, size, sortBy, order))
                .build();
    }
}
