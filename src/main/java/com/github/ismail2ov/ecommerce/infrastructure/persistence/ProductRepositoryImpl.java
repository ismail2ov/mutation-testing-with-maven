package com.github.ismail2ov.ecommerce.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<Product> findAll() {
        return List.of();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Product> getCrossSellProducts(Long id) {
        return List.of();
    }
}
