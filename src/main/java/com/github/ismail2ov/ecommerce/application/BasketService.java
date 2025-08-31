package com.github.ismail2ov.ecommerce.application;

import java.util.Optional;

import com.github.ismail2ov.ecommerce.domain.Basket;
import com.github.ismail2ov.ecommerce.domain.BasketRepository;
import com.github.ismail2ov.ecommerce.domain.Items;
import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.domain.exception.BasketNotFoundException;

public class BasketService {

    private final BasketRepository basketRepository;

    public BasketService(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    public Basket getForUser(long userId) {
        return basketRepository.getByUserId(userId).orElseThrow(() -> new BasketNotFoundException("Basket not found for user with id: " + userId));
    }

    public Basket addProductToBasket(Long userId, Product product) {
        Optional<Basket> optBasket = basketRepository.getByUserId(userId);
        Basket basket = optBasket.map(b -> b.addItem(product)).orElseGet(() -> Basket.forUser(userId, Items.with(product)));

        return basketRepository.save(basket);
    }
}
