package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.request.voucher.VoucherCreationRequest;
import com.swd392.skincare_products_sales_system.dto.response.VoucherPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.VoucherResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.user.User;
import com.swd392.skincare_products_sales_system.model.user.Voucher;
import com.swd392.skincare_products_sales_system.repository.UserRepository;
import com.swd392.skincare_products_sales_system.repository.VoucherRepository;
import com.swd392.skincare_products_sales_system.service.VoucherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherServiceImpl implements VoucherService {

    VoucherRepository voucherRepository;
    UserRepository userRepository;

    @Override
    @Transactional
    public VoucherResponse createVoucher(VoucherCreationRequest request) {
        Voucher voucher = Voucher.builder()
                .code(request.getCode())
                .point(request.getPoint())
                .discount(request.getDiscount())
                .description(request.getDescription())
                .minOrderValue(request.getMinOrderValue())
                .discountType(request.getDiscountType())
                .status(Status.ACTIVE)
                .build();
        voucherRepository.save(voucher);
        return toVoucherResponse(voucher);
    }

    @Override
    @Transactional
    public void deleteVoucher(Long voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        voucherRepository.delete(voucher);
    }

    @Override
    public VoucherPageResponse getVouchersByAdmin(int page, int size) {
        if (page > 0) page -= 1;
        Pageable pageable = PageRequest.of(page, size);
        Page<Voucher> vouchers = voucherRepository.findAllByFilterAdmin(pageable);
        VoucherPageResponse response = new VoucherPageResponse();
        List<VoucherResponse> voucherResponses = new ArrayList<>();
        for(Voucher voucher : vouchers.getContent()){
            VoucherResponse voucherResponse = toVoucherResponse(voucher);
            voucherResponses.add(voucherResponse);
        }
        response.setContent(voucherResponses);
        response.setTotalElements(vouchers.getTotalElements());
        response.setTotalPages(vouchers.getTotalPages());
        response.setPageNumber(vouchers.getNumber());
        response.setPageSize(vouchers.getSize());
        return response;
    }

    @Override
    @Transactional
    public void exchangeVoucher(Long voucherId) {
        User user = getAuthenticatedUser();
        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        user.getVouchers().forEach(v -> {
            if(v.getId().equals(voucher.getId())){
                throw new AppException(ErrorCode.INVALID_EXCHANGE_VOUCHER);
            }
        });
        if(user.getPoint() < voucher.getPoint()){
            throw new AppException(ErrorCode.NOT_ENOUGH_POINT);
        }
        user.setPoint(user.getPoint() - voucher.getPoint());
        user.addVoucher(voucher);
        userRepository.save(user);
    }

    @Override
    public VoucherPageResponse getVoucherByCustomer(int page, int size) {
        if (page > 0) page -= 1;
        Pageable pageable = PageRequest.of(page, size);
        Page<Voucher> vouchers = voucherRepository.findAllMyVoucher(pageable);

        VoucherPageResponse response = new VoucherPageResponse();
        List<VoucherResponse> voucherResponses = new ArrayList<>();
        for(Voucher voucher : vouchers.getContent()){
            VoucherResponse voucherResponse = toVoucherResponse(voucher);
            voucherResponses.add(voucherResponse);
        }
        response.setContent(voucherResponses);
        response.setTotalElements(vouchers.getTotalElements());
        response.setTotalPages(vouchers.getTotalPages());
        response.setPageNumber(vouchers.getNumber());
        response.setPageSize(vouchers.getSize());
        return response;
    }

    @Override
    public VoucherPageResponse getMyVouchers(int page, int size) {
        User user = getAuthenticatedUser();
        Pageable pageable = PageRequest.of(page, size);
        Page<Voucher> vouchers = voucherRepository.findAllByUserId(user.getId(), pageable);
        VoucherPageResponse response = new VoucherPageResponse();
        List<VoucherResponse> voucherResponses = new ArrayList<>();
        for(Voucher voucher : vouchers.getContent()){
            VoucherResponse voucherResponse = toVoucherResponse(voucher);
            voucherResponses.add(voucherResponse);
        }
        response.setContent(voucherResponses);
        response.setTotalElements(vouchers.getTotalElements());
        response.setTotalPages(vouchers.getTotalPages());
        response.setPageNumber(vouchers.getNumber());
        response.setPageSize(vouchers.getSize());
        return response;
    }

    private VoucherResponse toVoucherResponse(Voucher voucher){
        return VoucherResponse.builder()
                .id(voucher.getId())
                .code(voucher.getCode())
                .discount(voucher.getDiscount())
                .discountType(voucher.getDiscountType())
                .minOrderValue(voucher.getMinOrderValue())
                .description(voucher.getDescription())
                .point(voucher.getPoint())
                .quantity(voucher.getQuantity())
                .build();
    }
    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrThrow(username);
    }
}
