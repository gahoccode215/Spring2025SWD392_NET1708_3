package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.SkincareCreateRequest;
import com.swd392.skincare_products_sales_system.dto.request.SkincareUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.SkincareServiceResponse;
import com.swd392.skincare_products_sales_system.dto.response.SkincareServicePageResponse;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.SkincareService;

import java.util.List;


public interface SkincareServiceInterface {

    //Service
    SkincareServiceResponse createSkincareService (SkincareCreateRequest request);
    void deleteService(long skincareId);
    void changeStatusService (long skincareId, Status status);
    SkincareServiceResponse updateSkincareService (SkincareUpdateRequest request, Long skincareServiceId);
    SkincareServiceResponse getSkincareServiceById(long skincareId);
    List<SkincareService> getAllSkincareService();
}
