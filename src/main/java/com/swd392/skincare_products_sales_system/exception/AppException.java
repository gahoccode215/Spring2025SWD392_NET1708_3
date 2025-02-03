package com.swd392.skincare_products_sales_system.exception;

import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException{
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    private ErrorCode errorCode;

}
