package com.example.producttestapi.repository;

import com.example.producttestapi.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepo extends JpaRepository<Voucher, Integer> {
    Voucher findByCode(String code);
}
