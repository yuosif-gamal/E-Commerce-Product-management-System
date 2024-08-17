package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.ProductDto;
import com.example.Ecommerce.entity.Product;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    List<ProductDto> getAllProductsPagination(int page, int size);

    ProductDto findProductById(Long id);

    List<ProductDto> getProductsByCategoryID(Long categoryID);

    Product createProduct(Product product);

    Product updateProduct(Product product);

    void deleteProduct(Long id);

}
