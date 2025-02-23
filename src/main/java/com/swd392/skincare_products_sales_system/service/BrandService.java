package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.BrandCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.BrandUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.request.CategoryUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.BrandPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.BrandResponse;
import com.swd392.skincare_products_sales_system.dto.response.CategoryPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.CategoryResponse;
import com.swd392.skincare_products_sales_system.enums.Status;

import java.io.IOException;

public interface BrandService {
    BrandResponse createBrand(BrandCreationRequest request) ;
    void deleteBrand(Long id);

    BrandResponse getBrandById(Long id);

    void changeBrandStatus(Long brandId, Status status);

    BrandResponse updateBrand(BrandUpdateRequest request, Long id);

    BrandPageResponse getBrands(boolean admin, String keyword , int page, int size, String sortBy, String order);
}
