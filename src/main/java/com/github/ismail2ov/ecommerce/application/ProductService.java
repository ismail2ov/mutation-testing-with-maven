package com.github.ismail2ov.ecommerce.application;

import com.github.ismail2ov.ecommerce.domain.ProductRepository;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
