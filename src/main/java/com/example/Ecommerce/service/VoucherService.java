package com.example.Ecommerce.service;

import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.entity.Voucher;

public interface VoucherService {
    Voucher createVoucher(Voucher voucher);

    Voucher findVoucherByCode(String code);

    void deleteVoucher(Long id);

    void applyVoucherDiscount(Product product);
}
