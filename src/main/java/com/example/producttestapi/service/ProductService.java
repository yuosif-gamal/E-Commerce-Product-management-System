package com.example.producttestapi.service;

import com.example.producttestapi.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Product findProductById(int id);
    List<Product> getProductsByCategory(int categoryID);
    Product createProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(int id);

}
