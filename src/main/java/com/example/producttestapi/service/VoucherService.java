package com.example.producttestapi.service;

import com.example.producttestapi.dto.ProductDto;
import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.Voucher;

import java.util.Optional;

public interface VoucherService {
    Voucher createVoucher(Voucher voucher);
    Voucher findVoucherByCode(String code);
    void deleteVoucher(int id);
    void applyVoucherDiscount(Product product);
}
