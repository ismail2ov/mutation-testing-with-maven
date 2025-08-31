package com.github.ismail2ov.ecommerce.domain;

public record Product(Long id, String name, String price) {

    public Product(String name, String price) {
        this(null, name, price);
    }
}
