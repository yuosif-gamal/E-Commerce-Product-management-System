package com.example.producttestapi.service;

import com.example.producttestapi.entities.Category;
import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.CategoryRepo;
import com.example.producttestapi.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final VoucherService voucherService;

    @Autowired
    public ProductServiceImpl(ProductRepo productRepo, VoucherService voucherService ) {
        this.productRepo = productRepo;
        this.voucherService = voucherService;
    }
    @Override
    @Cacheable(value = "products")
    public List<Product> getAllProducts() {
        List<Product> products = productRepo.findAll();

        for (Product product : products) {
            voucherService.applyVoucherDiscount(product);
        }

        return products;
    }

    @Override
    @Cacheable(value = "FindProduct" , key = "#id")
    public Product findProductById(int id) {
        Product product = productRepo.findById(id).orElse(null);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        voucherService.applyVoucherDiscount(product);
        return product;
    }

    @Override
    @Cacheable(value = "productsForCategory", key = "#product.category.id")
    public List<Product> getProductsByCategoryID(int categoryID) {
        List<Product> products = productRepo.findAllByCategoryID(categoryID);
        for (Product product : products) {
            voucherService.applyVoucherDiscount(product);
        }
        return products;
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "products", allEntries = true),
                    @CacheEvict(value = "findProduct", allEntries = true),
                    @CacheEvict(value = "productsForCategory", key = "#product.category.id")
            }
    )
    public Product createProduct(Product product) {
        return productRepo.save(product);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "products", allEntries = true),
                    @CacheEvict(value = "productsForCategory", allEntries = true)
            },
            put = {
                    @CachePut(value = "FindProduct", key = "#product.id"),
            }
    )    public Product updateProduct(Product product) {
        if (!productRepo.existsById(product.getId())) {
            throw new ResourceNotFoundException("Product not found with id: " + product.getId());
        }
        return productRepo.save(product);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "products", allEntries = true),
                    @CacheEvict(value = "findProduct", key = "#id"),
                    @CacheEvict(value = "productsForCategory", allEntries = true)
            }
    )
    public void deleteProduct(int id) {
        if (!productRepo.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepo.deleteById(id);
    }

}
