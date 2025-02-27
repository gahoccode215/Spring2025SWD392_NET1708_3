package com.swd392.skincare_products_sales_system.service;


import com.swd392.skincare_products_sales_system.dto.request.product.BatchCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.product.BatchUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.product.BatchPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.BatchResponse;
import org.springframework.data.domain.Pageable;

public interface BatchService {
    BatchResponse createBatch(BatchCreationRequest request);
    BatchPageResponse getAllBatches(int page, int size);
}
