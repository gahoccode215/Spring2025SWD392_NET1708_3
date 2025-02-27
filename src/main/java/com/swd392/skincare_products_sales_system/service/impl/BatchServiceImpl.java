package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.request.product.BatchCreationRequest;
import com.swd392.skincare_products_sales_system.dto.response.product.BatchPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.BatchResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.ProductResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.product.Batch;
import com.swd392.skincare_products_sales_system.model.product.Product;
import com.swd392.skincare_products_sales_system.repository.BatchRepository;
import com.swd392.skincare_products_sales_system.repository.ProductRepository;
import com.swd392.skincare_products_sales_system.service.BatchService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BatchServiceImpl implements BatchService {

    ProductRepository productRepository;
    BatchRepository batchRepository;

    @Override
    @Transactional
    public BatchResponse createBatch(BatchCreationRequest request) {
        Product product = productRepository.findByIdAndIsDeletedFalse(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        // Tạo Batch mới
        Batch batch = Batch.builder()
                .batchCode("BATCH-" + System.currentTimeMillis())
                .product(product)
                .quantity(request.getQuantity())
                .importPrice(request.getImportPrice())
                .manufactureDate(request.getManufactureDate())
                .expirationDate(request.getExpirationDate())
                .build();
        batchRepository.save(batch);

        // Cập nhật tồn kho
        product.setStock(product.getStock() + request.getQuantity());
        productRepository.save(product);

        return toBatchResponse(batch);
    }

    @Override
    public BatchPageResponse getAllBatches(int page, int size) {
        if (page > 0) page -= 1;
        Pageable pageable = PageRequest.of(page, size);
        Page<Batch> batches;
        batches = batchRepository.findAll(pageable);
        BatchPageResponse response = new BatchPageResponse();
        List<BatchResponse> batchResponses = new ArrayList<>();
        for (Batch batch : batches.getContent()) {
            BatchResponse batchResponse = new BatchResponse();
            batchResponse.setId(batch.getId());
            batchResponse.setBatchCode(batch.getBatchCode());
            batchResponse.setQuantity(batch.getQuantity());
            batchResponse.setImportPrice(batch.getImportPrice());
            batchResponse.setManufactureDate(batch.getManufactureDate());
            batchResponse.setExpirationDate(batch.getExpirationDate());

            batchResponses.add(batchResponse);
        }
        response.setContent(batchResponses);
        response.setTotalElements(batches.getTotalElements());
        response.setTotalPages(batches.getTotalPages());
        response.setPageNumber(batches.getNumber());
        response.setPageSize(batches.getSize());
        return response;
    }
    private BatchResponse toBatchResponse(Batch batch){
        return BatchResponse.builder()
                .productId(batch.getProduct().getId())
                .importPrice(batch.getImportPrice())
                .expirationDate(batch.getExpirationDate())
                .manufactureDate(batch.getManufactureDate())
                .quantity(batch.getQuantity())
                .build();
    }
}
