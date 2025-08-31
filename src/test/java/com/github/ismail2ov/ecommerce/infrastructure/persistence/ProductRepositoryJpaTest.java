package com.github.ismail2ov.ecommerce.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import org.junit.jupiter.api.Test;

import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.domain.ProductRepository;
import com.github.ismail2ov.ecommerce.domain.exception.CrossSellRelationAlreadyExistsException;
import com.github.ismail2ov.ecommerce.domain.exception.ProductNotFoundException;
import com.github.ismail2ov.ecommerce.infrastructure.mapper.ProductMapperImpl;

@DataJpaTest(properties = {
    "spring.test.database.replace=NONE",
    "spring.datasource.url=jdbc:tc:postgresql:17-alpine:///test"
})
@Import({ProductRepositoryImpl.class, ProductMapperImpl.class})
@Sql(scripts = "/scripts/RESET_DB.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ProductRepositoryJpaTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void should_save_and_retrieve_product_by_id() {
        Product product = productRepository.save(new Product("Dell Latitude 3301 Intel Core i7-8565U/8GB/512GB SSD/13.3", "999,00 €"));

        Optional<Product> actual = productRepository.findById(product.id());

        assertThat(actual)
            .isPresent()
            .get()
            .extracting("name", "price")
            .contains("Dell Latitude 3301 Intel Core i7-8565U/8GB/512GB SSD/13.3", "999,00 €");
    }

    @Test
    void should_return_products_list() {
        productRepository.save(new Product("Dell Latitude 3301 Intel Core i7-8565U/8GB/512GB SSD/13.3", "999,00 €"));
        productRepository.save(new Product("Samsonite Airglow Laptop Sleeve 13.3", "41,34 €"));
        productRepository.save(new Product("Logitech Wireless Mouse M185", "10,78 €"));

        List<Product> actual = productRepository.findAll();

        assertThat(actual)
            .hasSize(3)
            .extracting("name")
            .containsAnyOf("Dell Latitude 3301 Intel Core i7-8565U/8GB/512GB SSD/13.3");
    }

    @Test
    void should_return_product_cross_sell() {
        Product product = productRepository.save(new Product("Dell Latitude 3301 Intel Core i7-8565U/8GB/512GB SSD/13.3", "999,00 €"));
        Product crossSellProduct1 = productRepository.save(new Product("Samsonite Airglow Laptop Sleeve 13.3", "41,34 €"));
        Product crossSellProduct2 = productRepository.save(new Product("Logitech Wireless Mouse M185", "10,78 €"));

        productRepository.addCrossSellProduct(product.id(), crossSellProduct1.id());
        productRepository.addCrossSellProduct(product.id(), crossSellProduct2.id());

        List<Product> actual = productRepository.getCrossSellProducts(product.id());

        assertThat(actual)
            .hasSize(2)
            .extracting("name")
            .containsExactlyInAnyOrder("Samsonite Airglow Laptop Sleeve 13.3", "Logitech Wireless Mouse M185");
    }

    @Test
    void should_throw_an_exception_if_product_is_null() {

        Throwable thrown = catchThrowable(() -> productRepository.save(null));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Product must not be null");
    }

    @Test
    void should_throw_an_exception_when_product_does_not_exists() {

        Throwable thrown = catchThrowable(() -> productRepository.addCrossSellProduct(1L, 2L));

        assertThat(thrown).isInstanceOf(ProductNotFoundException.class)
            .hasMessage("Product not found with id: 1");
    }

    @Test
    void should_throw_an_exception_when_cross_sell_product_does_not_exists() {
        Product product = productRepository.save(new Product("Dell Latitude 3301 Intel Core i7-8565U/8GB/512GB SSD/13.3", "999,00 €"));

        Throwable thrown = catchThrowable(() -> productRepository.addCrossSellProduct(product.id(), 2L));

        assertThat(thrown).isInstanceOf(ProductNotFoundException.class)
            .hasMessage("Cross-sell product not found with id: 2");
    }

    @Test
    void should_throw_an_exception_when_product_already_exist_in_basket() {
        Product product = productRepository.save(new Product("Dell Latitude 3301 Intel Core i7-8565U/8GB/512GB SSD/13.3", "999,00 €"));
        Product crossSellProduct = productRepository.save(new Product("Samsonite Airglow Laptop Sleeve 13.3", "41,34 €"));
        productRepository.addCrossSellProduct(product.id(), crossSellProduct.id());

        Throwable thrown = catchThrowable(() -> productRepository.addCrossSellProduct(product.id(), crossSellProduct.id()));

        assertThat(thrown).isInstanceOf(CrossSellRelationAlreadyExistsException.class)
            .hasMessage("Cross-sell relation already exists between product 1 and 2");
    }
}