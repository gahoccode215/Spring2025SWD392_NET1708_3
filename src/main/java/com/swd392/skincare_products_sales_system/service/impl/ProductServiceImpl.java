package com.swd392.skincare_products_sales_system.service.impl;


import com.github.slugify.Slugify;
import com.swd392.skincare_products_sales_system.constant.Query;
import com.swd392.skincare_products_sales_system.dto.request.ProductCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.ProductUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ProductPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.ProductResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.*;
import com.swd392.skincare_products_sales_system.repository.*;
import com.swd392.skincare_products_sales_system.service.ProductService;
import com.swd392.skincare_products_sales_system.util.SlugUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    Slugify slugify;
    SlugUtil slugUtil;
    ProductRepository productRepository;
    BrandRepository brandRepository;
    OriginRepository originRepository;
    CategoryRepository categoryRepository;


    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreationRequest request)  {
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .build();
        if (request.getCategory_id() != null) {
            Category category = categoryRepository.findByIdAndIsDeletedFalse(request.getCategory_id()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
            product.setCategory(category);
        }
        if (request.getBrand_id() != null) {
            Brand brand = brandRepository.findByIdAndIsDeletedFalse(request.getBrand_id()).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));
            product.setBrand(brand);
        }
        product.setThumbnail(request.getThumbnail());
        product.setStatus(Status.ACTIVE);
        product.setSlug(generateUniqueSlug(product.getName()));
        product.setIsDeleted(false);
        log.info("Product: {}", product);
        productRepository.save(product);
        return toProductResponse(product);
    }

    @Override
    @Transactional
    public void deleteProduct(String productId) {
        Product product = productRepository.findByIdAndIsDeletedFalse(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        product.setIsDeleted(true);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(ProductUpdateRequest request, String productId){
        Product product = productRepository.findByIdAndIsDeletedFalse(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        if (request.getCategory_id() != null) {
            Category category = categoryRepository.findByIdAndIsDeletedFalse(request.getCategory_id()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
            product.setCategory(category);
        }
        if (request.getBrand_id() != null) {
            Brand brand = brandRepository.findByIdAndIsDeletedFalse(request.getBrand_id()).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));
            product.setBrand(brand);
        }
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if(request.getThumbnail() != null){
            product.setThumbnail(request.getThumbnail());
        }
        if(request.getStatus() != null){
            product.setStatus(request.getStatus());
        }
        productRepository.save(product);
        return toProductResponse(product);
    }

    @Override
    public ProductPageResponse getProducts(boolean admin, String keyword, int page, int size, String categorySlug, String brandSlug, String originSlug, String sortBy, String order) {
        if (page > 0) page -= 1; // Hỗ trợ trang bắt đầu từ 0 hoặc 1

        Sort sort = getSort(sortBy, order);
        Pageable pageable = PageRequest.of(page, size, sort);

        Category category = categorySlug != null ? categoryRepository.findBySlugAndStatusAndIsDeletedFalse(categorySlug).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)) : null;
        Brand brand = brandSlug != null ? brandRepository.findBySlugAndStatusAndIsDeletedFalse(brandSlug).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)) : null;
        Origin origin = originSlug != null ? originRepository.findBySlugAndStatusAndIsDeletedFalse(originSlug).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)) : null;
        Page<Product> products;

        if (admin) {
            products = productRepository.findAllByFilters(keyword, null, category, brand, origin, pageable);
        } else {
            products = productRepository.findAllByFilters(keyword, Status.ACTIVE, category, brand, origin, pageable);
        }

        // Chuyển đổi từ `Page<Product>` sang `ProductPageResponse`
        ProductPageResponse response = new ProductPageResponse();

        List<ProductResponse> productResponses = new ArrayList<>();

        // Ánh xạ từng sản phẩm từ Page<Product> sang ProductResponse
        for (Product product : products.getContent()) {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(product.getId());
            productResponse.setName(product.getName());
            productResponse.setPrice(product.getPrice());
            productResponse.setDescription(product.getDescription());
            productResponse.setSlug(product.getSlug());
            productResponse.setThumbnail(product.getThumbnail());
            productResponse.setStatus(product.getStatus());
            if(product.getCategory() != null){
                productResponse.setCategory(product.getCategory());
            }
            productResponses.add(productResponse);
        }
        response.setProductResponses(productResponses);
        response.setTotalElements(products.getTotalElements());
        response.setTotalPages(products.getTotalPages());
        response.setPageNumber(products.getNumber());
        response.setPageSize(products.getSize());

        return response;
    }


    @Override
    public ProductResponse getProductBySlug(String slug) {
        Product product = productRepository.findBySlugAndIsDeletedFalseAndStatus(slug).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        return toProductResponse(product);
    }

    @Override
    public ProductResponse getProductById(String id) {
        Product product = productRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        return toProductResponse(product);
    }

    @Override
    @Transactional
    public void changeProductStatus(String productId, Status status) {
        Product product = productRepository.findByIdAndIsDeletedFalse(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        productRepository.updateProductStatus(product.getId(), status);
    }

    private Sort getSort(String sortBy, String order) {
        if (sortBy == null) {
            sortBy = Query.NAME; // mặc định là sắp xếp theo tên nếu không có sortBy
        }

        if (order == null || (!order.equals(Query.ASC) && !order.equals(Query.DESC))) {
            order = Query.ASC; // mặc định là theo chiều tăng dần nếu không có order hoặc order không hợp lệ
        }

        // Kiểm tra trường sortBy và tạo Sort tương ứng
        if (sortBy.equals(Query.PRICE)) {
            return order.equals(Query.ASC) ? Sort.by(Query.PRICE).ascending() : Sort.by(Query.PRICE).descending();
        }
        return order.equals(Query.ASC) ? Sort.by(Query.NAME).ascending() : Sort.by(Query.NAME).descending();
    }


    private String generateUniqueSlug(String name) {
        String baseSlug = slugify.slugify(name);
        String uniqueSlug = baseSlug;

        while (productRepository.existsBySlug(uniqueSlug)) {
            uniqueSlug = baseSlug + "-" + slugUtil.generateRandomString(6);
        }
        return uniqueSlug;
    }
    private ProductResponse toProductResponse(Product product){

         ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .slug(product.getSlug())
                .thumbnail(product.getThumbnail())
//                .category_id(product.getCategory().getId())
                .status(product.getStatus())
                .build();
         if(product.getCategory() != null){
             Category category = product.getCategory();
             productResponse.setCategory(category);
         }
         return productResponse;
    }
}
