package com.swd392.skincare_products_sales_system.exception;

import com.swd392.skincare_products_sales_system.enums.ErrorCode;

public class ResourceNotFoundException extends AppException {
    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
