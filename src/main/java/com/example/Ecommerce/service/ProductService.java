package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.ProductDto;
import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.filter.ProductFilter;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface ProductService {

    ProductDto findProductById(Long id);

    Product createProduct(Product product);

    Product updateProduct(Product product);

    void deleteProduct(Long id);

    List<ProductDto> getPaginatedProductsByCategoryID(Long id, int page, int size, String sortBy);

    List<ProductDto> getProducts(ProductFilter filter, int page, int size, String sortBy);

}
