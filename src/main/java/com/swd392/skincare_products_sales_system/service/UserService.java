package com.swd392.skincare_products_sales_system.service;


import com.swd392.skincare_products_sales_system.dto.request.user.UserCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.user.UserUpdateProfileRequest;
import com.swd392.skincare_products_sales_system.dto.request.user.UserUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.user.UserPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.user.UserResponse;
import com.swd392.skincare_products_sales_system.enums.Status;

public interface UserService {
    UserPageResponse getUsers(boolean admin, String keyword, int page, int size, String roleName, Status status, String sortBy, String order);

    UserResponse createUser(UserCreationRequest request);

    UserResponse getUser(String userId);

    UserResponse updateUser(UserUpdateRequest request, String userId);

    void deleteUser(String userId);

    void changeUserStatus(String userId, Status status);

    UserResponse getUserProfile();

    UserResponse updateUserProfile(UserUpdateProfileRequest request);
}
