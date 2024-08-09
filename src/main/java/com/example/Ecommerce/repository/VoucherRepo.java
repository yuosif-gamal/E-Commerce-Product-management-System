package com.example.Ecommerce.repository;

import com.example.Ecommerce.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepo extends JpaRepository<Voucher, Long> {
    Voucher findByCode(String code);
}
