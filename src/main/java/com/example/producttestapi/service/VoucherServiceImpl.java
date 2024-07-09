package com.example.producttestapi.service;

import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.repos.VoucherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return voucherRepo.findByCode(code);
    }

    @Override
    public void deleteVoucher(Long id) {
        voucherRepo.deleteById(id);
    }
}

