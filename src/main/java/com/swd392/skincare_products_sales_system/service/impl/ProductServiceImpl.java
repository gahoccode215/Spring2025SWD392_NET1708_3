package com.swd392.skincare_products_sales_system.service.impl;


import com.github.slugify.Slugify;
import com.swd392.skincare_products_sales_system.dto.request.ProductCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.ProductSearchRequest;
import com.swd392.skincare_products_sales_system.dto.request.ProductUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ProductResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.mapper.ProductMapper;
import com.swd392.skincare_products_sales_system.model.*;
import com.swd392.skincare_products_sales_system.repository.*;
import com.swd392.skincare_products_sales_system.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new SecureRandom();

    ProductRepository productRepository;
    ProductMapper productMapper;
    Slugify slugify;
    BrandRepository brandRepository;
    CategoryRepository categoryRepository;
    SkinTypeRepository skinTypeRepository;
    OriginRepository originRepository;
    FeatureRepository featureRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreationRequest request) {
        Product product = productMapper.toProduct(request);
        Brand brand = brandRepository.findByIdAndIsDeletedFalse(request.getBrand_id())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        Origin origin = originRepository.findByIdAndIsDeletedFalse(request.getOrigin_id())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        SkinType skinType = skinTypeRepository.findByIdAndIsDeletedFalse(request.getSkin_type_id())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        Category category = categoryRepository.findByIdAndIsDeletedFalse(request.getCategory_id())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        Set<Feature> features = new HashSet<>(featureRepository.findAllByIdAndIsDeletedFalse(request.getFeature_ids()));

        product.setBrand(brand);
        product.setOrigin(origin);
        product.setSkinType(skinType);
        product.setCategory(category);
        product.setFeatures(features);
        product.setSlug(generateUniqueSlug(request.getName()));
        productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(String productId) {
        Product product = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductResponse updateProduct(ProductUpdateRequest request, String productId) {
        log.info("Attempting to find product with ID: {}", productId);
        Product product = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
//        productMapper.updateProduct(product, request);
        log.info("{}", product);
        return productMapper.toProductResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse getProductById(String productId) {
        Product product = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        return productMapper.toProductResponse(product);
    }

    @Override
    public Page<ProductResponse> searchProducts(ProductSearchRequest request) {
        return null;
    }

    // Generate a unique slug
    private String generateUniqueSlug(String name) {
        String baseSlug = slugify.slugify(name);
        String uniqueSlug = baseSlug;

        while (productRepository.existsBySlug(uniqueSlug)) {
            uniqueSlug = baseSlug + "-" + generateRandomString(6);
        }
        return uniqueSlug;
    }
    // Helper method to generate a random string
    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

}
