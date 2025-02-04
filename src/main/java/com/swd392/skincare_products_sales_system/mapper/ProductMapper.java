package com.swd392.skincare_products_sales_system.mapper;


import com.swd392.skincare_products_sales_system.dto.request.ProductCreationRequest;
import com.swd392.skincare_products_sales_system.dto.response.ProductResponse;
import com.swd392.skincare_products_sales_system.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductCreationRequest request);
    ProductResponse toProductResponse(Product product);
}
