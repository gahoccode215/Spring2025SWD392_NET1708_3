package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.request.voucher.VoucherCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.voucher.VoucherUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.VoucherResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.entity.User;
import com.swd392.skincare_products_sales_system.entity.Voucher;
import com.swd392.skincare_products_sales_system.repository.UserRepository;
import com.swd392.skincare_products_sales_system.repository.VoucherRepository;
import com.swd392.skincare_products_sales_system.service.VoucherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherServiceImpl implements VoucherService {

    VoucherRepository voucherRepository;
    UserRepository userRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VoucherResponse createVoucher(VoucherCreationRequest request) {
        if(voucherRepository.findByVoucherCode(request.getVoucherCode()).isPresent()){
            throw new AppException(ErrorCode.VOUCHER_CODE_EXISTED);
        }
        if (request.getVoucherName() == null || request.getVoucherName().isEmpty()) {
            throw new AppException(ErrorCode.VOUCHER_CODE_EXISTED);
        }
        Voucher voucher = Voucher.builder()
                .voucherCode(request.getVoucherCode())
                .voucherName(request.getVoucherName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .point(request.getPoint())
                .description(request.getDescription())
                .discountAmount(request.getDiscountAmount())
                .status(Status.ACTIVE)
                .quantity(request.getQuantity())
                .build();
        voucher.setIsDeleted(false);
        voucherRepository.save(voucher);

        return VoucherResponse.builder()
                .id(voucher.getId())
                .voucherCode(voucher.getVoucherCode())
                .voucherName(voucher.getVoucherName())
                .startDate(voucher.getStartDate())
                .endDate(voucher.getEndDate())
                .point(voucher.getPoint())
                .description(voucher.getDescription())
                .discountAmount(voucher.getDiscountAmount())
                .status(voucher.getStatus())
                .quantity(voucher.getQuantity())
                .build();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public VoucherResponse updateVoucher(VoucherUpdateRequest request, Long voucherId) {
        Voucher checkVoucher = voucherRepository.findByIdAndIsDeletedFalse(voucherId)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_EXIST));

        if (voucherRepository.findByVoucherCode(request.getVoucherCode())
                .filter(v -> !v.getId().equals(voucherId))
                .isPresent()) {
            throw new AppException(ErrorCode.VOUCHER_CODE_EXISTED);
        }

        if (request.getVoucherName() == null || request.getVoucherName().isEmpty()) {
            throw new AppException(ErrorCode.VOUCHER_NAME_EXISTED);
        }

        checkVoucher.setVoucherCode(request.getVoucherCode());
        checkVoucher.setVoucherName(request.getVoucherName());
        checkVoucher.setStartDate(request.getStartDate());
        checkVoucher.setEndDate(request.getEndDate());
        checkVoucher.setPoint(request.getPoint());
        checkVoucher.setDescription(request.getDescription());
        checkVoucher.setDiscountAmount(request.getDiscountAmount());
        checkVoucher.setQuantity(request.getQuantity());

        voucherRepository.save(checkVoucher);

        return VoucherResponse.builder()
                .id(checkVoucher.getId())
                .voucherCode(checkVoucher.getVoucherCode())
                .voucherName(checkVoucher.getVoucherName())
                .startDate(checkVoucher.getStartDate())
                .endDate(checkVoucher.getEndDate())
                .point(checkVoucher.getPoint())
                .description(checkVoucher.getDescription())
                .discountAmount(checkVoucher.getDiscountAmount())
                .status(checkVoucher.getStatus())
                .quantity(checkVoucher.getQuantity())
                .build();
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteVoucher(Long voucherId) {
        Voucher voucher = voucherRepository.findByIdAndIsDeletedFalse(voucherId)
                    .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_EXIST));
        voucher.setIsDeleted(true);
        voucher.setStatus(Status.INACTIVE);
        voucherRepository.save(voucher);
    }

    @Override
    public void changeStatusVoucher(Long voucherId , Status status) {
        Voucher voucher = voucherRepository.findByIdAndIsDeletedFalse(voucherId)
                .orElseThrow(() -> new AppException(ErrorCode.QUIZ_EXISTED));
        if(voucher.getStatus() == Status.ACTIVE){
            voucher.setStatus(Status.INACTIVE);
        }else{
            voucher.setStatus(Status.ACTIVE);
        }
        voucherRepository.save(voucher);
    }

    @Override
    public VoucherResponse getVoucherById(Long voucherId) {
        Voucher voucher = voucherRepository.findByIdAndIsDeletedFalse(voucherId)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_EXIST));

        return VoucherResponse.builder()
                .id(voucher.getId())
                .voucherCode(voucher.getVoucherCode())
                .voucherName(voucher.getVoucherName())
                .startDate(voucher.getStartDate())
                .endDate(voucher.getEndDate())
                .point(voucher.getPoint())
                .description(voucher.getDescription())
                .discountAmount(voucher.getDiscountAmount())
                .status(voucher.getStatus())
                .quantity(voucher.getQuantity())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Voucher> getAllVoucher() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        List<Voucher> list;
        if(user.getRole().getId() == 2 || user.getRole().getId() == 6) {
            list = voucherRepository.findAll()
                    .stream()
                    .toList();
        }else{
            list = voucherRepository.findAll()
                    .stream()
                    .filter(voucher -> !voucher.getIsDeleted()
                            && voucher.getStatus() == Status.ACTIVE)
                    .collect(Collectors.toList());
        }
        return list;
    }
}
