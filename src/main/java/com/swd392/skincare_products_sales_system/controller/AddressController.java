package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.request.address.AddressCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.address.AddressUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.address.AddressResponse;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
@Tag(name = "Address Controller")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressController {
    AddressService addressService;

    @PostMapping
    @Operation(summary = "Thêm mới địa chỉ", description = "API Thêm mới địa chỉ")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ApiResponse<Void> addAddress(@RequestBody AddressCreationRequest addressDTO) {
        addressService.addAddress(addressDTO);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Thêm mới địa chỉ thành công")
                .build();
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "Cập nhật địa chỉ", description = "API Cập nhật địa chỉ")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ApiResponse<Void> updateAddress(@PathVariable Long addressId, @RequestBody AddressUpdateRequest addressDTO) {
        addressService.updateAddress(addressId, addressDTO);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Cập nhật địa chỉ thành công")
                .build();
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "Xóa địa chỉ", description = "API Xóa địa chỉ")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ApiResponse<Void> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Xóa địa chỉ thành công")
                .build();
    }

    @GetMapping
    @Operation(summary = "Xem danh sách địa chỉ", description = "API Xem danh sách địa chỉ")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ApiResponse<List<AddressResponse>> getAllAddresses() {
        List<AddressResponse> addresses = addressService.getAllAddresses();
        return ApiResponse.<List<AddressResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Xem danh sách địa chỉ thành công")
                .result(addresses)
                .build();
    }

    @PutMapping("/default/{addressId}")
    @Operation(summary = "Cập nhật địa chỉ mặc định", description = "API Cập nhật địa chỉ mặc định")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ApiResponse<AddressResponse> setDefaultAddress(@PathVariable Long addressId) {
        AddressResponse addressDTO = addressService.setDefaultAddress(addressId);
        return ApiResponse.<AddressResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Cập nhật địa chỉ mặc định thành công")
                .result(addressDTO)
                .build();
    }
}
