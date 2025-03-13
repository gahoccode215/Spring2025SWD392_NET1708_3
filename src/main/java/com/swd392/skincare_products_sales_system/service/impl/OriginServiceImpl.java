package com.swd392.skincare_products_sales_system.service.impl;

import com.github.slugify.Slugify;
import com.swd392.skincare_products_sales_system.constant.Query;
import com.swd392.skincare_products_sales_system.dto.request.product.OriginCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.product.OriginUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.product.OriginPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.OriginResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.enity.product.Origin;
import com.swd392.skincare_products_sales_system.repository.OriginRepository;
import com.swd392.skincare_products_sales_system.service.OriginService;
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
public class OriginServiceImpl implements OriginService {
    Slugify slugify;
    SlugUtil slugUtil;
    OriginRepository originRepository;

    @Override
    @Transactional
    public OriginResponse createOrigin(OriginCreationRequest request) {
        Origin origin = Origin.builder()
                .name(request.getName())
                .description(request.getDescription())
                .thumbnail(request.getThumbnail())
                .slug(generateUniqueSlug(generateUniqueSlug(request.getName())))
                .status(Status.ACTIVE)
                .build();
        origin.setIsDeleted(false);
        originRepository.save(origin);
        return toOriginResponse(origin);
    }

    @Override
    @Transactional
    public void deleteOrigin(Long id) {
        Origin origin = originRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new AppException(ErrorCode.ORIGIN_NOT_EXISTED));
        origin.setIsDeleted(true);
        originRepository.save(origin);
    }

    @Override
    public OriginResponse getOriginById(Long id) {
        Origin origin = originRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new AppException(ErrorCode.ORIGIN_NOT_EXISTED));
        return toOriginResponse(origin);
    }

    @Override
    public OriginResponse update(OriginUpdateRequest request, Long id) {
        Origin origin = originRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new AppException(ErrorCode.ORIGIN_NOT_EXISTED));
        if (request.getName() != null) origin.setName(request.getName());
        if (request.getDescription() != null) origin.setDescription(request.getDescription());
        if (request.getThumbnail() != null) origin.setThumbnail(request.getThumbnail());
        if (request.getStatus() != null) origin.setStatus(request.getStatus());
        originRepository.save(origin);
        return toOriginResponse(origin);
    }

    @Override
    public OriginPageResponse getAll(boolean admin, String keyword, int page, int size, String sortBy, String order) {
        if (page > 0) page -= 1;
        Pageable pageable;
        Sort sort = getSort(sortBy, order);
        pageable = PageRequest.of(page, size, sort);
        Page<Origin> origins;
        if (admin) {
            origins = originRepository.findAllByFilters(keyword, null, pageable);
        } else {
            origins = originRepository.findAllByFilters(keyword, Status.ACTIVE, pageable);
        }
        OriginPageResponse response = new OriginPageResponse();
        List<OriginResponse> originResponses = new ArrayList<>();
        for (Origin origin : origins.getContent()) {
            OriginResponse originResponse = new OriginResponse();
            originResponse.setId(origin.getId());
            originResponse.setName(origin.getName());
            originResponse.setDescription(origin.getDescription());
            originResponse.setStatus(origin.getStatus());
            originResponse.setSlug(origin.getSlug());
            originResponse.setThumbnail(origin.getThumbnail());
            originResponses.add(originResponse);
        }
        response.setOriginResponses(originResponses);
        response.setTotalElements(origins.getTotalElements());
        response.setTotalPages(origins.getTotalPages());
        response.setPageNumber(origins.getNumber());
        response.setPageSize(origins.getSize());
        return response;
    }

    // Generate a unique slug
    private String generateUniqueSlug(String name) {
        String baseSlug = slugify.slugify(name);
        String uniqueSlug = baseSlug;

        while (originRepository.existsBySlug(uniqueSlug)) {
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

    private OriginResponse toOriginResponse(Origin origin) {
        return OriginResponse.builder()
                .id(origin.getId())
                .name(origin.getName())
                .description(origin.getDescription())
                .thumbnail(origin.getThumbnail())
                .slug(origin.getSlug())
                .status(origin.getStatus())
                .build();
    }
}
