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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/brands")
@RequiredArgsConstructor
@Tag(name = "Brand Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminBrandController {
    BrandService brandService;

    @PostMapping
    @Operation(summary = "Tạo mới hãng (ADMIN, MANAGER)", description = "API Tạo mới hãng")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<BrandResponse> createBrand(@RequestBody @Valid BrandCreationRequest request)
                                                        {
        return ApiResponse.<BrandResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Tạo mới hãng thành công")
                .result(brandService.createBrand(request))
                .build();
    }
    @DeleteMapping("/{brandId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Xóa một hãng (ADMIN, MANAGER)", description = "API Xóa một hãng bằng Id")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<Void> deleteBrand(@PathVariable Long brandId) {
        brandService.deleteBrand(brandId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Xóa hãng thành công")
                .build();
    }
    @GetMapping("/{brandId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Lấy chi tiết một hãng (ADMIN, MANAGER, STAFF)", description = "API Lấy chi tiết một hãng bằng Id")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ApiResponse<BrandResponse> getBrand(@PathVariable Long brandId) {
        return ApiResponse.<BrandResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy chi tiết hãng thành công")
                .result(brandService.getBrandById(brandId))
                .build();
    }

    @PutMapping("/{brandId}")
    @Operation(summary = "Cập nhật một hãng (ADMIN, MANAGER)", description = "API Cập nhật một hãng bằng Id")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<BrandResponse> updateBrand(@RequestBody @Valid BrandUpdateRequest request, @PathVariable Long brandId) {
        return ApiResponse.<BrandResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Cập nhật hãng thành công")
                .result(brandService.updateBrand(request, brandId))
                .build();
    }
    @GetMapping
    @Operation(summary = "Lấy danh sách hãng (ADMIN, MANAGER, STAFF)", description = "API Lấy danh sách hãng với phân trang, search, order")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ApiResponse<BrandPageResponse> getAllBrands(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "100") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order) {
        return ApiResponse.<BrandPageResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách hãng thành công")
                .result(brandService.getBrands( keyword, page, size, sortBy, order))
                .build();
    }
}
