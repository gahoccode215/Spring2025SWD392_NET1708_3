package com.swd392.skincare_products_sales_system.dto.response;

import com.swd392.skincare_products_sales_system.model.SkincareService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SkincareServicePageResponse extends AbstractPageResponse {
    List<SkincareService> skincareServices;
}
