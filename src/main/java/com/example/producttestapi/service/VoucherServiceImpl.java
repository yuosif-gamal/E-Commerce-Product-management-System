package com.example.producttestapi.service;

import com.example.producttestapi.entity.Product;
import com.example.producttestapi.entity.Voucher;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repository.ProductRepo;
import com.example.producttestapi.repository.VoucherRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor

public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepo voucherRepo;
    private final ProductRepo productRepo;


    @Override
    public Voucher createVoucher(Voucher voucher) {
        String s = voucher.getCode();
        if (voucherRepo.findByCode(s) != null){
            throw  new ResourceNotFoundException("already exist Code : " + s);
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireDateTime = voucher.getExpireDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (expireDateTime.isBefore(now)) {
            throw new ResourceNotFoundException("this is expired date .. ");
        }
            return voucherRepo.save(voucher);
    }

    @Override
    public Voucher findVoucherByCode(String code) {
        Voucher voucher = voucherRepo.findByCode(code);
        if (voucher == null) {
            throw new ResourceNotFoundException("Voucher not found with this code : " + code);
        }
        return voucher;
    }

    @Override
    public void deleteVoucher(Long id) {
        List<Product> products = productRepo.findAllByVoucherID(id);

        for (Product product : products) {
            product.setVoucherCode(null);
            productRepo.save(product);
        }
        if (!voucherRepo.existsById(id)) {
            throw new ResourceNotFoundException("Voucher not found with id: " + id);
        }
        voucherRepo.deleteById(id);
    }


    public void applyVoucherDiscount(Product product) {
        if (product.getVoucherCode() != null) {
            Voucher voucher = product.getVoucherCode();
            if (voucher != null) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expireDateTime = voucher.getExpireDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                if (expireDateTime.isAfter(now)) {
                    BigDecimal discount = voucher.getDiscount();
                    BigDecimal productPrice = BigDecimal.valueOf(product.getPrice());
                    BigDecimal discountedPrice = productPrice.subtract(productPrice.multiply(discount.divide(BigDecimal.valueOf(100))));

                    product.setPrice(discountedPrice.doubleValue());
                }
                else {
                    Long id = voucher.getId();
                    deleteVoucher(id);
                    product.setPrice(product.getPrice());
                }

            }
        }
    }
}

