package com.swd392.skincare_products_sales_system.service;

import org.hibernate.mapping.Map;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudService {
    String uploadFile(MultipartFile multipartFile) throws IOException;
}
