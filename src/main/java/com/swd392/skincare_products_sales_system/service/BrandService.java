package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.product.BrandCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.product.BrandUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.product.BrandPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.BrandResponse;
import com.swd392.skincare_products_sales_system.enums.Status;

public interface BrandService {
    BrandResponse createBrand(BrandCreationRequest request) ;
    void deleteBrand(Long id);

    BrandResponse getBrandById(Long id);

    void changeBrandStatus(Long brandId, Status status);

    BrandResponse updateBrand(BrandUpdateRequest request, Long id);

    BrandPageResponse getBrands(boolean admin, String keyword , int page, int size, String sortBy, String order);
}
