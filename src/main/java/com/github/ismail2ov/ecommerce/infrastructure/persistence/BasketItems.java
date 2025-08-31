package com.github.ismail2ov.ecommerce.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;

import com.github.ismail2ov.ecommerce.domain.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketItems {

    List<Product> products = new ArrayList<>();
}
