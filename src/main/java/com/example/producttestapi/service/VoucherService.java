package com.example.producttestapi.service;

import com.example.producttestapi.entities.Voucher;

import java.util.Optional;

public interface VoucherService {
    Voucher createVoucher(Voucher voucher);
    Optional<Voucher> findVoucherByCode(String code);
    void deleteVoucher(Long id);
}
