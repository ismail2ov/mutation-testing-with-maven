package com.github.ismail2ov.ecommerce.domain;

import com.github.ismail2ov.ecommerce.domain.exception.InvalidUserException;

public record Basket(Long id, Long userId, Items items) {

    public Basket {
        if (userId == null || userId <= 0) {
            throw new InvalidUserException("Invalid userId provided: " + userId);
        }
    }

    public static Basket forUser(Long userId) {
        return new Basket(null, userId, Items.create());
    }

    public static Basket forUser(Long userId, Items items) {
        return new Basket(null, userId, items);
    }

    public Basket addItem(Product product) {
        return new Basket(id, userId, items.addItem(product));
    }
}