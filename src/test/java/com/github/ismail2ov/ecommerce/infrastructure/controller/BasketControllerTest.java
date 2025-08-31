package com.github.ismail2ov.ecommerce.infrastructure.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.ismail2ov.ecommerce.application.BasketService;
import com.github.ismail2ov.ecommerce.domain.Basket;
import com.github.ismail2ov.ecommerce.domain.Items;
import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.domain.exception.BasketNotFoundException;
import com.github.ismail2ov.ecommerce.domain.exception.CrossSellRelationAlreadyExistsException;
import com.github.ismail2ov.ecommerce.infrastructure.configuration.GlobalExceptionHandler;
import com.github.ismail2ov.ecommerce.infrastructure.mapper.BasketMapperImpl;
import com.github.ismail2ov.ecommerce.infrastructure.mapper.ProductMapperImpl;

@WebMvcTest(BasketController.class)
@Import({ProductMapperImpl.class, BasketMapperImpl.class, GlobalExceptionHandler.class})
class BasketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BasketService basketService;

    @Test
    void when_the_basket_exists_then_return_it() throws Exception {

        Basket basket = new Basket(1L, 1L, Items.create());

        when(basketService.getForUser(1L)).thenReturn(basket);

        this.mockMvc
            .perform(get("/users/1/basket"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.userId").value(1))
            .andExpect(jsonPath("$.items.products.size()").value(0));
    }

    @Test
    void when_basket_does_not_exist_then_return_not_found() throws Exception {
        when(basketService.getForUser(1001L)).thenThrow(BasketNotFoundException.class);

        this.mockMvc
            .perform(get("/products/1001"))
            .andExpect(status().isNotFound());
    }

    @Test
    void when_add_product_to_basket_then_return_basket_with_the_product() throws Exception {
        Product product = new Product(3L, "Logitech Wireless Mouse M185", "10,78 €");
        Basket basket = new Basket(1L, 1L, Items.with(product));

        when(basketService.addProductToBasket(1L, product)).thenReturn(basket);

        this.mockMvc
            .perform(post("/users/1/basket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(product)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.userId").value(1))
            .andExpect(jsonPath("$.items.size()").value(1));
    }

    @Test
    void when_adding_product_that_already_exists_to_basket_the_return_conflict() throws Exception {
        Product product = new Product(3L, "Logitech Wireless Mouse M185", "10,78 €");

        doThrow(CrossSellRelationAlreadyExistsException.class)
            .when(basketService).addProductToBasket(1L, product);

        this.mockMvc
            .perform(post("/users/1/basket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(product)))
            .andExpect(status().isConflict());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
