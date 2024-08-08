package com.example.producttestapi.service;

import com.example.producttestapi.entity.Product;
import com.example.producttestapi.entity.Voucher;

public interface VoucherService {
    Voucher createVoucher(Voucher voucher);
    Voucher findVoucherByCode(String code);
    void deleteVoucher(int id);
    void applyVoucherDiscount(Product product);
}
