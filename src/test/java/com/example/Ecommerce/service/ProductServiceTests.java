package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.ProductDto;
import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.repository.ProductRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    private ProductRepo productRepo;

    @Mock
    private VoucherService voucherService;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testGetAllProducts() {
        // Arrange
        List<Product> products = new ArrayList<>();
        Product product = Product.builder()
                .id(1L)
                .name("Product1")
                .description("Desc1")
                .price(100.0)
                .quantity(10)
                .build();
        Product product2 = Product.builder()
                .id(2L)
                .name("Product2")
                .description("Desc2")
                .price(1030.0)
                .quantity(130).build();

        products.add(product2);
        products.add(product);

        when(productRepo.findAll()).thenReturn(products);

        // Act
        List<ProductDto> result = productService.getAllProductsPagination(0,2);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product2", result.get(0).getName());
        verify(productRepo, times(1)).findAll();
        verify(voucherService, times(result.size())).applyVoucherDiscount(any(Product.class));
    }

    @Test
    void testFindProductById_Success() {
        // Arrange
        Long productId = 1L;
        Product product = Product.builder()
                .id(productId)
                .name("Product1")
                .description("Description1")
                .price(100.0)
                .quantity(10)
                .build();

        when(productRepo.findById(productId)).thenReturn(Optional.of(product));

        // Act
        ProductDto result = productService.findProductById(productId);

        // Assert
        assertNotNull(result);
        assertEquals("Product1", result.getName());
        verify(productRepo, times(1)).findById(productId);
        verify(voucherService, times(1)).applyVoucherDiscount(product);
    }

    @Test
    void testFindProductById_Failure() {
        // Arrange
        Long productId = 1L;
        when(productRepo.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException thrownException = assertThrows(ResourceNotFoundException.class, () -> {
            productService.findProductById(productId);
        });

        assertEquals("Product not found with id: " + productId, thrownException.getMessage());
        verify(productRepo, times(1)).findById(productId);
        verify(voucherService, times(0)).applyVoucherDiscount(any(Product.class));
    }

    @Test
    void testGetProductsByCategoryID_Success() {
        // Arrange
        Long categoryId = 1L;
        List<Product> products = new ArrayList<>();
        Product product = Product.builder()
                .id(1L)
                .name("Product1")
                .description("Description1")
                .price(100.0)
                .quantity(10)
                .build();
        products.add(product);

        when(productRepo.findAllByCategoryID(categoryId)).thenReturn(products);

        // Act
        List<ProductDto> result = productService.getProductsByCategoryID(categoryId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Product1", result.get(0).getName());
        verify(productRepo, times(1)).findAllByCategoryID(categoryId);
        verify(voucherService, times(1)).applyVoucherDiscount(any(Product.class));
    }

    @Test
    void testCreateProduct() {
        // Arrange
        Product product = Product.builder()
                .name("Product1")
                .description("Description1")
                .price(100.0)
                .quantity(10)
                .build();

        when(productRepo.save(product)).thenReturn(product);

        // Act
        Product result = productService.createProduct(product);

        // Assert
        assertNotNull(result);
        assertEquals("Product1", result.getName());
        verify(productRepo, times(1)).save(product);
    }

    @Test
    void testUpdateProduct_Success() {
        // Arrange
        Product product = Product.builder()
                .id(1L)
                .name("UpdatedProduct")
                .description("UpdatedDescription")
                .price(150.0)
                .quantity(5)
                .build();

        when(productRepo.findById(product.getId())).thenReturn(Optional.of(product));
        when(productRepo.save(product)).thenReturn(product);

        // Act
        Product result = productService.updateProduct(product);

        // Assert
        assertNotNull(result);
        assertEquals("UpdatedProduct", result.getName());
        verify(productRepo, times(1)).findById(product.getId());
        verify(productRepo, times(1)).save(product);
    }

    @Test
    void testUpdateProduct_Failure() {
        // Arrange
        Product product = Product.builder()
                .id(1L)
                .name("UpdatedProduct")
                .description("UpdatedDescription")
                .price(150.0)
                .quantity(5)
                .build();

        when(productRepo.findById(product.getId())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException thrownException = assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(product);
        });

        assertEquals("Product not found with id: " + product.getId(), thrownException.getMessage());
        verify(productRepo, times(1)).findById(product.getId());
        verify(productRepo, times(0)).save(product);
    }

    @Test
    void testDeleteProduct_Success() {
        // Arrange
        Long productId = 1L;

        when(productRepo.findById(productId)).thenReturn(Optional.of(Product.builder().id(productId).build()));
        doNothing().when(productRepo).deleteById(productId);

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepo, times(1)).findById(productId);
        verify(productRepo, times(1)).deleteById(productId);
    }

    @Test
    void testDeleteProduct_Failure() {
        // Arrange
        Long productId = 1L;

        when(productRepo.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException thrownException = assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(productId);
        });

        assertEquals("Product not found with id: " + productId, thrownException.getMessage());
        verify(productRepo, times(1)).findById(productId);
        verify(productRepo, times(0)).deleteById(productId);
    }
}
