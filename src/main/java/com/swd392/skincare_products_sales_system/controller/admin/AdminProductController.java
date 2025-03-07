package com.swd392.skincare_products_sales_system.controller.admin;

import com.swd392.skincare_products_sales_system.dto.request.product.BatchCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.product.ProductCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.product.ProductUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.BatchPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.ProductPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.ProductResponse;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/products")
@Tag(name = "Product Controller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminProductController {
    ProductService productService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Create a product (ADMIN, MANAGER)", description = "API retrieve product attribute to create")
    public ApiResponse<ProductResponse> createProduct(@RequestBody @Valid ProductCreationRequest request
    ) {

        return ApiResponse.<ProductResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Tạo mới sản phẩm thành công")
                .result(productService.createProduct(request))
                .build();
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Delete a product (ADMIN, MANAGER)", description = "API delete product by its id")
    public ApiResponse<Void> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Xóa sản phẩm thành công")
                .build();
    }

    @PutMapping("{productId}")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Update a product (ADMIN, MANAGER)", description = "API update product by its id")
    public ApiResponse<ProductResponse> updateProduct(@RequestBody @Valid ProductUpdateRequest request, @PathVariable String productId) {
        return ApiResponse.<ProductResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Cập nhật thành công")
                .result(productService.updateProduct(request, productId))
                .build();
    }

    @GetMapping()
    @Operation(summary = "Get all products with options: search, pagination, sort, filter (ADMIN, MANAGER)  ", description = "Retrieve all products with search, pagination, sorting, and filtering.")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<ProductPageResponse> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categorySlug,
            @RequestParam(required = false) String brandSlug,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order
    ) {
        return ApiResponse.<ProductPageResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách sản phẩm thành công")
                .result(productService.getProducts(true, keyword, page, size, categorySlug, brandSlug, sortBy, order))
                .build();
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get a product by id (ADMIN, MANAGER)", description = "Retrieve product id to get product detail")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<ProductResponse> getProductById(
            @PathVariable(required = false) String productId) {
        return ApiResponse.<ProductResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get product detail successfully")
                .result(productService.getProductById(productId))
                .build();
    }

    @PatchMapping("/change-status/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change product status (ADMIN, MANAGER)", description = "API to change product status (ACTIVE/INACTIVE)")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<Void> changeProductStatus(@PathVariable String productId, @RequestParam Status status) {
        productService.changeProductStatus(productId, status);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Thay đổi trạng thái thành công")
                .build();
    }
    @DeleteMapping("/delete-batch/{batchId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> deleteBatch(@PathVariable String batchId){
        productService.deleteBatch(batchId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Xóa lô hàng thành công")
                .build();
    }

    @PostMapping("/import-batch/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ProductResponse> importBatch(@RequestBody BatchCreationRequest request, @PathVariable String productId) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.importBatch(request, productId))
                .code(HttpStatus.OK.value())
                .message("Nhập lô hàng thành công")
                .build();
    }

    @GetMapping("/{productId}/batches")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<BatchPageResponse> getBatchesByProductId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable String productId) {
        return ApiResponse.<BatchPageResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách lô theo sản phẩm thành công")
                .result(productService.getBatches(page, size, productId))
                .build();
    }
}
