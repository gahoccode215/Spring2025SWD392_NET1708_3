package com.swd392.skincare_products_sales_system.dto.response.product;

import com.swd392.skincare_products_sales_system.dto.response.AbstractPageResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OriginPageResponse extends AbstractPageResponse {
    List<OriginResponse> originResponses;
}
