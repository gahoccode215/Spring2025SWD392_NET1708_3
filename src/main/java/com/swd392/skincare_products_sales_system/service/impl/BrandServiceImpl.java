package com.swd392.skincare_products_sales_system.service.impl;

import com.github.slugify.Slugify;
import com.swd392.skincare_products_sales_system.constant.Query;
import com.swd392.skincare_products_sales_system.dto.request.BrandCreationRequest;
import com.swd392.skincare_products_sales_system.dto.response.BrandResponse;
import com.swd392.skincare_products_sales_system.dto.response.CategoryResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.Brand;
import com.swd392.skincare_products_sales_system.model.Category;
import com.swd392.skincare_products_sales_system.repository.BrandRepository;
import com.swd392.skincare_products_sales_system.service.BrandService;
import com.swd392.skincare_products_sales_system.service.CloudService;
import com.swd392.skincare_products_sales_system.util.SlugUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandServiceImpl implements BrandService {
    BrandRepository brandRepository;
    Slugify slugify;
    SlugUtil slugUtil;
    CloudService cloudService;


    @Override
    @Transactional
    public BrandResponse createBrand(BrandCreationRequest request) throws IOException {
        Brand brand = Brand.builder()
                .name(request.getName())
                .description(request.getDescription())
                .thumbnail(cloudService.uploadFile(request.getThumbnail()))
                .build();
        brand.setStatus(Status.ACTIVE);
        brand.setIsDeleted(false);
        brand.setSlug(generateUniqueSlug(brand.getName()));
        brandRepository.save(brand);
        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .description(brand.getDescription())
                .thumbnail(brand.getThumbnail())
                .slug(brand.getSlug())
                .build();
    }

    @Override
    @Transactional
    public void deleteBrand(Long id) {
        Brand brand = brandRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));
        brand.setIsDeleted(true);
        brandRepository.save(brand);
    }

    // Generate a unique slug
    private String generateUniqueSlug(String name) {
        String baseSlug = slugify.slugify(name);
        String uniqueSlug = baseSlug;

        while (brandRepository.existsBySlug(uniqueSlug)) {
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
        if (sortBy.equals(Query.NAME)) {
            return order.equals(Query.ASC) ? Sort.by(Query.NAME).ascending() : Sort.by(Query.NAME).descending();
        }
        return order.equals(Query.ASC) ? Sort.by(Query.NAME).ascending() : Sort.by(Query.NAME).descending();
    }
}
