package com.example.Ecommerce.service;

import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.entity.Voucher;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.repository.ProductRepo;
import com.example.Ecommerce.repository.VoucherRepo;
import lombok.RequiredArgsConstructor;
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

    @Override
    public Voucher createVoucher(Voucher voucher) {
        if (voucherRepo.findByCode(voucher.getCode()) != null) {
            throw new ResourceNotFoundException("Voucher with code already exists: " + voucher.getCode());
        }
        checkIfVoucherIsExpired(voucher.getExpireDate());
        return voucherRepo.save(voucher);
    }


    @Override
    public Voucher findVoucherByCode(String code) {
        Voucher voucher = voucherRepo.findByCode(code);
        if (voucher == null) {
            throw new ResourceNotFoundException("Voucher not found with code: " + code);
        }
        return voucher;
    }

    @Override
    public void deleteVoucher(Long id) {
        checkIfVoucherExistsById(id);
        List<Product> products = productRepo.findAllByVoucherID(id);
        updateProductsWithoutVoucher(products);
        voucherRepo.deleteById(id);
    }


    @Override
    public void applyVoucherDiscount(Product product) {
        if (product.getVoucherCode() != null) {
            Voucher voucher = product.getVoucherCode();
            if (isVoucherDateValid(voucher.getExpireDate())) {
                BigDecimal discountedPrice = calculateDiscountedPrice(product.getPrice(), voucher.getDiscount());
                product.setPrice(discountedPrice.doubleValue());
            } else {
                product.setPrice(product.getPrice());
                deleteVoucher(voucher.getId());

            }
        }
    }


    private void checkIfVoucherExistsById(Long id) {
        if (!voucherRepo.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Voucher not found with id: " + id);
        }
    }


    private void checkIfVoucherIsExpired(Date expireDate) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireDateTime = convertToLocalDateTime(expireDate);

        if (expireDateTime.isBefore(now)) {
            throw new ResourceNotFoundException("Voucher has expired.");
        }
    }

    private LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private void updateProductsWithoutVoucher(List<Product> products) {
        for (Product product : products) {
            product.setVoucherCode(null);
            productRepo.save(product);
        }
    }


    private boolean isVoucherDateValid(Date voucher) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireDateTime = voucher.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return expireDateTime.isAfter(now);
    }

    private BigDecimal calculateDiscountedPrice(Double price, BigDecimal discount) {
        BigDecimal productPrice = BigDecimal.valueOf(price);
        return productPrice.subtract(productPrice.multiply(discount.divide(BigDecimal.valueOf(100))));
    }
}
