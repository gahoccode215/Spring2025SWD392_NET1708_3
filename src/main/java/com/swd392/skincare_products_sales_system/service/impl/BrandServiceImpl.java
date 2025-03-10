package com.swd392.skincare_products_sales_system.service.impl;

import com.github.slugify.Slugify;
import com.swd392.skincare_products_sales_system.constant.Query;
import com.swd392.skincare_products_sales_system.dto.request.product.BrandCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.product.BrandUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.product.BrandPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.BrandResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.product.Brand;
import com.swd392.skincare_products_sales_system.repository.BrandRepository;
import com.swd392.skincare_products_sales_system.service.BrandService;
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
public class BrandServiceImpl implements BrandService {
    BrandRepository brandRepository;
    Slugify slugify;
    SlugUtil slugUtil;

    @Override
    @Transactional
    public BrandResponse createBrand(BrandCreationRequest request) {
        Brand brand = Brand.builder()
                .name(request.getName())
                .description(request.getDescription())
                .thumbnail(request.getThumbnail())
                .build();
        brand.setIsDeleted(false);
        brand.setSlug(generateUniqueSlug(brand.getName()));
        brandRepository.save(brand);
        return toBrandResponse(brand);
    }

    @Override
    @Transactional
    public void deleteBrand(Long id) {
        Brand brand = brandRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));
        brand.setIsDeleted(true);
        brandRepository.save(brand);
    }

    @Override
    public BrandResponse getBrandById(Long id) {
        Brand brand = brandRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));
        return toBrandResponse(brand);
    }


    @Override
    @Transactional
    public BrandResponse updateBrand(BrandUpdateRequest request, Long id) {
        Brand brand = brandRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));
        if (request.getName() != null) brand.setName(request.getName());
        if (request.getDescription() != null) brand.setDescription(request.getDescription());
        if (request.getThumbnail() != null) brand.setThumbnail(request.getThumbnail());
        brandRepository.save(brand);
        return toBrandResponse(brand);
    }

    @Override
    public BrandPageResponse getBrands(String keyword, int page, int size, String sortBy, String order) {
        if (page > 0) page -= 1;
        Pageable pageable;
        Sort sort = getSort(sortBy, order);
        pageable = PageRequest.of(page, size, sort);
        Page<Brand> brands;
        brands = brandRepository.findAllByFilters(keyword, pageable);
        BrandPageResponse response = new BrandPageResponse();
        List<BrandResponse> brandResponses = new ArrayList<>();
        for (Brand brand : brands.getContent()) {
            BrandResponse brandResponse = toBrandResponse(brand);
            brandResponses.add(brandResponse);
        }
        response.setBrandResponses(brandResponses);
        response.setTotalElements(brands.getTotalElements());
        response.setTotalPages(brands.getTotalPages());
        response.setPageNumber(brands.getNumber());
        response.setPageSize(brands.getSize());
        return response;
    }

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

    private BrandResponse toBrandResponse(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .description(brand.getDescription())
                .thumbnail(brand.getThumbnail())
                .slug(brand.getSlug())
                .build();
    }
}
