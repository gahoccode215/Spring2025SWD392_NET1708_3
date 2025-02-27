package com.swd392.skincare_products_sales_system.service;


import com.swd392.skincare_products_sales_system.dto.request.product.BatchCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.product.BatchUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.product.BatchPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.BatchResponse;

public interface BatchService {
    BatchResponse importBatch(BatchCreationRequest request);
    BatchResponse updateBatch(BatchUpdateRequest request, Long id);
    void deleteBatch(Long id);
    BatchResponse getBatchById(Long id);
    BatchPageResponse getBatches(int page, int size, String sortBy, String order);
}
