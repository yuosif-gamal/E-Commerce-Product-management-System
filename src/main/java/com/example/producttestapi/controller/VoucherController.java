package com.example.producttestapi.controller;

import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.repos.VoucherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class VoucherController {
    @Autowired
    VoucherRepo repo;
    @PostMapping("/voucher")
    public Voucher create(@RequestBody Voucher voucher){
        return repo.save(voucher);
    }
    @GetMapping("voucher/{code}")
    public Voucher getVoucher(@PathVariable("code") String code){
        return repo.findByCode(code);
    }

    @DeleteMapping("voucher/{id}")
    public void deleteVoucher(@PathVariable Long id) {
        repo.deleteById(id);
    }

}
