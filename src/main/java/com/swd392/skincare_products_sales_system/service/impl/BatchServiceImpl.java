package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.request.BatchCreationRequest;
import com.swd392.skincare_products_sales_system.dto.response.BatchResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.Batch;
import com.swd392.skincare_products_sales_system.model.Product;
import com.swd392.skincare_products_sales_system.repository.ProductRepository;
import com.swd392.skincare_products_sales_system.service.BatchService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BatchServiceImpl implements BatchService {

    ProductRepository productRepository;

    @Override
    @Transactional
    public BatchResponse importBatch(BatchCreationRequest request) {
        Product product = productRepository.findByIdAndIsDeletedFalse(request.getProduct_id()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        Batch batch = Batch.builder()
                .product(product)
                .manufactureDate(request.getManufactureDate())
                .quantity(request.getQuantity())
                .expirationDate(request.getExpirationDate())
                .build();
        return toBatchResponse(batch);

    }
    private BatchResponse toBatchResponse(Batch batch){
        return BatchResponse.builder()
                .id(batch.getId())
                .quantity(batch.getQuantity())
                .expirationDate(batch.getExpirationDate())
                .manufactureDate(batch.getManufactureDate())
                .product(batch.getProduct())
                .build();
    }
}
