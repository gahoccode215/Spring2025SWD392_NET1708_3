package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.model.User;
import com.swd392.skincare_products_sales_system.repository.UserRepository;
import com.swd392.skincare_products_sales_system.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/mock")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MockController {

    UserService userService;
    UserRepository userRepository;
    @GetMapping
    public String test(){
        Optional<User> user = userRepository.findByUsername("admin");
        return user.toString();
    }
}
