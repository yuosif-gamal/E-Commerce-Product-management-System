package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.ProductDto;
import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.mapper.ProductMapper;
import com.example.Ecommerce.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final VoucherService voucherService;

    @Override
    @Cacheable(value = "Products", key = "'all'")
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepo.findAll();
        applyVoucher(products);
        List<ProductDto> productDto = convertToDto(products);
        return productDto;

    }

    @Override
    @Cacheable(value = "Products", key = "#id")
    public ProductDto findProductById(Long id) {
        Product product = verifyExistingProduct(id);
        voucherService.applyVoucherDiscount(product);
        ProductDto productDto = ProductMapper.ProductEntityToDto(product);
        return productDto;
    }

    @Override
    @Cacheable(value = "Products", key = "#categoryID")
    public List<ProductDto> getProductsByCategoryID(Long categoryID) {
        List<Product> products = productRepo.findAllByCategoryID(categoryID);
        applyVoucher(products);
        return convertToDto(products);
    }

    @Override

    @CacheEvict(value = "Products", allEntries = true)
    public Product createProduct(Product product) {
        return productRepo.save(product);
    }

    @Override
    @CacheEvict(value = "Products", allEntries = true)
    @CachePut(value = "FindProduct", key = "#product.id")
    public Product updateProduct(Product product) {
        verifyExistingProduct(product.getId());
        return productRepo.save(product);
    }

    @Override
    @CacheEvict(value = "Products", allEntries = true)
    public void deleteProduct(Long id) {
        verifyExistingProduct(id);
        productRepo.deleteById(id);
    }

    private void applyVoucher(List<Product> products) {
        products.forEach(voucherService::applyVoucherDiscount);
    }

    private List<ProductDto> convertToDto(List<Product> products) {
        List<ProductDto> productDtos = products.stream()
                .map(ProductMapper::ProductEntityToDto)
                .collect(Collectors.toList());
        return productDtos;
    }

    private Product verifyExistingProduct(Long ID) {
        return productRepo.findById(ID)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + ID));
    }

}
