package com.swd392.skincare_products_sales_system.service.impl;

import com.github.slugify.Slugify;
import com.swd392.skincare_products_sales_system.constant.Query;
import com.swd392.skincare_products_sales_system.dto.request.product.CategoryCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.product.CategoryUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.product.CategoryPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.CategoryResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.product.Category;
import com.swd392.skincare_products_sales_system.repository.CategoryRepository;
import com.swd392.skincare_products_sales_system.service.CategoryService;
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
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    Slugify slugify;
    SlugUtil slugUtil;


    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryCreationRequest request)  {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .thumbnail(request.getThumbnail())
                .build();
        category.setStatus(Status.ACTIVE);
        category.setIsDeleted(false);
        category.setSlug(generateUniqueSlug(category.getName()));
        categoryRepository.save(category);
        return toCategoryResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(CategoryUpdateRequest request, String categoryId)  {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(categoryId).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        if(request.getName() != null){
            category.setName(request.getName());
        }
        if(request.getDescription() != null){
            category.setDescription(request.getDescription());
        }
        if(request.getThumbnail() != null){
            category.setThumbnail(request.getThumbnail());
        }
        if(request.getStatus() != null){
            category.setStatus(request.getStatus());
        }
        categoryRepository.save(category);
        return toCategoryResponse(category);
    }

    @Override
    @Transactional
    public void deleteCategory(String categoryId) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(categoryId).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        category.setIsDeleted(true);
        categoryRepository.save(category);
    }

    @Override
    public CategoryResponse getCategoryById(String id) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        return toCategoryResponse(category);
    }

    @Override
    public CategoryPageResponse getCategories(boolean admin, String keyword, int page, int size, String sortBy, String order) {
        if (page > 0) page -= 1;

        Pageable pageable;
        Sort sort = getSort(sortBy, order);
        pageable = PageRequest.of(page, size, sort);


        Page<Category> categories;
        if (admin) {
            categories = categoryRepository.findAllByFilters(keyword, null, pageable);
        } else {
            categories = categoryRepository.findAllByFilters(keyword, Status.ACTIVE, pageable);
        }

        // Chuyển đổi từ `Page<Product>` sang `ProductPageResponse`
        CategoryPageResponse response = new CategoryPageResponse();

        List<CategoryResponse> categoryResponses = new ArrayList<>();

        // Ánh xạ từng sản phẩm từ Page<Product> sang ProductResponse
        for (Category category : categories.getContent()) {
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setId(category.getId());
            categoryResponse.setName(category.getName());
            categoryResponse.setDescription(category.getDescription());
            categoryResponse.setStatus(category.getStatus());
            categoryResponse.setSlug(category.getSlug());
            categoryResponse.setThumbnail(category.getThumbnail());
            categoryResponses.add(categoryResponse);
        }
        response.setCategoryResponses(categoryResponses);
        response.setTotalElements(categories.getTotalElements());
        response.setTotalPages(categories.getTotalPages());
        response.setPageNumber(categories.getNumber());
        response.setPageSize(categories.getSize());
        return response;
    }

    @Override
    @Transactional
    public void changeCategoryStatus(String categoryId, Status status) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(categoryId).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        categoryRepository.updateCategoryStatus(category.getId(), status);
    }

    // Generate a unique slug
    private String generateUniqueSlug(String name) {
        String baseSlug = slugify.slugify(name);
        String uniqueSlug = baseSlug;

        while (categoryRepository.existsBySlug(uniqueSlug)) {
            uniqueSlug = baseSlug + "-" + slugUtil.generateRandomString(6);
        }
        return uniqueSlug;
    }
    private Sort getSort(String sortBy, String order) {
        if (sortBy == null) {
            sortBy = Query.NAME; // mặc định là sắp xếp theo tên nếu không có sortBy
        }

        if (order == null || (!order.equals(Query.ASC) && !order.equals(Query.DESC))) {
            order = Query.ASC; // mặc định là theo chiều tăng dần nếu không có order hoặc order không hợp lệ
        }

        // Kiểm tra trường sortBy và tạo Sort tương ứng
        if (sortBy.equals(Query.NAME)) {
            return order.equals(Query.ASC) ? Sort.by(Query.NAME).ascending() : Sort.by(Query.NAME).descending();
        }
        return order.equals(Query.ASC) ? Sort.by(Query.NAME).ascending() : Sort.by(Query.NAME).descending();
    }
    private CategoryResponse toCategoryResponse(Category category){
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .thumbnail(category.getThumbnail())
                .slug(category.getSlug())
                .status(category.getStatus())
                .build();
    }
}
