package com.example.producttestapi.controller;

import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/voucher")

public class VoucherController {
    private final VoucherService voucherService;

    @Autowired
    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @PostMapping("/voucher")
    public Voucher create(@RequestBody Voucher voucher) {
        return voucherService.createVoucher(voucher);
    }

    @GetMapping("/voucher/{code}")
    public Voucher getVoucher(@PathVariable("code") String code) {
        return voucherService.findVoucherByCode(code);
    }

    @DeleteMapping("/voucher/{id}")
    public void deleteVoucher(@PathVariable Long id) {
        voucherService.deleteVoucher(id);
    }

}
