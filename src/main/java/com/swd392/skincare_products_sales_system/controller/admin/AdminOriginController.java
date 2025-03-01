package com.swd392.skincare_products_sales_system.controller.admin;

import com.swd392.skincare_products_sales_system.dto.request.product.OriginCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.product.OriginUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.*;
import com.swd392.skincare_products_sales_system.dto.response.product.OriginPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.OriginResponse;
import com.swd392.skincare_products_sales_system.service.OriginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/origins")
@Tag(name = "Origin Controller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminOriginController {

    OriginService originService;

    @PostMapping
    @Operation(summary = "Create origin (ADMIN, MANAGER)", description = "API retrieve attribute to create origin")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OriginResponse> createOrigin(@RequestBody @Valid OriginCreationRequest request)
    {

        return ApiResponse.<OriginResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create origin successfully")
                .result(originService.createOrigin(request))
                .build();
    }
    @DeleteMapping("/{originId}")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Delete a origin (ADMIN, MANAGER)", description = "API delete origin by its id")
    public ApiResponse<Void> deleteOrigin(@PathVariable Long originId) {
        originService.deleteOrigin(originId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete origin successfully")
                .build();
    }
    @GetMapping("/{originId}")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get a origin (ADMIN, MANAGER)", description = "API get origin by its id")
    public ApiResponse<OriginResponse> getOrigin(@PathVariable Long originId) {
        return ApiResponse.<OriginResponse>builder()
                .code(HttpStatus.OK.value())
                .message("get origin detail successfully")
                .result(originService.getOriginById(originId))
                .build();
    }
    @PutMapping("/{originId}")
    @Operation(summary = "Update a origin (ADMIN, MANAGER)", description = "API retrieve origin id to update origin")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<OriginResponse> updateOrigin(@RequestBody @Valid OriginUpdateRequest request, @PathVariable Long originId) {
        return ApiResponse.<OriginResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update origin successfully")
                .result(originService.update(request, originId))
                .build();
    }
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
                .result(originService.getAll(true, keyword, page, size, sortBy, order))
                .build();
    }
}
