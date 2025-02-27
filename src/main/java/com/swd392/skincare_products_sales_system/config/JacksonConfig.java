package com.swd392.skincare_products_sales_system.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Đăng ký JavaTimeModule để hỗ trợ LocalDate, LocalDateTime, v.v.
        objectMapper.registerModule(new JavaTimeModule());

        // Tắt tính năng WRITE_DATES_AS_TIMESTAMPS để sử dụng định dạng ISO cho ngày
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Cho phép xử lý các ký tự điều khiển không được trích dẫn
        objectMapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);

        return objectMapper;
    }
}
