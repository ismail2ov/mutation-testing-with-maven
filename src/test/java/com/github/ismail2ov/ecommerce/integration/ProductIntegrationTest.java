package com.github.ismail2ov.ecommerce.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import org.junit.jupiter.api.Test;

import com.github.ismail2ov.ecommerce.ProductTestDataFactory;
import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.domain.ProductRepository;
import com.github.ismail2ov.ecommerce.infrastructure.controller.model.ProductPageRDTO;
import com.github.ismail2ov.ecommerce.infrastructure.controller.model.ProductRDTO;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/scripts/RESET_DB.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ProductIntegrationTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    ProductRepository productRepository;

    @Test
    void when_get_product_by_id_then_return_also_cross_sell_products() {
        List<Product> products = ProductTestDataFactory.products(List.of(1L, 2L, 3L));

        Product product = productRepository.save(newProductFrom(products.get(0)));
        Product crossSell1 = productRepository.save(newProductFrom(products.get(1)));
        Product crossSell2 = productRepository.save(newProductFrom(products.get(2)));

        productRepository.addCrossSellProduct(product.id(), crossSell1.id());
        productRepository.addCrossSellProduct(product.id(), crossSell2.id());

        ResponseEntity<ProductPageRDTO> result = testRestTemplate.getForEntity("/products/1", ProductPageRDTO.class);

        assertThat(result)
            .returns(HttpStatus.OK, ResponseEntity::getStatusCode)
            .extracting(ResponseEntity::getBody)
            .isNotNull()
            .satisfies(body -> {
                assertThat(body.getProduct())
                    .isNotNull()
                    .extracting("name", "price")
                    .containsExactly("Dell Latitude 3301 Intel Core i7-8565U/8GB/512GB SSD/13.3", "999,00 €");

                assertThat(body.getCrossSelling())
                    .hasSize(2);
            });
    }

    @Test
    void when_try_to_get_product_that_not_exists_then_returns_not_found() {

        ResponseEntity<ProductRDTO> result = testRestTemplate.getForEntity("/products/1001", ProductRDTO.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void when_get_products_then_return_all_products() {
        List<Product> products = ProductTestDataFactory.products(List.of(1L, 2L, 3L, 4L));
        products.forEach(product -> productRepository.save(newProductFrom(product)));

        ResponseEntity<Product[]> result = testRestTemplate.getForEntity("/products", Product[].class);

        assertThat(result).satisfies(response -> {
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(4);
        });
    }

    private @NotNull Product newProductFrom(Product product) {
        return new Product(product.name(), product.price());
    }
}