package com.swd392.skincare_products_sales_system.controller.admin;

import com.swd392.skincare_products_sales_system.dto.request.voucher.VoucherCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.voucher.VoucherUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.VoucherResponse;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/voucher")
@RequiredArgsConstructor
@Tag(name = "Voucher Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminVoucherController {

    VoucherService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a voucher by {ADMIN , MANAGER }", description = "API retrieve service attribute to create service")
    public ApiResponse<VoucherResponse> createVoucher(@Valid @RequestBody VoucherCreationRequest request) {
        return ApiResponse.<VoucherResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create service successfully")
                .result(service.createVoucher(request))
                .build();
    }

    @PutMapping("/{voucherId}")
    @Operation(summary = "Update a voucher", description = "API retrieve value to change service attribute")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<VoucherResponse> updateVoucher(@RequestBody @Valid VoucherUpdateRequest request, @PathVariable Long voucherId) {
        return ApiResponse.<VoucherResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update service successfully")
                .result(service.updateVoucher(request, voucherId
                ))
                .build();
    }

    @DeleteMapping("/{voucherId}")
    @Operation(summary = "Delete a voucher", description = "API retrieve id to delete service")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> deleteVoycher (@PathVariable Long voucherId) {
        service.deleteVoucher(voucherId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete service successfully")
                .build();
    }

    @PatchMapping("/{voucherID}")
    @Operation(summary = "Change status  voucher", description = "API retrieve id to change status")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> changeStatusVoucher (@PathVariable Long voucherId, @RequestParam Status status) {
        service.changeStatusVoucher(voucherId, status);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete service successfully")
                .build();
    }

}
