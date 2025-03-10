package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.request.authentication.*;
import com.swd392.skincare_products_sales_system.dto.response.*;
import com.swd392.skincare_products_sales_system.dto.response.authentication.LoginResponse;
import com.swd392.skincare_products_sales_system.dto.response.authentication.RefreshTokenResponse;
import com.swd392.skincare_products_sales_system.dto.response.authentication.RegisterResponse;
import com.swd392.skincare_products_sales_system.repository.UserRepository;
import com.swd392.skincare_products_sales_system.service.AuthenticationService;
import com.swd392.skincare_products_sales_system.service.OtpService;
import com.swd392.skincare_products_sales_system.util.JwtUtil;
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
    JwtUtil jwtUtil;
    UserRepository userRepository;
    OtpService otpService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register new account customer", description = "API retrieve user attribute to create account customer")
    public ApiResponse<RegisterResponse> register(@RequestBody @Valid RegisterRequest request){
        return ApiResponse.<RegisterResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Register successfully")
                .result(authenticationService.register(request))
                .build();
    }
    @PostMapping("/login")
    @Operation(summary = "Login", description = "API retrieve username and password to login")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request){
        return ApiResponse.<LoginResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Login successfully")
                .result(authenticationService.login(request))
                .build();
    }
    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get new Access Token", description = "API retrieve old token to get new Access Token")
    ApiResponse<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<RefreshTokenResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get new Access Token by old token successfully")
                .result(result).build();
    }
    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Invalidate JWT token to logout user")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> logout(@RequestBody LogoutRequest request) {
        authenticationService.logout(request);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .message("Logout successfully")
                .build();
    }
    @PostMapping("/change-password")
    @Operation(summary = "Change password", description = "Change password with new password")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        authenticationService.changePassword(request);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .message("Change password successfully")
                .build();
    }
    @PostMapping("/verify-otp")
    @Operation(summary = "Verify account", description = "Verify account")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> verifyOtp(@RequestParam String userId, @RequestParam String otpCode) {
        otpService.verifyOtp(userId, otpCode);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .message("Verify account successfully")
                .build();
    }
    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot Password", description = "Handle forgot password and send reset email")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authenticationService.forgotPassword(request);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .message("Password reset email sent successfully.")
                .build();
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset Password", description = "Reset user password using token")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        authenticationService.resetPassword(request);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .message("Password reset successfully.")
                .build();
    }
    @PostMapping("/resend-otp")
    public ApiResponse<Void> resendOtp(@RequestParam String userId){
        otpService.resendOtp(userId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Otp resend successfully")
                .build();
    }
}
