package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.BrandUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.request.OriginCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.OriginUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.BrandPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.BrandResponse;
import com.swd392.skincare_products_sales_system.dto.response.OriginPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.OriginResponse;

public interface OriginService {
    OriginResponse createOrigin(OriginCreationRequest request);
    void deleteOrigin(Long id);

    OriginResponse getOriginById(Long id);

    OriginResponse update(OriginUpdateRequest request, Long id);

    OriginPageResponse getAll(boolean admin, String keyword , int page, int size, String sortBy, String order);
}
