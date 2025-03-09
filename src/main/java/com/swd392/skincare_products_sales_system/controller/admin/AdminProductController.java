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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Operation(summary = "Tạo mới sản phẩm (ADMIN, MANAGER)", description = "API tạo mới sản phẩm")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
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
    @Operation(summary = "Xóa sản phẩm (ADMIN, MANAGER)", description = "API Xóa sản phẩm bằng Id")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<Void> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Xóa sản phẩm thành công")
                .build();
    }

    @PutMapping("{productId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Cập nhật sản phẩm (ADMIN, MANAGER)", description = "API Cập nhật sản phẩm bằng Id")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<ProductResponse> updateProduct(@RequestBody @Valid ProductUpdateRequest request, @PathVariable String productId) {
        return ApiResponse.<ProductResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Cập nhật thành công")
                .result(productService.updateProduct(request, productId))
                .build();
    }

    @GetMapping()
    @Operation(summary = "Lấy danh sách sản phẩm (ADMIN, MANAGER, STAFF)  ", description = "API lấy danh sách sản phẩm với phân trang, filter, sort")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
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
    @Operation(summary = "Lấy chi tiết sản phẩm (ADMIN, MANAGER, STAFF)", description = "API Lấy chi tiết sản phẩm bằng Id")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ApiResponse<ProductResponse> getProductById(
            @PathVariable(required = false) String productId) {
        return ApiResponse.<ProductResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy chi tiết sản phẩm thành công")
                .result(productService.getProductById(productId))
                .build();
    }

    @PatchMapping("/change-status/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Đổi trạng thái sản phẩm (ADMIN, MANAGER)", description = "API Đổi trạng thái sản phẩm ")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<Void> changeProductStatus(@PathVariable String productId, @RequestParam Status status) {
        productService.changeProductStatus(productId, status);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Thay đổi trạng thái thành công")
                .build();
    }

    @DeleteMapping("/delete-batch/{batchId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Xóa lô hàng (ADMIN, MANAGER)", description = "API Xóa lô hàng")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<Void> deleteBatch(@PathVariable String batchId) {
        productService.deleteBatch(batchId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Xóa lô hàng thành công")
                .build();
    }

    @PostMapping("/import-batch/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Nhập lô hàng (ADMIN, MANAGER)", description = "API Nhập lô hàng")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<ProductResponse> importBatch(@RequestBody BatchCreationRequest request, @PathVariable String productId) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.importBatch(request, productId))
                .code(HttpStatus.OK.value())
                .message("Nhập lô hàng thành công")
                .build();
    }

    @GetMapping("/{productId}/batches")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Xem danh sách lô hàng của sản phẩm (ADMIN, MANAGER)", description = "API Xem danh sách lô hàng của sản phẩm bằng Id")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
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
