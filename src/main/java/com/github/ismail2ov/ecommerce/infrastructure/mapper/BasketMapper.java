package com.github.ismail2ov.ecommerce.infrastructure.mapper;

import org.mapstruct.Mapper;

import com.github.ismail2ov.ecommerce.domain.Basket;
import com.github.ismail2ov.ecommerce.infrastructure.controller.model.BasketRDTO;

@Mapper
public interface BasketMapper {

    BasketRDTO from(Basket basket);
}