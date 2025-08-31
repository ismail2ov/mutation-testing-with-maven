package com.github.ismail2ov.ecommerce.infrastructure.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.github.ismail2ov.ecommerce.application.ProductService;
import com.github.ismail2ov.ecommerce.infrastructure.controller.api.ProductsApi;
import com.github.ismail2ov.ecommerce.infrastructure.controller.model.ProductPageRDTO;
import com.github.ismail2ov.ecommerce.infrastructure.controller.model.ProductRDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController implements ProductsApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<List<ProductRDTO>> getAll() {
        return null;
    }

    @Override
    public ResponseEntity<ProductPageRDTO> getById(Long id) {
        return null;
    }
}
