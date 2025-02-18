package com.swd392.skincare_products_sales_system.controller.admin;

import com.swd392.skincare_products_sales_system.dto.request.BrandCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.CategoryCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.ProductCreationRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.BrandResponse;
import com.swd392.skincare_products_sales_system.dto.response.CategoryResponse;
import com.swd392.skincare_products_sales_system.dto.response.ProductResponse;
import com.swd392.skincare_products_sales_system.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin/brands")
@RequiredArgsConstructor
@Tag(name = "Brand Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminBrandController {
    BrandService brandService;

    @PostMapping
    @Operation(summary = "Create brand (ADMIN, MANAGER)", description = "API retrieve attribute to create brand")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<BrandResponse> createBrand(@RequestPart("request") @Valid BrandCreationRequest request,
                                                        @RequestPart("thumbnail") MultipartFile thumbnail) throws IOException {
        request.setThumbnail(thumbnail);
        return ApiResponse.<BrandResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create brand successfully")
                .result(brandService.createBrand(request))
                .build();
    }
    @DeleteMapping("/{brandId}")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Delete a brand (ADMIN, MANAGER)", description = "API delete brand by its id")
    public ApiResponse<Void> deleteBrand(@PathVariable Long brandId) {
        brandService.deleteBrand(brandId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete brand successfully")
                .build();
    }
    @GetMapping("/{brandId}")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get a brand (ADMIN, MANAGER)", description = "API get brand by its id")
    public ApiResponse<BrandResponse> getCategory(@PathVariable Long brandId) {
        return ApiResponse.<BrandResponse>builder()
                .code(HttpStatus.OK.value())
                .message("get brand detail successfully")
                .result(brandService.getBrandById(brandId))
                .build();
    }
}
