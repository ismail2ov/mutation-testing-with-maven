package com.github.ismail2ov.ecommerce.infrastructure.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.github.ismail2ov.ecommerce.application.ProductService;
import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.domain.ProductPage;
import com.github.ismail2ov.ecommerce.infrastructure.controller.api.ProductsApi;
import com.github.ismail2ov.ecommerce.infrastructure.controller.model.ProductPageRDTO;
import com.github.ismail2ov.ecommerce.infrastructure.controller.model.ProductRDTO;
import com.github.ismail2ov.ecommerce.infrastructure.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController implements ProductsApi {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Override
    public ResponseEntity<List<ProductRDTO>> getAll() {
        List<Product> products = productService.getAllProducts();
        List<ProductRDTO> productRdtos = productMapper.from(products);

        return ResponseEntity.ok(productRdtos);
    }

    @Override
    public ResponseEntity<ProductPageRDTO> getById(Long id) {
        ProductPage productPage = productService.getProductBy(id);
        ProductPageRDTO productPageRDTO = productMapper.from(productPage);

        return ResponseEntity.ok(productPageRDTO);
    }
}
