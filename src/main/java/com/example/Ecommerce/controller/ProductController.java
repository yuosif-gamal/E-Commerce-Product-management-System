package com.example.Ecommerce.controller;

import com.example.Ecommerce.dto.ProductDto;
import com.example.Ecommerce.dto.SuccessResponse;
import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Validated
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final static Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/hello")
    @Operation(summary = "Say Hello", description = "Returns a hello message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hello message returned successfully")
    })
    public ResponseEntity<SuccessResponse> hello() {
        LOGGER.info("Received request for hello message");
        SuccessResponse response = new SuccessResponse("Hello", null, HttpStatus.OK.value());
        LOGGER.info("Returning hello message");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get All Products", description = "Returns a list of all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> getAllProducts() {
        LOGGER.info("Received request to retrieve all products");
        List<ProductDto> products = productService.getAllProducts();
        LOGGER.info("Returning list of {} products", products.size());
        return ResponseEntity.ok(new SuccessResponse("Products retrieved successfully", products, HttpStatus.OK.value()));
    }

    @GetMapping("/category/{id}")
    @Operation(summary = "Get Products by Category ID", description = "Returns a list of products by category ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> getProductsByCategoryID(@PathVariable("id") Long id) {
        LOGGER.info("Received request to retrieve products for category ID {}", id);
        List<ProductDto> products = productService.getProductsByCategoryID(id);
        LOGGER.info("Returning list of {} products for category ID {}", products.size(), id);
        return ResponseEntity.ok(new SuccessResponse("Products retrieved successfully", products, HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Product by ID", description = "Returns a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> getProduct(@PathVariable("id") Long id) {
        LOGGER.info("Received request to retrieve product with ID {}", id);
        ProductDto product = productService.findProductById(id);
        LOGGER.info("Returning product with ID {}", id);
        return ResponseEntity.ok(new SuccessResponse("Product retrieved successfully", product, HttpStatus.OK.value()));
    }

    @PostMapping
    @Operation(summary = "Create Product", description = "Creates a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> createProduct(@Valid @RequestBody Product product) {
        LOGGER.info("Received request to create a new product with details: {}", product);
        Product createdProduct = productService.createProduct(product);
        LOGGER.info("Product created successfully with details: {}", createdProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Product created successfully", createdProduct, HttpStatus.CREATED.value()));
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
        LOGGER.info("Received request to update product with details: {}", product);
        Product updatedProduct = productService.updateProduct(product);
        LOGGER.info("Product updated successfully with details: {}", updatedProduct);
        return ResponseEntity.ok(new SuccessResponse("Product updated successfully", updatedProduct, HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Product", description = "Deletes a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> deleteProduct(@PathVariable("id") Long id) {
        LOGGER.info("Received request to delete product with ID {}", id);
        productService.deleteProduct(id);
        LOGGER.info("Product with ID {} deleted successfully", id);
        return ResponseEntity.ok(new SuccessResponse("Product deleted successfully", null, HttpStatus.OK.value()));
    }
}
