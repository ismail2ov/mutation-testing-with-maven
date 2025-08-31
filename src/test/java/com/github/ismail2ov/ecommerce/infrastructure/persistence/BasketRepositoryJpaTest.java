package com.github.ismail2ov.ecommerce.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import org.junit.jupiter.api.Test;

import com.github.ismail2ov.ecommerce.domain.Basket;
import com.github.ismail2ov.ecommerce.domain.BasketRepository;
import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.infrastructure.mapper.BasketMapperImpl;

@DataJpaTest(properties = {
    "spring.test.database.replace=NONE",
    "spring.datasource.url=jdbc:tc:postgresql:17-alpine:///test"
})
@Sql(scripts = "/scripts/RESET_DB.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Import({BasketRepositoryImpl.class, BasketMapperImpl.class})
class BasketRepositoryJpaTest {

    private static final Product PRODUCT_1 = new Product(1L, "Dell Latitude 3301 Intel Core i7-8565U/8GB/512GB SSD/13.3", "999,00 €");
    private static final Product PRODUCT_2 = new Product(2L, "Samsonite Airglow Laptop Sleeve 13.3", "41,34 €");

    @Autowired
    BasketRepository basketRepository;

    @Test
    void should_return_basket_by_user_id() {
        Basket basket = Basket.forUser(456L);
        basket.addItem(PRODUCT_1);
        basket.addItem(PRODUCT_2);
        basketRepository.save(basket);

        Optional<Basket> actual = basketRepository.getByUserId(456L);

        assertThat(actual)
            .isPresent()
            .get()
            .extracting(Basket::userId, Basket::items)
            .containsExactly(basket.userId(), basket.items());
    }

    @Test
    void should_create_new_basket() {
        Basket basket = Basket.forUser(25L);

        Basket actual = basketRepository.save(basket);

        assertThat(actual)
            .isNotNull()
            .extracting(Basket::userId)
            .isEqualTo(basket.userId());
    }

    @Test
    void should_throw_an_exception_if_basket_is_null() {

        Throwable thrown = catchThrowable(() -> basketRepository.save(null));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Basket must not be null");
    }
}