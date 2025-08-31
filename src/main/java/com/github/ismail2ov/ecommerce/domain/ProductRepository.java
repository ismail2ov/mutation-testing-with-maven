package com.github.ismail2ov.ecommerce.domain;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    void addCrossSellProduct(Long productId, Long xsellId);

    List<Product> findAll();

    Optional<Product> findById(Long id);

    List<Product> getCrossSellProducts(Long id);
}
