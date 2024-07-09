package com.example.producttestapi.service;

import com.example.producttestapi.entities.Voucher;

public interface VoucherService {
    Voucher createVoucher(Voucher voucher);
    Voucher findVoucherByCode(String code);
    void deleteVoucher(Long id);
}
