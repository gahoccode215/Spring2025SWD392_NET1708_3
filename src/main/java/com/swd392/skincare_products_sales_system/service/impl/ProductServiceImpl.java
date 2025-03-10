package com.swd392.skincare_products_sales_system.service.impl;


import com.github.slugify.Slugify;
import com.swd392.skincare_products_sales_system.constant.Query;
import com.swd392.skincare_products_sales_system.dto.request.product.*;
import com.swd392.skincare_products_sales_system.dto.response.product.*;
import com.swd392.skincare_products_sales_system.dto.response.user.UserResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.product.*;
import com.swd392.skincare_products_sales_system.model.user.User;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    Slugify slugify;
    SlugUtil slugUtil;
    ProductRepository productRepository;
    BrandRepository brandRepository;
    CategoryRepository categoryRepository;
    BatchRepository batchRepository;
    SpecificationRepository specificationRepository;
    FeedBackRepository feedBackRepository;

    @Override
    public void deleteBatch(String batchId) {
        Batch batch = batchRepository.findById(batchId).orElseThrow(() -> new AppException(ErrorCode.BATCH_NOT_FOUND));
        batchRepository.delete(batch);
    }

    @Override
    @Transactional
    public ProductResponse importBatch(BatchCreationRequest request, String productId) {
        Batch batch = Batch.builder()
                .batchCode("BATCH-" + System.currentTimeMillis())
                .quantity(request.getQuantity())
                .manufactureDate(request.getManufactureDate())
                .expirationDate(request.getExpirationDate())
                .build();
        Product product = productRepository.findByIdAndIsDeletedFalse(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        product.addBatch(batch);
        productRepository.save(product);
        return toProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreationRequest request) {
        Specification specification = toSpecification(request.getSpecification());
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .ingredient(request.getIngredient())
                .specification(specification)
                .usageInstruction(request.getUsageInstruction())
                .build();
        if (request.getCategory_id() != null) {
            Category category = categoryRepository.findByIdAndIsDeletedFalse(request.getCategory_id()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            product.setCategory(category);
        }
        if (request.getBrand_id() != null) {
            Brand brand = brandRepository.findByIdAndIsDeletedFalse(request.getBrand_id()).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));
            product.setBrand(brand);
        }

        specification.setProduct(product);
        product.setThumbnail(request.getThumbnail());
        product.setStatus(Status.ACTIVE);
        product.setSlug(generateUniqueSlug(product.getName()));
        product.setIsDeleted(false);
        product.setRating(5.0);
        log.info("Product: {}", product);
        productRepository.save(product);
        return toProductResponse(product);
    }

    @Override
    @Transactional
    public void deleteProduct(String productId) {
        Product product = productRepository.findByIdAndIsDeletedFalse(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        product.setIsDeleted(true);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(ProductUpdateRequest request, String productId) {
        Product product = productRepository.findByIdAndIsDeletedFalse(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        if (request.getCategory_id() != null) {
            Category category = categoryRepository.findByIdAndIsDeletedFalse(request.getCategory_id()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
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
        if (request.getIngredient() != null) {
            product.setIngredient(request.getIngredient());
        }
        if (request.getUsageInstruction() != null) {
            product.setUsageInstruction(request.getUsageInstruction());
        }
        if (request.getThumbnail() != null) {
            product.setThumbnail(request.getThumbnail());
        }
        if (request.getStatus() != null) {
            product.setStatus(request.getStatus());
        }
        if (request.getSpecification() != null) {
            SpecificationUpdateRequest specRequest = request.getSpecification();
            Specification existingSpec = product.getSpecification();
            if (existingSpec == null) {
                Specification newSpec = toSpecificationUpdate(specRequest);
                newSpec.setProduct(product);
                specificationRepository.save(newSpec);
                product.setSpecification(newSpec);
            } else {
                existingSpec.setOrigin(specRequest.getOrigin());
                existingSpec.setBrandOrigin(specRequest.getBrandOrigin());
                existingSpec.setManufacturingLocation(specRequest.getManufacturingLocation());
                existingSpec.setSkinType(specRequest.getSkinType());
                specificationRepository.save(existingSpec);
            }
        }
        productRepository.save(product);
        return toProductResponse(product);
    }

    @Override
    public ProductPageResponse getProducts(boolean admin, String keyword, int page, int size, String categorySlug, String brandSlug, String sortBy, String order) {
        if (page > 0) page -= 1; // Hỗ trợ trang bắt đầu từ 0 hoặc 1
        Sort sort = getSort(sortBy, order);
        Pageable pageable = PageRequest.of(page, size, sort);
        Category category = categorySlug != null ? categoryRepository.findBySlugAndStatusAndIsDeletedFalse(categorySlug).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)) : null;
        Brand brand = brandSlug != null ? brandRepository.findBySlugAndStatusAndIsDeletedFalse(brandSlug).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)) : null;
        Page<Product> products;
        if (admin) {
            products = productRepository.findAllByFilters(keyword, null, category, brand, pageable);
        } else {
            products = productRepository.findAllByFilters(keyword, Status.ACTIVE, category, brand, pageable);
        }
        // Chuyển đổi từ `Page<Product>` sang `ProductPageResponse`
        ProductPageResponse response = new ProductPageResponse();
        List<ProductResponse> productResponses = new ArrayList<>();
        // Ánh xạ từng sản phẩm từ Page<Product> sang ProductResponse
        for (Product product : products.getContent()) {
            ProductResponse productResponse = toProductResponse(product);
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
        Product product = productRepository.findBySlugAndIsDeletedFalseAndStatus(slug).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return toProductResponse(product);
    }

    @Override
    public ProductResponse getProductById(String id) {
        Product product = productRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return toProductResponse(product);
    }

    @Override
    @Transactional
    public void changeProductStatus(String productId, Status status) {
        Product product = productRepository.findByIdAndIsDeletedFalse(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        productRepository.updateProductStatus(product.getId(), status);
    }

    @Override
    public List<ProductResponse> getLatestProducts(int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit);
        List<Product> products = productRepository.findLatestProductsByStatus(Status.ACTIVE, pageRequest).getContent();
        return products.stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BatchPageResponse getBatches(int page, int size, String productId) {
        if (page > 0) page -= 1; // Hỗ trợ trang bắt đầu từ 0 hoặc 1
        Pageable pageable = PageRequest.of(page, size);
        Page<Batch> batches;
        batches = batchRepository.findAllByProductIdAnd(productId, pageable);
        BatchPageResponse response = new BatchPageResponse();
        List<BatchResponse> batchResponses = new ArrayList<>();
        for (Batch batch : batches.getContent()) {
            BatchResponse batchResponse = toBatchResponse(batch);
            batchResponses.add(batchResponse);
        }
        response.setContent(batchResponses);
        response.setTotalElements(batches.getTotalElements());
        response.setTotalPages(batches.getTotalPages());
        response.setPageNumber(batches.getNumber());
        response.setPageSize(batches.getSize());
        return response;

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

    private ProductResponse toProductResponse(Product product) {
        Batch batch = batchRepository.findFirstBatchByProductIdAndQuantityGreaterThanZero(product.getId());

        List<FeedBack> feedBacks = feedBackRepository.findAllByProductId(product.getId());

        List<FeedBackResponse> feedBackResponses = feedBacks.stream()
                .map(this::toFeedBackResponse)
                .toList();

        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .feedBacks(feedBackResponses)
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .slug(product.getSlug())
                .thumbnail(product.getThumbnail())
                .status(product.getStatus())
                .rating(product.getRating())
                .usageInstruction(product.getUsageInstruction())
                .ingredient(product.getIngredient())
                .build();
        if (product.getCategory() != null) {
            productResponse.setCategory(product.getCategory());
        }
        if (product.getBrand() != null) {
            productResponse.setBrand(product.getBrand());
        }
        if (product.getBatches() != null) {
            productResponse.setStock(toQuantityProduct(product.getBatches()));
            if (!(product.getBatches().isEmpty())) {
                productResponse.setExpirationTime(batch.getExpirationDate());
            }
        }
        if (product.getSpecification() != null) {
            productResponse.setSpecification(toSpecificationResponse(product.getSpecification()));
        }
        return productResponse;
    }

    private BatchResponse toBatchResponse(Batch batch) {
        return BatchResponse.builder()
                .id(batch.getId())
                .product(batch.getProduct())
                .batchCode(batch.getBatchCode())
                .quantity(batch.getQuantity())
                .manufactureDate(batch.getManufactureDate())
                .expirationDate(batch.getExpirationDate())
                .build();
    }

    private Specification toSpecificationUpdate(SpecificationUpdateRequest request) {
        return Specification.builder()
                .origin(request.getOrigin())
                .brandOrigin(request.getBrandOrigin())
                .manufacturingLocation(request.getManufacturingLocation())
                .skinType(request.getSkinType())
                .build();
    }

    private SpecificationResponse toSpecificationResponse(Specification specification){
        return SpecificationResponse.builder()
                .origin(specification.getOrigin())
                .brandOrigin(specification.getBrandOrigin())
                .manufacturingLocation(specification.getManufacturingLocation())
                .skinType(specification.getSkinType())
                .build();
    }

    private Specification toSpecification(SpecificationCreationRequest request) {
        return Specification.builder()
                .origin(request.getOrigin())
                .brandOrigin(request.getBrandOrigin())
                .manufacturingLocation(request.getManufacturingLocation())
                .skinType(request.getSkinType())
                .build();
    }

    private int toQuantityProduct(List<Batch> batches) {
        int stock = 0;
        for (Batch batch : batches) {
            stock += batch.getQuantity();
        }
        return stock;
    }

    private FeedBackResponse toFeedBackResponse(FeedBack feedBack) {
        return FeedBackResponse.builder()
                .id(feedBack.getId())
//                .product(feedBack.getProduct())
                .rating(feedBack.getRating())
                .description(feedBack.getDescription())
                .userResponse(toUserResponse(feedBack.getUser()))
                .build();
    }
    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .email(user.getEmail())
                .username(user.getUsername())
                .birthday(user.getBirthday())
                .roleName(user.getRole().getName())
                .point(user.getPoint())
                .avatar(user.getAvatar())
                .status(user.getStatus())
                .build();
    }
}
