package com.github.ismail2ov.ecommerce.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import jakarta.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import com.github.ismail2ov.ecommerce.ProductTestDataFactory;
import com.github.ismail2ov.ecommerce.domain.Basket;
import com.github.ismail2ov.ecommerce.domain.Items;
import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.domain.exception.BasketPersistenceException;
import com.github.ismail2ov.ecommerce.infrastructure.mapper.BasketMapper;
import com.github.ismail2ov.ecommerce.infrastructure.mapper.BasketMapperImpl;

@ExtendWith(SpringExtension.class)
@Import(BasketMapperImpl.class)
class BasketRepositoryImplTest {

    public static final long USER_ID = 101;
    public static final Product PRODUCT = ProductTestDataFactory.product(3L);
    public static final Basket BASKET = new Basket(1L, USER_ID, Items.with(PRODUCT));

    @Mock
    EntityManager entityManager;

    @Autowired
    BasketMapper basketMapper;

    BasketRepositoryImpl basketRepository;

    @BeforeEach
    void setUp() {
        basketRepository = new BasketRepositoryImpl(entityManager, basketMapper);
    }

    @Test
    void should_throw_an_exception_if_basket_is_null() {

        Throwable thrown = catchThrowable(() -> basketRepository.save(null));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Basket must not be null");
    }

    @Test
    void should_save_a_basket() {

        Basket basket = basketRepository.save(Basket.forUser(1L, Items.with(PRODUCT)));

        assertThat(basket)
            .isNotNull();
    }

    @Test
    void should_throw_an_exception_when_try_to_update_unexisting_basket() {

        doThrow(BasketPersistenceException.class)
            .when(entityManager).merge(any(BasketEntity.class));

        Throwable thrown = catchThrowable(() -> basketRepository.save(BASKET));

        assertThat(thrown).isInstanceOf(BasketPersistenceException.class)
            .hasMessage("Failed to save basket");
    }

}
