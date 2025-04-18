package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.request.user.UserUpdateProfileRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.user.UserResponse;
import com.swd392.skincare_products_sales_system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {

    UserService userService;

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Lấy thông tin cá nhân của tài khoản hiện tại", description = "API Lấy thông tin cá nhân của tài khoản hiện tại")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy thông tin cá nhân thành công")
                .result(userService.getUserProfile())
                .build();
    }
    @PutMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Cập nhật thông tin cá nhân người dùng", description = "API Cập nhật thông tin cá nhân người dùng")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    ApiResponse<UserResponse> updateMyInfo(@RequestBody UserUpdateProfileRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Cập nhật thông tin cá nhân thành công")
                .result(userService.updateUserProfile(request))
                .build();
    }

}
