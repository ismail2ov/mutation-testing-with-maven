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

    public static Items create() {
        return new Items(List.of());
    }

    public Items addItem(Product product) {
        Objects.requireNonNull(product, "Product must not be null");
        if (products.contains(product)) {
            return this;
        }
        var newProducts = new java.util.ArrayList<>(products);
        newProducts.add(product);
        return new Items(newProducts);
    }
}
