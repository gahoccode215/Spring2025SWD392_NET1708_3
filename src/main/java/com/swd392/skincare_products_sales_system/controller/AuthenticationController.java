package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.request.authentication.*;
import com.swd392.skincare_products_sales_system.dto.response.*;
import com.swd392.skincare_products_sales_system.dto.response.authentication.LoginResponse;
import com.swd392.skincare_products_sales_system.dto.response.authentication.RefreshTokenResponse;
import com.swd392.skincare_products_sales_system.dto.response.authentication.RegisterResponse;
import com.swd392.skincare_products_sales_system.service.AuthenticationService;
import com.swd392.skincare_products_sales_system.service.OtpService;
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
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;
    OtpService otpService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Đăng ký tài khoản khách hàng", description = "API Đăng ký tài khoản khách hàng")
    public ApiResponse<RegisterResponse> register(@RequestBody @Valid RegisterRequest request){
        return ApiResponse.<RegisterResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Đăng ký thành công")
                .result(authenticationService.register(request))
                .build();
    }
    @PostMapping("/login")
    @Operation(summary = "Đăng nhập", description = "API đăng nhập")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request){
        return ApiResponse.<LoginResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Đăng nhập thành công")
                .result(authenticationService.login(request))
                .build();
    }
    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Lấy Access Token mới", description = "API Lấy Access Token mới")
    ApiResponse<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<RefreshTokenResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy Access Token mới thành công")
                .result(result).build();
    }
    @PostMapping("/logout")
    @Operation(summary = "Đăng xuất", description = "Invalidate JWT token to logout user")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> logout(@RequestBody LogoutRequest request) {
        authenticationService.logout(request);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .message("Đăng xuất thành công")
                .build();
    }
    @PostMapping("/change-password")
    @Operation(summary = "Đổi mật khẩu", description = "API Đổi mật khẩu")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        authenticationService.changePassword(request);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .message("Đổi mật khẩu thành công")
                .build();
    }
    @PostMapping("/verify-otp")
    @Operation(summary = "Xác minh tài khoản", description = "API Xác minh tài khoản")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> verifyOtp(@RequestParam String userId, @RequestParam String otpCode) {
        otpService.verifyOtp(userId, otpCode);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .message("Xác minh tài khoản thành công")
                .build();
    }
    @PostMapping("/forgot-password")
    @Operation(summary = "Quên mật khẩu", description = "API Quên mật khẩu")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authenticationService.forgotPassword(request);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .message("Đã gửi mail reset password")
                .build();
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset mật khẩu", description = "API Reset mật khẩu với token")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        authenticationService.resetPassword(request);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .message("Reset mật khẩu thành công")
                .build();
    }
    @PostMapping("/resend-otp")
    public ApiResponse<Void> resendOtp(@RequestParam String userId){
        otpService.resendOtp(userId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Otp gửi thành công")
                .build();
    }
}
