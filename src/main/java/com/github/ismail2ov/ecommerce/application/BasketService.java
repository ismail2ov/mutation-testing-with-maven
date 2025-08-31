package com.github.ismail2ov.ecommerce.application;

import com.github.ismail2ov.ecommerce.domain.Basket;
import com.github.ismail2ov.ecommerce.domain.BasketRepository;
import com.github.ismail2ov.ecommerce.domain.Product;

public class BasketService {

    private final BasketRepository basketRepository;

    public BasketService(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    public Basket getForUser(long userId) {
        return null;
    }

    public Basket addProductToBasket(Long userId, Product product) {
        return null;
    }
}
