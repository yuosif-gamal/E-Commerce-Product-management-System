package com.example.producttestapi.service;

import com.example.producttestapi.dto.ProductDto;
import com.example.producttestapi.entity.Product;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
    ProductDto findProductById(Long id);
    List<ProductDto> getProductsByCategoryID(Long categoryID);
    Product createProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(Long id);
}
