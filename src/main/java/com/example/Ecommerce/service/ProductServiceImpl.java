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

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepo productRepo;
    private final VoucherService voucherService;

    @Override
    @Cacheable(value = "Products", key = "#categoryID + '_' + #page + '_' + #size + '_' + #sortBy")
    public List<ProductDto> getPaginatedProductsByCategoryID(Long categoryID, int page, int size, String sortBy) {
        LOGGER.info("Fetching products for category ID: {} with page {}, size {}, and sort {}",
                categoryID, page, size, sortBy);

        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage = productRepo.findByCategoryId(categoryID, pageable);
        List<Product> products = productPage.getContent();
        applyVoucher(products);
        List<ProductDto> productDto = convertToDto(products);

        LOGGER.info("Successfully fetched {} products for category ID: {}", productDto.size(), categoryID);
        return productDto;
    }


    @Override
    @Cacheable(value = "Products", key = "#id")
    public ProductDto findProductById(Long id) {
        LOGGER.info("Fetching product with ID: {}", id);

        Product product = verifyExistingProduct(id);
        voucherService.applyVoucherDiscount(product);
        ProductDto productDto = ProductMapper.convertEntityToDto(product);

        LOGGER.info("Product found and converted to DTO: {}", productDto);
        return productDto;
    }

    @Cacheable(value = "Products", key = "#filter.toString() + '_' + #page + '_' + #size + '_' + #sortBy")
    @Override
    public List<ProductDto> getProducts(ProductFilter filter, int page, int size, String sortBy) {
        LOGGER.info("Fetching products with filters: {}, page: {}, size: {}, sortBy: {}", filter, page, size, sortBy);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Specification<Product> spec = new ProductSpecification(filter);

        Page<Product> productPage = productRepo.findAll(spec, pageable);
        List<Product> products = productPage.getContent();

        LOGGER.info("Applying vouchers to the retrieved products.");
        applyVoucher(products);

        LOGGER.info("Successfully converted {} products to DTOs.", products.size());
        return convertToDto(products);
    }

    @Override
    @CacheEvict(value = "Products", allEntries = true)
    public Product createProduct(Product product) {
        LOGGER.info("Creating new product: {}", product);

        Product createdProduct = productRepo.save(product);
        LOGGER.info("Product created with ID: {}", createdProduct.getId());
        return createdProduct;
    }

    @Override
    @CacheEvict(value = "Products", allEntries = true)
    @CachePut(value = "FindProduct", key = "#product.id")
    public Product updateProduct(Product product) {
        LOGGER.info("Updating product with ID: {}", product.getId());

        verifyExistingProduct(product.getId());
        Product updatedProduct = productRepo.save(product);

        LOGGER.info("Product updated successfully: {}", updatedProduct);
        return updatedProduct;
    }

    @Override
    @CacheEvict(value = "Products", allEntries = true)
    public void deleteProduct(Long id) {
        LOGGER.info("Deleting product with ID: {}", id);

        verifyExistingProduct(id);
        productRepo.deleteById(id);

        LOGGER.info("Product with ID: {} deleted successfully.", id);
    }

    private void applyVoucher(List<Product> products) {
        LOGGER.debug("Applying vouchers to {} products.", products.size());
        products.forEach(voucherService::applyVoucherDiscount);
    }

    private List<ProductDto> convertToDto(List<Product> products) {
        LOGGER.debug("Converting {} products to DTOs.", products.size());
        return products.stream()
                .map(ProductMapper::convertEntityToDto)
                .collect(Collectors.toList());
    }

    private Product verifyExistingProduct(Long id) {
        LOGGER.debug("Verifying existence of product with ID: {}", id);
        return productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

}
