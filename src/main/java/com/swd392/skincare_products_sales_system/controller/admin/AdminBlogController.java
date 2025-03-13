package com.swd392.skincare_products_sales_system.controller.admin;

import com.swd392.skincare_products_sales_system.dto.request.blog.BlogCreateRequest;
import com.swd392.skincare_products_sales_system.dto.request.blog.BlogUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.BlogResponse;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.enity.Blog;
import com.swd392.skincare_products_sales_system.repository.BlogRepository;
import com.swd392.skincare_products_sales_system.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/blog")
@Tag(name = "Blog Controller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminBlogController {

    BlogService service;
    BlogRepository blogRepository;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a Blog for ADMIN, MANAGER", description = "API create Blog ")
    public ApiResponse<BlogResponse> createBlog(@RequestBody @Valid BlogCreateRequest request) {
        return ApiResponse.<BlogResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Create Blog successfully")
                .result(service.createBlog(request))
                .build();
    }

    @PutMapping("/{blogId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a Blog for ADMIN, MANAGER", description = "API retrieve Blog ")
    public ApiResponse<BlogResponse> updateBlog( @PathVariable Long blogId, @RequestBody @Valid BlogUpdateRequest request) {
        return ApiResponse.<BlogResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update Blog successfully")
                .result(service.updateBlog(request, blogId))
                .build();
    }

    @PatchMapping("/{blogId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change status Blog for ADMIN, MANAGER", description = "API change a status Blog ")
    public ApiResponse<Void> updateBlog(@RequestParam Status status, @PathVariable Long blogId) {
        service.changeStatus(blogId, status);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Changer Status successfully")
                .build();
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all Blog for ADMIN, MANAGER", description = "API retrieve Blog ")
    public ApiResponse<List<Blog>> getBlogs() {
        List<Blog> list = blogRepository.findAll();
        return ApiResponse.<List<Blog>>builder()
                .code(HttpStatus.OK.value())
                .message("List successfully")
                .result(list)
                .build();
    }

    @DeleteMapping("/{blogId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete Blog for ADMIN, MANAGER", description = "API delete Blog ")
    public ApiResponse<Blog> deleteBlog (@PathVariable Long blogId) {
        return ApiResponse.<Blog>builder()
                .code(HttpStatus.OK.value())
                .message("Delete Blog successfully")
                .result(service.deleteBlog(blogId))
                .build();
    }
}
