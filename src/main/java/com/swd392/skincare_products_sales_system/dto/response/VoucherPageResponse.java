package com.swd392.skincare_products_sales_system.dto.response;

import com.swd392.skincare_products_sales_system.dto.response.product.BatchResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherPageResponse extends AbstractPageResponse{
    List<VoucherResponse> content;
}
