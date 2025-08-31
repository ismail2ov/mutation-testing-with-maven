package com.github.ismail2ov.ecommerce.application;

import java.util.List;

import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.domain.ProductPage;
import com.github.ismail2ov.ecommerce.domain.ProductRepository;
import com.github.ismail2ov.ecommerce.domain.exception.ProductNotFoundException;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    public ProductPage getProductBy(Long id) {
        Product product = this.productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        List<Product> crossSellProducts = productRepository.getCrossSellProducts(id);

        return new ProductPage(product, crossSellProducts);
    }
}
