package com.example.producttestapi.controller;

import com.example.producttestapi.dto.ProductDto;
import com.example.producttestapi.dto.SuccessResponse;
import com.example.producttestapi.entity.Product;
import com.example.producttestapi.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Validated
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/hello")
    @Operation(summary = "Say Hello", description = "Returns a hello message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hello message returned successfully")
    })
    public ResponseEntity<SuccessResponse> hello() {
        return ResponseEntity.ok(new SuccessResponse("Hello", true, null, HttpStatus.OK.value()));
    }

    @GetMapping
    @Operation(summary = "Get All Products", description = "Returns a list of all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(new SuccessResponse("Products retrieved successfully", true, products, HttpStatus.OK.value()));
    }

    @GetMapping("/category/{id}")
    @Operation(summary = "Get Products by Category ID", description = "Returns a list of products by category ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> getProductsByCategoryID(@PathVariable("id") int id) {
        List<ProductDto> products = productService.getProductsByCategoryID(id);
        return ResponseEntity.ok(new SuccessResponse("Products retrieved successfully", true, products, HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Product by ID", description = "Returns a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> getProduct(@PathVariable("id") int id) {
        ProductDto product = productService.findProductById(id);
        return ResponseEntity.ok(new SuccessResponse("Product retrieved successfully", true, product, HttpStatus.OK.value()));
    }

    @PostMapping
    @Operation(summary = "Create Product", description = "Creates a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> createProduct(@Valid @RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Product created successfully", true, createdProduct, HttpStatus.CREATED.value()));
    }

    @PutMapping
    @Operation(summary = "Update Product", description = "Updates an existing product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> updateProduct(@Valid @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(product);
        return ResponseEntity.ok(new SuccessResponse("Product updated successfully", true, updatedProduct, HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Product", description = "Deletes a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> deleteProduct(@PathVariable("id") int id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new SuccessResponse("Product deleted successfully", true, null, HttpStatus.OK.value()));
    }
}
