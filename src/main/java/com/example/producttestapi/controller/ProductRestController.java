package com.example.producttestapi.controller;

import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.repos.ProductRepo;
import com.example.producttestapi.repos.VoucherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductRestController {

    @Autowired
    private ProductRepo repo;

    @Autowired
    private VoucherRepo Vrepo;

    @GetMapping("/")
    public String hello() {
        return "hello";
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        List<Product> products = repo.findAll();
        products.forEach(this::applyVoucherDiscount);
        return products;
    }

    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable("id") int id) {
        Optional<Product> optionalProduct = repo.findById(id);
        optionalProduct.ifPresent(this::applyVoucherDiscount);
        return optionalProduct.orElse(null);
    }

    @GetMapping("/products/category/{categoryID}")
    public List<Product> getProductsInCategory(@PathVariable("categoryID") int categoryID) {
        List<Product> products = repo.findByCategoryID(categoryID);
        products.forEach(this::applyVoucherDiscount);
        return products;
    }

    @PostMapping("/product")
    public Product createProduct(@RequestBody Product product) {
        applyVoucherDiscount(product);
        return repo.save(product);
    }

    @PutMapping("/product")
    public Product updateProduct(@RequestBody Product product) {
        applyVoucherDiscount(product);
        return repo.save(product);
    }

    @DeleteMapping("/product/{id}")
    public void deleteProduct(@PathVariable("id") int id) {
        repo.deleteById(id);
    }

    private void applyVoucherDiscount(Product product) {
        if (product.getVoucher() != null) {
            Voucher voucher = Vrepo.findByCode(product.getVoucher());
            if (voucher != null) {
                BigDecimal discount = voucher.getDiscount();
                BigDecimal productPrice = BigDecimal.valueOf(product.getPrice());
                BigDecimal discountedPrice = productPrice.subtract(productPrice.multiply(discount.divide(BigDecimal.valueOf(100))));

                product.setPrice(discountedPrice.doubleValue());
            }
        }
    }
}
