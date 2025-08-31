package com.github.ismail2ov.ecommerce.infrastructure.persistence;

import org.springframework.stereotype.Repository;

import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.domain.ProductRepository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @Override
    public Product save(Product product) {
        return null;
    }

    @Override
    public void addCrossSellProduct(Long productId, Long xsellId) {

    }
}
