package com.github.ismail2ov.ecommerce.domain.exception;

public class BasketPersistenceException extends RuntimeException {

    public BasketPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
