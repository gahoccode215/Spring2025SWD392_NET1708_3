package com.swd392.skincare_products_sales_system.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractPageResponse implements Serializable {
    Integer pageNumber;
    Integer pageSize;
    Integer totalPages;
    Long totalElements;
}
