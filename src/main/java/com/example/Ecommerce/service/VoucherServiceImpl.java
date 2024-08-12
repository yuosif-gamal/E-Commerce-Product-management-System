package com.example.Ecommerce.service;

import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.entity.Voucher;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.repository.ProductRepo;
import com.example.Ecommerce.repository.VoucherRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepo voucherRepo;
    private final ProductRepo productRepo;

    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherServiceImpl.class);

    @Override
    public Voucher createVoucher(Voucher voucher) {
        LOGGER.info("Creating voucher with code: {}", voucher.getCode());

        if (voucherRepo.findByCode(voucher.getCode()) != null) {
            LOGGER.error("Voucher with code already exists: {}", voucher.getCode());
            throw new ResourceNotFoundException("Voucher with code already exists: " + voucher.getCode());
        }
        checkIfVoucherIsExpired(voucher.getExpireDate());
        Voucher savedVoucher = voucherRepo.save(voucher);
        LOGGER.info("Voucher created successfully: {}", savedVoucher);
        return savedVoucher;
    }

    @Override
    public Voucher findVoucherByCode(String code) {
        LOGGER.info("Finding voucher with code: {}", code);

        Voucher voucher = voucherRepo.findByCode(code);
        if (voucher == null) {
            LOGGER.error("Voucher not found with code: {}", code);
            throw new ResourceNotFoundException("Voucher not found with code: " + code);
        }
        LOGGER.info("Voucher found: {}", voucher);
        return voucher;
    }

    @Override
    public void deleteVoucher(Long id) {
        LOGGER.info("Deleting voucher with ID: {}", id);

        checkIfVoucherExistsById(id);
        List<Product> products = productRepo.findAllByVoucherID(id);
        updateProductsWithoutVoucher(products);
        voucherRepo.deleteById(id);

        LOGGER.info("Voucher deleted successfully with ID: {}", id);
    }

    @Override
    public void applyVoucherDiscount(Product product) {
        LOGGER.info("Applying voucher discount to product ID: {}", product.getId());

        if (product.getVoucherCode() != null) {
            Voucher voucher = product.getVoucherCode();
            if (isVoucherDateValid(voucher.getExpireDate())) {
                BigDecimal discountedPrice = calculateDiscountedPrice(product.getPrice(), voucher.getDiscount());
                product.setPrice(discountedPrice.doubleValue());
                LOGGER.info("Discount applied. New price: {}", product.getPrice());
            } else {
                LOGGER.warn("Voucher expired. Removing voucher from product ID: {}", product.getId());
                product.setPrice(product.getPrice());
                deleteVoucher(voucher.getId());
            }
        }
    }

    private void checkIfVoucherExistsById(Long id) {
        LOGGER.debug("Checking if voucher exists with ID: {}", id);
        if (!voucherRepo.findById(id).isPresent()) {
            LOGGER.error("Voucher not found with ID: {}", id);
            throw new ResourceNotFoundException("Voucher not found with id: " + id);
        }
    }

    private void checkIfVoucherIsExpired(Date expireDate) {
        LOGGER.debug("Checking if voucher is expired. Expire date: {}", expireDate);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireDateTime = convertToLocalDateTime(expireDate);

        if (expireDateTime.isBefore(now)) {
            LOGGER.error("Voucher has expired. Expire date: {}", expireDate);
            throw new ResourceNotFoundException("Voucher has expired.");
        }
    }

    private LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private void updateProductsWithoutVoucher(List<Product> products) {
        LOGGER.debug("Updating products without voucher. Number of products: {}", products.size());
        for (Product product : products) {
            product.setVoucherCode(null);
            productRepo.save(product);
        }
    }

    private boolean isVoucherDateValid(Date voucher) {
        LOGGER.debug("Checking if voucher date is valid. Voucher date: {}", voucher);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireDateTime = voucher.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return expireDateTime.isAfter(now);
    }

    private BigDecimal calculateDiscountedPrice(Double price, BigDecimal discount) {
        LOGGER.debug("Calculating discounted price. Original price: {}, Discount: {}", price, discount);
        BigDecimal productPrice = BigDecimal.valueOf(price);
        return productPrice.subtract(productPrice.multiply(discount.divide(BigDecimal.valueOf(100))));
    }
}
