package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.blog.BlogCreateRequest;
import com.swd392.skincare_products_sales_system.dto.request.blog.BlogUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.BlogResponse;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.enity.Blog;

import java.util.List;

public interface BlogService {

    BlogResponse createBlog(BlogCreateRequest request);
    BlogResponse updateBlog(BlogUpdateRequest request, Long id);
    Blog deleteBlog(Long id);
    BlogResponse getBlogById(Long id);
    List<Blog> getBlog();
    void changeStatus(Long id, Status status);

}
