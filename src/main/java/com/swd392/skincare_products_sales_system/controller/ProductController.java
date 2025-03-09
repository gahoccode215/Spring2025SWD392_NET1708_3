package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.ProductPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.ProductResponse;
import com.swd392.skincare_products_sales_system.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;

    @GetMapping("/{slug}")
    @Operation(summary = "Lấy chi tiết sản phẩm", description = "API Lấy chi tiết sản phẩm bằng slug")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ProductResponse> getProductBySlug(
            @PathVariable(required = false) String slug) {
        return ApiResponse.<ProductResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy chi tiết sản phẩm thành công")
                .result(productService.getProductBySlug(slug))
                .build();
    }

    @GetMapping("/latest")
    @Operation(summary = "Lấy danh sách sản phẩm mới nhất", description = "API lấy danh sách sản phẩm mới nhất với số lượng nhận vào")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<ProductResponse>> getLatestProducts(@RequestParam int limit) {
        return ApiResponse.<List<ProductResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách sản phẩm mới nhất thành công")
                .result(productService.getLatestProducts(limit))
                .build();
    }

    @GetMapping()
    @Operation(summary = "Lấy danh sách sản phẩm ở trạng thái ACTIVE", description = "API lấy danh sách sản phẩm ở trạng thái ACTIVE với phaân trang, filter, sort")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ProductPageResponse> getAllProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String categorySlug,
            @RequestParam(required = false) String brandSlug,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order) {
        return ApiResponse.<ProductPageResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách sản phẩm thành công")
                .result(productService.getProducts(false, keyword, page, size, categorySlug, brandSlug, sortBy, order))
                .build();
    }

}
