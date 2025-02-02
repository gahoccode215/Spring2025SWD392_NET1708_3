package com.swd392.skincare_products_sales_system.service;


import com.swd392.skincare_products_sales_system.dto.request.*;
import com.swd392.skincare_products_sales_system.dto.response.UserPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.UserResponse;

import java.util.Map;

public interface UserService {
    UserPageResponse findAll(String keyword, String sort, int page, int size);

    UserResponse findById(Long id);

    UserResponse findByUsername(String username);

    UserResponse findByEmail(String email);

    UserResponse createUser(UserCreationRequest request);

    UserResponse getUser(String userId);

    UserResponse updateUser(UserUpdateRequest request, String userId);

    void changePassword(UserPasswordRequest req);

    void deleteUser(String userId);

    
}
