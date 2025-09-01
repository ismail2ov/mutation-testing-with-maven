package com.github.ismail2ov.ecommerce.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.github.ismail2ov.ecommerce.ProductTestDataFactory;
import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.domain.ProductPage;
import com.github.ismail2ov.ecommerce.domain.ProductRepository;
import com.github.ismail2ov.ecommerce.domain.exception.ProductNotFoundException;

@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    public static final long PRODUCT_ID = 1;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @Test
    void when_products_found_then_return_them() {
        List<Product> expectedProductsList = ProductTestDataFactory.products(List.of(1L, 2L));
        when(productService.getAllProducts()).thenReturn(expectedProductsList);

        List<Product> actualProductsList = productService.getAllProducts();

        assertThat(actualProductsList).isEqualTo(expectedProductsList);
    }

    @Test
    void when_product_found_then_return_it() {
        Product product = ProductTestDataFactory.product(1L);
        List<Product> crossSellProducts = ProductTestDataFactory.products(List.of(2L, 3L));
        ProductPage expected = new ProductPage(product, crossSellProducts);

        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productRepository.getCrossSellProducts(PRODUCT_ID)).thenReturn(crossSellProducts);

        ProductPage actual = productService.getProductBy(PRODUCT_ID);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void when_the_product_is_not_found_then_throw_an_exception() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> productService.getProductBy(PRODUCT_ID));

        assertThat(thrown).isInstanceOf(ProductNotFoundException.class);
    }
}