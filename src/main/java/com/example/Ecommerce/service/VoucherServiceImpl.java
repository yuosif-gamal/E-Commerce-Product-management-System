package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.VoucherDto;
import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.entity.Voucher;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.mapper.VoucherMapper;
import com.example.Ecommerce.repository.ProductRepo;
import com.example.Ecommerce.repository.VoucherRepo;
import jakarta.transaction.Transactional;
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

import static com.example.Ecommerce.util.DateUtil.convertToLocalDateTime;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepo voucherRepo;
    private final ProductRepo productRepo;

    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherServiceImpl.class);

    @Override
    public VoucherDto createVoucher(VoucherDto voucherDto) {
        LOGGER.info("Creating voucher with code: {}", voucherDto.getCode());

        if (voucherRepo.findByCode(voucherDto.getCode()) != null) {
            LOGGER.error("Voucher with code already exists: {}", voucherDto.getCode());
            throw new ResourceNotFoundException("Voucher with code already exists: " + voucherDto.getCode());
        }
        checkIfVoucherIsExpired(voucherDto.getExpiryDate());

        Voucher voucher = VoucherMapper.convertDtoToEntity(voucherDto);
        Voucher savedVoucher = voucherRepo.save(voucher);
        LOGGER.info("Voucher created successfully: {}", savedVoucher);
        return VoucherMapper.convertEntityToDto(savedVoucher);
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
        updateProductsWithoutVoucher(id);
        voucherRepo.deleteById(id);
        LOGGER.info("Voucher deleted successfully with ID: {}", id);
    }

    @Override
    public void applyVoucherDiscount(Product product) {
        LOGGER.info("Applying voucher discount to product ID: {}", product.getId());

        if (product.getVoucherCode() != null) {
            Voucher voucher = product.getVoucherCode();
            if (isVoucherDateValid(voucher.getExpiryDate())) {
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

    @Override
    @Transactional
    public void addProductsToVoucher(Long voucherId, List<Long> productIds) {
        LOGGER.info("Adding products to voucher with ID: {}", voucherId);
        checkIfVoucherExistsById(voucherId);
        Voucher voucher = voucherRepo.findById(voucherId).orElse(null);

        LOGGER.info("Fetching products with IDs: {}", productIds);
        List<Product> products = productRepo.findAllById(productIds);

        if (products.size() != productIds.size()) {
            LOGGER.error(
                    "Mismatch between provided product IDs and found products. Provided IDs: {}" +
                    ", Found Products: {}",
                    productIds.size(), products.size());
            throw new ResourceNotFoundException("One or more product IDs do not match any existing products.");
        }

        for (Product product : products) {
            LOGGER.debug("Associating product with ID: {} to voucher with ID: {}", product.getId(), voucherId);
            product.setVoucherCode(voucher);
        }

        productRepo.saveAll(products);
        LOGGER.info("Successfully added {} products to voucher with ID: {}", products.size(), voucherId);
    }


    private void checkIfVoucherExistsById(Long id) {
        LOGGER.debug("Checking if voucher exists with ID: {}", id);
        if (voucherRepo.findById(id).isEmpty()) {
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

    private void updateProductsWithoutVoucher(Long voucherId) {
        LOGGER.debug("Updating products without voucher. Deleting all products with voucher_id {}", voucherId);
        productRepo.updateProductsWithoutVoucher(voucherId);
    }

    private boolean isVoucherDateValid(Date voucher) {
        LOGGER.debug("Checking if voucher date is valid. Voucher date: {}", voucher);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireDateTime = convertToLocalDateTime(voucher);
        return expireDateTime.isAfter(now);
    }

    private BigDecimal calculateDiscountedPrice(Double price, BigDecimal discount) {
        LOGGER.debug("Calculating discounted price. Original price: {}, Discount: {}", price, discount);
        BigDecimal productPrice = BigDecimal.valueOf(price);
        return productPrice.subtract(productPrice.multiply(discount.divide(BigDecimal.valueOf(100))));
    }
}
