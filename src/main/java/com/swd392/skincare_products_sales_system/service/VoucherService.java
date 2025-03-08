package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.voucher.VoucherCreationRequest;
import com.swd392.skincare_products_sales_system.dto.response.VoucherPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.VoucherResponse;

public interface VoucherService {
    VoucherResponse createVoucher(VoucherCreationRequest request);
    void deleteVoucher(Long voucherId);
    VoucherPageResponse getVouchersByAdmin(int page, int size);
    void exchangeVoucher(Long voucherId);
    VoucherPageResponse getVoucherByCustomer(int page, int size);
}
