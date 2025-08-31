package com.github.ismail2ov.ecommerce.domain;

import java.util.List;
import java.util.Objects;

public record Items(List<Product> products) {

    public Items {
        products = List.copyOf(Objects.requireNonNull(products, "Products must not be null"));
    }

    public static Items with(Product product) {
        return new Items(List.of(product));
    }

}
