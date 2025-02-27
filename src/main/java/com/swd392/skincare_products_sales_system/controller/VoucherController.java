package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.request.VoucherCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.VoucherUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.VoucherResponse;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.Voucher;
import com.swd392.skincare_products_sales_system.repository.SkincareServiceRepository;
import com.swd392.skincare_products_sales_system.repository.VoucherRepository;
import com.swd392.skincare_products_sales_system.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vouchers")
@RequiredArgsConstructor
@Tag(name = "Voucher Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherController {

    VoucherService service;
    VoucherRepository voucherRepository;

    @GetMapping("/{voucherId}")
    @Operation(summary = "Get a voucher", description = "API retrieve id to get service")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<VoucherResponse> getVoucher(@PathVariable Long voucherId) {
        return ApiResponse.<VoucherResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Delete service successfully")
                .result(service.getVoucherById(voucherId))
                .build();
    }

    @GetMapping("/system/getVoucherAll")
    @Operation(summary = "For user login", description = "API retrieve id to delete service")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<Voucher>> getAllVoucher() {
        return ApiResponse.<List<Voucher>>builder()
                .result(service.getAllVoucher())
                .code(HttpStatus.OK.value())
                .message("List Voucher")
                .build();
    }

    @GetMapping("/getVoucherAll")
    @Operation(summary = "For all role(guest) ", description = "")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<Voucher>> getAllVouchers() {
        List<Voucher> list = voucherRepository.findAll()
                .stream()
                .filter(voucher -> !voucher.getIsDeleted() && voucher.getStatus().equals(Status.ACTIVE))
                .toList();
        return ApiResponse.<List<Voucher>>builder()
                .result(list)
                .code(HttpStatus.OK.value())
                .message("List Voucher")
                .build();
    }
}
