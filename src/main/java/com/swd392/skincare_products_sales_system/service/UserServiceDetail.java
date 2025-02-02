package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public record UserServiceDetail(UserRepository userRepository) {
//    public UserDetailsService userDetailsService() {
//        return userRepository::findByUsername;
//    }
}
