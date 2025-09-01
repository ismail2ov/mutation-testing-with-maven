package com.github.ismail2ov.ecommerce.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.github.ismail2ov.ecommerce.ProductTestDataFactory;
import com.github.ismail2ov.ecommerce.domain.exception.InvalidUserException;

class BasketTest {

    public static final Product PRODUCT = ProductTestDataFactory.product(1L);

    @Test
    void when_product_does_not_exists_in_then_basket_the_add_it() {

        Basket basket = new Basket(1L, 1L, Items.with(PRODUCT));
        basket.addItem(ProductTestDataFactory.product(3L));

        assertThat(basket)
            .isNotNull()
            .extracting(b -> b.items().products())
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .hasSize(1);
    }

    @Test
    void when_product_exists_in_basket_do_not_duplicate_it() {

        Basket basket = new Basket(1L, 1L, Items.with(PRODUCT));
        basket.addItem(PRODUCT);

        assertThat(basket)
            .isNotNull()
            .extracting(b -> b.items().products())
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .hasSize(1);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = -1L)
    void when_user_id_is_not_valid_then_throw_an_exception(Long userId) {

        Throwable thrown = catchThrowable(() -> new Basket(1L, userId, Items.create()));

        assertThat(thrown).isInstanceOf(InvalidUserException.class)
            .hasMessage("Invalid userId provided: " + userId);
    }
}