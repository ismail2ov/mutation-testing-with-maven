package com.github.ismail2ov.ecommerce.infrastructure.persistence;

import org.springframework.stereotype.Repository;

import com.github.ismail2ov.ecommerce.domain.Basket;
import com.github.ismail2ov.ecommerce.domain.BasketRepository;

@Repository
public class BasketRepositoryImpl implements BasketRepository {

    @Override
    public Basket save(Basket basket) {
        return null;
    }
}
