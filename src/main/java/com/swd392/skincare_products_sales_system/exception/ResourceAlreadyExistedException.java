package com.swd392.skincare_products_sales_system.exception;

import com.swd392.skincare_products_sales_system.enums.ErrorCode;

public class ResourceAlreadyExistedException extends AppException{
    public ResourceAlreadyExistedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
