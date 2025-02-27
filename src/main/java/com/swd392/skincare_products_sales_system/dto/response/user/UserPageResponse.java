package com.swd392.skincare_products_sales_system.dto.response.user;

import com.swd392.skincare_products_sales_system.dto.response.AbstractPageResponse;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UserPageResponse extends AbstractPageResponse implements Serializable {

    private List<UserResponse> userResponses;
}
