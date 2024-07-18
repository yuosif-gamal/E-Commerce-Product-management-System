package com.example.producttestapi.service;

import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.ProductRepo;
import com.example.producttestapi.repos.VoucherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepo voucherRepo;
    private final ProductRepo productRepo;



    @Autowired
    public VoucherServiceImpl(VoucherRepo voucherRepo ,ProductRepo productRepo) {
        this.voucherRepo = voucherRepo;
        this.productRepo = productRepo;

    }

    @Override
    public Voucher createVoucher(Voucher voucher) {
        String s = voucher.getCode();
        if (voucherRepo.findByCode(s) != null){
            throw  new ResourceNotFoundException("already exist Code : " + s);
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
    public void deleteVoucher(int id) {
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
                    int id = voucher.getId();
                    deleteVoucher(id);
                    product.setPrice(product.getPrice());

                }

            }
        }
    }
}

