package com.github.ismail2ov.ecommerce.domain.exception;

public class BasketNotFoundException extends RuntimeException {

    public BasketNotFoundException(String message) {
        super(message);
    }

}
