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
    @Mapping(target = "brand.id", source = "brand_id")  // Mapping brand_id từ request vào brand.id
    @Mapping(target = "category.id", source = "category_id")  // Mapping category_id từ request vào category.id// Mapping category_id
    Product toProduct(ProductCreationRequest request);

    @Mapping(source = "brand.name", target = "brandName")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "origin.name", target = "originName")
    @Mapping(source = "skinType.type", target = "skinTypeType")
    @Mapping(source = "features", target = "featureNames") // ánh xạ từ Set<Feature> sang danh sách tên tính năng
    ProductResponse toProductResponse(Product product);

}
