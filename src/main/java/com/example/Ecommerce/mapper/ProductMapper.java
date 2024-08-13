package com.example.Ecommerce.mapper;

import com.example.Ecommerce.dto.ProductDto;
import com.example.Ecommerce.entity.Product;

public class ProductMapper {
    public static ProductDto convertEntityToDto(Product product) {
        return ProductDto.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }
}
