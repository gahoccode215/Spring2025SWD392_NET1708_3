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
    @Operation(summary = "Add new address", description = "Add a new delivery address for the user")
    public ApiResponse<Void> addAddress(@RequestBody AddressCreationRequest addressDTO) {
        addressService.addAddress(addressDTO);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Address added successfully")
                .build();
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "Update address", description = "Update an existing address")
    public ApiResponse<Void> updateAddress(@PathVariable Long addressId, @RequestBody AddressUpdateRequest addressDTO) {
        addressService.updateAddress(addressId, addressDTO);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Address updated successfully")
                .build();
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "Delete address", description = "Delete an existing address")
    public ApiResponse<Void> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Address deleted successfully")
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all addresses", description = "Retrieve all addresses for the authenticated user")
    public ApiResponse<List<AddressResponse>> getAllAddresses() {
        List<AddressResponse> addresses = addressService.getAllAddresses();
        return ApiResponse.<List<AddressResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Addresses fetched successfully")
                .result(addresses)
                .build();
    }

    @PutMapping("/default/{addressId}")
    @Operation(summary = "Set default address", description = "Set a specific address as the default")
    public ApiResponse<AddressResponse> setDefaultAddress(@PathVariable Long addressId) {
        AddressResponse addressDTO = addressService.setDefaultAddress(addressId);
        return ApiResponse.<AddressResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Address set as default successfully")
                .result(addressDTO)
                .build();
    }
}
