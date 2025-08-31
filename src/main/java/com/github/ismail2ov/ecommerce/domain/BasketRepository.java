package com.github.ismail2ov.ecommerce.domain;

import java.util.Optional;

public interface BasketRepository {

    Basket save(Basket basket);

    Optional<Basket> getByUserId(long userId);
}
