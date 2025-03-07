package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.request.booking_order.SkincareCreateRequest;
import com.swd392.skincare_products_sales_system.dto.request.booking_order.SkincareUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.SkincareServiceResponse;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.SkincareService;
import com.swd392.skincare_products_sales_system.repository.SkincareServiceRepository;
import com.swd392.skincare_products_sales_system.service.SkincareServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skincare-service")
@RequiredArgsConstructor
@Tag(name = "SkincareService Controller")
public class SkincareServiceController {

    private final SkincareServiceInterface service;
    SkincareServiceRepository serviceRepository;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a service", description = "API to create a new skincare service")
    public ApiResponse<SkincareServiceResponse> createSkincareService(@Valid @RequestBody SkincareCreateRequest request) {
        SkincareServiceResponse response = service.createSkincareService(request);
        return ApiResponse.<SkincareServiceResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create service successfully")
                .result(response)
                .build();
    }

    @PutMapping("/{skincareServiceId}")
    @Operation(summary = "Update a service", description = "API Service")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<SkincareServiceResponse> updateSkincareService(@RequestBody @Valid SkincareUpdateRequest request, @PathVariable Long skincareServiceId) {

        return ApiResponse.<SkincareServiceResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update service successfully")
                .result(service.updateSkincareService(request, skincareServiceId))
                .build();
    }

    @GetMapping("/{skincareServiceId}")
    @Operation(summary = "Get a service by Id", description = "API retrieve id to get service")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<SkincareServiceResponse> getSkincareService(@PathVariable Long skincareServiceId) {
        return ApiResponse.<SkincareServiceResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Delete service successfully")
                .result(service.getSkincareServiceById(skincareServiceId))
                .build();
    }

    @DeleteMapping("/{skincareServiceId}")
    @Operation(summary = "Delete a service", description = "API retrieve id to delete service")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> deleteSkincareService(@PathVariable Long skincareServiceId) {
        service.deleteService(skincareServiceId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete service successfully")
                .build();
    }

    @PatchMapping("/change-status/{skincareServiceId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change service status (ADMIN, MANAGER)", description = "API to change status service (ACTIVE/INACTIVE)")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<Void> changeSkincareServiceStatus(@PathVariable Long skincareServiceId, @RequestParam Status status) {
        service.changeStatusService(skincareServiceId, status);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Change status service successfully")
                .build();
    }

    @GetMapping("/system/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List voucher (ADMIN, MANAGER)", description = "Thấy được tất cả voucher kể cả đã xoá hay inactive")
    public ApiResponse<List<SkincareService>> getAllSkincare() {

        return ApiResponse.<List<SkincareService>>builder()
                .code(HttpStatus.OK.value())
                .result(service.getAllSkincareService())
                .message("Get all service successfully")
                .build();
    }

    @GetMapping("/alls")
    @Operation(summary = "For all role(guest) ", description = "Dành cho tất cả các role thấy được voucher đang hoạt động")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<SkincareService>> getAllSkincares() {
        return ApiResponse.<List<SkincareService>>builder()
                .result(service.getAllSkincareServices())
                .code(HttpStatus.OK.value())
                .message("List Voucher")
                .build();
    }
}
