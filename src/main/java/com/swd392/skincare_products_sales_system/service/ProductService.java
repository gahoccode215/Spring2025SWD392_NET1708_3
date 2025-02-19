package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.ProductCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.ProductUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ProductPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.ProductResponse;
import com.swd392.skincare_products_sales_system.enums.Status;

import java.io.IOException;

public interface ProductService {
    ProductResponse createProduct(ProductCreationRequest request) throws IOException;
    void deleteProduct(String productId);
    ProductResponse updateProduct(ProductUpdateRequest request, String productId) throws IOException;
    ProductPageResponse getProducts(boolean admin, String keyword, int page, int size, String categorySlug, String brandSlug, String originSlug, String sortBy, String order);
    ProductResponse getProductBySlug(String slug);
    ProductResponse getProductById(String id);
    void changeProductStatus(String productId, Status status);

}
