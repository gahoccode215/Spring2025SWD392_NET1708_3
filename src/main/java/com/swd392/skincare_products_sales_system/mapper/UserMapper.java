package com.swd392.skincare_products_sales_system.mapper;

import com.swd392.skincare_products_sales_system.dto.request.UserCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.UserUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.RoleResponse;
import com.swd392.skincare_products_sales_system.dto.response.UserPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.UserResponse;
import com.swd392.skincare_products_sales_system.model.Role;
import com.swd392.skincare_products_sales_system.model.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);

}
