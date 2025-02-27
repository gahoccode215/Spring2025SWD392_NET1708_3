package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.product.OriginCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.product.OriginUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.product.OriginPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.OriginResponse;

public interface OriginService {
    OriginResponse createOrigin(OriginCreationRequest request);
    void deleteOrigin(Long id);

    OriginResponse getOriginById(Long id);

    OriginResponse update(OriginUpdateRequest request, Long id);

    OriginPageResponse getAll(boolean admin, String keyword , int page, int size, String sortBy, String order);
}
