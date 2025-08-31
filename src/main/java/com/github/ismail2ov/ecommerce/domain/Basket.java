package com.github.ismail2ov.ecommerce.domain;

import com.github.ismail2ov.ecommerce.domain.exception.InvalidUserException;

public record Basket(Long id, Long userId, Items items) {

    public Basket {
        if (userId == null || userId <= 0) {
            throw new InvalidUserException("Invalid userId provided: " + userId);
        }
    }

    public static Basket forUser(Long userId, Items items) {
        return new Basket(null, userId, items);
    }

}