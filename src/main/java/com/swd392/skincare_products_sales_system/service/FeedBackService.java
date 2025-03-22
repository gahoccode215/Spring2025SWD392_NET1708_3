package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.product.FeedBackCreationRequest;
import com.swd392.skincare_products_sales_system.dto.response.product.FeedBackResponse;

public interface FeedBackService {
    FeedBackResponse createFeedBack(FeedBackCreationRequest request, String productId, Long orderId, Long orderItemId);

}
