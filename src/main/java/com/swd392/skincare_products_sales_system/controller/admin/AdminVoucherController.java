package com.swd392.skincare_products_sales_system.controller.admin;

import com.swd392.skincare_products_sales_system.dto.request.voucher.VoucherCreationRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.VoucherPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.VoucherResponse;
import com.swd392.skincare_products_sales_system.service.VoucherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/vouchers")
@RequiredArgsConstructor
@Tag(name = "Voucher Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminVoucherController {

    VoucherService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<VoucherResponse> createVoucher(@RequestBody @Valid VoucherCreationRequest request) {
        return ApiResponse.<VoucherResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Tạo mới voucher thành công")
                .result(service.createVoucher(request))
                .build();
    }

    @DeleteMapping("{voucherId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<Void> deleteVoucher(@PathVariable Long voucherId) {
        service.deleteVoucher(voucherId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Xóa voucher thành công")
                .build();
    }
    @GetMapping("/{voucherId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ApiResponse<VoucherResponse> getVoucher(@PathVariable Long voucherId
    ) {
        return ApiResponse.<VoucherResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy chi tiết voucher thành công")
                .result(service.getVoucher(voucherId))
                .build();
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ApiResponse<VoucherPageResponse> getAllVouchers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<VoucherPageResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách voucher thành công")
                .result(service.getVouchersByAdmin(page, size))
                .build();
    }
}
