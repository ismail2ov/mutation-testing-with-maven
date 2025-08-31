package com.github.ismail2ov.ecommerce.infrastructure.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.domain.ProductPage;
import com.github.ismail2ov.ecommerce.infrastructure.controller.model.ProductPageRDTO;
import com.github.ismail2ov.ecommerce.infrastructure.controller.model.ProductRDTO;
import com.github.ismail2ov.ecommerce.infrastructure.persistence.ProductEntity;

@Mapper
public interface ProductMapper {

    List<ProductRDTO> from(List<Product> products);

    ProductPageRDTO from(ProductPage productPage);

    Product fromEntity(ProductEntity productEntity);

    ProductEntity entityFrom(Product product);
}
