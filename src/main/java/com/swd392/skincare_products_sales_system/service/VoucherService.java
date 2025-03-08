package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.voucher.VoucherCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.voucher.VoucherUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.VoucherResponse;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.Voucher;
import jakarta.validation.Valid;

import java.util.List;

public interface VoucherService {
    VoucherResponse createVoucher(VoucherCreationRequest request);
}
