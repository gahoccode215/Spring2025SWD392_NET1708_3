package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.request.product.FeedBackCreationRequest;
import com.swd392.skincare_products_sales_system.dto.response.product.FeedBackResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.product.FeedBack;
import com.swd392.skincare_products_sales_system.model.product.Product;
import com.swd392.skincare_products_sales_system.repository.FeedBackRepository;
import com.swd392.skincare_products_sales_system.repository.ProductRepository;
import com.swd392.skincare_products_sales_system.service.FeedBackService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedBackServiceImpl implements FeedBackService {
    ProductRepository productRepository;
    FeedBackRepository feedBackRepository;

    @Override
    @Transactional
    public FeedBackResponse createFeedBack(FeedBackCreationRequest request, String productId) {
        Product product = productRepository.findByIdAndIsDeletedFalse(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        FeedBack feedBack = FeedBack.builder()
                .description(request.getDescription())
                .rating(request.getRating())
                .product(product)
                .build();
        feedBackRepository.save(feedBack);

        updateProductRating(product);

        return FeedBackResponse.builder()
                .id(feedBack.getId())
                .rating(feedBack.getRating())
                .description(feedBack.getDescription())
                .product(feedBack.getProduct())
                .build();
    }
    private void updateProductRating(Product product) {
        // Lấy tất cả rating của sản phẩm
        List<FeedBack> feedbacks = feedBackRepository.findByProduct(product);

        // Tính tổng số rating
//        int totalRatings = feedbacks.size();

        // Tính điểm trung bình của sản phẩm
        double averageRating = feedbacks.stream()
                .mapToInt(FeedBack::getRating)
                .average()
                .orElse(0.0);

        // Cập nhật lại thông tin rating và tổng rating cho sản phẩm
        product.setRating(averageRating);
        log.info("{}", product.getRating());
        // Lưu lại thông tin đã cập nhật
        productRepository.save(product);
    }
}
