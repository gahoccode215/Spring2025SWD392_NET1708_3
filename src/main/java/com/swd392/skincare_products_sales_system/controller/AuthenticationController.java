package com.swd392.skincare_products_sales_system.controller;

import com.nimbusds.jose.JOSEException;
import com.swd392.skincare_products_sales_system.dto.request.LoginRequest;
import com.swd392.skincare_products_sales_system.dto.request.RefreshTokenRequest;
import com.swd392.skincare_products_sales_system.dto.request.RegisterRequest;
import com.swd392.skincare_products_sales_system.dto.response.*;
import com.swd392.skincare_products_sales_system.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register", description = "API retrieve users attribute to create account")
    public ApiResponse<RegisterResponse> register(@RequestBody RegisterRequest request){
        return ApiResponse.<RegisterResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Register successfully")
                .result(authenticationService.register(request))
                .build();
    }
    @PostMapping("/login")
    @Operation(summary = "Login", description = "API retrieve user attribute to login")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request){
        return ApiResponse.<LoginResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Login successfully")
                .result(authenticationService.login(request))
                .build();
    }
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get new Access Token", description = "API retrieve old token to get new Access Token")
    ApiResponse<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<RefreshTokenResponse>builder()
                .code(HttpStatus.OK.value())
                .result(result).build();
    }

}
