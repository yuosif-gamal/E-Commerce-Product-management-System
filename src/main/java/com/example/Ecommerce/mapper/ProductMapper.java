package com.example.Ecommerce.mapper;

import com.example.Ecommerce.dto.ProductDto;
import com.example.Ecommerce.entity.Product;

public class ProductMapper {
    public static ProductDto ProductEntityToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setQuantity(product.getQuantity());
        return productDto;
    }
}
