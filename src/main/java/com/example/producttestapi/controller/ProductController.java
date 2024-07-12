package com.example.producttestapi.controller;

import com.example.producttestapi.dto.SuccessResponse;
import com.example.producttestapi.entities.Product;
import com.example.producttestapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public ResponseEntity<SuccessResponse> hello() {
        return ResponseEntity.ok(new SuccessResponse("Hello", true, null, HttpStatus.OK.value()));
    }

    @GetMapping("/products")
    public ResponseEntity<SuccessResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(new SuccessResponse("Products retrieved successfully", true, products, HttpStatus.OK.value()));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<SuccessResponse> getProduct(@PathVariable("id") int id) {
        Product product =  productService.findProductById(id);
        return ResponseEntity.ok(new SuccessResponse("Products retrieved successfully", true, product, HttpStatus.OK.value()));

    }

    @GetMapping("/products/category/{categoryID}")
    public ResponseEntity<SuccessResponse> getProductsByCategory(@PathVariable("categoryID") int categoryID) {
        List<Product> products = productService.getProductsByCategory(categoryID);
        return ResponseEntity.ok(new SuccessResponse("Products retrieved successfully", true, products, HttpStatus.OK.value()));
    }

    @PostMapping("/product")
    public ResponseEntity<SuccessResponse> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Product created successfully", true, createdProduct, HttpStatus.CREATED.value()));
    }

    @PutMapping("/product")
    public ResponseEntity<SuccessResponse> updateProduct(@RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(product);
        return ResponseEntity.ok(new SuccessResponse("Product updated successfully", true, updatedProduct, HttpStatus.OK.value()));
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<SuccessResponse> deleteProduct(@PathVariable("id") int id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new SuccessResponse("Product deleted successfully", true, null, HttpStatus.OK.value()));
    }
}
