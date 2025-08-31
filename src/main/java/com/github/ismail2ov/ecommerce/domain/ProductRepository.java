package com.github.ismail2ov.ecommerce.domain;

public interface ProductRepository {

    Product save(Product product);

    void addCrossSellProduct(Long productId, Long xsellId);
}
