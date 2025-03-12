
package com.swd392.skincare_products_sales_system.enums;

import lombok.Getter;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    //10XX
    RESOURCE_NOT_FOUND(1000, "Resource not found", HttpStatus.NOT_FOUND),
    USERNAME_EXISTED(1001, "Username existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1002, "User not existed", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTED(1003, "Product not existed", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(1004, "Category not existed", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1005, "User existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1006, "Role not found", HttpStatus.BAD_REQUEST),
    CART_NOT_FOUND(1007, "Cart not found", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTED_IN_CART(1008, "Product not existed in cart", HttpStatus.BAD_REQUEST),
    BRAND_NOT_EXISTED(1009, "Brand not existed", HttpStatus.BAD_REQUEST),
    USER_INACTIVE(1013, "User inactive", HttpStatus.BAD_REQUEST),
    ORIGIN_NOT_EXISTED(1010, "Origin not existed", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_FOUND(1011, "Address not found", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(1012, "Order not found", HttpStatus.BAD_REQUEST),
    EMAIL_SENDING_FAILED(1013, "Email fail", HttpStatus.BAD_REQUEST),
    EMAIL_REQUIRED(1014, "Email can not blank", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1015, "Email existed", HttpStatus.BAD_REQUEST),
    BATCH_NOT_FOUND(1016, "Batch not found", HttpStatus.BAD_REQUEST),
    EMAIL_SEND_FAILED(1018, "Email send fail", HttpStatus.BAD_REQUEST),
    ACCOUNT_ALREADY_VERIFIED(1019, "Account already verified", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND(1020, "Email not found", HttpStatus.BAD_REQUEST),
    //11XX
    INVALID_KEY(1100, "Invalid uncategorized error", HttpStatus.BAD_REQUEST),
    INVALID_LOGIN(1101, "Username or password not correct", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1102, "Token invalid", HttpStatus.UNAUTHORIZED),
    INVALID_USERNAME(1103, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1104, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_BIRTHDAY(1105, "Birthday must be at least {min} years", HttpStatus.BAD_REQUEST),
    INVALID_CONFIRM_PASSWORD(1106, "Confirm password not match with password", HttpStatus.BAD_REQUEST),
    INVALID_CHANGE_PASSWORD(1107, "Old password not correct", HttpStatus.BAD_REQUEST),
    INVALID_GENDER(1107, "Gender invalid", HttpStatus.BAD_REQUEST),
    INVALID_ROLE(1108, "Role invalid", HttpStatus.BAD_REQUEST),

    INVALID_JSON(1109, "Json invalid", HttpStatus.BAD_REQUEST),
    INVALID_QUANTITY(1110, "Quantity invalid.Quantity must greater than 0", HttpStatus.BAD_REQUEST),
    INVALID_PAYMENT_METHOD(1111, "Invalid Payment Method", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1112, "Invalid Email", HttpStatus.BAD_REQUEST),
    // 12XX
    UNAUTHENTICATED(1201, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(1202, "Forbidden", HttpStatus.FORBIDDEN),
    // 13XX
    REGISTER_ERROR(1301, "Register failed", HttpStatus.BAD_REQUEST),
    //14XX
    QUIZ_EXISTED(1401, "Quiz existed", HttpStatus.BAD_REQUEST),
    TITLE_EXISTED(1402, "Title existed", HttpStatus.BAD_REQUEST),
    QUIZ_NOT_EXISTED(1403, "Quiz not existed", HttpStatus.BAD_REQUEST),
    QUIZ_NOT_FOUND(1404, "Quiz not found", HttpStatus.BAD_REQUEST),
    //15XX
    SERVICE_EXISTED(1501, "Service existed", HttpStatus.BAD_REQUEST),
    SERVICENAME_EXISTED(1502, "Service name existed", HttpStatus.BAD_REQUEST),
    SERVICE_NOT_EXIST(1503, "Service not existed", HttpStatus.BAD_REQUEST),
    SERVICENAME_NOT_EXIST(1504, "Service name not existed", HttpStatus.BAD_REQUEST),
    SERVICE_TYPE_EXISTED(1505, "Service type existed", HttpStatus.BAD_REQUEST),
    SERVICE_TYPE_NOT_EXIST(1506, "Service type not existed", HttpStatus.BAD_REQUEST),
    SERVICE_INACTIVE(1507, "Service inactive", HttpStatus.BAD_REQUEST),
    //16XX
    QUESTION_EXISTED(1601, "Question existed", HttpStatus.BAD_REQUEST),
    QUESTION_NOT_EXIST(1602, "Question not existed", HttpStatus.BAD_REQUEST),
    QUESTION_NOT_FOUND(1603, "Question not found", HttpStatus.BAD_REQUEST),
    //17XX
    ANSWER_EXISTED(1701, "Answer existed", HttpStatus.BAD_REQUEST),
    ANSWER_NOT_EXIST(1702, "Answer not existed", HttpStatus.BAD_REQUEST),
    ANSWER_NOT_FOUND(1703, "Answer not found", HttpStatus.BAD_REQUEST),
    //18XX
    VOUCHER_EXISTED(1801, "Voucher existed", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_EXIST(1802, "Voucher not existed", HttpStatus.BAD_REQUEST),
    VOUCHER_CODE_EXISTED(1803, "Voucher code existed", HttpStatus.BAD_REQUEST),
    VOUCHER_NAME_EXISTED(1804, "Voucher name existed", HttpStatus.BAD_REQUEST),
    VOUCHER_NAME_NOT_EXIST(1805, "Voucher name not existed", HttpStatus.BAD_REQUEST),
    VOUCHER_CODE_NOT_EXIST(1806, "Voucher code not existed", HttpStatus.BAD_REQUEST),
    //19XX
    BOOKING_EXISTED(1901, "Booking existed", HttpStatus.BAD_REQUEST),
    BOOKING_NOT_EXIST(1902, "Booking not existed", HttpStatus.BAD_REQUEST),
    BOOKING_ORDER_NOT_EXIST(1903, "Booking order not existed", HttpStatus.BAD_REQUEST),
    EXPERT_NOT_EXIST(1904, "Expert not existed", HttpStatus.BAD_REQUEST),
    BOOKING_VALID_SPAM(1905, "Spam Booking", HttpStatus.BAD_REQUEST),
    BOOKING_TIME_OUT(1906, "Spam Booking", HttpStatus.BAD_REQUEST),
    EXPERT_TIME_SLOT_UNAVAILABLE(1907 , "Expert was booked", HttpStatus.BAD_REQUEST),
    //20XX
    BLOG_NAME_EXISTED(2000, "Blog name existed", HttpStatus.BAD_REQUEST),
    BLOG_NOT_EXIST(2001, "Blog not existed", HttpStatus.BAD_REQUEST),
    BLOG_EXISTED(2002, "Blog existed", HttpStatus.BAD_REQUEST),
    INVALID_BLOG_NAME(2003, "Blog name not existed", HttpStatus.BAD_REQUEST),
    ;


    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
