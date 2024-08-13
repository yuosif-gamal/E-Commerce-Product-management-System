package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.VoucherDto;
import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.entity.Voucher;

public interface VoucherService {
    VoucherDto createVoucher(VoucherDto voucher);

    Voucher findVoucherByCode(String code);

    void deleteVoucher(Long id);

    void applyVoucherDiscount(Product product);
}
