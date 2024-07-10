package com.example.producttestapi.service;

import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.excetion.ResourceNotFoundException;
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
        products.forEach(this::applyVoucherDiscount);
        return products;
    }

    @Override
    public Optional<Product> getProductById(int id) {
        Optional<Product> optionalProduct = productRepo.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        return optionalProduct;
    }

    @Override
    public List<Product> getProductsByCategory(int categoryID) {
        List<Product> products = productRepo.findByCategoryID(categoryID);
        products.forEach(this::applyVoucherDiscount);
        return products;
    }

    @Override
    public Product createProduct(Product product) {
        applyVoucherDiscount(product);
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

    private void applyVoucherDiscount(Product product) {
        if (product.getVoucher() != null) {
            Optional<Voucher> voucher = voucherService.findVoucherByCode(product.getVoucher());
            if (voucher != null) {
                BigDecimal discount = voucher.get().getDiscount();
                BigDecimal productPrice = BigDecimal.valueOf(product.getPrice());
                BigDecimal discountedPrice = productPrice.subtract(productPrice.multiply(discount.divide(BigDecimal.valueOf(100))));

                product.setPrice(discountedPrice.doubleValue());
            }
        }
    }
}
