package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
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
    public ApiResponse<Void> getAllCategories(
            @RequestParam String productId,
            @RequestParam Integer quantity) {
        cartService.addProductToCart(productId, quantity);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Product added to cart successfully")
//                .result(cartService.addProductToCart(productId, quantity))
                .build();
    }
}
