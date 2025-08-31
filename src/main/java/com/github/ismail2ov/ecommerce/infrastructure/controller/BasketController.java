package com.github.ismail2ov.ecommerce.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.github.ismail2ov.ecommerce.application.BasketService;
import com.github.ismail2ov.ecommerce.domain.Basket;
import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.infrastructure.controller.api.UsersApi;
import com.github.ismail2ov.ecommerce.infrastructure.controller.model.BasketRDTO;
import com.github.ismail2ov.ecommerce.infrastructure.controller.model.ProductRDTO;
import com.github.ismail2ov.ecommerce.infrastructure.mapper.BasketMapper;
import com.github.ismail2ov.ecommerce.infrastructure.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BasketController implements UsersApi {

    private final BasketService basketService;

    private final ProductMapper productMapper;

    private final BasketMapper basketMapper;

    @Override
    public ResponseEntity<BasketRDTO> addProductToBasket(Long userId, ProductRDTO productRDTO) {
        Product product = productMapper.productFrom(productRDTO);
        Basket basket = basketService.addProductToBasket(userId, product);

        return ResponseEntity.ok(basketMapper.from(basket));
    }

    @Override
    public ResponseEntity<BasketRDTO> getByUserId(Long userId) {
        Basket basket = basketService.getForUser(userId);

        return ResponseEntity.ok(basketMapper.from(basket));
    }
}
