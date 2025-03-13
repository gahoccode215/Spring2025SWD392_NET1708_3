package com.swd392.skincare_products_sales_system.mapper;

import com.swd392.skincare_products_sales_system.dto.response.user.UserResponse;
import com.swd392.skincare_products_sales_system.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);

}
