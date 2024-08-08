package com.example.producttestapi.repository;

import com.example.producttestapi.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepo extends JpaRepository<Voucher, Long> {
    Voucher findByCode(String code);
}
