package com.example.producttestapi.service;

import com.example.producttestapi.entities.Product;

import java.util.Collection;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(int id);
    List<Product> getProductsByCategory(int categoryID);
    Product createProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(int id);

}
