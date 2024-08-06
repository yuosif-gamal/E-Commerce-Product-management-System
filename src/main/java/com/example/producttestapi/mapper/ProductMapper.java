package com.example.producttestapi.mapper;

import com.example.producttestapi.dto.CategoryDto;
import com.example.producttestapi.dto.ProductDto;
import com.example.producttestapi.entities.Category;
import com.example.producttestapi.entities.Product;

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
