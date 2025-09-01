package com.github.ismail2ov.ecommerce.infrastructure.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import org.junit.jupiter.api.Test;

import com.github.ismail2ov.ecommerce.ProductTestDataFactory;
import com.github.ismail2ov.ecommerce.application.ProductService;
import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.domain.ProductPage;
import com.github.ismail2ov.ecommerce.domain.exception.ProductNotFoundException;
import com.github.ismail2ov.ecommerce.infrastructure.mapper.ProductMapperImpl;

@WebMvcTest(ProductController.class)
@Import(ProductMapperImpl.class)
@SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void when_there_are_no_products_then_return_empty_list() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of());

        this.mockMvc
            .perform(get("/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void when_there_are_products_then_return_list_with_them() throws Exception {

        when(productService.getAllProducts()).thenReturn(ProductTestDataFactory.products(List.of(1L, 2L, 3L, 4L)));

        this.mockMvc
            .perform(get("/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(4))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("Samsonite Airglow Laptop Sleeve 13.3"))
            .andExpect(jsonPath("$[1].price").value("41,34 €"));
    }

    @Test
    void when_the_product_exists_then_return_it() throws Exception {
        Product product = ProductTestDataFactory.product(1L);
        List<Product> crossSelling = ProductTestDataFactory.products(List.of(2L, 3L));
        ProductPage productPage = new ProductPage(product, crossSelling);

        when(productService.getProductBy(1L)).thenReturn(productPage);

        this.mockMvc
            .perform(get("/products/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.product.id").value(1))
            .andExpect(jsonPath("$.product.name").value("Dell Latitude 3301 Intel Core i7-8565U/8GB/512GB SSD/13.3"))
            .andExpect(jsonPath("$.product.price").value("999,00 €"))
            .andExpect(jsonPath("$.cross_selling.size()").value(2));
    }

    @Test
    void when_product_id_is_not_a_number_then_return_bad_request() throws Exception {

        this.mockMvc
            .perform(get("/products/A"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void when_product_does_not_exist_then_return_not_found() throws Exception {
        when(productService.getProductBy(1001L)).thenThrow(ProductNotFoundException.class);

        this.mockMvc
            .perform(get("/products/1001"))
            .andExpect(status().isNotFound());
    }

    @Test
    void when_something_goes_wrong_then_return_internal_server_error() throws Exception {

        doThrow(InternalServerError.class)
            .when(productService).getAllProducts();

        this.mockMvc
            .perform(get("/products"))
            .andExpect(status().isInternalServerError());
    }
}