package com.github.ismail2ov.ecommerce.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.github.ismail2ov.ecommerce.ProductTestDataFactory;
import com.github.ismail2ov.ecommerce.domain.Basket;
import com.github.ismail2ov.ecommerce.domain.BasketRepository;
import com.github.ismail2ov.ecommerce.domain.Items;
import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.domain.exception.BasketNotFoundException;

@ExtendWith(SpringExtension.class)
class BasketServiceTest {

    public static final long USER_ID = 101;
    public static final Product PRODUCT = ProductTestDataFactory.product(3L);
    public static final Basket EXPECTED_BASKET = new Basket(1L, USER_ID, Items.with(PRODUCT));

    @Mock
    BasketRepository basketRepository;

    @InjectMocks
    BasketService basketService;

    @Test
    void when_basket_found_then_return_it() {
        when(basketRepository.getByUserId(USER_ID)).thenReturn(Optional.of(EXPECTED_BASKET));

        Basket actualBasket = basketService.getForUser(USER_ID);

        assertThat(actualBasket).isNotNull();
    }

    @Test
    void when_basket_does_not_exists_then_throw_an_exception() {

        Throwable thrown = catchThrowable(() -> basketService.getForUser(USER_ID));

        assertThat(thrown).isInstanceOf(BasketNotFoundException.class);
    }

    @Test
    void when_add_product_to_basket_then_return_it() {
        when(basketRepository.getByUserId(USER_ID)).thenReturn(Optional.of(new Basket(1L, USER_ID, Items.create())));
        when(basketRepository.save(EXPECTED_BASKET)).thenReturn(EXPECTED_BASKET);

        Basket actualBasket = basketService.addProductToBasket(USER_ID, PRODUCT);

        assertThat(actualBasket).isNotNull();
    }

    @Test
    void when_basket_does_not_exists_then_create_it() {
        Basket expectedBasket = mock(Basket.class);
        when(basketRepository.getByUserId(USER_ID)).thenReturn(Optional.empty());
        when(basketRepository.save(any(Basket.class))).thenReturn(expectedBasket);

        Basket actualBasket = basketService.addProductToBasket(USER_ID, PRODUCT);

        assertThat(actualBasket).isNotNull();
    }

}