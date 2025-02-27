package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.CartResponse;
import com.swd392.skincare_products_sales_system.model.Cart;
import com.swd392.skincare_products_sales_system.model.User;
import com.swd392.skincare_products_sales_system.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
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
            @RequestParam String productId, @RequestParam(defaultValue = "1" ) @Min(1) int quantity) {
        cartService.addProductToCart(productId, quantity);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Product added to cart successfully")
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
    @DeleteMapping("/remove")
    @Operation(summary = "Remove product from cart ", description = "Remove product from cart")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> removeProductFromCart(@RequestParam List<String> productIds) {
        cartService.removeProductFromCart(productIds);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Remove successfully")
                .build();
    }
    @PatchMapping("/update-quantity")
    @Operation(summary = "Update quantity cart item", description = "update quantity cart item")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> updateQuantityCartItem(@RequestParam String productId, @RequestParam @Min(1) Integer quantity){
        cartService.updateProductQuantityInCart(productId, quantity);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Update successfully")
                .build();
    }
}
