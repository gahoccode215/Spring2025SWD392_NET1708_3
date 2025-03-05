package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.product.ProductCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.product.ProductUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.product.ProductPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.ProductResponse;
import com.swd392.skincare_products_sales_system.enums.Status;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductCreationRequest request) ;
    void deleteProduct(String productId);
    ProductResponse updateProduct(ProductUpdateRequest request, String productId) ;
    ProductPageResponse getProducts(boolean admin, String keyword, int page, int size, String categorySlug, String brandSlug, String originSlug, String sortBy, String order);
    ProductResponse getProductBySlug(String slug);
    ProductResponse getProductById(String id);
    void changeProductStatus(String productId, Status status);
    List<ProductResponse> getLatestProducts(int limit);
}
