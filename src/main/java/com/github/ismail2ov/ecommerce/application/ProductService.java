package com.github.ismail2ov.ecommerce.application;

import java.util.List;

import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.domain.ProductPage;
import com.github.ismail2ov.ecommerce.domain.ProductRepository;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return null;
    }

    public ProductPage getProductBy(Long id) {
        return null;
    }
}
