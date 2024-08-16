package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.VoucherDto;
import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.entity.Voucher;
import jakarta.validation.Valid;

import java.util.List;

public interface VoucherService {
    VoucherDto createVoucher(VoucherDto voucher);

    Voucher findVoucherByCode(String code);

    void deleteVoucher(Long id);

    void applyVoucherDiscount(Product product);

    void addProductsToVoucher(Long voucherId, List<Long> productIds);
}