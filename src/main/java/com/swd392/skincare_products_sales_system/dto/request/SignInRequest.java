package com.swd392.skincare_products_sales_system.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequest {
    private String username;
    private String password;
    private String platform; // web, mobile, tablet
    private String deviceToken; // for push notify
    private String versionApp;
}
