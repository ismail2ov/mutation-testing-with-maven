package com.github.ismail2ov.ecommerce.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import org.junit.jupiter.api.Test;

import com.github.ismail2ov.ecommerce.ProductTestDataFactory;
import com.github.ismail2ov.ecommerce.domain.Basket;
import com.github.ismail2ov.ecommerce.domain.BasketRepository;
import com.github.ismail2ov.ecommerce.domain.Items;
import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.infrastructure.controller.model.BasketRDTO;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/scripts/RESET_DB.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BasketIntegrationTest {

    public static final int USER_ID = 2;
    public static final String BASKET_URL = "/users/" + USER_ID + "/basket";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    BasketRepository basketRepository;

    @Test
    void when_add_product_to_empty_basket_then_basket_has_1_item() {
        int userId = 1;
        Product productToAdd = ProductTestDataFactory.product(4L);

        ResponseEntity<BasketRDTO> result = testRestTemplate.postForEntity("/users/" + userId + "/basket", productToAdd, BasketRDTO.class);

        assertThat(result).satisfies(response -> {
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response).isNotNull();
            assertThat(Objects.requireNonNull(response.getBody()).getItems().getProducts()).hasSize(1);
        });
    }

    @Test
    void when_add_product_to_basket_the_same_item_then_basket_has_1_item() {

        Product productToAdd = ProductTestDataFactory.product(3L);

        ResponseEntity<BasketRDTO> result = testRestTemplate.postForEntity(BASKET_URL, productToAdd, BasketRDTO.class);

        assertThat(result).satisfies(response -> {
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(Objects.requireNonNull(response.getBody()).getItems().getProducts()).hasSize(1);
        });
    }

    @Test
    void when_add_product_to_basket_with_item_then_basket_has_2_items() {
        createBasket();

        Product productToAdd = ProductTestDataFactory.product(4L);

        ResponseEntity<BasketRDTO> result = testRestTemplate.postForEntity(BASKET_URL, productToAdd, BasketRDTO.class);

        assertThat(result).satisfies(response -> {
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(Objects.requireNonNull(response.getBody()).getItems().getProducts()).hasSize(2);
        });
    }

    @Test
    void when_user_has_not_basket_then_returns_not_found() {
        int userId = 1;

        ResponseEntity<BasketRDTO> result = testRestTemplate.getForEntity("/users/" + userId + "/basket", BasketRDTO.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void when_user_has_basket_then_returns_the_basket() {
        createBasket();

        ResponseEntity<BasketRDTO> result = testRestTemplate.getForEntity(BASKET_URL, BasketRDTO.class);

        assertThat(result).satisfies(response -> {
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(Objects.requireNonNull(response.getBody()).getItems().getProducts()).hasSize(1);
        });
    }

    private void createBasket() {
        Product product = ProductTestDataFactory.product(3L);
        Basket basket = Basket.forUser((long) USER_ID, Items.with(product));
        basketRepository.save(basket);
    }
}
