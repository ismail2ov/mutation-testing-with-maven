package com.github.ismail2ov.ecommerce;

import java.util.List;

import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.infrastructure.controller.model.ProductRDTO;

public class ProductTestDataFactory {

    private static final List<Product> PRODUCTS = List.of(
        new Product(1L, "Dell Latitude 3301 Intel Core i7-8565U/8GB/512GB SSD/13.3", "999,00 €"),
        new Product(2L, "Samsonite Airglow Laptop Sleeve 13.3", "41,34 €"),
        new Product(3L, "Logitech Wireless Mouse M185", "10,78 €"),
        new Product(4L, "Fellowes Mouse Pad Black", "1,34 €")
    );

    private static final List<ProductRDTO> PRODUCT_RDTOS = List.of(
        buildProductRDTO(1L, "Dell Latitude 3301 Intel Core i7-8565U/8GB/512GB SSD/13.3", "999,00 €"),
        buildProductRDTO(2L, "Samsonite Airglow Laptop Sleeve 13.3", "41,34 €"),
        buildProductRDTO(3L, "Logitech Wireless Mouse M185", "10,78 €"),
        buildProductRDTO(4L, "Fellowes Mouse Pad Black", "1,34 €")
    );

    public static List<Product> products(List<Long> productIds) {
        return PRODUCTS.stream().filter(product -> productIds.contains(product.id())).toList();
    }

    public static Product product(Long productId) {
        return PRODUCTS.stream().filter(product -> productId.equals(product.id())).findFirst().orElse(null);
    }

    public static List<ProductRDTO> productRdtos(List<Long> productIds) {
        return PRODUCT_RDTOS.stream().filter(product -> productIds.contains(product.getId())).toList();
    }

    public static ProductRDTO productRdto(Long productId) {
        return PRODUCT_RDTOS.stream().filter(product -> productId.equals(product.getId())).findFirst().orElse(null);
    }

    private static ProductRDTO buildProductRDTO(long id, String name, String price) {
        return new ProductRDTO()
            .id(id)
            .name(name)
            .price(price);
    }
}
