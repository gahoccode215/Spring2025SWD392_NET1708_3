package com.swd392.skincare_products_sales_system.service;


import com.swd392.skincare_products_sales_system.dto.request.BatchCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.BatchUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.request.CategoryUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.BatchPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.BatchResponse;
import com.swd392.skincare_products_sales_system.dto.response.CategoryPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.CategoryResponse;

public interface BatchService {
    BatchResponse importBatch(BatchCreationRequest request);
    BatchResponse updateBatch(BatchUpdateRequest request, Long id);
    void deleteBatch(Long id);
    BatchResponse getBatchById(Long id);
    BatchPageResponse getBatches(int page, int size, String sortBy, String order);
}
