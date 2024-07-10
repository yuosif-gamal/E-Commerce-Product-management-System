package com.example.producttestapi.service;

import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.excetion.ResourceNotFoundException;
import com.example.producttestapi.repos.VoucherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Optional<Voucher> findVoucherByCode(String code) {
        Optional<Voucher> optionalVoucher = Optional.ofNullable(voucherRepo.findByCode(code));
        if (!optionalVoucher.isPresent()) {
            throw new ResourceNotFoundException("Voucher not found with this code : " + code);
        }
        return optionalVoucher;
    }

    @Override
    public void deleteVoucher(Long id) {
        if (!voucherRepo.existsById(id)) {
            throw new ResourceNotFoundException("Voucher not found with id: " + id);
        }
        voucherRepo.deleteById(id);
    }
}

