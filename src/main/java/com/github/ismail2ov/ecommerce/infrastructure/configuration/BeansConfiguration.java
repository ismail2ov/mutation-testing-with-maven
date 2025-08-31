package com.github.ismail2ov.ecommerce.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.ismail2ov.ecommerce.application.ProductService;
import com.github.ismail2ov.ecommerce.domain.ProductRepository;

@Configuration
public class BeansConfiguration {

    @Bean
    public ProductService getProductService(ProductRepository productRepository) {
        return new ProductService(productRepository);
    }
}