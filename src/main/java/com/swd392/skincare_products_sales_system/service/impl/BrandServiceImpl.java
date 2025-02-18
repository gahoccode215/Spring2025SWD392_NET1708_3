package com.swd392.skincare_products_sales_system.service.impl;

import com.github.slugify.Slugify;
import com.swd392.skincare_products_sales_system.repository.BrandRepository;
import com.swd392.skincare_products_sales_system.service.BrandService;
import com.swd392.skincare_products_sales_system.util.SlugUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandServiceImpl implements BrandService {
    BrandRepository brandRepository;
    Slugify slugify;
    SlugUtil slugUtil;

//    @Override
//    @Transactional
//    public BrandResponse createBrand(BrandCreationRequest request) throws IOException {
//        Brand brand = Brand.builder()
//                .name(request.getName())
//                .description(request.getDescription())
//                .status(Status.ACTIVE)
//                .slug(generateUniqueSlug(request.getName()))
//                .build();
//        brand.setIsDeleted(false);
//        brand.setThumbnail(cloudService.uploadFile(request.getThumbnail()));
//        return BrandResponse.builder()
//                .name(brand.getName())
//                .id(brand.getId())
//                .description(brand.getDescription())
//                .slug(brand.getSlug())
//                .thumbnail(brand.getThumbnail())
//                .build();
//    }
//
//    // Generate a unique slug
//    private String generateUniqueSlug(String name) {
//        String baseSlug = slugify.slugify(name);
//        String uniqueSlug = baseSlug;
//
//        while (brandRepository.existsBySlug(uniqueSlug)) {
//            uniqueSlug = baseSlug + "-" + slugUtil.generateRandomString(6);
//        }
//        return uniqueSlug;
//    }
}
