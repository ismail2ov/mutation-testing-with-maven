package com.github.ismail2ov.ecommerce.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.junit.jupiter.api.Test;

import com.github.ismail2ov.ecommerce.TestcontainersConfiguration;
import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.infrastructure.controller.model.ProductPageRDTO;
import com.github.ismail2ov.ecommerce.infrastructure.controller.model.ProductRDTO;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductIntegrationTest {

    @Autowired
    public TestRestTemplate testRestTemplate;

    @Test
    void when_get_product_by_id_then_return_also_cross_sell_products() {

        ResponseEntity<ProductPageRDTO> result = testRestTemplate.getForEntity("/products/1", ProductPageRDTO.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void when_try_to_get_product_that_not_exists_then_returns_not_found() {

        ResponseEntity<ProductRDTO> result = testRestTemplate.getForEntity("/products/2", ProductRDTO.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void when_get_products_then_return_all_products() {

        ResponseEntity<Product[]> result = testRestTemplate.getForEntity("/products", Product[].class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}