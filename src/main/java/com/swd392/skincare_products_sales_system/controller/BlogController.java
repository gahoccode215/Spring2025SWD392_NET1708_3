package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.BlogResponse;
import com.swd392.skincare_products_sales_system.model.Blog;
import com.swd392.skincare_products_sales_system.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
@Tag(name = "Blog Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogController {

    BlogService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all blog", description = "API blog")
    public ApiResponse<List<Blog>> getBlog() {
        return ApiResponse.<List<Blog>>builder()
                .code(HttpStatus.OK.value())
                .message("Get all successfully")
                .result(service.getBlog())
                .build();
    }

    @GetMapping("/{blogId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get blog by Id", description = "API blog")
    public ApiResponse<BlogResponse> getBlogById(@PathVariable Long blogId) {
        return ApiResponse.<BlogResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Successfully")
                .result(service.getBlogById(blogId))
                .build();
    }
}
