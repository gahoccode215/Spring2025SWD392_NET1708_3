package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.VoucherPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.VoucherResponse;
import com.swd392.skincare_products_sales_system.service.VoucherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vouchers")
@RequiredArgsConstructor
@Tag(name = "Voucher Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherController {

    VoucherService service;

    @PostMapping("/exchange-voucher/{voucherId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ApiResponse<Void> exchangeVoucher(@PathVariable Long voucherId) {
        service.exchangeVoucher(voucherId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.CREATED.value())
                .message("Đổi voucher thành công")
                .build();
    }
    @GetMapping("/{voucherId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
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
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ApiResponse<VoucherPageResponse> getAllVouchers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<VoucherPageResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách voucher thành công")
                .result(service.getVoucherByCustomer(page, size))
                .build();
    }
    @GetMapping("my-voucher")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ApiResponse<VoucherPageResponse> getMyVouchers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<VoucherPageResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách voucher của tôi  thành công")
                .result(service.getMyVouchers(page, size))
                .build();
    }
}
