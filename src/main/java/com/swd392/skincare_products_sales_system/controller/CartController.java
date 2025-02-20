package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.CartResponse;
import com.swd392.skincare_products_sales_system.model.Cart;
import com.swd392.skincare_products_sales_system.model.User;
import com.swd392.skincare_products_sales_system.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@Tag(name = "Cart Controller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartController {
    
    CartService cartService;

    @PostMapping
    @Operation(summary = "Add item to cart ", description = "Retrieve products to cart")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> addItemToCart(
            @RequestParam String productId, @RequestParam int quantity) {
        cartService.addProductToCart(productId, quantity);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Product added to cart successfully")
//                .result(cartService.addProductToCart(productId, quantity))
                .build();
    }
    @GetMapping
    @Operation(summary = "Get cart ", description = "Retrieve user id to get cart")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CartResponse> getCart() {
        return ApiResponse.<CartResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get cart successfully")
                .result(cartService.getCart())
                .build();
    }
//    @DeleteMapping("/remove/{productIds}")
//    @Operation(summary = "Remove product from cart ", description = "Remove product from cart")
//    @ResponseStatus(HttpStatus.OK)
//    public ApiResponse<Void> removeProductFromCart(@RequestParam List<String> productIds) {
//        cartService.removeProductsFromCart(productIds);
//        return ApiResponse.<Void>builder()
//                .code(HttpStatus.OK.value())
//                .message("Remove successfully")
//                .build();
//    }
}
