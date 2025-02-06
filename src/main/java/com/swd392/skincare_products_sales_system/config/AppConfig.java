package com.swd392.skincare_products_sales_system.config;

import com.github.slugify.Slugify;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Slugify slugify() {
        return new Slugify();
    }
}
