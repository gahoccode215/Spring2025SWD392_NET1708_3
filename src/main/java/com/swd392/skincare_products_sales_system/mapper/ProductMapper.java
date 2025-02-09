package com.swd392.skincare_products_sales_system.mapper;


import com.swd392.skincare_products_sales_system.dto.request.ProductCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.ProductUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.FeatureResponse;
import com.swd392.skincare_products_sales_system.dto.response.ProductResponse;
import com.swd392.skincare_products_sales_system.model.Feature;
import com.swd392.skincare_products_sales_system.model.Product;
import org.mapstruct.*;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, imports = {Feature.class, Collectors.class})
public interface ProductMapper {
    @Mapping(target = "brand.id", source = "brand_id")  // Mapping brand_id từ request vào brand.id
    @Mapping(target = "category.id", source = "category_id")  // Mapping category_id từ request vào category.id// Mapping category_id
    @Mapping(target = "origin.id", source = "origin_id")
    @Mapping(target = "skinType.id", source = "skin_type_id")
    // Mapping danh sách feature_ids từ request sang Set<Feature>
    @Mapping(target = "features", expression = "java(request.getFeature_ids().stream().map(id -> new Feature(id)).collect(Collectors.toSet()))")
    Product toProduct(ProductCreationRequest request);

    @Mapping(source = "brand.name", target = "brandName")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "origin.name", target = "originName")
    @Mapping(source = "skinType.type", target = "skinTypeType")
    @Mapping(source = "features", target = "featureNames", qualifiedByName = "mapFeatures")
    ProductResponse toProductResponse(Product product);

    @Named("mapFeatures")
    default Set<FeatureResponse> mapFeatures(Set<Feature> features) {
        if (features == null) {
            return Collections.emptySet();
        }
        return features.stream()
                .map(feature -> new FeatureResponse(feature.getId(), feature.getName()))
                .collect(Collectors.toSet());
    }
}
