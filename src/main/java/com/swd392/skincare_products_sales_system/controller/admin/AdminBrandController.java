package com.swd392.skincare_products_sales_system.controller.admin;

import com.swd392.skincare_products_sales_system.dto.request.product.BrandCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.product.BrandUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.*;
import com.swd392.skincare_products_sales_system.dto.response.product.BrandPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.BrandResponse;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse<BrandResponse> createBrand(@RequestBody @Valid BrandCreationRequest request)
                                                        {

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
    public ApiResponse<BrandResponse> getBrand(@PathVariable Long brandId) {
        return ApiResponse.<BrandResponse>builder()
                .code(HttpStatus.OK.value())
                .message("get brand detail successfully")
                .result(brandService.getBrandById(brandId))
                .build();
    }

    @PutMapping("/{brandId}")
    @Operation(summary = "Update a brand (ADMIN, MANAGER)", description = "API retrieve brand id to update brand")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<BrandResponse> updateBrand(@RequestBody @Valid BrandUpdateRequest request, @PathVariable Long brandId) {
        return ApiResponse.<BrandResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update brand successfully")
                .result(brandService.updateBrand(request, brandId))
                .build();
    }
    @GetMapping
    @Operation(summary = "Get all brands (ADMIN, MANAGER)", description = "Retrieve all brands with pagination, sorting, and filtering.")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<BrandPageResponse> getAllBrands(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "100") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order) {
        return ApiResponse.<BrandPageResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get brands successfully")
                .result(brandService.getBrands( keyword, page, size, sortBy, order))
                .build();
    }
}
