package com.swd392.skincare_products_sales_system.service;


import com.swd392.skincare_products_sales_system.dto.request.*;
import com.swd392.skincare_products_sales_system.dto.response.ProductPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.UserPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.UserResponse;
import com.swd392.skincare_products_sales_system.enums.Status;

public interface UserService {
    UserPageResponse getUsers(boolean admin, String keyword, int page, int size,String roleName, Status status, String sortBy, String order);

    UserResponse createUser(UserCreationRequest request);

    UserResponse getUser(String userId);

    UserResponse updateUser(UserUpdateRequest request, String userId);

    void deleteUser(String userId);

    void changeUserStatus(String userId, Status status);

    UserResponse getUserProfile();
}
