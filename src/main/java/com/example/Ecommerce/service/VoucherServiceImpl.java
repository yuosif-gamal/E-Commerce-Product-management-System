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


    @Override
    public VoucherDto createVoucher(VoucherDto voucherDto) {

        if (voucherRepo.findByCode(voucherDto.getCode()) != null) {
            throw new ResourceNotFoundException("Voucher with code already exists: " + voucherDto.getCode());
        }
        checkIfVoucherIsExpired(voucherDto.getExpiryDate());

        Voucher voucher = VoucherMapper.convertDtoToEntity(voucherDto);
        Voucher savedVoucher = voucherRepo.save(voucher);
        return VoucherMapper.convertEntityToDto(savedVoucher);
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
        updateProductsWithoutVoucher(id);
        voucherRepo.deleteById(id);
    }

    @Override
    public void applyVoucherDiscount(Product product) {

        if (product.getVoucherCode() != null) {
            Voucher voucher = product.getVoucherCode();
            if (isVoucherDateValid(voucher.getExpiryDate())) {
                BigDecimal discountedPrice = calculateDiscountedPrice(product.getPrice(), voucher.getDiscount());
                product.setPrice(discountedPrice.doubleValue());
            } else {
                product.setPrice(product.getPrice());
                deleteVoucher(voucher.getId());
            }
        }
    }

    @Override
    @Transactional
    public void addProductsToVoucher(Long voucherId, List<Long> productIds) {
        checkIfVoucherExistsById(voucherId);
        Voucher voucher = voucherRepo.findById(voucherId).orElse(null);

        List<Product> products = productRepo.findAllById(productIds);

        if (products.size() != productIds.size()) {
            throw new ResourceNotFoundException("One or more product IDs do not match any existing products.");
        }

        for (Product product : products) {
            product.setVoucherCode(voucher);
        }

        productRepo.saveAll(products);
    }


    private void checkIfVoucherExistsById(Long id) {
        if (voucherRepo.findById(id).isEmpty()) {
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

    private void updateProductsWithoutVoucher(Long voucherId) {
        productRepo.updateProductsWithoutVoucher(voucherId);
    }

    private boolean isVoucherDateValid(Date voucher) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireDateTime = convertToLocalDateTime(voucher);
        return expireDateTime.isAfter(now);
    }

    private BigDecimal calculateDiscountedPrice(Double price, BigDecimal discount) {
        BigDecimal productPrice = BigDecimal.valueOf(price);
        return productPrice.subtract(productPrice.multiply(discount.divide(BigDecimal.valueOf(100))));
    }
}
