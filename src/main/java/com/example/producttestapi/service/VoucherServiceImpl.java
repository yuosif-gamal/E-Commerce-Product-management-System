package com.example.producttestapi.service;

import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.VoucherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepo voucherRepo;


    @Autowired
    public VoucherServiceImpl(VoucherRepo voucherRepo) {
        this.voucherRepo = voucherRepo;
    }

    @Override
    public Voucher createVoucher(Voucher voucher) {
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
        if (!voucherRepo.existsById(id)) {
            throw new ResourceNotFoundException("Voucher not found with id: " + id);
        }
        voucherRepo.deleteById(id);
    }
    public void applyVoucherDiscount(Product product) {
        if (product.getVoucherCode() != null) {
            Voucher voucher = product.getVoucherCode();
            System.out.println(voucher);
            if (voucher != null) {
                BigDecimal discount = voucher.getDiscount();
                BigDecimal productPrice = BigDecimal.valueOf(product.getPrice());
                BigDecimal discountedPrice = productPrice.subtract(productPrice.multiply(discount.divide(BigDecimal.valueOf(100))));

                product.setPrice(discountedPrice.doubleValue());
            }
        }
    }
}

