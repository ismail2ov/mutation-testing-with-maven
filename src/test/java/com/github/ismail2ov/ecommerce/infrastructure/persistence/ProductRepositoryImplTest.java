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
import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.domain.exception.ProductPersistenceException;
import com.github.ismail2ov.ecommerce.infrastructure.mapper.ProductMapper;
import com.github.ismail2ov.ecommerce.infrastructure.mapper.ProductMapperImpl;

@ExtendWith(SpringExtension.class)
@Import(ProductMapperImpl.class)
class ProductRepositoryImplTest {

    public static final Product PRODUCT = ProductTestDataFactory.product(3L);

    @Mock
    EntityManager entityManager;

    @Autowired
    ProductMapper productMapper;

    ProductRepositoryImpl productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepositoryImpl(entityManager, productMapper);
    }

    @Test
    void should_throw_an_exception_if_product_is_null() {

        Throwable thrown = catchThrowable(() -> productRepository.save(null));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Product must not be null");
    }

    @Test
    void should_save_a_product() {

        Product product = productRepository.save(new Product("Logitech Wireless Mouse M185", "10,78 €"));

        assertThat(product)
            .isNotNull();
    }

    @Test
    void should_throw_an_exception_when_try_to_update_unexisting_product() {

        doThrow(ProductPersistenceException.class)
            .when(entityManager).merge(any(ProductEntity.class));

        Throwable thrown = catchThrowable(() -> productRepository.save(PRODUCT));

        assertThat(thrown).isInstanceOf(ProductPersistenceException.class)
            .hasMessage("Failed to save product");
    }

}
