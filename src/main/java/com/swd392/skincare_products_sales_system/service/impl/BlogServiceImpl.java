package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.request.blog.BlogCreateRequest;
import com.swd392.skincare_products_sales_system.dto.request.blog.BlogUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.BlogResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.Blog;
import com.swd392.skincare_products_sales_system.repository.BlogRepository;
import com.swd392.skincare_products_sales_system.service.BlogService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogServiceImpl implements BlogService {

    BlogRepository blogRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public BlogResponse createBlog(BlogCreateRequest request) {
        if(blogRepository.findBlogByBlogNameAndIsDeletedFalse(request.getBlogName()).isPresent()){
            throw new AppException(ErrorCode.BLOG_NAME_EXISTED);
        }
        if (request.getBlogName() == null || request.getBlogName().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_BLOG_NAME);
        }

        Blog blog = Blog.builder()
                .blogName(request.getBlogName())
                .image(request.getImage())
                .description(request.getDescription())
                .content(request.getContent())
                .date(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
        blog.setIsDeleted(false);
        blogRepository.save(blog);
        return BlogResponse.builder()
                .blogName(blog.getBlogName())
                .content(blog.getContent())
                .image(blog.getImage())
                .status(blog.getStatus())
                .id(blog.getId())
                .description(blog.getDescription())
                .date(LocalDate.now())
                .build();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public BlogResponse updateBlog(BlogUpdateRequest request, Long blogId) {
        Blog existingBlog = blogRepository.findById(blogId)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXIST));

        if (blogRepository.findBlogByBlogNameAndIsDeletedFalse(request.getBlogName()).isPresent()) {
            throw new AppException(ErrorCode.BLOG_NAME_EXISTED);
        }

        if (request.getBlogName() == null || request.getBlogName().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_BLOG_NAME);
        }
        existingBlog.setBlogName(request.getBlogName());
        existingBlog.setImage(request.getImage());
        existingBlog.setDescription(request.getDescription());
        existingBlog.setContent(request.getContent());
        existingBlog.setDate(LocalDateTime.now());
        existingBlog.setStatus(Status.ACTIVE);

        blogRepository.save(existingBlog);

        return BlogResponse.builder()
                .blogName(existingBlog.getBlogName())
                .content(existingBlog.getContent())
                .image(existingBlog.getImage())
                .status(existingBlog.getStatus())
                .id(existingBlog.getId())
                .description(existingBlog.getDescription())
                .date(existingBlog.getDate().toLocalDate())
                .build();
    }


    @Override
    public Blog deleteBlog(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXIST));
        blog.setIsDeleted(true);
        blog.setStatus(Status.INACTIVE);
        return blogRepository.save(blog);
    }

    @Override
    public BlogResponse getBlog(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXIST));
        return BlogResponse.builder()
                .date(blog.getDate().toLocalDate())
                .blogName(blog.getBlogName())
                .content(blog.getContent())
                .image(blog.getImage())
                .status(blog.getStatus())
                .id(blog.getId())
                .description(blog.getDescription())
                .build();
    }

    @Override
    public List<Blog> getBlog() {
        List<Blog> list = blogRepository.findAll()
                .stream()
                .filter(blog -> !blog.getIsDeleted() && blog.getStatus().equals(Status.ACTIVE))
                .toList();
        return list;
    }

    @Override
    public void changeStatus(Long id, Status status) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_EXIST));
        if(blog.getStatus() == Status.ACTIVE){
            blog.setStatus(Status.INACTIVE);
        }else{
            blog.setStatus(Status.ACTIVE);
        }
        blogRepository.save(blog);
    }
}
