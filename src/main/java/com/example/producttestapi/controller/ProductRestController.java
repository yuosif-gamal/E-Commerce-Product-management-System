package com.example.producttestapi.controller;

import com.example.producttestapi.entities.Product;
import com.example.producttestapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductRestController {
    private final ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String hello() {
        return "hello";
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/product/{id}")
    public Optional<Product> getProduct(@PathVariable("id") int id) {
        return productService.getProductById(id);
    }

    @GetMapping("/products/category/{categoryID}")
    public List<Product> getProductsByCategory(@PathVariable("categoryID") int categoryID) {
        return productService.getProductsByCategory(categoryID);
    }

    @PostMapping("/product")
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping("/product")
    public Product updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    @DeleteMapping("/product/{id}")
    public void deleteProduct(@PathVariable("id") int id) {
        productService.deleteProduct(id);
    }
}
