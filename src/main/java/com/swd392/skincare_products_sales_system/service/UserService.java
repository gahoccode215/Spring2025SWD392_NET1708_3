package com.swd392.skincare_products_sales_system.service;


import com.swd392.skincare_products_sales_system.dto.request.UserCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.UserPasswordRequest;
import com.swd392.skincare_products_sales_system.dto.request.UserUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.UserPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.UserResponse;

public interface UserService {
    UserPageResponse findAll(String keyword, String sort, int page, int size);

    UserResponse findById(Long id);

    UserResponse findByUsername(String username);

    UserResponse findByEmail(String email);

    long save(UserCreationRequest req);

    void update(UserUpdateRequest req);

    void changePassword(UserPasswordRequest req);

    void delete(Long id);
}
