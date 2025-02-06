package com.swd392.skincare_products_sales_system.mapper;


import com.swd392.skincare_products_sales_system.dto.request.ProductCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.ProductUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ProductResponse;
import com.swd392.skincare_products_sales_system.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    Product toProduct(ProductCreationRequest request);
    ProductResponse toProductResponse(Product product);
    @Mapping(target = "price", expression = "java(request.getPrice() != 0 ? request.getPrice() : product.getPrice())")
    @Mapping(target = "stock", expression = "java(request.getStock() != 0 ? request.getStock() : product.getStock())")
    void updateProduct(@MappingTarget Product product, ProductUpdateRequest request);
}
