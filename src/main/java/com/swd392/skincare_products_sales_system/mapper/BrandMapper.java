package com.swd392.skincare_products_sales_system.mapper;

import com.swd392.skincare_products_sales_system.dto.request.BrandCreationRequest;
import com.swd392.skincare_products_sales_system.dto.response.BrandResponse;
import com.swd392.skincare_products_sales_system.model.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    Brand toBrand(BrandCreationRequest request);
    BrandResponse toBrandResponse(Brand brand);
}
