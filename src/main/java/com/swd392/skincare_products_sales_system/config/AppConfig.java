package com.swd392.skincare_products_sales_system.config;

import com.github.slugify.Slugify;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    public Slugify slugify() {
        return new Slugify();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
