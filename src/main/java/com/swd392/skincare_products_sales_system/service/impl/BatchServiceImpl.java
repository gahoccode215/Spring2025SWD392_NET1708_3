package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.constant.Query;
import com.swd392.skincare_products_sales_system.dto.request.BatchCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.BatchUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.BatchPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.BatchResponse;
import com.swd392.skincare_products_sales_system.dto.response.ProductResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.Batch;
import com.swd392.skincare_products_sales_system.model.Product;
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
import org.springframework.data.domain.Sort;
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
    public BatchResponse importBatch(BatchCreationRequest request) {
        Product product = productRepository.findByIdAndIsDeletedFalse(request.getProduct_id()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        Batch batch = Batch.builder()
                .product(product)
                .manufactureDate(request.getManufactureDate())
                .quantity(request.getQuantity())
                .expirationDate(request.getExpirationDate())
                .build();
        batchRepository.save(batch);
        return toBatchResponse(batch);
    }

    @Override
    @Transactional
    public BatchResponse updateBatch(BatchUpdateRequest request, Long id) {
        Product product = productRepository.findByIdAndIsDeletedFalse(request.getProduct_id()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        Batch batch = batchRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BATCH_NOT_FOUND));
        batch.setProduct(product);
        if (request.getExpirationDate() != null) batch.setExpirationDate(request.getExpirationDate());
        if (request.getManufactureDate() != null) batch.setManufactureDate(request.getManufactureDate());
        if (request.getQuantity() != null) batch.setQuantity(request.getQuantity());
        batchRepository.save(batch);
        return toBatchResponse(batch);
    }

    @Override
    @Transactional
    public void deleteBatch(Long id) {
        Batch batch = batchRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BATCH_NOT_FOUND));
        batchRepository.delete(batch);
    }

    @Override
    public BatchResponse getBatchById(Long id) {
        Batch batch = batchRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BATCH_NOT_FOUND));
        return toBatchResponse(batch);
    }

    @Override
    public BatchPageResponse getBatches(int page, int size, String sortBy, String order) {
//        if (page > 0) page -= 1;
//        Page<Batch> batches;
//        Pageable pageable = PageRequest.of(page, size);
//
//        batches = batchRepository.findAll(pageable);
//        BatchPageResponse response = new BatchPageResponse();
//        List<BatchResponse> batchResponses = new ArrayList<>();
//        for (Batch x : batches.getContent()) {
//            BatchResponse batchResponse = new BatchResponse();
//            batchResponse.setId(x.getId());
//            batchResponse.setExpirationDate(x.getExpirationDate());
//            batchResponse.setManufactureDate(x.getManufactureDate());
//            batchResponse.setQuantity(x.getQuantity());
//            batchResponses.add(batchResponse);
//        }
//        response.setBatchResponses(batchResponses);
//        response.setTotalElements(batches.getTotalElements());
//        response.setTotalPages(batches.getTotalPages());
//        response.setPageNumber(batches.getNumber());
//        response.setPageSize(batches.getSize());
//
//        return response;
        return null;
    }

    private BatchResponse toBatchResponse(Batch batch) {
        return BatchResponse.builder()
                .id(batch.getId())
                .quantity(batch.getQuantity())
                .expirationDate(batch.getExpirationDate())
                .manufactureDate(batch.getManufactureDate())
                .product(batch.getProduct())
                .build();
    }
//    private Sort getSort(String sortBy, String order) {
//        if (sortBy == null) {
//            sortBy = Query.; // mặc định là sắp xếp theo tên nếu không có sortBy
//        }
//
//        if (order == null || (!order.equals(Query.ASC) && !order.equals(Query.DESC))) {
//            order = Query.ASC; // mặc định là theo chiều tăng dần nếu không có order hoặc order không hợp lệ
//        }
//
//        // Kiểm tra trường sortBy và tạo Sort tương ứng
//        if (sortBy.equals(Query.PRICE)) {
//            return order.equals(Query.ASC) ? Sort.by(Query.PRICE).ascending() : Sort.by(Query.PRICE).descending();
//        }
//        return order.equals(Query.ASC) ? Sort.by(Query.NAME).ascending() : Sort.by(Query.NAME).descending();
//    }
}
