package com.example.producttestapi.service;

import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final VoucherService voucherService;

    @Autowired
    public ProductServiceImpl(ProductRepo productRepo, VoucherService voucherService) {
        this.productRepo = productRepo;
        this.voucherService = voucherService;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = productRepo.findAll();
        return products;
    }

    @Override
    public Product findProductById(int id) {
        Product product = productRepo.findById(id).orElse(null);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        return product;
    }

    @Override
    public List<Product> getProductsByCategory(int categoryID) {
        List<Product> products = productRepo.findByCategoryID(categoryID);
        return products;
    }

    @Override
    public Product createProduct(Product product) {
        voucherService.applyVoucherDiscount(product);
        return productRepo.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        if (!productRepo.existsById(product.getId())) {
            throw new ResourceNotFoundException("Product not found with id: " + product.getId());
        }
        return productRepo.save(product);
    }

    @Override
    public void deleteProduct(int id) {
        if (!productRepo.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepo.deleteById(id);
    }

}
