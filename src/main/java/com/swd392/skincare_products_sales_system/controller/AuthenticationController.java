package com.swd392.skincare_products_sales_system.controller;

import com.nimbusds.jose.JOSEException;
import com.swd392.skincare_products_sales_system.dto.request.*;
import com.swd392.skincare_products_sales_system.dto.response.*;
import com.swd392.skincare_products_sales_system.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

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

}
