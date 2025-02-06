package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.ProductCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.ProductUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ProductResponse;

public interface ProductService {
    ProductResponse createProduct(ProductCreationRequest request);
    void deleteProduct(String productId);
    ProductResponse updateProduct(ProductUpdateRequest request, String productId);
    ProductResponse getProductById(String productId);
}
