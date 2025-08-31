package com.github.ismail2ov.ecommerce.domain;

import java.util.List;

public record ProductPage(Product product, List<Product> crossSelling) {

}
