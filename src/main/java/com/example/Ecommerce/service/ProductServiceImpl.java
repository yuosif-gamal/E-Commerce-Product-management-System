package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.ProductDto;
import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.filter.ProductFilter;
import com.example.Ecommerce.mapper.ProductMapper;
import com.example.Ecommerce.repository.ProductRepo;
import com.example.Ecommerce.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {


    private final ProductRepo productRepo;
    private final VoucherService voucherService;

    @Override
    @Cacheable(value = "Products", key = "#categoryID + '_' + #page + '_' + #size + '_' + #sortBy")
    public List<ProductDto> getPaginatedProductsByCategoryID(Long categoryID, int page, int size, String sortBy) {
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage = productRepo.findByCategoryId(categoryID, pageable);
        List<Product> products = productPage.getContent();
        applyVoucher(products);
        List<ProductDto> productDto = convertToDto(products);

        return productDto;
    }


    @Override
    @Cacheable(value = "Products", key = "#id")
    public ProductDto findProductById(Long id) {

        Product product = verifyExistingProduct(id);
        voucherService.applyVoucherDiscount(product);
        ProductDto productDto = ProductMapper.convertEntityToDto(product);

        return productDto;
    }

    @Cacheable(value = "Products", key = "#filter.toString() + '_' + #page + '_' + #size + '_' + #sortBy")
    @Override
    public List<ProductDto> getProducts(ProductFilter filter, int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Specification<Product> spec = new ProductSpecification(filter);

        Page<Product> productPage = productRepo.findAll(spec, pageable);
        List<Product> products = productPage.getContent();
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
        return products.stream()
                .map(ProductMapper::convertEntityToDto)
                .collect(Collectors.toList());
    }

    private Product verifyExistingProduct(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

}
