package com.swd392.skincare_products_sales_system.service;


import com.swd392.skincare_products_sales_system.dto.request.BatchCreationRequest;
import com.swd392.skincare_products_sales_system.dto.response.BatchResponse;

public interface BatchService {
    BatchResponse importBatch(BatchCreationRequest request);
}
