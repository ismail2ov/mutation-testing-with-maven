package com.github.ismail2ov.ecommerce.infrastructure.mapper;

import org.mapstruct.Mapper;

import com.github.ismail2ov.ecommerce.domain.Basket;
import com.github.ismail2ov.ecommerce.domain.Items;
import com.github.ismail2ov.ecommerce.infrastructure.controller.model.BasketRDTO;
import com.github.ismail2ov.ecommerce.infrastructure.persistence.BasketEntity;
import com.github.ismail2ov.ecommerce.infrastructure.persistence.BasketItems;

@Mapper
public interface BasketMapper {

    BasketRDTO from(Basket basket);

    Basket fromEntity(BasketEntity basketEntity);

    BasketEntity entityFrom(Basket basket);

    BasketItems itemsFrom(Items items);
}