package com.example.producttestapi.service;

import com.example.producttestapi.dto.ProductDto;
import com.example.producttestapi.entity.Product;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
    ProductDto findProductById(int id);
    List<ProductDto> getProductsByCategoryID(int categoryID);
    Product createProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(int id);
}
