package com.swd392.skincare_products_sales_system.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface CloudService {
    String uploadFile(MultipartFile multipartFile) throws IOException;
}
